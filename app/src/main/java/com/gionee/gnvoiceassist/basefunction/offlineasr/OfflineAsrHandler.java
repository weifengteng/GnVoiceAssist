package com.gionee.gnvoiceassist.basefunction.offlineasr;

import android.text.TextUtils;

import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.ContactProcessor;
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
                //TODO: 实现选联系人、选卡多轮交互
                iBaseFunction.getPhoneCallPresenter().showPhoneContactSelectView
                        (ContactProcessor.getContactProcessor().assembleContactInfoByNumber(phoneNumsMap));
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
            //TODO: 实现取消打电话的功能
            iBaseFunction.getPhoneCallPresenter().cancelCallPhone();
        }
    }

    private void handleSelect(String intent, String rawText, Map<String, String> resultMap) {
        if(TextUtils.equals(intent, "operate")) {

        }
    }

    public interface OfflineAsrHandlerCallback {
        void requestSpeak(String text, boolean displaySpeakText);
    }

}
