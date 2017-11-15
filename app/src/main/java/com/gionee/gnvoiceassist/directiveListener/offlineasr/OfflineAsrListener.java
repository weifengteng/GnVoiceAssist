package com.gionee.gnvoiceassist.directiveListener.offlineasr;

import android.text.TextUtils;

import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.OffLineAsrDirective;
import com.baidu.duer.dcs.offline.asr.bean.ErrorTranslation;
import com.baidu.duer.dcs.offline.asr.bean.RecogResult;
import com.baidu.duer.dcs.offline.asr.listener.IRecogListener;
import com.baidu.duer.dcs.util.NetWorkUtil;
import com.gionee.gnvoiceassist.DirectiveListenerManager;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.basefunction.offlineasr.OfflineAsrHandler;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.sdk.module.offlineasr.OffLineDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.phonecall.message.ContactInfo;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.ContactProcessor;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.T;
import com.gionee.gnvoiceassist.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twf on 2017/8/22.
 */

public class OfflineAsrListener extends BaseDirectiveListener
        implements IRecogListener,OffLineDeviceModule.IOfflineDirectiveListener {
    public static final String TAG = OfflineAsrListener.class.getSimpleName();
    public OfflineAsrHandler offlineAsrHandler;

    public OfflineAsrListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
        offlineAsrHandler = iBaseFunction.getOfflineAsrHandler();
    }

