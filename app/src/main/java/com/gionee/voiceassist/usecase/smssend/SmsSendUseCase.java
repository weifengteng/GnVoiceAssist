package com.gionee.voiceassist.usecase.smssend;

import android.content.Context;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.controller.customuserinteraction.ICuiResult;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.SmsDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.SmsInfo;
import com.gionee.voiceassist.customlink.CustomLinkSchema;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.voiceassist.directiveListener.sms.SmsDirectiveListener;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.CUInteractionUrlParser;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.widget.ContactsListItem;
import com.gionee.voiceassist.widget.SimCardListItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twf
 * @date 2017/8/28
 *
 * 短信 功能实现
 * 主要实现发送短信功能。读取系统内短信未能实现。
 */

public class SmsSendUseCase extends BaseUsecase {
    public static final String TAG = SmsSendUseCase.class.getSimpleName();
    public static final String UTTER_SHOW_SELECT_SMS_CONTACT_VIEW = "utter_show_select_sms_contact_view";
    public static final String UTTER_SHOW_SELECT_SMS_SIM_VIEW = "utter_show_select_sms_sim_view";
    public static final String UTTER_ENTER_SELECT_SMS_CONTACT_CUI = "utter_enter_select_sms_contact_cui";
    public static final String UTTER_ENTER_SELECT_SMS_SIM_CUI = "utter_enter_select_sms_sim_cui";
    public static final String CUI_SELECT_SMS_CONTACT = "cui_select_sms_contact";
    public static final String CUI_SELECT_SMS_SIM = "cui_select_sms_sim";
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
    private SmsSendUseCaseHelper smsSendUseCaseHelper;
    private List<SmsInfo> smsInfos;

