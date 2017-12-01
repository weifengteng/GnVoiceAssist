package com.gionee.voiceassist.basefunction.phonecall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.basefunction.BasePresenter;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.basefunction.recordcontrol.RecordController;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.voiceassist.directiveListener.phonecall.PhoneCallDirectiveListener;
import com.gionee.voiceassist.sdk.module.phonecall.message.ContactInfo;
import com.gionee.voiceassist.util.ContactProcessor;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.widget.ContactsListItem;
import com.gionee.voiceassist.widget.SimCardListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by twf on 2017/8/28.
 *
 * 处理打电话操作
 * 类中进行的操作
 * （1）底层实现拨打电话
 * （2）控制选号、选卡显示的渲染
 *
 */

public class PhoneCallPresenter extends BasePresenter {
    public static final String TAG = PhoneCallPresenter.class.getSimpleName();
    public static final String UTTER_READY_TO_CALL = "utter_ready_to_call";
    private RecordController recordController;
    private PhoneCallDirectiveListener.PhoneCardSelectCallback mPhoneSimQueryInterface;
    private boolean isContactSelectViewCanClick = false;
    private boolean isSimCardSelectViewCanClick = false;
    private HashMap<String, Integer> mSimNameMap = new HashMap<String, Integer>();
    private String mPhoneNumber = "";
    private String mSimId = "";
    private Context mAppCtx;

