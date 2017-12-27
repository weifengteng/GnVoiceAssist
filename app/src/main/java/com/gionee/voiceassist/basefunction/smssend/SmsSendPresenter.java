package com.gionee.voiceassist.basefunction.smssend;

import android.content.Context;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.basefunction.BasePresenter;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.voiceassist.directiveListener.sms.SmsDirectiveListener;
import com.gionee.voiceassist.sdk.module.sms.message.SmsInfo;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.widget.ContactsListItem;
import com.gionee.voiceassist.widget.SimCardListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by twf on 2017/8/28.
 *
 * 短信 功能实现
 * 主要实现发送短信功能。读取系统内短信未能实现。
 */

public class SmsSendPresenter extends BasePresenter {
    public static final String TAG = SmsSendPresenter.class.getSimpleName();
    private SmsDirectiveListener.SmsSimQueryInterface mSmsSimQueryInterface;
    private RecordController recordController;
    private String mPhoneNumber = "";
    private String mSmsContent = "";
    private String mSimId = "";
    private Context mAppCtx;
    private HashMap<String, String> messageContentMap = new HashMap<>();
    private HashMap<String, Integer> mSimNameMap = new HashMap<String, Integer>();
    private boolean isContactSelectViewCanClick = false;
    private boolean isSimCardSelectViewCanClick = false;

    public SmsSendPresenter(IBaseFunction baseFunction) {
        super(baseFunction);
        this.recordController = baseFunction.getRecordController();
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    // 打开短信选择列表
    public void showSmsContactSelectView(List<SmsInfo> smsInfos) {
        isContactSelectViewCanClick = true;
        showContactList(convertContactsStructure(smsInfos));
        resetSendSmsParam();
    }

    public void disableContactSelect() {
        isContactSelectViewCanClick = false;
    }

    private HashMap<String, HashMap<String, ArrayList<String>>> convertContactsStructure(List<SmsInfo> smsInfos) {
        if(smsInfos.isEmpty()) {
            return null;
        }
        HashMap<String, HashMap<String, ArrayList<String>>> availableNumList = new HashMap<>();
        for(SmsInfo smsInfo : smsInfos) {
            String name = smsInfo.getName();
            HashMap<String, ArrayList<String>> phoneTypeNumberMap = new HashMap<>();
            List<SmsInfo.NumberInfo> numberInfos = smsInfo.getPhoneNumbersList();
            for(SmsInfo.NumberInfo numberInfo : numberInfos) {
                String numberType = numberInfo.getNumberType();
                String phoneNumber = numberInfo.getPhoneNumber();
                ArrayList<String> phoneNumberList = new ArrayList<>();
                phoneNumberList.add(phoneNumber);
                phoneTypeNumberMap.put(numberType, phoneNumberList);
            }
            availableNumList.put(name, phoneTypeNumberMap);
            String messageContent = smsInfo.getMessageContent();
            messageContentMap.put(name, messageContent);
        }
        return availableNumList;
    }

    public void sendSms(String phoneNumber, String content, String simId) {
        // TODO: 发短信时选卡功能
        playAndRenderText("正在为您发送短信", true);
        TelephonyManager tm = (TelephonyManager)mAppCtx.getSystemService(Context.TELEPHONY_SERVICE);
        String simSer = tm.getSimSerialNumber();
        SmsManager smsManager = SmsManager.getDefault();
        if (!TextUtils.isEmpty(simSer))
        {
            if (content.length() >= 70)
            {
                List<String> ms = smsManager.divideMessage(content);
                for (String str : ms) {
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        smsManager.sendTextMessage(phoneNumber, null, str, null, null);
                    }
                }
            }
            else if (!TextUtils.isEmpty(phoneNumber))
            {
                smsManager.sendTextMessage(phoneNumber, null, content, null, null);
            }
        }
        else {
            Toast.makeText(mAppCtx, "手机当前没有插入SIM卡",Toast.LENGTH_SHORT).show();
        }
        resetSendSmsParam();
        isContactSelectViewCanClick = false;

        //TODO: 发送短信成功/失败的回调提示
    }

    public void setSmsSimQueryInterface(SmsDirectiveListener.SmsSimQueryInterface smsSimQueryInterface) {
        this.mSmsSimQueryInterface = smsSimQueryInterface;
    }

    public void setSmsParam(final String phoneNumber, final String smsContent) {
        mPhoneNumber = phoneNumber;
        mSmsContent = smsContent;
    }

    public void showPhoneSimChooseView() {
        mSimNameMap.put("卡1", 1);
        mSimNameMap.put("卡2", 2);
        showSimCardListView();
    }

    public void disableChooseSimCard() {
        isSimCardSelectViewCanClick = false;
    }

    private void resetSendSmsParam() {
        mPhoneNumber = "";
        mSmsContent = "";
        mSimId = "";
    }

    public boolean isNeedChoosePhoneSim() {
        // TODO:
        return true;
    }

    public void onDestroy() {
        // TODO:
    }

    private void showContactList(HashMap<String,HashMap<String, ArrayList<String>>> list) {
        ContactsListItem contactList = new ContactsListItem(mAppCtx, list) {
            @Override
            public void onClick(View arg0) {
//                super();
                if(isContactSelectViewCanClick) {
                    contactListClickToChoose(arg0);
                }
            }
        };
        screenRender.renderInfoPanel(contactList.getView());
    }

    private void contactListClickToChoose(View view){
        String name = ((TextView) view.findViewById(R.id.title)).getTag().toString();
        String phoneNumber = ((TextView) view.findViewById(R.id.info)).getTag().toString();
//        SharedData.ContactInfo contactInfo = mSharedData.new ContactInfo(0, name, number);
//        mContactsInfo.clear();
//        mContactsInfo.add(contactInfo);
//        doWithContentInfo(false, false);

        recordController.cancelRecord();
        CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
        boolean isNeedChoosePhoneSim = isNeedChoosePhoneSim();
        if(isNeedChoosePhoneSim) {
            mPhoneNumber = phoneNumber;
            mSmsContent = messageContentMap.get(name);
            if(mSmsSimQueryInterface != null) {
                mSmsSimQueryInterface.querySmsSim(phoneNumber, mSmsContent);
            }
        } else {
            sendSms(phoneNumber, mSmsContent, "1");
        }
    }

    private void showSimCardListView() {
        SimCardListItem simcardlist = new SimCardListItem(mAppCtx, SimCardListItem.SelectType.MESSAGE){

            @Override
            public void onClick(View arg0) {
                if(isSimCardSelectViewCanClick) {
                    mSimId = mSimNameMap.get(arg0.getTag().toString()).toString();
//                doCallAction();


                    recordController.cancelRecord();
                    CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                    disableChooseSimCard();
                    T.showShort("您选择了卡" + mSimId);
                    mSimId = String.valueOf(mSimId);
                    if(!TextUtils.isEmpty(mPhoneNumber) && !TextUtils.isEmpty(mSimId) && !TextUtils.isEmpty(mSmsContent)) {
                        // TODO: send sms
                        sendSms(mPhoneNumber, mSmsContent, mSimId);
                    }
                }
            }
        };
        isSimCardSelectViewCanClick = true;
        screenRender.renderInfoPanel(simcardlist.getView());
    }
}
