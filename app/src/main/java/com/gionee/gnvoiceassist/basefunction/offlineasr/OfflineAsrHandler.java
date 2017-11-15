package com.gionee.gnvoiceassist.basefunction.offlineasr;

import android.text.TextUtils;

import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.gnvoiceassist.statemachine.Scene;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.ContactProcessor;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.SharedData;
import com.gionee.gnvoiceassist.util.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gionee.gnvoiceassist.util.Utils.doUserActivity;

/**
 * Created by liyingheng on 11/14/17.
 */

public class OfflineAsrHandler extends BasePresenter{

    private static final String UTTER_ID_CHOOSE_CONTACT = "offline_cui_choose_contact";
    private static final String UTTER_ID_CUI_IRRELEVENT = "offline_cui_irrelevent";

    private IBaseFunction iBaseFunction;
    private OfflineAsrHandlerCallback mCallback;

    public OfflineAsrHandler(IBaseFunction baseFunction) {
        super(baseFunction);
        iBaseFunction = baseFunction;
    }

    @Override
    public void onDestroy() {

    }

    public void dispatchOfflineAsr(String domain, String intent, String rawText, Map<String, String> offlineResultMap) {
        if (isInCustomInteract()) {
            if (!isRelevantTopic(rawText)) {
                handleIrreleventCustomInteract();
                return;
            }
        }

        switch (domain) {
            case "kookong":
                handleGnRemote(intent, rawText, offlineResultMap);
                break;
            case "music":
                handleMusic(intent, rawText, offlineResultMap);
                break;
            case "app":
                handleAppLaunch(intent, rawText, offlineResultMap);
                break;
            case "device":
                handleDeviceControl(intent, rawText, offlineResultMap);
                break;
            case "time":
                handleTimeQuery(intent, rawText, offlineResultMap);
                break;
            case "msg":
                handleSms(intent, rawText, offlineResultMap);
                break;
            case "telephone":
                handlePhonecall(intent, rawText, offlineResultMap);
                break;
            case "select":
                handleSelect(intent, rawText, offlineResultMap);
                break;
        }

    }