    public PhoneCallPresenter(IBaseFunction baseFunction) {
        super(baseFunction);
        this.recordController = baseFunction.getRecordController();
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    public void setPhoneCardSelectCallback(PhoneCallDirectiveListener.PhoneCardSelectCallback simSelectInterface) {
        this.mPhoneSimQueryInterface = simSelectInterface;
    }

    @Override
    public void onSpeakFinish(String utterId) {
        super.onSpeakFinish(utterId);
        if(TextUtils.equals(utterId, UTTER_READY_TO_CALL)) {
            callPhone();
        }

    }

    public void setContactInfo(String phoneNumber, String simId) {
        if(!TextUtils.isEmpty(phoneNumber)) {
            this.mPhoneNumber = phoneNumber;
        }
        this.mPhoneNumber = phoneNumber;
        if(!TextUtils.isEmpty(simId)) {
            this.mSimId = simId;
        }
    }

    public void readyToCallPhone() {
        playAndRenderText("正在为您呼叫", UTTER_READY_TO_CALL, this, true);
    }

    public void cancelCallPhone() {
        disableSelectContact();
        disableSelectSimCard();
        CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
    }

    public void showPhoneContactSelectView(List<ContactInfo> contactInfos) {
        resetCallPhoneParam();
        showContacts(convertContactsStructure(contactInfos));
    }

    public void showPhoneSimChooseView() {
        mSimNameMap.put("卡1", 1);
        mSimNameMap.put("卡2", 2);
        showSimCardListView();
    }

    public void procMultiNameContact(String[] names) {
        LogUtil.d(TAG, "FocusTelephone procMultiNameContact names[0] = " + names[0]);
        HashMap<String, HashMap<String, ArrayList<String>>> availableNumList = new HashMap<>();
        ContactProcessor cp = ContactProcessor.getContactProcessor();
        for (String name : names) {
            availableNumList.put(name, cp.getNumberByName(name));
        }

        showContacts(availableNumList);
        /*if(availableNumList.size() < 1) {
            showNoContact();
        } else {
            if(getTelephoneNumbCount(availableNumList) > 0) {
                showContacts(availableNumList);
            } else {
                notifyByInfo(mAppCtx.getString(R.string.rsp_no_phone_number_for_call), "", this);
            }
        }*/
    }

    public boolean isNeedChoosePhoneSim() {
        // TODO:
        return true;
    }

    public void disableSelectContact() {
        isContactSelectViewCanClick = false;
    }

    public void disableSelectSimCard() {
        isSimCardSelectViewCanClick = false;
    }

    private void resetCallPhoneParam() {
        mPhoneNumber = "";
        mSimId = "";
    }

    private void callPhone() {

        if (!TextUtils.isEmpty(mPhoneNumber) && mAppCtx != null) {
            Uri uri = Uri.parse("tel:" + mPhoneNumber);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(uri);
            if (intent.resolveActivity(mAppCtx.getPackageManager()) != null) {
                // TODO: 实现选卡打电话功能
                mAppCtx.startActivity(intent);
            }
        }
        resetCallPhoneParam();
        isContactSelectViewCanClick = false;
        isSimCardSelectViewCanClick = false;
    }

    public void onDestroy() {
        // TODO:
    }

    private HashMap<String, HashMap<String, ArrayList<String>>> convertContactsStructure(List<ContactInfo> contactInfos) {
        if(contactInfos.isEmpty()) {
            return null;
        }
        LogUtil.d(TAG, "convertContactsStructure contactInfos size = " + contactInfos.size());
        HashMap<String, HashMap<String, ArrayList<String>>> availableNumList = new HashMap<>();
        for(ContactInfo contactInfo : contactInfos) {
            String name = contactInfo.getName();
            LogUtil.d(TAG, "convertContactsStructure contactInfos name = " + name);
            HashMap<String, ArrayList<String>> phoneTypeNumberMap = new HashMap<>();
            List<ContactInfo.NumberInfo> numberInfos = contactInfo.getPhoneNumbersList();
            for(ContactInfo.NumberInfo numberInfo : numberInfos) {
                String numberType = numberInfo.getNumberType();
                String phoneNumber = numberInfo.getPhoneNumber();
                ArrayList<String> phoneNumberList = new ArrayList<>();
                phoneNumberList.add(phoneNumber);
                phoneTypeNumberMap.put(numberType, phoneNumberList);
                LogUtil.d(TAG, "convertContactsStructure contactInfos phoneNumber = " + phoneNumber);
            }
            availableNumList.put(name, phoneTypeNumberMap);
        }
        return availableNumList;
    }

    long lastClickTime1;
    private void showContacts(HashMap<String, HashMap<String, ArrayList<String>>> availableNumList) {
        LogUtil.d(TAG, "FocusTelephone showContacts availableNumList = " + availableNumList);
        String info = mAppCtx.getString(R.string.rsp_multiple_same_name_found);
        String ttsInfo = mAppCtx.getString(R.string.rsp_multiple_name_number_found);
        isContactSelectViewCanClick = true;
        ContactsListItem contactList = new ContactsListItem(mAppCtx, availableNumList){

            @Override
            public void onClick(View arg0) {
                if(isContactSelectViewCanClick &&
                        SystemClock.uptimeMillis() - lastClickTime1 > 500) {
                    contactListClickToChoose(arg0);
                    lastClickTime1 = SystemClock.uptimeMillis();
                }
            }

        };
        screenRender.renderInfoPanel(contactList.getView());
    }

    private void contactListClickToChoose(View view) {
//        removeViewAndStopRec();
        TextView infoLabel = (TextView) view.findViewById(R.id.info);
        TextView infoName = (TextView) view.findViewById(R.id.title);
//        setContactInfo(infoName.getTag().toString(), infoLabel.getTag().toString());
        String contactName = infoName.getTag().toString();
        this.mPhoneNumber = infoLabel.getTag().toString();

        recordController.cancelRecord();
        CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
        boolean isNeedChoosePhoneSim = isNeedChoosePhoneSim();
        if(isNeedChoosePhoneSim) {
            if(mPhoneSimQueryInterface != null) {
                mPhoneSimQueryInterface.onSelectContact(mPhoneNumber);
            }
        } else {
//            playAndRenderText("正在为您呼叫", UTTER_READY_TO_CALL, PhoneCallPresenter.this, true);
            readyToCallPhone();
        }

        /*checkSimCard();*/
    }

    private void askUserToChooseSimCard() {
//        putMapAndAddItem();
//        addCmdList(mSimNameMap.keySet());
//        mData.setGlobalScene(Scene.WAIT_CHOOSE_SIM_TO_CALL);
//        mFCB.updateSelectCmd(mCmdList.toArray(new String[mCmdList.size()]));
//        mFCB.playTts("[n1]" + getTtsInfo(), UTTER_ID_FIRE_FOCUS, this);
//        mActivity.addView(null,getTtsInfo(),null);
        showSimCardListView();
    }

    long lastClickTime;
    private void showSimCardListView() {
        SimCardListItem simcardlist = new SimCardListItem(mAppCtx, SimCardListItem.SelectType.TELEPHONE){

            @Override
            public void onClick(View arg0) {
                if(isSimCardSelectViewCanClick && SystemClock.uptimeMillis() - lastClickTime > 500) {
                    lastClickTime = SystemClock.uptimeMillis();

                    mSimId = mSimNameMap.get(arg0.getTag().toString()).toString();
//                doCallAction();

                    recordController.cancelRecord();
                    CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                    disableSelectSimCard();
                    T.showShort("您选择了卡" + mSimId);
                    mSimId = String.valueOf(mSimId);
                    if(!TextUtils.isEmpty(mPhoneNumber) && !TextUtils.isEmpty(mSimId)) {
//                        playAndRenderText("正在为您呼叫", UTTER_READY_TO_CALL, PhoneCallPresenter.this, true);
                        readyToCallPhone();
                    }
                }
            }
        };
        isSimCardSelectViewCanClick = true;
        screenRender.renderInfoPanel(simcardlist.getView());
    }
}
