package com.gionee.gnvoiceassist.directiveListener.phonecall;

import android.text.TextUtils;

import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.util.CommonUtil;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.customlink.CustomLinkSchema;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.gnvoiceassist.message.io.DirectiveResponseGenerator;
import com.gionee.gnvoiceassist.message.io.MetadataParser;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.ContactsMetadata;
import com.gionee.gnvoiceassist.message.model.metadata.PhonecallMetadata;
import com.gionee.gnvoiceassist.sdk.module.phonecall.IPhoneCallImpl;
import com.gionee.gnvoiceassist.sdk.module.phonecall.PhoneCallDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.phonecall.message.CandidateCallee;
import com.gionee.gnvoiceassist.sdk.module.phonecall.message.CandidateCalleeNumber;
import com.gionee.gnvoiceassist.sdk.module.phonecall.message.ContactInfo;
import com.gionee.gnvoiceassist.sdk.module.phonecall.message.PhonecallByNamePayload;
import com.gionee.gnvoiceassist.sdk.module.phonecall.message.PhonecallByNumberPayload;
import com.gionee.gnvoiceassist.sdk.module.phonecall.message.SelectCalleePayload;
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.SharedData;
import com.gionee.gnvoiceassist.util.constants.ActionConstants.PhonecallAction;
import com.gionee.gnvoiceassist.util.constants.UsecaseConstants;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnvoiceassist.util.Utils.doUserActivity;

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
//    private PhoneCallPresenter mPhoneCallPresenter;
    private PhoneCardSelectCallback mCardSelectCallback;
    private IPhoneCallImpl mPhonecallImpl;

    public PhoneCallDirectiveListener(IDirectiveListenerCallback callback) {
        super(callback);
        // 原版本将此回调接口使用PhonecallPresenter.setPhoneCardSelectCallback()方法传递给Presenter
        mPhonecallImpl = new IPhoneCallImpl();
        mCardSelectCallback = new PhoneCardSelectCallback() {
            @Override
            public void onSelectContact(String phoneNumber) {
//                PhoneCallDirectiveListener.this.initiatePhoneSimSelect(phoneNumber);
            }

            @Override
            public void onSelectSimCard(String simSlot) {

            }
        };
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


    private void phoneCallDirectiveReceived(List<ContactInfo> list) {
        LogUtil.d(TAG, "phoneCallDirectiveReceived: " + list.toString());

        mContactInfos = list;
        PhonecallMetadata metadata = new PhonecallMetadata();
        List<ContactsMetadata> contactsInfos = new ArrayList<>();
        for (ContactInfo info:list) {
            ContactsMetadata transformContactInfo = new ContactsMetadata();
            transformContactInfo.setName(info.getName());
            List<String> numbers = new ArrayList<>();
            for (ContactInfo.NumberInfo numberInfo:info.getPhoneNumbersList()) {
                //TODO 这里结构很复杂，必须处理
                numbers.add(numberInfo.getPhoneNumber());
            }
            transformContactInfo.setNumber(numbers);
            contactsInfos.add(transformContactInfo);
        }
        metadata.setContacts(contactsInfos);
        DirectiveResponseEntity response = new DirectiveResponseGenerator("phonecall")
                .setAction("request_call")
                .setInCustomInteractive(false)
                .setMetadata(metadata.toJson())
                .build();
        mCallback.onDirectiveResponse(response);
    }

    @Override
    public void handleCUInteractionUnknownUtterance(String id) {
        super.handleCUInteractionUnknownUtterance(id);
        String alert = "";
        MaxUpriseCounter.increaseUpriseCount();
        if(TextUtils.equals(id, CUI_SELECT_PHONE_CONTACT)) {
            if(MaxUpriseCounter.isMaxCount()) {
                alert = "太累了,我先休息一下";
//                mPhoneCallPresenter.disableSelectContact();
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                playTTS(alert, true);
            } else if(TextUtils.isEmpty(alert)) {
                alert = "选择第几条？";
                playTTS(alert, UTTER_ENTER_SELECT_PHONE_CONTACT_CUI, this, true);
            }

        } else if(TextUtils.equals(id, CUI_SELECT_PHONE_SIM)) {
            if(MaxUpriseCounter.isMaxCount()) {
                alert = "太累了,我先休息一下";
//                mPhoneCallPresenter.disableSelectSimCard();
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

            DirectiveResponseGenerator builder = new DirectiveResponseGenerator(UsecaseConstants.UsecaseAlias.PHONECALL);

            PhonecallMetadata metadata = MetadataParser.toEntity
                    (CustomUserInteractionManager.getInstance().getCustomInteractorMetadata(),PhonecallMetadata.class);
            CustomUserInteractionManager.getInstance().clearCustomInteractorMetadata();

            if(contents.length == 1) {
                //有电话号码
//                mPhoneCallPresenter.disableSelectContact();
//                mPhoneCallPresenter.setContactInfo(phoneNumber, null);

//                if(mPhoneCallPresenter.isNeedChoosePhoneSim()) {
//                    initiatePhoneSimSelect(phoneNumber);
//                } else {
//                    CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
////                    mPhoneCallPresenter.callPhone(phoneNumber, "1");
////                    playTTS("正在为您呼叫", UTTER_READY_TO_CALL, this, true);
//                    mPhoneCallPresenter.readyToCallPhone();
//                }

                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                List<ContactsMetadata> contactsInfos = new ArrayList<>();
                contactsInfos.add(new ContactsMetadata("",phoneNumber));
                metadata.setContacts(contactsInfos);

            } else if(contents.length == 2) {
                //选卡
//                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                String simId = contents[1].substring(contents[1].indexOf("=") + 1);
                LogUtil.d(TAG, "customUserInteractionDirectiveReceived phoneNumber= " + phoneNumber + " simId= " + simId);
//                mPhoneCallPresenter.disableSelectSimCard();
//                mPhoneCallPresenter.callPhone(phoneNumber, simId);
//                mPhoneCallPresenter.setContactInfo(phoneNumber, simId);
//                mPhoneCallPresenter.readyToCallPhone();
//                playTTS("正在为您呼叫", UTTER_READY_TO_CALL, this, true);

                //设置选择的联系人
                CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
                List<ContactsMetadata> contactsInfos = new ArrayList<>();
                contactsInfos.add(new ContactsMetadata("",phoneNumber));
                metadata.setContacts(contactsInfos);
                metadata.setSimSlot(simId);

                //TODO 取得Query的内容
//                String asrResult = iBaseFunction.getScreenRender().getAsrResult();  //取得Query的内容
//                if(TextUtils.equals(asrResult, "卡已") || TextUtils.equals(asrResult, "卡伊")) {
//                    iBaseFunction.getScreenRender().renderQueryInScreen("卡一");
//                } else if(TextUtils.equals(asrResult, "卡尔") || TextUtils.equals(asrResult, "卡而")) {
//                    iBaseFunction.getScreenRender().renderQueryInScreen("卡二");
//                }
            }
            switch (id) {
                case PhonecallAction.ACTION_QUERY_MULTI_NUMBER:
                    builder.setAction(PhonecallAction.ACTION_CUI_MULTI_NUMBER);
                    break;
                case PhonecallAction.ACTION_QUERY_SIMSLOT:
                    builder.setAction(PhonecallAction.ACTION_CUI_SIMSLOT);
                    break;
            }
            builder.setInCustomInteractive(false)
                    .setMetadata(metadata.toJson());
            DirectiveResponseEntity response = builder.build();
            mCallback.onDirectiveResponse(response);
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
