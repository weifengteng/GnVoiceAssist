package com.gionee.gnvoiceassist.directiveListener.phonecall;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.baidu.duer.dcs.devicemodule.phonecall.PhoneCallDeviceModule;
import com.baidu.duer.dcs.devicemodule.phonecall.message.ContactInfo;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.util.CommonUtil;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.basefunction.phonecall.PhoneCallPresenter;
import com.gionee.gnvoiceassist.customlink.CustomLinkSchema;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.gnvoiceassist.util.SharedData;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnvoiceassist.util.Utils.doUserActivity;

/**
 * Created by twf on 2017/8/26.
 */

public class PhoneCallDirectiveListener extends BaseDirectiveListener implements PhoneCallDeviceModule.IPhoneCallDirectiveListener {
    public static final String TAG = PhoneCallDirectiveListener.class.getSimpleName();
    public static final String UTTER_SHOW_SELECT_PHONE_CONTACT_VIEW = "utter_show_select_phone_contact_view";
    public static final String UTTER_SHOW_SELECT_PHONE_SIM_VIEW = "utter_show_select_phone_sim_view";
    public static final String UTTER_ENTER_SELECT_PHONE_CONTACT_CUI = "utter_enter_select_phone_contact_cui";
    public static final String UTTER_ENTER_SELECT_PHONE_SIM_CUI = "utter_enter_select_phone_sim_cui";
    public static final String CUI_SELECT_PHONE_CONTACT = "cui_select_phone_contact";
    public static final String CUI_SELECT_PHONE_SIM = "cui_select_phone_sim";
    private List<ContactInfo> mContactInfos;
    private PhoneCallPresenter mPhoneCallPresenter;
    private PhoneSimQueryInterface mPhoneSimQueryInterface;

