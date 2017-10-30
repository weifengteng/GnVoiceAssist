package com.gionee.gnvoiceassist;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.duer.dcs.devicemodule.audioplayer.AudioPlayerDeviceModule;
import com.baidu.duer.dcs.devicemodule.contacts.ContactsDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.phonecall.PhoneCallDeviceModule;
import com.baidu.duer.dcs.devicemodule.sms.SmsDeviceModule;
import com.baidu.duer.dcs.devicemodule.system.SystemDeviceModule;
import com.baidu.duer.dcs.devicemodule.ttsoutput.TtsOutputDeviceModule;
import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.internalApi.IDcsRequestBodySentListener;
import com.baidu.duer.dcs.framework.internalApi.IDirectiveReceivedListener;
import com.baidu.duer.dcs.framework.internalApi.IErrorListener;
import com.baidu.duer.dcs.framework.message.DcsRequestBody;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.alarm.AlarmDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.applauncher.AppLauncherListener;
import com.gionee.gnvoiceassist.directiveListener.audioplayer.AudioPlayerListener;
import com.gionee.gnvoiceassist.directiveListener.audioplayer.LocalAudioPlayerListener;
import com.gionee.gnvoiceassist.directiveListener.contacts.ContactsDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.CUIDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.devicecontrol.DeviceControlListener;
import com.gionee.gnvoiceassist.directiveListener.devicemodule.DeviceModuleListener;
import com.gionee.gnvoiceassist.directiveListener.location.LocationHandler;
import com.gionee.gnvoiceassist.directiveListener.offlineasr.OfflineAsrListener;
import com.gionee.gnvoiceassist.directiveListener.phonecall.PhoneCallDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.screen.ScreenDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.sms.SmsDirectiveListener;
import com.gionee.gnvoiceassist.directiveListener.telecontroller.TeleControllerListener;
import com.gionee.gnvoiceassist.directiveListener.tvLive.TvLiveListener;
import com.gionee.gnvoiceassist.directiveListener.voiceinput.AsrVoiceInputListener;
import com.gionee.gnvoiceassist.directiveListener.voiceinputvolume.VoiceInputVolumeListener;
import com.gionee.gnvoiceassist.directiveListener.webbrowser.WebBrowserListener;
import com.gionee.gnvoiceassist.sdk.SdkManagerImpl;
import com.gionee.gnvoiceassist.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.applauncher.AppLauncherDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.offlineasr.OffLineDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.screen.ScreenDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.webbrowser.WebBrowserDeviceModule;
import com.gionee.gnvoiceassist.tts.SpeakTxtListener;
import com.gionee.gnvoiceassist.util.T;

/**
 * Created by twf on 2017/8/24.
 */

public class DirectiveListenerManager {
    public final String TAG = DirectiveListenerManager.class.getSimpleName();

    private IBaseFunction baseFunctionManager;
    private PhoneCallDirectiveListener phoneCallDirectiveListener;
    private SmsDirectiveListener smsDirectiveListener;
    private SpeakTxtListener speakTxtListener;
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

        SdkManagerImpl.getInstance().getInternalApi().addDirectiveReceivedListener(new IDirectiveReceivedListener() {
            @Override
            public void onDirective(Directive directive) {
                //TODO: 处理IDirectiveReceivedListener，评估是否可以拿掉

                if (!TextUtils.isEmpty(directive.rawMessage)) {
                    Log.v(TAG, "directive-rawMessage:" + directive.rawMessage);
                }
                String name = directive.getName();
                Log.v(TAG, "directive-name:" + name);
                Payload payload = directive.getPayload();
                if ("StopListen".equals(name)) {

                }
            }
        });

        //初始化错误回调接口
        SdkManagerImpl.getInstance().getInternalApi().addErrorListener(new IErrorListener() {
            @Override
            public void onErrorCode(ErrorCode errorCode) {
                T.showShort("SDK出现错误" + errorCode.getMessage());
            }
        });

