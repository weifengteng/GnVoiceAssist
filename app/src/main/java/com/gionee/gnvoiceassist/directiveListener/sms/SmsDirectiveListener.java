package com.gionee.gnvoiceassist.directiveListener.sms;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.baidu.duer.dcs.devicemodule.sms.SmsDeviceModule;
import com.baidu.duer.dcs.devicemodule.sms.message.SmsInfo;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.util.CommonUtil;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.basefunction.smssend.SmsSendPresenter;
import com.gionee.gnvoiceassist.customlink.CustomLinkSchema;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.gnvoiceassist.util.SharedData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnvoiceassist.util.Utils.doUserActivity;

/**
 * Created by twf on 2017/8/26.
 */

public class SmsDirectiveListener extends BaseDirectiveListener implements SmsDeviceModule.ISmsDirectiveListener {
    public static final String TAG = SmsDirectiveListener.class.getSimpleName();
    public static final String UTTER_SHOW_SELECT_SMS_CONTACT_VIEW = "utter_show_select_sms_contact_view";
    public static final String UTTER_SHOW_SELECT_SMS_SIM_VIEW = "utter_show_select_sms_sim_view";
    public static final String UTTER_ENTER_SELECT_SMS_CONTACT_CUI = "utter_enter_select_sms_contact_cui";
    public static final String UTTER_ENTER_SELECT_SMS_SIM_CUI = "utter_enter_select_sms_sim_cui";
    public static final String CUI_SELECT_SMS_CONTACT = "cui_select_sms_contact";
    public static final String CUI_SELECT_SMS_SIM = "cui_select_sms_sim";

    private List<SmsInfo> mSmsInfos;
    private SmsSendPresenter smsSendPresenter;
    private SmsSimQueryInterface smsSimQueryInterface;