    public SmsSendUseCase() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
        smsSendUseCaseHelper = new SmsSendUseCaseHelper();
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
        playAndRenderText("正在为您发送短信");
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
        // TODO:
//        screenRender.renderInfoPanel(contactList.getView());
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
        // TODO:
//        screenRender.renderInfoPanel(simcardlist.getView());
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        smsInfos = smsSendUseCaseHelper.getSmsInfoFromDirectiveEntity((SmsDirectiveEntity) payload);
        if(needChooseSmsContact()) {
            //TODO: showChooseContaceView
            chooseSmsContactByVoice();
        } else {
            mPhoneNumber = smsInfos.get(0).getPhoneNumbersList().get(0).getPhoneNumber();
            mSmsContent = smsInfos.get(0).getMessageContent();
            if(needChooseSim()) {
                // TODO: showChooseSimView
                chooseSimByVoice(mPhoneNumber, mSmsContent);
            }
        }
    }

    @Override
    public String getAlias() {
        return "smsSend";
    }

    public boolean needChooseSmsContact() {
        if(smsInfos.isEmpty() || smsInfos.size() < 2) {
            return false;
        }
        return true;
    }

    public void chooseSmsContactByVoice() {
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator() {
            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState customClicentContextMachineState) {
                if(getCuiController().isCUIShouldStop()) {
                    return new CustomClientContextPayload(null);
                }

                ArrayList<CustomClientContextHyperUtterace> hyperUtterances = smsSendUseCaseHelper.getHyperUtteranceListForSmsChooseContact(smsInfos);
                CustomClientContextPayload payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };

        getCuiController().startCustomUserInteraction(generator,new ICuiResult() {

            @Override
            public void handleCUInteractionUnknownUtterance() {
                String alert = "";
                if(getCuiController().isCUIShouldStop()) {
                    alert = "太累了,我先休息一下";
                    // TODO: disable Contact choose view
                    getCuiController().stopCurrentCustomUserInteraction();
                    playAndRenderText(alert);
                } else if(TextUtils.isEmpty(alert)) {
                    alert = "选择第几个？";
                    playAndRenderText(alert, UTTER_ENTER_SELECT_SMS_CONTACT_CUI, new SimpleTtsCallback(){
                        @Override
                        public void onSpeakFinish(String utterId) {
                            super.onSpeakFinish(utterId);
                            startRecord();
                        }
                    });
                }
            }

            @Override
            public void handleCUInteractionTargetUrl(String url) {
                if(url.startsWith(CustomLinkSchema.LINK_SMS)) {
                    // 短信协议：sms://{num=phonenumber}#{msg=messageContent}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}
                    Map<String, String> result = CUInteractionUrlParser.parseSmsUrl(url);
                    String phoneNumber = result.get("num");
                    String msgContent = result.get("msg");
                    String simId = result.get("sim");

                    //判断字段是否为空
                    boolean hasPhoneNumber = !TextUtils.isEmpty(phoneNumber);
                    boolean hasMsgContent = !TextUtils.isEmpty(msgContent);
                    boolean hasSimId = !TextUtils.isEmpty(simId);

                    if(!hasSimId && hasPhoneNumber && hasMsgContent) {
                        // TODO: disableContactSelect();
                        setSmsParam(phoneNumber, msgContent);
                        if(needChooseSim()) {
                            chooseSimByVoice(phoneNumber, msgContent);
                        } else {
                            getCuiController().stopCurrentCustomUserInteraction();
                            // TODO:
                            sendSms(phoneNumber, msgContent, "1");
                        }

                    }
                }
            }
        });

        // TODO: showSmsContactSelectView(mSmsInfos);
        playAndRenderText("请问你要选择哪一个号码？", UTTER_SHOW_SELECT_SMS_CONTACT_VIEW, new SimpleTtsCallback() {
            @Override
            public void onSpeakFinish(String utterId) {
                super.onSpeakFinish(utterId);
                startRecord();
            }
        });

    }

    public boolean needChooseSim() {
        return true;
    }

    public void chooseSimByVoice(final String phoneNumber, final String msgContent) {
        // 上传自定义交互
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator(){

            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState customClicentContextMachineState) {
                if(getCuiController().isCUIShouldStop()) {
                    // 达到最大多轮交互次数，跳出自定义多轮交互状态
                    return new CustomClientContextPayload(null);
                }

                Payload payload;
                ArrayList<CustomClientContextHyperUtterace> hyperUtterances = smsSendUseCaseHelper.getHyperUtteraceListForSmsChooseSim(phoneNumber, msgContent);
                payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };
        getCuiController().startCustomUserInteraction(generator, new ICuiResult() {

            @Override
            public void handleCUInteractionUnknownUtterance() {
                if(getCuiController().isCUIShouldStop()) {
                    // TODO: dislable choose sim view
                    getCuiController().stopCurrentCustomUserInteraction();
                    playAndRenderText("太累了,我先休息一下");
                } else {
                    playAndRenderText("卡1发送还是卡2？", UTTER_ENTER_SELECT_SMS_SIM_CUI, new SimpleTtsCallback() {
                        @Override
                        public void onSpeakFinish(String utterId) {
                            super.onSpeakFinish(utterId);
                            startRecord();
                        }
                    });
                }
            }

            @Override
            public void handleCUInteractionTargetUrl(String url) {
                if(url.startsWith(CustomLinkSchema.LINK_SMS)) {
                    // 短信协议：sms://{num=phonenumber}#{msg=messageContent}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}
                    Map<String, String> result = CUInteractionUrlParser.parseSmsUrl(url);
                    String phoneNumber = result.get("num");
                    String msgContent = result.get("msg");
                    String simId = result.get("sim");

                    //判断字段是否为空
                    boolean hasPhoneNumber = !TextUtils.isEmpty(phoneNumber);
                    boolean hasMsgContent = !TextUtils.isEmpty(msgContent);
                    boolean hasSimId = !TextUtils.isEmpty(simId);

                    if(hasSimId && hasPhoneNumber && hasMsgContent){
                        // TODO:
                        /*String asrResult = iBaseFunction.getScreenRender().getAsrResult();
                        if(TextUtils.equals(asrResult, "卡已") || TextUtils.equals(asrResult, "卡伊")) {
                            screenRender.modifyLastTextInScreen("卡1");
                        } else if(TextUtils.equals(asrResult, "卡尔") || TextUtils.equals(asrResult, "卡而")) {
                            screenRender.modifyLastTextInScreen("卡2");
                        }*/

                        getCuiController().stopCurrentCustomUserInteraction();
                        LogUtil.d(TAG, "customUserInteractionDirectiveReceived phoneNumber= " + phoneNumber + " msgContent= " + msgContent + "simId= " + simId);
                        // TODO: disableChooseSimCard();
                        sendSms(phoneNumber, msgContent, simId);
                    }
                }
            }
        });
        //TODO: showPhoneSimChooseView();
        playAndRenderText("卡1发送还是卡2？", UTTER_SHOW_SELECT_SMS_SIM_VIEW, new SimpleTtsCallback() {
            @Override
            public void onSpeakFinish(String utterId) {
                super.onSpeakFinish(utterId);
                startRecord();
            }
        });
    }

    class SimpleTtsCallback implements TtsCallback {

        @Override
        public void onSpeakStart() {

        }

        @Override
        public void onSpeakFinish(String utterId) {

        }

        @Override
        public void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s) {

        }
    }
}
