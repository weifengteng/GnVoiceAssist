package com.gionee.voiceassist;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.duer.dcs.devicemodule.audioplayer.AudioPlayerDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.system.SystemDeviceModule;
import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.internalapi.IDcsRequestBodySentListener;
import com.baidu.duer.dcs.framework.internalapi.IDirectiveReceivedListener;
import com.baidu.duer.dcs.framework.internalapi.IErrorListener;
import com.baidu.duer.dcs.framework.message.DcsRequestBody;
import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.alarm.AlarmDirectiveListener;
import com.gionee.voiceassist.directiveListener.applauncher.AppLauncherListener;
import com.gionee.voiceassist.directiveListener.audioplayer.AudioPlayerListener;
import com.gionee.voiceassist.directiveListener.audioplayer.LocalAudioPlayerListener;
import com.gionee.voiceassist.directiveListener.contacts.ContactsDirectiveListener;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CUIDirectiveListener;
import com.gionee.voiceassist.directiveListener.devicecontrol.DeviceControlListener;
import com.gionee.voiceassist.directiveListener.devicemodule.DeviceModuleListener;
import com.gionee.voiceassist.directiveListener.location.LocationHandler;
import com.gionee.voiceassist.directiveListener.offlineasr.OfflineAsrListener;
import com.gionee.voiceassist.directiveListener.phonecall.PhoneCallDirectiveListener;
import com.gionee.voiceassist.directiveListener.screen.ScreenDirectiveListener;
import com.gionee.voiceassist.directiveListener.sms.SmsDirectiveListener;
import com.gionee.voiceassist.directiveListener.telecontroller.TeleControllerListener;
import com.gionee.voiceassist.directiveListener.tvLive.TvLiveListener;
import com.gionee.voiceassist.directiveListener.voiceinput.AsrVoiceInputListener;
import com.gionee.voiceassist.directiveListener.voiceinputvolume.VoiceInputVolumeListener;
import com.gionee.voiceassist.directiveListener.webbrowser.WebBrowserListener;
import com.gionee.voiceassist.sdk.SdkManager;
import com.gionee.voiceassist.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.voiceassist.sdk.module.applauncher.AppLauncherDeviceModule;
import com.gionee.voiceassist.sdk.module.contacts.ContactsDeviceModule;
import com.gionee.voiceassist.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.voiceassist.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.voiceassist.sdk.module.offlineasr.OffLineDeviceModule;
import com.gionee.voiceassist.sdk.module.phonecall.PhoneCallDeviceModule;
import com.gionee.voiceassist.sdk.module.screen.ScreenDeviceModule;
import com.gionee.voiceassist.sdk.module.sms.SmsDeviceModule;
import com.gionee.voiceassist.sdk.module.telecontroller.ApiConstants;
import com.gionee.voiceassist.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.voiceassist.sdk.module.webbrowser.WebBrowserDeviceModule;
import com.gionee.voiceassist.tts.TtsEventListener;
import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.T;

/**
 * Created by twf on 2017/8/24.
 */

public class DirectiveListenerManager {
    public final String TAG = DirectiveListenerManager.class.getSimpleName();

    private IBaseFunction baseFunctionManager;
    private PhoneCallDirectiveListener phoneCallDirectiveListener;
    private SmsDirectiveListener smsDirectiveListener;
    private TtsEventListener ttsEventListener;
    private CUIDirectiveListener cuiDirectiveListener;
    private VoiceInputVolumeListener voiceInputVolumeListener;
    private AudioPlayerListener audioPlayerListener;
    private DeviceModuleListener deviceModuleListener;
    private ContactsDirectiveListener contactsDirectiveListener;
    private AppLauncherListener appLauncherListener;
    private WebBrowserListener webBrowserListener;
    private AlarmDirectiveListener alarmDirectiveListener;
    private DeviceControlListener deviceControlListener;
    private ScreenDirectiveListener screenDirectiveListener;
    private TeleControllerListener teleControllerListener;
    private TvLiveListener tvLiveListener;
    private LocationHandler locationHandler;
    private OfflineAsrListener offlineAsrListener;
    private AsrVoiceInputListener asrVoiceInputListener;
    private LocalAudioPlayerListener localAudioPlayerListener;