    public SmsDirectiveListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
        smsSendPresenter = iBaseFunction.getSmsSendPresenter();
        smsSimQueryInterface = new SmsSimQueryInterface() {
            @Override
            public void querySmsSim(String phoneNumber, String smsContent) {
                selectPhoneSim(phoneNumber, smsContent);
            }
        };
        smsSendPresenter.setSmsSimQueryInterface(smsSimQueryInterface);
    }

    @Override
    public void smsDirectiveReceived(List<SmsInfo> list, Directive directive) {
        //TODO: 此处依赖了SDK的SmsInfo，应封装成自己的
        LogUtil.d(TAG, "smsDirectiveReceived!");
        mSmsInfos = list;
        selectSmsContact(mSmsInfos);
    }

    @Override
    public void handleCUInteractionUnknownUtterance(String id) {
        super.handleCUInteractionUnknownUtterance(id);
        String alert = "";
        MaxUpriseCounter.increaseUpriseCount();

        if(TextUtils.equals(id, CUI_SELECT_SMS_CONTACT)) {
            if(MaxUpriseCounter.isMaxCount()) {
                alert = "太累了,我先休息一下";
                smsSendPresenter.disableContactSelect();
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                playTTS(alert, true);
            } else if(TextUtils.isEmpty(alert)) {
                alert = "选择第几个？";
                playTTS(alert, UTTER_ENTER_SELECT_SMS_CONTACT_CUI, this, true);
            }
        } else if(TextUtils.equals(id, CUI_SELECT_SMS_SIM)) {
            if(MaxUpriseCounter.isMaxCount()) {
                alert = "太累了,我先休息一下";
                smsSendPresenter.disableChooseSimCard();
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                playTTS(alert, true);
            } else if(TextUtils.isEmpty(alert)) {
                alert = "卡1发送还是卡2？";
                playTTS(alert, UTTER_ENTER_SELECT_SMS_SIM_CUI, this, true);
            }
        }
    }

    @Override
    public void handleCUInteractionTargetUrl(String id, String url) {
        super.handleCUInteractionTargetUrl(id, url);
        if(url.startsWith(CustomLinkSchema.LINK_SMS)) {
            // 短信协议：sms://{num=phonenumber}#{msg=messageContent}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}
            int beginIdx = url.indexOf(":");
            String realContent = url.substring(beginIdx + 1);
            String[] contents = realContent.split("#");
            // phoneNumber
            String phoneNumber =  contents[0].substring(contents[0].indexOf("=") + 1);
            // messageContent
            String msgContent = contents[1].substring(contents[1].indexOf("=") + 1);
            try {
                msgContent = URLDecoder.decode(msgContent, "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(contents.length == 2) {
                smsSendPresenter.disableContactSelect();
                smsSendPresenter.setSmsParam(phoneNumber, msgContent);
                if(smsSendPresenter.isNeedChoosePhoneSim()) {
                    selectPhoneSim(phoneNumber, msgContent);
                } else {
                    CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                    smsSendPresenter.sendSms(phoneNumber, msgContent, "1");
                }

            } else if(contents.length == 3){
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);

                String simId = contents[2].substring(contents[2].indexOf("=") + 1);
                LogUtil.d(TAG, "customUserInteractionDirectiveReceived phoneNumber= " + phoneNumber + " msgContent= " + msgContent + "simId= " + simId);
                smsSendPresenter.disableChooseSimCard();
                // TODO:
                smsSendPresenter.sendSms(phoneNumber, msgContent, simId);
                String asrResult = iBaseFunction.getScreenRender().getAsrResult();
                if(TextUtils.equals(asrResult, "卡已") || TextUtils.equals(asrResult, "卡伊")) {
                    iBaseFunction.getScreenRender().renderQueryInScreen("卡一");
                } else if(TextUtils.equals(asrResult, "卡尔") || TextUtils.equals(asrResult, "卡而")) {
                    iBaseFunction.getScreenRender().renderQueryInScreen("卡二");
                }
            }
        }
    }

    @Override
    public void onSpeakFinish(String utterId) {
        if(TextUtils.equals(utterId, UTTER_SHOW_SELECT_SMS_CONTACT_VIEW)) {
//            smsSendPresenter.showSmsContactSelectView(mSmsInfos);
        } else if(TextUtils.equals(utterId, UTTER_SHOW_SELECT_SMS_SIM_VIEW)) {
//            smsSendPresenter.showPhoneSimChooseView();
        }

        if(CustomUserInteractionManager.getInstance().isCustomUserInteractionProcessing()) {
            if(CommonUtil.isFastDoubleClick()) {
                return;
            }

            if(SharedData.getInstance().isStopListenReceiving()) {
                iBaseFunction.getRecordController().stopRecord();
                SharedData.getInstance().setStopListenReceiving(false);
                return;
            }

            LogUtil.d(TAG, "DCSF ----------- onSpeechFinish startRecordOnline");
            SharedData.getInstance().setStopListenReceiving(true);
            iBaseFunction.getRecordController().startRecordOnline();
            doUserActivity();
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {

    }

    private void selectSmsContact(final List<SmsInfo> smsInfos) {
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator() {
            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState customClicentContextMachineState) {
                if(CustomUserInteractionManager.getInstance().shouldStopCurrentInteraction()) {
                    return new CustomClientContextPayload(null);
                }

                int index = 1;
                Payload payload;
                ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();
                for(int i=0; i<smsInfos.size(); i++) {
                    SmsInfo smsInfo = smsInfos.get(i);
                    List<SmsInfo.NumberInfo> numberInfos = smsInfo.getPhoneNumbersList();
                    if(numberInfos != null && numberInfos.size() > 0) {
                        for(int j=0; j < numberInfos.size(); j++) {
                            List<String> utterances = new ArrayList<>();
                            index = i + j + 1;
                            utterances.add("第" + index + "个");

                            String phoneNumber = numberInfos.get(j).getPhoneNumber();
                            String simIndex = smsInfo.getSimIndex();
                            String carrier = smsInfo.getCarrierOprator();
                            String url = CustomLinkSchema.LINK_SMS +
                                    "num=" + phoneNumber +
                                    "#msg=" + smsInfo.getMessageContent();

                            if(!TextUtils.isEmpty(simIndex)) {
                                url += "#" + "sim=" + simIndex;
                            }

                            if(!TextUtils.isEmpty(carrier)){
                                url += "#" + "carrier=" + carrier;
                            }
                            LogUtil.d(TAG, "index= " + (i+j+1) + " phoneNumber: " + phoneNumber);
                            CustomClientContextHyperUtterace customClientContextHyperUtterance =
                                    new CustomClientContextHyperUtterace(utterances, url);
                            hyperUtterances.add(customClientContextHyperUtterance);
                        }
                    }
                }
                payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };
        CustomUserInteractionManager.getInstance().startCustomUserInteraction(generator, CUI_SELECT_SMS_CONTACT, this);
        smsSendPresenter.showSmsContactSelectView(mSmsInfos);
        playTTS("请问你要选择哪一个号码？", UTTER_SHOW_SELECT_SMS_CONTACT_VIEW, this, true);
    }

    // 发短信场景的选卡
    private void selectPhoneSim(final String phoneNumber, final String msgContent) {
        // 上传自定义交互
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator(){

            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState customClicentContextMachineState) {
                if(CustomUserInteractionManager.getInstance().shouldStopCurrentInteraction()) {
                    // 达到最大多轮交互次数，跳出自定义多轮交互状态
                    return new CustomClientContextPayload(null);
                }

                Payload payload;
                ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();

                for(int i=1; i < 3; i++) {
                    List<String> utterances = new ArrayList<>();
                    utterances.add("卡" + i);
                    utterances.add("sim卡" + i);
                    if(i == 1) {
                        utterances.add("卡已");
                        utterances.add("卡伊");
                    }else if(i == 2) {
                        utterances.add("卡尔");
                        utterances.add("卡而");
                    }

                    String url = CustomLinkSchema.LINK_SMS;
                    url += "num=" + phoneNumber;
                    url += "#msg=" + msgContent.trim();
                    url += "#sim=" + i;
                    CustomClientContextHyperUtterace customClientContextHyperUtterance =
                            new CustomClientContextHyperUtterace(utterances, url);
                    hyperUtterances.add(customClientContextHyperUtterance);
                }

                payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };
        CustomUserInteractionManager.getInstance().startCustomUserInteraction(generator, CUI_SELECT_SMS_SIM, this);
        smsSendPresenter.showPhoneSimChooseView();
        playTTS("卡1发送还是卡2？", UTTER_SHOW_SELECT_SMS_SIM_VIEW, this, true);
    }

    public interface SmsSimQueryInterface {
        void querySmsSim(String phoneNumber, String smsContent);
    }
}
