package com.gionee.voiceassist.usecase.phonecall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.gionee.voiceassist.coreservice.datamodel.PhoneCallDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.phonecall.message.ContactInfo;
import com.gionee.voiceassist.customlink.CustomLinkSchema;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.voiceassist.directiveListener.phonecall.PhoneCallDirectiveListener;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.CUInteractionUrlParser;
import com.gionee.voiceassist.util.ContactProcessor;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.widget.ContactsListItem;
import com.gionee.voiceassist.widget.SimCardListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twf on 2017/8/28.
 *
 * 处理打电话操作
 * 类中进行的操作
 * （1）底层实现拨打电话
 * （2）控制选号、选卡显示的渲染
 *
 */

public class PhoneCallUsecase extends BaseUsecase {
    public static final String TAG = PhoneCallUsecase.class.getSimpleName();
    public static final String UTTER_READY_TO_CALL = "utter_ready_to_call";
    public static final String UTTER_CHOOSE_CONTACT = "utter_choose_contact";
    public static final String UTTER_CHOOSE_CONTACT_CUI = "utter_choose_contact_cui";
    public static final String UTTER_CHOOSE_PHONE_SIM = "utter_choose_phone_sim";
    public static final String UTTER_ENTER_SELECT_PHONE_SIM_CUI = "utter_enter_select_phone_sim_cui";
    private RecordController recordController;
    private PhoneCallDirectiveListener.PhoneCardSelectCallback mPhoneSimQueryInterface;
    private boolean isContactSelectViewCanClick = false;
    private boolean isSimCardSelectViewCanClick = false;
    private HashMap<String, Integer> mSimNameMap = new HashMap<String, Integer>();
    private String mPhoneNumber = "";
    private String mSimId = "";
    private Context mAppCtx;
    private PhoneCallUseCaseHelper mPhoneCallHelper;

