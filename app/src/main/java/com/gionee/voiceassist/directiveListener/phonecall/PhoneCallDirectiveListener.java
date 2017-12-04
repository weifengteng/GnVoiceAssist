package com.gionee.voiceassist.directiveListener.phonecall;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.util.CommonUtil;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.basefunction.MaxUpriseCounter;
import com.gionee.voiceassist.basefunction.phonecall.PhoneCallPresenter;
import com.gionee.voiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.voiceassist.customlink.CustomLinkSchema;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.voiceassist.sdk.module.phonecall.IPhoneCallImpl;
import com.gionee.voiceassist.sdk.module.phonecall.PhoneCallDeviceModule;
import com.gionee.voiceassist.sdk.module.phonecall.message.CandidateCallee;
import com.gionee.voiceassist.sdk.module.phonecall.message.CandidateCalleeNumber;
import com.gionee.voiceassist.sdk.module.phonecall.message.ContactInfo;
import com.gionee.voiceassist.sdk.module.phonecall.message.PhonecallByNamePayload;
import com.gionee.voiceassist.sdk.module.phonecall.message.PhonecallByNumberPayload;
import com.gionee.voiceassist.sdk.module.phonecall.message.SelectCalleePayload;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.SharedData;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.voiceassist.util.Utils.doUserActivity;

/**
 * Created by twf on 2017/8/26.
 */

public class PhoneCallDirectiveListener extends BaseDirectiveListener implements PhoneCallDeviceModule.IPhoneCallListener {
    public static final String TAG = PhoneCallDirectiveListener.class.getSimpleName();
    public static final String UTTER_SHOW_SELECT_PHONE_CONTACT_VIEW = "utter_show_select_phone_contact_view";
    public static final String UTTER_SHOW_SELECT_PHONE_SIM_VIEW = "utter_show_select_phone_sim_view";
    public static final String UTTER_ENTER_SELECT_PHONE_CONTACT_CUI = "utter_enter_select_phone_contact_cui";
    public static final String UTTER_ENTER_SELECT_PHONE_SIM_CUI = "utter_enter_select_phone_sim_cui";
    public static final String CUI_SELECT_PHONE_CONTACT = "cui_select_phone_contact";
    public static final String CUI_SELECT_PHONE_SIM = "cui_select_phone_sim";
    private List<ContactInfo> mContactInfos;
    private PhoneCallPresenter mPhoneCallPresenter;
    private ScreenRender mScreenRender;
    private PhoneCardSelectCallback mCardSelectCallback;
    private IPhoneCallImpl mPhonecallImpl;