        //初始化事件监听回调接口
        SdkManagerImpl.getInstance().getInternalApi().addRequestBodySentListener(new IDcsRequestBodySentListener() {
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
        SdkManagerImpl.getInstance().getDcsSdk().getVoiceRequest().addDialogStateListener(asrVoiceInputListener);
        SdkManagerImpl.getInstance().getInternalApi().setLocationHandler(locationHandler);
        ((AudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.audio_player")).addAudioPlayListener(audioPlayerListener);
        ((SystemDeviceModule)getDeviceModule("ai.dueros.device_interface.system")).addModuleListener(deviceModuleListener);
        ((PhoneCallDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.telephone")).addDirectiveListener(phoneCallDirectiveListener);
        ((SmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.sms")).addDirectiveListener(smsDirectiveListener);
        ((ContactsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.contacts")).addDirectiveListener(contactsDirectiveListener);
        ((AppLauncherDeviceModule)getDeviceModule("ai.dueros.device_interface.app_launcher")).addAppLauncherDirectiveListener(appLauncherListener);
        ((WebBrowserDeviceModule)getDeviceModule("ai.dueros.device_interface.web_browser")).addDirectiveListener(webBrowserListener);
        ((AlarmsDeviceModule)getDeviceModule("ai.dueros.device_interface.android.alerts")).addDirectiveListener(alarmDirectiveListener);
        ((DeviceControlDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.device_control")).addDirectiveListener(deviceControlListener);
        ((ScreenDeviceModule)getDeviceModule("ai.dueros.device_interface.screen")).addScreenListener(screenDirectiveListener);
        ((TeleControllerDeviceModule)getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.voiceassist")).addDirectiveListener(teleControllerListener);
        ((CustomUserInteractionDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction")).setCustomUserInteractionDirectiveListener(cuiDirectiveListener);
        ((LocalAudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player")).addLocalAudioPlayerListener(localAudioPlayerListener);
        ((VoiceOutputDeviceModule)getDeviceModule("ai.dueros.device_interface.voice_output")).addVoiceOutputListener(speakTxtListener);
        ((OffLineDeviceModule)getDeviceModule("ai.dueros.device_interface.offline")).addOfflineDirectiveListener(offlineAsrListener);
    }

    public void unRegisterDirectiveListener() {
//        DcsSDK.getInstance().getAsr().registAsrVoiceInputListener(null);
//        DcsSDK.getInstance().getAsr().registOfflineListener(null);

        ((AudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.audio_player")).removeAudioPlayListener(audioPlayerListener);
        ((SystemDeviceModule)getDeviceModule("ai.dueros.device_interface.system")).removeListener(deviceModuleListener);
        ((PhoneCallDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.telephone")).addDirectiveListener(null);
        ((SmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.sms")).addDirectiveListener(null);
        ((ContactsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.contacts")).removeDirectiveListener(contactsDirectiveListener);
        ((AppLauncherDeviceModule)getDeviceModule("ai.dueros.device_interface.app_launcher")).addAppLauncherDirectiveListener(null);
        ((WebBrowserDeviceModule)getDeviceModule("ai.dueros.device_interface.web_browser")).addDirectiveListener(null);
        ((AlarmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.alert_smartphone")).removeDirectiveLIstener(alarmDirectiveListener);
        ((DeviceControlDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.device_control")).removeDirectiveListener(deviceControlListener);
        ((ScreenDeviceModule)getDeviceModule("ai.dueros.device_interface.screen")).removeScreenListener(screenDirectiveListener);
        ((TeleControllerDeviceModule)getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.voiceassist")).addDirectiveListener(null);
        ((CustomUserInteractionDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction")).setCustomUserInteractionDirectiveListener(null);
        ((LocalAudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player")).addLocalAudioPlayerListener(null);
        ((VoiceOutputDeviceModule)getDeviceModule("ai.dueros.device_interface.voice_output")).removeVoiceOutputListener(speakTxtListener);
        ((OffLineDeviceModule)getDeviceModule("ai.dueros.device_interface.offline")).removeOfflineDirectiveListener(offlineAsrListener);
        SdkManagerImpl.getInstance().getInternalApi().setLocationHandler(null);

    }

    public void initDirectiveListener() {

        //语音识别、TTS监听器
        speakTxtListener = new SpeakTxtListener();

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
        return SdkManagerImpl.getInstance().getInternalApi().getDeviceModule(namespace);
    }
}