    public void setCallback(OfflineAsrHandlerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onSpeakFinish(String utterId) {
        super.onSpeakFinish(utterId);
        switch (utterId) {
            case UTTER_ID_CHOOSE_CONTACT:
            case UTTER_ID_CUI_IRRELEVENT:
            {
                if(SharedData.getInstance().isStopListenReceiving()) {
                    iBaseFunction.getRecordController().stopRecord();
                    SharedData.getInstance().setStopListenReceiving(false);
                    return;
                }
                SharedData.getInstance().setStopListenReceiving(true);
                iBaseFunction.getRecordController().startRecordOnline();
            }
        }

    }

    private void handleGnRemote(String intent, String rawText, Map<String,String> resultMap) {
        if (TextUtils.equals(intent,"control")) {
            if(iBaseFunction != null) {
                iBaseFunction.getKookongOperator().executeVoiceCmd(rawText);
            }
        }
    }

    private void handleMusic(String intent, String rawText, Map<String,String> resultMap) {
        if(TextUtils.equals(intent, "bargin")) {
            // TODO: 实现音乐播放
            T.showShort("domain: music  intent: bargin");
        }
    }

    private void handleAppLaunch(String intent, String rawText, Map<String,String> resultMap) {
        if(TextUtils.equals(intent, "launch")) {
            String appName = resultMap.get(Constants.SLOT_APPNAME);
            if (iBaseFunction != null) {
                boolean isSuccess = iBaseFunction.getAppLaunchPresenter()
                        .launchAppByName(appName);
                if (!isSuccess) {
                    playAndRenderText("系统中没有安装该应用", true);
                }
            }
        }
    }

    private void handleDeviceControl(String intent, String rawText, Map<String,String> resultMap) {
        if(TextUtils.equals(intent, "control")) {
            if (iBaseFunction != null) {
                iBaseFunction.getDeviceControlOperator().operateOfflineDeviceControlCmd(rawText);
            }
        }
    }

    private void handleTimeQuery(String intent, String rawText, Map<String,String> resultMap) {
        if(TextUtils.equals(intent, "query")) {
            if(iBaseFunction != null) {
                iBaseFunction.getTimerQuery().queryNowTime();
            }
        }
    }

    private void handleSms(String intent, String rawText, Map<String,String> resultMap) {
        if(TextUtils.equals(intent, "send")) {
            String contactName = resultMap.get(Constants.SLOT_CONTACTNAME);
            if (!TextUtils.isEmpty(contactName)) {
                //TODO: 发送短信
            }
            // TODO: msg send
        }  else if(TextUtils.equals(intent, "cancel")) {
            // TODO:

        }
    }

    private void handlePhonecall(String intent, String rawText, Map<String,String> resultMap) {
        if(TextUtils.equals(intent,"call")) {
            String contactName = resultMap.get(Constants.SLOT_CONTACTNAME);
            HashMap<String,ArrayList<String>> phoneNumsMap = ContactProcessor.getContactProcessor().getNumberByName(contactName);
            List<String> phoneNumList = new ArrayList<>();
            for (String name:phoneNumsMap.keySet()) {
                phoneNumList.addAll(phoneNumsMap.get(name));
            }

            if (phoneNumList.size() > 1) {
                iBaseFunction.getPhoneCallPresenter().showPhoneContactSelectView
                        (ContactProcessor.getContactProcessor().assembleContactInfoByNumber(phoneNumsMap));
                playAndRenderText("您要选择第几条？",UTTER_ID_CHOOSE_CONTACT,this,true);
                List<String> utterances = new ArrayList<>();
                List<String> utteranceExtraInfos = new ArrayList<>();
                for (int i = 1; i <= phoneNumList.size(); i++) {
                    utterances.add("第" + i + "条");
                    utteranceExtraInfos.add("phone://num=" + phoneNumList.get(i - 1));
                }
                CustomUserInteractionManager.getInstance().startOfflineCustomUserInteraction
                        (Scene.WAIT_CHOOSE_PHONE_TYPE_TO_CALL,
                                "您要选择第几条？",
                                utterances,
                                utteranceExtraInfos);
                return;
            }

            if(!TextUtils.isEmpty(contactName)) {
                //TODO: 实现离线打电话功能选卡
                //若phoneNums为空值怎么办？
                if (phoneNumList.size() > 0) {
                    iBaseFunction.getPhoneCallPresenter().setContactInfo(phoneNumList.get(0), "1");
                    iBaseFunction.getPhoneCallPresenter().readyToCallPhone();
                }
            } else {
                // TODO: contact name is empty
            }
            // TODO:

        }  else if(TextUtils.equals(intent,"cancel")) {
            iBaseFunction.getPhoneCallPresenter().cancelCallPhone();
        } else if (TextUtils.equals(intent,"operate")) {

        } else if (TextUtils.equals(intent,"select")){
            // 选择
            // 判断场景
            switch (CustomUserInteractionManager.getInstance().getOfflineCuiScene()) {
                case WAIT_CHOOSE_PHONE_TYPE_TO_CALL: {
                    List<String> extraInfo = CustomUserInteractionManager.getInstance().getOfflineCuiUtteranceExtraInfo();
                    List<String> phoneNumbers = new ArrayList<>();
                    for (String url:extraInfo) {
                        int beginIdx = url.indexOf(":");
                        String realContent = url.substring(beginIdx + 1);
                        String[] contents = realContent.split("#");
                        String phoneNumber = contents[0].substring(contents[0].indexOf("=") + 1);
                        phoneNumbers.add(phoneNumber);
                    }

                    int selectItem = Integer.valueOf(resultMap.get("number"));
                    if (selectItem > 0) {
                        String selectedNumber = phoneNumbers.get(selectItem - 1);
                        iBaseFunction.getPhoneCallPresenter().setContactInfo(selectedNumber,"0");
                        iBaseFunction.getPhoneCallPresenter().readyToCallPhone();
                        stopCustomInteract();
                    }

                }
                break;
                case WAIT_CHOOSE_SIM_TO_CALL: {

                }
            }
            //再判断是否有卡

            //打电话
        }
    }

    private void handleSelect(String intent, String rawText, Map<String, String> resultMap) {
        if(TextUtils.equals(intent, "operate")) {

        } if (TextUtils.equals(intent,"select")) {
            handlePhonecall(intent,rawText,resultMap);
        }
    }

    private void startCustomInteract(Scene scene, String answerWord, List<String> utterances, List<String> utteranceExtraInfo) {
        // 1.根据当前场景，设置多轮交互的场景
        // 2.通知SharedData这一轮交互用户可能回答的命令词（用来区分用户所回答的内容是否相关）
        // 3.设置界面需要显示的文字、显示文字等界面操作
        // 3.重置多轮交互计数器
//        MaxUpriseCounter.resetUpriseCount();
//        SharedData.getInstance().setCurrentCuiScene(scene);
//        SharedData.getInstance().setAnswerWord(answerWord);
//        SharedData.getInstance().setUtteranceWords(utterances);
//        SharedData.getInstance().setUtteranceExtraInfo(utteranceExtraInfo);
        CustomUserInteractionManager.getInstance()
                .startOfflineCustomUserInteraction(scene,answerWord,utterances,utteranceExtraInfo);
    }

    private void stopCustomInteract() {
        MaxUpriseCounter.resetUpriseCount();
        CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
    }

    /**
     * 处理不相关的离线交互命令
     */
    private void handleIrreleventCustomInteract() {
        //逻辑
        // 先把多轮交互计数器加一，再判断多轮交互次数是否超出。
        // 若没有超出，再次播报前一次多轮交互的提示词；
        // 若已超出，退出多轮交互
        MaxUpriseCounter.increaseUpriseCount();
        if (MaxUpriseCounter.isMaxCount()) {
            playAndRenderText("听不懂你在说什么，已取消");
            stopCustomInteract();
        } else {
            //重新播报提示词
            String answerWord = CustomUserInteractionManager.getInstance().getOfflineCuiAnswerWord();
            playAndRenderText(answerWord,UTTER_ID_CUI_IRRELEVENT,this,true);
        }
    }

    private boolean isInCustomInteract() {
        return CustomUserInteractionManager.getInstance().isCustomUserInteractionProcessing();
    }

    private boolean isRelevantTopic(String reply) {
//        List<String> cuiUtterances = SharedData.getInstance().getUtteranceWords();
        List<String> cuiUtterances = CustomUserInteractionManager.getInstance().getOfflineCuiUtteranceWord();
        for (String utterance:cuiUtterances) {
            if (TextUtils.equals(reply,utterance)) {
                return true;
            }
        }

        return false;
    }

    public interface OfflineAsrHandlerCallback {
        void requestSpeak(String text, boolean displaySpeakText);
        void requestSpeak(String text, String utteranceId, boolean displaySpeakText);
    }

}