    public DirectiveListenerManager(IBaseFunction baseFunctionManager) {
        this.baseFunctionManager = baseFunctionManager;
    }

    public  void registerDirectiveListener() {
//        DcsSDK.getInstance().getAsr().registOfflineListener(offlineAsrListener);
//        DcsSDK.getInstance().getAudioRecord().registerRecordListener(voiceInputVolumeListener);

        SdkManager.getInstance().getSdkInternalApi().addDirectiveReceivedListener(new IDirectiveReceivedListener() {
            @Override
            public void onDirective(Directive directive) {
                if (!TextUtils.isEmpty(directive.rawMessage)) {
                    Log.v(TAG, "directive-rawMessage:" + directive.rawMessage);
                }
                String name = directive.getName();
                Log.v(TAG, "directive-name:" + name);
            }
        });

        //初始化错误回调接口
        SdkManager.getInstance().getSdkInternalApi().addErrorListener(new IErrorListener() {
            @Override
            public void onErrorCode(ErrorCode errorCode) {
                ErrorHelper.sendError(com.gionee.voiceassist.util.ErrorCode.SDK_INTERNAL_ERROR,
                        "SDK内部错误。错误信息：" + errorCode.getMessage());
            }
        });

        //初始化事件监听回调接口
        SdkManager.getInstance().getSdkInternalApi().addRequestBodySentListener(new IDcsRequestBodySentListener() {
            @Override
            public void onDcsRequestBody(DcsRequestBody event) {
                String eventName = event.getEvent().getHeader().getName();
                Log.v(TAG, "eventName:" + eventName);

                //处理TTS状态回调
                if (eventName.equals("SpeechStarted")) {
                    // online tts start
                } else if (eventName.equals("SpeechFinished")) {
                    // online tts finish
                }
            }
        });

        //初始化对话回调接口
        SdkManager.getInstance().getSdkInstance().getVoiceRequest().addDialogStateListener(asrVoiceInputListener);
        SdkManager.getInstance().getSdkInternalApi().setLocationHandler(locationHandler);
        ((AudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.audio_player")).addAudioPlayListener(audioPlayerListener);
        ((SystemDeviceModule)getDeviceModule("ai.dueros.device_interface.system")).addModuleListener(deviceModuleListener);
        ((PhoneCallDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.telephone")).addPhoneCallListener(phoneCallDirectiveListener);
        ((SmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.sms")).addSmsListener(smsDirectiveListener);
        ((ContactsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.contacts")).addContactsListener(contactsDirectiveListener);
        ((AppLauncherDeviceModule)getDeviceModule("ai.dueros.device_interface.app_launcher")).addAppLauncherDirectiveListener(appLauncherListener);
        ((WebBrowserDeviceModule)getDeviceModule("ai.dueros.device_interface.web_browser")).addDirectiveListener(webBrowserListener);
        ((AlarmsDeviceModule)getDeviceModule("ai.dueros.device_interface.android.alerts")).addDirectiveListener(alarmDirectiveListener);
        ((DeviceControlDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.device_control")).addDirectiveListener(deviceControlListener);
        ((ScreenDeviceModule)getDeviceModule("ai.dueros.device_interface.screen")).addScreenListener(screenDirectiveListener);
        ((CustomUserInteractionDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction")).setCustomUserInteractionDirectiveListener(cuiDirectiveListener);
        ((LocalAudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player")).addLocalAudioPlayerListener(localAudioPlayerListener);
        ((VoiceOutputDeviceModule)getDeviceModule("ai.dueros.device_interface.voice_output")).addVoiceOutputListener(ttsEventListener);
        ((OffLineDeviceModule)getDeviceModule("ai.dueros.device_interface.offline")).addOfflineDirectiveListener(offlineAsrListener);
        ((TeleControllerDeviceModule)getDeviceModule(com.gionee.voiceassist.sdk.module.telecontroller.ApiConstants.NAMESPACE)).addDirectivieListener(teleControllerListener);
    }

    public void unRegisterDirectiveListener() {
//        DcsSDK.getInstance().getAsr().registAsrVoiceInputListener(null);
//        DcsSDK.getInstance().getAsr().registOfflineListener(null);

        ((AudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.audio_player")).removeAudioPlayListener(audioPlayerListener);
        ((SystemDeviceModule)getDeviceModule("ai.dueros.device_interface.system")).removeListener(deviceModuleListener);
        ((PhoneCallDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.telephone")).removePhoneCallListener(phoneCallDirectiveListener);
        ((SmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.sms")).removeSmsListener(smsDirectiveListener);
        ((ContactsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.contacts")).removeContactsListener(contactsDirectiveListener);
        ((AppLauncherDeviceModule)getDeviceModule("ai.dueros.device_interface.app_launcher")).addAppLauncherDirectiveListener(null);
        ((WebBrowserDeviceModule)getDeviceModule("ai.dueros.device_interface.web_browser")).addDirectiveListener(null);
        ((AlarmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.alert_smartphone")).removeDirectiveLIstener(alarmDirectiveListener);
        ((DeviceControlDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.device_control")).removeDirectiveListener(deviceControlListener);
        ((ScreenDeviceModule)getDeviceModule("ai.dueros.device_interface.screen")).removeScreenListener(screenDirectiveListener);
        ((TeleControllerDeviceModule)getDeviceModule(com.gionee.voiceassist.sdk.module.telecontroller.ApiConstants.NAMESPACE)).addDirectivieListener(null);
        ((CustomUserInteractionDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction")).setCustomUserInteractionDirectiveListener(null);
        ((LocalAudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player")).addLocalAudioPlayerListener(null);
        ((VoiceOutputDeviceModule)getDeviceModule("ai.dueros.device_interface.voice_output")).removeVoiceOutputListener(ttsEventListener);
        ((OffLineDeviceModule)getDeviceModule("ai.dueros.device_interface.offline")).removeOfflineDirectiveListener(offlineAsrListener);
        SdkManager.getInstance().getSdkInternalApi().setLocationHandler(null);

    }

    public void initDirectiveListener() {

        //语音识别、TTS监听器
        ttsEventListener = new TtsEventListener();

        offlineAsrListener = new OfflineAsrListener(baseFunctionManager);

        asrVoiceInputListener = new AsrVoiceInputListener(baseFunctionManager);

        //端功能监听器
        phoneCallDirectiveListener = new PhoneCallDirectiveListener(baseFunctionManager);

        smsDirectiveListener = new SmsDirectiveListener(baseFunctionManager);

        voiceInputVolumeListener = new VoiceInputVolumeListener(baseFunctionManager);

        contactsDirectiveListener = new ContactsDirectiveListener(baseFunctionManager);

        appLauncherListener = new AppLauncherListener(baseFunctionManager);

        webBrowserListener = new WebBrowserListener(baseFunctionManager);

        alarmDirectiveListener = new AlarmDirectiveListener(baseFunctionManager);

        deviceControlListener = new DeviceControlListener(baseFunctionManager);

        screenDirectiveListener = new ScreenDirectiveListener(baseFunctionManager);

        teleControllerListener = new TeleControllerListener(baseFunctionManager);

        tvLiveListener = new TvLiveListener(baseFunctionManager);

        audioPlayerListener = new AudioPlayerListener(baseFunctionManager);

        deviceModuleListener = new DeviceModuleListener(baseFunctionManager);

        localAudioPlayerListener = new LocalAudioPlayerListener(baseFunctionManager);

        //位置回调
        locationHandler = new LocationHandler(baseFunctionManager);

        //自定义多伦交互回调
        cuiDirectiveListener = new CUIDirectiveListener();
    }

    public void onDestroy(){

        unRegisterDirectiveListener();

        if(audioPlayerListener != null) {
            audioPlayerListener.onDestroy();
        }
        if(deviceModuleListener != null) {
            deviceModuleListener.onDestroy();
        }

        if(appLauncherListener != null) {
            appLauncherListener.onDestroy();
        }

        if(asrVoiceInputListener != null) {
            appLauncherListener.onDestroy();
        }

        if(baseFunctionManager != null) {
            baseFunctionManager.onDestroy();
            baseFunctionManager = null;
        }
    }

    private BaseDeviceModule getDeviceModule(String namespace) {
        return SdkManager.getInstance().getSdkInternalApi().getDeviceModule(namespace);
    }
}