    public PhoneCallUsecase() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
        mPhoneCallHelper = new PhoneCallUseCaseHelper();
    }

    public void setPhoneCardSelectCallback(PhoneCallDirectiveListener.PhoneCardSelectCallback simSelectInterface) {
        this.mPhoneSimQueryInterface = simSelectInterface;
    }

    /*@Override
    public void onSpeakFinish(String utterId) {
        super.onSpeakFinish(utterId);
        if(TextUtils.equals(utterId, UTTER_READY_TO_CALL)) {
            callPhone();
        }

    }*/

    public void setContactInfo(String phoneNumber, String simId) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            this.mPhoneNumber = phoneNumber;
        }
        this.mPhoneNumber = phoneNumber;
        if (!TextUtils.isEmpty(simId)) {
            this.mSimId = simId;
        }
    }

    public void readyToCallPhone() {
//        playAndRenderText("正在为您呼叫", UTTER_READY_TO_CALL, this, true);
        playAndRenderText("正在为您呼叫", UTTER_READY_TO_CALL, new SimpleTtsCallback() {
            @Override
            public void onSpeakFinish(String utterId) {
                super.onSpeakFinish(utterId);
                if (TextUtils.equals(utterId, UTTER_READY_TO_CALL)) {
                    callPhone();
                }
            }
        });
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
                if (ActivityCompat.checkSelfPermission(mAppCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
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
//        screenRender.renderInfoPanel(contactList.getView());
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
//        screenRender.renderInfoPanel(simcardlist.getView());
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        PhoneCallDirectiveEntity phoneCallDirectiveEntity = (PhoneCallDirectiveEntity) payload;
        List<ContactInfo> contactInfoList = getContactInfos(phoneCallDirectiveEntity);

        if(needChoosePhoneNumber(contactInfoList)) {
            showPhoneNumberList();
            choosePhoneNumberByVoice(contactInfoList);
        } else {
            mPhoneNumber = contactInfoList.get(0).getPhoneNumbersList().get(0).getPhoneNumber();
            if(needChooseSimId()) {
                showSimChooseView();
                chooseSimByVoice(mPhoneNumber);
            }
        }
    }

    @Override
    public String getAlias() {
        return "phonecall";
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

    private List<ContactInfo> getContactInfos(PhoneCallDirectiveEntity phoneCallDirectiveEntity) {
        List<ContactInfo> contactInfoList = new ArrayList<>();
        switch (phoneCallDirectiveEntity.getAction()) {
            case BY_SELECT_CALLEE:
            case BY_NUMBER:
                contactInfoList = mPhoneCallHelper.assembleContactInfoByCalleeNumber(phoneCallDirectiveEntity);
                break;
            case BY_NAME:
                contactInfoList = mPhoneCallHelper.assembleContactInfoByName(phoneCallDirectiveEntity);
                break;
            default:
                break;
        }
        return contactInfoList;
    }

    /**
     * 在界面上显示选择电话列表
     */
    private void showPhoneNumberList() {
        // TODO:

    }

    /**
     * 语音选择 phoneNumber
     */
    private void choosePhoneNumberByVoice(final List<ContactInfo> contactInfos) {
        // TODO: 界面相关逻辑操作待实现
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator() {
            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState state) {
                LogUtil.d(TAG, "generateContextPayloadByInteractionState");
                if(getCuiController().isCUIShouldStop()) {
                    return new CustomClientContextPayload(null);
                }

                ArrayList<CustomClientContextHyperUtterace> hyperUtterances
                        = mPhoneCallHelper.generateHyperUtteranceOfPhoneNumberChoose(contactInfos);
                Payload payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };

        getCuiController().startCustomUserInteraction(generator, new ICuiResult() {
            @Override
            public void handleCUInteractionUnknownUtterance() {
                String alert;
                if(getCuiController().isCUIShouldStop()) {
                    alert = "太累了,我先休息一下";
                    // TODO: disappera choose phone number view
                    getCuiController().stopCurrentCustomUserInteraction();
                    playAndRenderText(alert);
                } else {
                    alert = "选择第几条？";
                    playAndRenderText(alert, UTTER_CHOOSE_CONTACT_CUI, new SimpleTtsCallback() {
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
                if(url.startsWith(CustomLinkSchema.LINK_PHONE)) {
                    // Phone协议eg: phone://{num=phonenumber}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}

                    Map<String, String> result = CUInteractionUrlParser.parsePhonecallUrl(url);
                    String phoneNumber = result.get("num");
                    String simId = result.get("sim");
                    boolean hasPhoneNumber = !TextUtils.isEmpty(phoneNumber);
                    boolean hasSimId = isSimValid();
                    LogUtil.d(TAG, "customUserInteractionDirectiveReceived phoneNumber= " + phoneNumber + " simId= " + simId);

                    if(!hasSimId && hasPhoneNumber) {
                        // 有电话，询问用户是否需要选卡
                        // TODO:
//                        disableSelectContact();
                        setContactInfo(phoneNumber, null);
                        if(needChooseSimId()) {
                            // TODO: choose sim id
                            chooseSimByVoice(phoneNumber);
                        } else {
                            getCuiController().stopCurrentCustomUserInteraction();
                            readyToCallPhone();
                        }

                    } else if(hasSimId && hasPhoneNumber) {
                        // 有电话，有选卡号，发起拨打电话的操作
                        // TODO: update view
                        /*String asrResult = mScreenRender.getAsrResult();
                        if(TextUtils.equals(asrResult, "卡已") || TextUtils.equals(asrResult, "卡伊")) {
                            mScreenRender. modifyLastTextInScreen("卡1");
                        } else if(TextUtils.equals(asrResult, "卡尔") || TextUtils.equals(asrResult, "卡而")) {
                            mScreenRender.modifyLastTextInScreen("卡2");
                        }*/
                        getCuiController().stopCurrentCustomUserInteraction();
                        // TODO:
//                        disableSelectSimCard();
                        setContactInfo(phoneNumber, simId);
                        readyToCallPhone();
                    }
                }
            }
        });

        playAndRenderText("选择第几条？", UTTER_CHOOSE_CONTACT, new SimpleTtsCallback() {
            @Override
            public void onSpeakFinish(String utterId) {
                super.onSpeakFinish(utterId);
                startRecord();
            }
        });
    }

    /**
     * 判断是否需要选择电话号码
     * @return
     */
    private boolean needChoosePhoneNumber(List<ContactInfo> contactInfoList) {
        if(contactInfoList.size() == 1) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否需要选 sim 卡
     */
    private boolean needChooseSimId() {
        // TODO:
        return true;
    }

    private boolean isSimValid() {
        return TextUtils.equals(mSimId, "1") || TextUtils.equals(mSimId, "2");
    }

    /**
     * 在界面上显示选择 sim 卡
     */
    private void showSimChooseView() {
        // TODO:
    }

    /**
     * 语音发起选择 sim 卡
     */
    private void chooseSimByVoice(final String phoneNumber) {
        // TODO: 界面相关逻辑操作待实现
        CustomUserInteractionDeviceModule.PayLoadGenerator generator =
                new CustomUserInteractionDeviceModule.PayLoadGenerator(){

                    @Override
                    public Payload generateContextPayloadByInteractionState(
                            CustomClicentContextMachineState customClicentContextMachineState) {

                        if(CustomUserInteractionManager.getInstance().shouldStopCurrentInteraction()) {
                            // 达到最大多轮交互次数，跳出自定义多轮交互状态
                            return new CustomClientContextPayload(null);
                        }

                        ArrayList<CustomClientContextHyperUtterace> hyperUtterances
                                = mPhoneCallHelper.generateHyperUtteranceOfSimChoose(phoneNumber);
                        Payload payload
                                = new CustomClientContextPayload(false, hyperUtterances);
                        return payload;
                    }
                };
        // 上传自定义交互
        getCuiController().startCustomUserInteraction(generator, new ICuiResult() {
            @Override
            public void handleCUInteractionUnknownUtterance() {
                String alert;
                if(getCuiController().isCUIShouldStop()) {
                    alert = "太累了,我先休息一下";
                    // TODO: disappear sim choose view
                    getCuiController().stopCurrentCustomUserInteraction();
                    playAndRenderText(alert);
                } else {
                    alert = "卡1呼叫还是卡2？";
                    playAndRenderText(alert, UTTER_ENTER_SELECT_PHONE_SIM_CUI, new SimpleTtsCallback() {
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
                if(url.startsWith(CustomLinkSchema.LINK_PHONE)) {
                    // Phone协议eg: phone://{num=phonenumber}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}

                    Map<String, String> result = CUInteractionUrlParser.parsePhonecallUrl(url);
                    String phoneNumber = result.get("num");
                    String simId = result.get("sim");
                    boolean hasPhoneNumber = !TextUtils.isEmpty(phoneNumber);
                    boolean hasSimId = !TextUtils.isEmpty(simId);
                    LogUtil.d(TAG, "customUserInteractionDirectiveReceived phoneNumber= " + phoneNumber + " simId= " + simId);
                    if(hasSimId && hasPhoneNumber) {
                        // 有电话，有选卡号，发起拨打电话的操作
                        // TODO: update view
                        /*String asrResult = mScreenRender.getAsrResult();
                        if(TextUtils.equals(asrResult, "卡已") || TextUtils.equals(asrResult, "卡伊")) {
                            mScreenRender. modifyLastTextInScreen("卡1");
                        } else if(TextUtils.equals(asrResult, "卡尔") || TextUtils.equals(asrResult, "卡而")) {
                            mScreenRender.modifyLastTextInScreen("卡2");
                        }*/
                        // TODO:
//                        disableSelectSimCard();
                        getCuiController().stopCurrentCustomUserInteraction();
                        setContactInfo(phoneNumber, simId);
                        readyToCallPhone();
                    }
                }
            }
        });

        // TODO:
//        showPhoneSimChooseView();
//        playTTS("卡1呼叫还是卡2？", UTTER_SHOW_SELECT_PHONE_SIM_VIEW,this, true);
        playAndRenderText("卡1呼叫还是卡2？", UTTER_CHOOSE_PHONE_SIM, new SimpleTtsCallback() {
            @Override
            public void onSpeakFinish(String utterId) {
                super.onSpeakFinish(utterId);
                startRecord();
            }
        });

    }
}
