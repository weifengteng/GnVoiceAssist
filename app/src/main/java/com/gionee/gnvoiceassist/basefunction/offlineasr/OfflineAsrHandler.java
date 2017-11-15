package com.gionee.gnvoiceassist.basefunction.offlineasr;

import android.text.TextUtils;

import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.statemachine.Scene;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.ContactProcessor;
import com.gionee.gnvoiceassist.util.SharedData;
import com.gionee.gnvoiceassist.util.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyingheng on 11/14/17.
 */

public class OfflineAsrHandler extends BasePresenter{

    IBaseFunction iBaseFunction;
    OfflineAsrHandlerCallback mCallback;

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
//                stopCustomInteract();
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
                    mCallback.requestSpeak("系统中没有安装该应用", true);
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
                mCallback.requestSpeak("您要选择第几条？",true);
                List<String> utterances = new ArrayList<>();
                List<String> utteranceExtraInfos = new ArrayList<>();
                for (int i = 1; i <= phoneNumList.size(); i++) {
                    utterances.add("第" + i + "条");
                    utteranceExtraInfos.add("phone://num=" + phoneNumList.get(i - 1));
                }
                startCustomInteract(Scene.WAIT_CHOOSE_PHONE_TYPE_TO_CALL,utterances,utteranceExtraInfos);
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
            switch (SharedData.getInstance().getCurrentCuiScene()) {
                case WAIT_CHOOSE_PHONE_TYPE_TO_CALL: {
                    List<String> extraInfo = SharedData.getInstance().getUtteranceExtraInfo();
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

    private void startCustomInteract(Scene scene, List<String> utterances, List<String> utteranceExtraInfo) {
        // 1.根据当前场景，设置多轮交互的场景
        // 2.通知SharedData这一轮交互用户可能回答的命令词（用来区分用户所回答的内容是否相关）
        // 3.设置界面需要显示的文字、显示文字等界面操作
        // 3.重置多轮交互计数器
        MaxUpriseCounter.resetUpriseCount();
        SharedData.getInstance().setCurrentCuiScene(scene);
        SharedData.getInstance().setUtteranceWords(utterances);
        SharedData.getInstance().setUtteranceExtraInfo(utteranceExtraInfo);
    }

    private void stopCustomInteract() {
        MaxUpriseCounter.resetUpriseCount();
        SharedData.getInstance().setCurrentCuiScene(Scene.IDLE);
    }

    private boolean isInCustomInteract() {
        return SharedData.getInstance().getCurrentCuiScene() == Scene.IDLE;
    }

    private boolean isRelevantTopic(String reply) {
        List<String> cuiUtterances = SharedData.getInstance().getUtteranceWords();
        for (String utterance:cuiUtterances) {
            if (TextUtils.equals(reply,utterance)) {
                return true;
            }
        }

        return false;
    }

    public interface OfflineAsrHandlerCallback {
        void requestSpeak(String text, boolean displaySpeakText);
    }

}