//    @Override
//    public void onEvent(AsrInterface.OfflineEvent offlineEvent, JSONObject eventJson) {
//        LogUtil.i(TAG, "DCSF-- 离线asr：event=" + offlineEvent + " " + eventJson.toString());
//        MaxUpriseCounter.resetUpriseCount();
//        if(offlineEvent == AsrInterface.OfflineEvent.ERROR) {
//            if(!NetWorkUtil.isNetworkConnected(GnVoiceAssistApplication.getInstance())) {
//                playTTS("没联网我懂得很少，你可以说打电话给小明或打开相机试试看", true);
//            }
//
//        } else if (offlineEvent == AsrInterface.OfflineEvent.FINISH) {
////            T.showShort("离线结果："
////                    + eventJson.toString());
//
//            ArrayList<String> keyList = new ArrayList<>();
//            keyList.add(Constants.SLOT_APPNAME);
//            Map<String, String> offlineResultMap = Utils.parseOfflineResult(eventJson.toString(), keyList);
//            if(offlineResultMap != null && !offlineResultMap.isEmpty()) {
//                String rawText = offlineResultMap.get(Constants.SLOT_RAW_TEXT);
//                String domain = offlineResultMap.get(Constants.SLOT_DOMAIN);
//                String intent = offlineResultMap.get(Constants.SLOT_INTENT);
//                LogUtil.d(TAG, "DCSF--------------------- rawText= " + rawText + " domain= " + domain + "   intent= " + intent);
//                if(iBaseFunction != null) {
//                    iBaseFunction.getScreenRender().renderQueryInScreen(rawText);
//                }
//
//                if(!TextUtils.isEmpty(domain)) {
//                    if(TextUtils.equals(domain, "kookong")
//                            && TextUtils.equals(intent, "control")) {
//                        // teleControll
//                        if(iBaseFunction != null) {
//                            iBaseFunction.getKookongOperator().executeVoiceCmd(rawText);
//                        }
//                    } else if(TextUtils.equals(domain, "music")
//                            && TextUtils.equals(intent, "bargin")) {
//                        // TODO: music bargin
//                        T.showShort("domain: music  intent: bargin");
//
//                    }  else if(TextUtils.equals(domain, "app")
//                            && TextUtils.equals(intent, "launch")) {
//                        String appName = offlineResultMap.get(Constants.SLOT_APPNAME);
//                        if(iBaseFunction != null) {
//                            boolean isSuccess = iBaseFunction.getAppLaunchPresenter()
//                                    .launchAppByName(appName);
//                            if(!isSuccess) {
//                                playTTS("系统中没有安装该应用", true);
//                            }
//                        }
//
//                    }  else if(TextUtils.equals(domain, "device")
//                            && TextUtils.equals(intent, "control")) {
//                        // TODO: device control
//                        if(iBaseFunction != null) {
//                            iBaseFunction.getDeviceControlOperator().operateOfflineDeviceControlCmd(rawText);
//                        }
//
//                    }  else if(TextUtils.equals(domain, "time")
//                            && TextUtils.equals(intent, "query")) {
//                        if(iBaseFunction != null) {
//                            iBaseFunction.getTimerQuery().queryNowTime();
//                        }
//
//                    }  else if(TextUtils.equals(domain, "msg")
//                            && TextUtils.equals(intent, "send")) {
//                        // TODO: msg send
//
//                    }  else if(TextUtils.equals(domain, "msg")
//                            && TextUtils.equals(intent, "cancel")) {
//                        // TODO:
//
//                    }  else if(TextUtils.equals(domain, "telephone")
//                            && TextUtils.equals(intent, "call")) {
//                        String contactname = offlineResultMap.get(Constants.SLOT_CONTACTNAME);
//                        if(!TextUtils.isEmpty(contactname)) {
//
//                        } else {
//                            // TODO: contact name is empty
//                        }
//                        // TODO:
//
//                    }  else if(TextUtils.equals(domain, "telephone")
//                            && TextUtils.equals(intent, "cancel")) {
//
//                    }  else if(TextUtils.equals(domain, "select")
//                            && TextUtils.equals(intent, "operate")) {
//
//                    }
//                }
//            }
//        }
//    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {

    }

    @Override
    public void onAsrReady() {

    }

    @Override
    public void onAsrBegin() {

    }

    @Override
    public void onAsrEnd() {

    }

    @Override
    public void onAsrPartialResult(String[] strings, RecogResult recogResult) {

    }

    @Override
    public void onAsrFinalResult(String[] strings, RecogResult recogResult) {

    }

    @Override
    public void onAsrFinish(RecogResult recogResult) {
        List<String> keyList = Utils.getParseOfflineKeyList();
        Map<String, String> offlineResultMap = Utils.parseOfflineResult(recogResult.getOrigalJson(), (ArrayList<String>) keyList);
        if(offlineResultMap != null && !offlineResultMap.isEmpty()) {
            String rawText = offlineResultMap.get(Constants.SLOT_RAW_TEXT);
            String domain = offlineResultMap.get(Constants.SLOT_DOMAIN);
            String intent = offlineResultMap.get(Constants.SLOT_INTENT);
            LogUtil.d(TAG, "DCSF--------------------- rawText= " + rawText + " domain= " + domain + "   intent= " + intent);
            if(iBaseFunction != null) {
                iBaseFunction.getScreenRender().renderQueryInScreen(rawText);
            }

            if(!TextUtils.isEmpty(domain)) {
                offlineAsrHandler.dispatchOfflineAsr(domain,intent,rawText,offlineResultMap);
            }
        }
    }

    @Override
    public void onAsrFinishError(int i, String s, String s1) {
        if(!NetWorkUtil.isNetworkConnected(GnVoiceAssistApplication.getInstance())) {
            playTTS("没联网我懂得很少，你可以说打电话给小明或打开相机试试看", true);
        }
        LogUtil.e(TAG, "onAsrFinishError(), 错误信息：" + s + s1);
    }

    @Override
    public void onAsrLongFinish() {

    }

    @Override
    public void onAsrVolume(int i, int i1) {

    }

    @Override
    public void onAsrAudio(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onAsrExit() {

    }

    @Override
    public void onAsrCancel() {

    }

    @Override
    public void onAsrOnlineNluResult(String s) {

    }

    @Override
    public void onOfflineLoaded() {

    }

    @Override
    public void onOfflineUnLoaded() {
        if(!NetWorkUtil.isNetworkConnected(GnVoiceAssistApplication.getInstance())) {
            playTTS("没联网我懂得很少，你可以说打电话给小明或打开相机试试看", true);
        }
    }

    @Override
    public void onDirectiveReceived(OffLineAsrDirective directive) {

        String offlineData = directive.offLineData;
        RecogResult recogResult = RecogResult.parseJson(offlineData);
        if (recogResult.hasError())
        {
            int errorCode = recogResult.getError();
            LogUtil.e("RecogEventAdapter", "asr error:" + offlineData);
            this.onAsrFinishError(errorCode, ErrorTranslation.recogError(errorCode), recogResult.getDesc());
        }
        else
        {
            if (recogResult.isFinalResult()) {
                this.onAsrFinish(recogResult);
            }
        }
    }
}