    public PhoneCallDirectiveListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
        mPhonecallImpl = new IPhoneCallImpl();
        mPhoneCallPresenter = iBaseFunction.getPhoneCallPresenter();
        mScreenRender = iBaseFunction.getScreenRender();
        mCardSelectCallback = new PhoneCardSelectCallback() {
            @Override
            public void onSelectContact(String phoneNumber) {
                PhoneCallDirectiveListener.this.initiatePhoneSimSelect(phoneNumber);
            }

            @Override
            public void onSelectSimCard(String simSlot) {

            }
        };
        mPhoneCallPresenter.setPhoneCardSelectCallback(mCardSelectCallback);
    }

    @Override
    public void onPhoneCallByName(PhonecallByNamePayload payload) {
        LogUtil.d(TAG,"onPhoneCallByName(), payload = " + payload);
        phoneCallDirectiveReceived(assembleContactInfoByName(payload));
    }

    @Override
    public void onSelectCallee(SelectCalleePayload payload) {
        LogUtil.d(TAG,"onSelectCallee(), payload = " + payload);
        phoneCallDirectiveReceived(assembleContactInfoByNumber(payload));
    }

    @Override
    public void onPhoneCallByNumber(PhonecallByNumberPayload payload) {
        LogUtil.d(TAG,"onPhoneCallByNumber(), payload = " + payload);
        phoneCallDirectiveReceived(assembleContactInfoBySingleNumber(payload));
    }

    //TODO Deprecated. Should migrate to new SDK
    public void phoneCallDirectiveReceived(List<ContactInfo> list) {
        LogUtil.d(TAG, "phoneCallDirectiveReceived: " + list.toString());
//        directive.getName();
//        PhonecallByNamePayload namePayload = (PhonecallByNamePayload) directive.getPayload();
//        List<CandidateCallee> callees = namePayload.getCandidateCallees();
//        mPhoneCallPresenter.procMultiNameContact((String[]) callees.toArray());

        mContactInfos = list;
        initiatePhoneContactSelect(mContactInfos);
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
                    initiatePhoneSimSelect(phoneNumber);
                } else {
                    CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
//                    mPhoneCallPresenter.callPhone(phoneNumber, "1");
//                    playTTS("正在为您呼叫", UTTER_READY_TO_CALL, this, true);
                    mPhoneCallPresenter.readyToCallPhone();
                }

            } else if(contents.length == 2) {
                String asrResult = mScreenRender.getAsrResult();
                if(TextUtils.equals(asrResult, "卡已") || TextUtils.equals(asrResult, "卡伊")) {
                    mScreenRender. modifyLastTextInScreen("卡1");
                } else if(TextUtils.equals(asrResult, "卡尔") || TextUtils.equals(asrResult, "卡而")) {
                    mScreenRender.modifyLastTextInScreen("卡2");
                }
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                String simId = contents[1].substring(contents[1].indexOf("=") + 1);
                LogUtil.d(TAG, "customUserInteractionDirectiveReceived phoneNumber= " + phoneNumber + " simId= " + simId);
                mPhoneCallPresenter.disableSelectSimCard();
//                mPhoneCallPresenter.callPhone(phoneNumber, simId);
                mPhoneCallPresenter.setContactInfo(phoneNumber, simId);
                mPhoneCallPresenter.readyToCallPhone();
//                playTTS("正在为您呼叫", UTTER_READY_TO_CALL, this, true);
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
    private void initiatePhoneContactSelect(final List<ContactInfo> contactInfos) {
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
                            LogUtil.d(TAG, "initiatePhoneContactSelect url= " + url + " index= " + index);
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
    private void initiatePhoneSimSelect(final String phoneNumber) {
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

    private List<ContactInfo> assembleContactInfoByName(Payload payload) {
        List<ContactInfo> infos = new ArrayList();
        if ((payload instanceof PhonecallByNamePayload))
        {
            List<CandidateCallee> recommendNames = ((PhonecallByNamePayload)payload).getCandidateCallees();

            String simCard = ((PhonecallByNamePayload)payload).getUseSimIndex();

            String carrierInfo = ((PhonecallByNamePayload)payload).getUseCarrier();
            for (int i = 0; i < recommendNames.size(); i++)
            {
                List<ContactInfo> oneTmpInfo = this.mPhonecallImpl.getPhoneContactsByName(recommendNames.get(i), simCard, carrierInfo);
                infos.addAll(oneTmpInfo);
            }
        }
        return infos;
    }

    private List<ContactInfo> assembleContactInfoByNumber(Payload payload) {
        List<ContactInfo> infos = new ArrayList();
        if ((payload instanceof SelectCalleePayload))
        {
            List<CandidateCalleeNumber> numbers = ((SelectCalleePayload)payload).getCandidateCallees();
            String simIndex = ((SelectCalleePayload)payload).getUseSimIndex();
            String carrierInfo = ((SelectCalleePayload)payload).getUseCarrier();
            for (int i = 0; i < numbers.size(); i++)
            {
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setType(ContactInfo.TYPE_NUMBER);
                contactInfo.setName(((CandidateCalleeNumber)numbers.get(i)).getDisplayName());

                List<ContactInfo.NumberInfo> numberList = new ArrayList();
                ContactInfo.NumberInfo numberInfo = new ContactInfo.NumberInfo();
                numberInfo.setPhoneNumber(((CandidateCalleeNumber)numbers.get(i)).getPhoneNumber());
                numberList.add(numberInfo);
                contactInfo.setPhoneNumbersList(numberList);
                if (!TextUtils.isEmpty(simIndex)) {
                    contactInfo.setSimIndex(simIndex);
                }
                if (!TextUtils.isEmpty(carrierInfo)) {
                    contactInfo.setCarrierOprator(carrierInfo);
                }
                infos.add(contactInfo);
            }
        }
        return infos;
    }

    private List<ContactInfo> assembleContactInfoBySingleNumber(Payload payload) {
        List<ContactInfo> infos = new ArrayList();
        if ((payload instanceof PhonecallByNumberPayload))
        {
            CandidateCalleeNumber callee = ((PhonecallByNumberPayload)payload).getCallee();
            String simIndex = ((PhonecallByNumberPayload)payload).getUseSimIndex();
            String carrierInfo = ((PhonecallByNumberPayload)payload).getUseCarrier();
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setType(ContactInfo.TYPE_NUMBER);
            contactInfo.setName(callee.getDisplayName());

            List<ContactInfo.NumberInfo> numberList = new ArrayList();
            ContactInfo.NumberInfo numberInfo = new ContactInfo.NumberInfo();
            numberInfo.setPhoneNumber(callee.getPhoneNumber());
            numberList.add(numberInfo);
            contactInfo.setPhoneNumbersList(numberList);
            if (!TextUtils.isEmpty(simIndex)) {
                contactInfo.setSimIndex(simIndex);
            }
            if (!TextUtils.isEmpty(carrierInfo)) {
                contactInfo.setCarrierOprator(carrierInfo);
            }
            infos.add(contactInfo);
        }
        return infos;
    }

    //PhonecallPresenter的回调接口
    public interface PhoneCardSelectCallback {
        void onSelectContact(String phoneNumber);
        void onSelectSimCard(String simSlot);
    }
}