    public PhoneCallDirectiveListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
        mPhoneCallPresenter = iBaseFunction.getPhoneCallPresenter();
        mPhoneSimQueryInterface = new PhoneSimQueryInterface() {
            @Override
            public void queryPhoneSim(String phoneNumber) {
                PhoneCallDirectiveListener.this.queryPhoneSim(phoneNumber);
            }
        };
        mPhoneCallPresenter.setPhoneSimSelectInterface(mPhoneSimQueryInterface);
    }

    @Override
    public void phoneCallDirectiveReceived(List<ContactInfo> list, Directive directive) {
        LogUtil.d(TAG, "phoneCallDirectiveReceived: " + list.toString());
//        directive.getName();
//        PhonecallByNamePayload namePayload = (PhonecallByNamePayload) directive.getPayload();
//        List<CandidateCallee> callees = namePayload.getCandidateCallees();
//        mPhoneCallPresenter.procMultiNameContact((String[]) callees.toArray());

        mContactInfos = list;
        selectPhoneContact(mContactInfos);
    }

    @Override
    public void handleCUInteractionUnknownUtterance(String id) {
        super.handleCUInteractionUnknownUtterance(id);
        String alert = "";
        MaxUpriseCounter.increaseUpriseCount();
        if(TextUtils.equals(id, CUI_SELECT_PHONE_CONTACT)) {
            if(MaxUpriseCounter.isMaxCount()) {
                alert = "太累了,我先休息一下";
                mPhoneCallPresenter.disableSelectContact();
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                playTTS(alert, true);
            } else if(TextUtils.isEmpty(alert)) {
                alert = "选择第几条？";
                playTTS(alert, UTTER_ENTER_SELECT_PHONE_CONTACT_CUI, this, true);
            }

        } else if(TextUtils.equals(id, CUI_SELECT_PHONE_SIM)) {
            if(MaxUpriseCounter.isMaxCount()) {
                alert = "太累了,我先休息一下";
                mPhoneCallPresenter.disableSelectSimCard();
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                playTTS(alert, true);
            } else if(TextUtils.isEmpty(alert)) {
                alert = "卡1呼叫还是卡2？";
                playTTS(alert, UTTER_ENTER_SELECT_PHONE_SIM_CUI, this, true);
            }
        }
    }

    @Override
    public void handleCUInteractionTargetUrl(String id, String url) {
        super.handleCUInteractionTargetUrl(id, url);
        if(url.startsWith(CustomLinkSchema.LINK_PHONE)) {
            // Phone协议eg: phone://{num=phonenumber}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}
            int beginIdx = url.indexOf(":");
            String realContent = url.substring(beginIdx + 1);
            String[] contents = realContent.split("#");
            String phoneNumber = contents[0].substring(contents[0].indexOf("=") + 1);
            LogUtil.d(TAG, "customUserInteractionDirectiveReceived contents[0]= " + contents[0]);

            if(contents.length == 1) {
                mPhoneCallPresenter.disableSelectContact();
                mPhoneCallPresenter.setContactInfo(phoneNumber, null);
                if(mPhoneCallPresenter.isNeedChoosePhoneSim()) {
                    queryPhoneSim(phoneNumber);
                } else {
                    CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
//                    mPhoneCallPresenter.callPhone(phoneNumber, "1");
//                    playTTS("正在为您呼叫", UTTER_READY_TO_CALL, this, true);
                    mPhoneCallPresenter.readyToCallPhone();
                }

            } else if(contents.length == 2) {
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                String simId = contents[1].substring(contents[1].indexOf("=") + 1);
                LogUtil.d(TAG, "customUserInteractionDirectiveReceived phoneNumber= " + phoneNumber + " simId= " + simId);
                mPhoneCallPresenter.disableSelectSimCard();
//                mPhoneCallPresenter.callPhone(phoneNumber, simId);
                mPhoneCallPresenter.setContactInfo(phoneNumber, simId);
                mPhoneCallPresenter.readyToCallPhone();
//                playTTS("正在为您呼叫", UTTER_READY_TO_CALL, this, true);
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
        if(TextUtils.equals(utterId, UTTER_SHOW_SELECT_PHONE_CONTACT_VIEW)) {
            // 展示多个联系人的弹窗
//            mPhoneCallPresenter.showPhoneContactSelectView(mContactInfos);
        } else if(TextUtils.equals(utterId, UTTER_SHOW_SELECT_PHONE_SIM_VIEW)) {
//            mPhoneCallPresenter.showPhoneSimChooseView();
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

            LogUtil.d(TAG, "DCSF -------- onSpeechFinish startRecordOnline");
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

    /**
     * 选联系人适配，此处需要手动进行语音识别开启，并提示打给谁的tts播报。
     */
    private void selectPhoneContact(final List<ContactInfo> contactInfos) {
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator() {
            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState state) {
                LogUtil.d(TAG, "generateContextPayloadByInteractionState");
                if(CustomUserInteractionManager.getInstance().shouldStopCurrentInteraction()) {
                    return new CustomClientContextPayload(null);
                }

                int index;
                Payload payload;
                ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();

                for (int i = 0; i < contactInfos.size(); i++) {
                    ContactInfo contactInfo = contactInfos.get(i);
                    List<ContactInfo.NumberInfo> numberInfos = contactInfo.getPhoneNumbersList();
                    if (numberInfos != null && numberInfos.size() > 0) {
                        for (int j = 0; j < numberInfos.size(); j++) {
                            List<String> utterances = new ArrayList<String>();
                            index = i + j + 1;
                            utterances.add("第" + index + "条");
                            // 开始拼凑phone协议schema
                            String phoneNumber = numberInfos.get(j).getPhoneNumber();
                            String simIndex = contactInfo.getSimIndex();
                            String carrier = contactInfo.getCarrierOprator();
                            String url = CustomLinkSchema.LINK_PHONE +
                                    "num=" + phoneNumber;
                            if (!TextUtils.isEmpty(simIndex)) {
                                url += "#" + "sim=" + simIndex;
                            }
                            if (!TextUtils.isEmpty(carrier)) {
                                url += "#" + "carrier=" + carrier;

                            }
                            LogUtil.d(TAG, "selectPhoneContact url= " + url + " index= " + index);
                            CustomClientContextHyperUtterace customClientContextHyperUtterace =
                                    new CustomClientContextHyperUtterace(utterances, url);
                            hyperUtterances.add(customClientContextHyperUtterace);
                        }
                    }
                }
                payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };

        CustomUserInteractionManager.getInstance().startCustomUserInteraction(generator, CUI_SELECT_PHONE_CONTACT, PhoneCallDirectiveListener.this);
        mPhoneCallPresenter.showPhoneContactSelectView(mContactInfos);
        playTTS("选择第几条？", UTTER_SHOW_SELECT_PHONE_CONTACT_VIEW, this, true);
    }

    //打电话场景的选卡
    private void queryPhoneSim(final String phoneNumber) {
        CustomUserInteractionDeviceModule.PayLoadGenerator generator =
                new CustomUserInteractionDeviceModule.PayLoadGenerator(){

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
                            String url = CustomLinkSchema.LINK_PHONE;
                            url += "num=" + phoneNumber;
                            url += "#sim=" + i;
                            CustomClientContextHyperUtterace customClientContextHyperUtterance =
                                    new CustomClientContextHyperUtterace(utterances, url);
                            hyperUtterances.add(customClientContextHyperUtterance);
                        }

                        payload = new CustomClientContextPayload(false, hyperUtterances);
                        return payload;
                    }
                };
        // 上传自定义交互
        CustomUserInteractionManager.getInstance().startCustomUserInteraction(generator, CUI_SELECT_PHONE_SIM, this);
        mPhoneCallPresenter.showPhoneSimChooseView();
        playTTS("卡1呼叫还是卡2？", UTTER_SHOW_SELECT_PHONE_SIM_VIEW,this, true);
    }


    public interface PhoneSimQueryInterface {
        void queryPhoneSim(String phoneNumber);
    }
}
