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
import com.baidu.duer.dcs.framework.InternalApi;
import com.baidu.duer.dcs.framework.internalApi.IDcsRequestBodySentListener;
import com.baidu.duer.dcs.framework.internalApi.IDirectiveReceivedListener;
import com.baidu.duer.dcs.framework.internalApi.IErrorListener;
import com.baidu.duer.dcs.framework.message.DcsRequestBody;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
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
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;
import com.gionee.gnvoiceassist.tts.SpeakTxtListener;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.Preconditions;
import com.gionee.gnvoiceassist.util.T;
import com.gionee.gnvoiceassist.util.constants.UsecaseConstants.UsecaseAlias;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twf on 2017/8/24.
 */

public class DirectiveListenerManager {
    public final String TAG = DirectiveListenerManager.class.getSimpleName();

    private InternalApi mInternalApi;
    private IDirectiveListenerCallback mCallback;
    private PhoneCallDirectiveListener phoneCallDirectiveListener;
    private SmsDirectiveListener smsDirectiveListener;
//    private SpeakTxtListener speakTxtListener;
    private CUIDirectiveListener cuiDirectiveListener;
//    private VoiceInputVolumeListener voiceInputVolumeListener;
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
    private OfflineAsrListener offlineAsrListener;
    private LocalAudioPlayerListener localAudioPlayerListener;

    private Map<String,BaseDirectiveListener> mDirectiveListenerMap;


    public DirectiveListenerManager(IDirectiveListenerCallback callback) {
        mDirectiveListenerMap = new HashMap<>();
        mCallback = callback;
        initDirectiveListener();
    }

    public void injectSdkInternalApi(InternalApi internalApi) {
        //TODO 考虑如何更好地取得SDK的InternalApi依赖
        mInternalApi = internalApi;
    }

    public void initDirectiveListener() {

        //语音识别、TTS监听器

        offlineAsrListener = new OfflineAsrListener(mCallback);

        //端功能监听器
        phoneCallDirectiveListener = new PhoneCallDirectiveListener(mCallback);

        smsDirectiveListener = new SmsDirectiveListener(mCallback);

        contactsDirectiveListener = new ContactsDirectiveListener(mCallback);

        appLauncherListener = new AppLauncherListener(mCallback);

        webBrowserListener = new WebBrowserListener(mCallback);

        alarmDirectiveListener = new AlarmDirectiveListener(mCallback);

        deviceControlListener = new DeviceControlListener(mCallback);

        screenDirectiveListener = new ScreenDirectiveListener(mCallback);

        teleControllerListener = new TeleControllerListener(mCallback);

        tvLiveListener = new TvLiveListener(mCallback);

        audioPlayerListener = new AudioPlayerListener(mCallback);

        deviceModuleListener = new DeviceModuleListener(mCallback);

        localAudioPlayerListener = new LocalAudioPlayerListener(mCallback);

        //位置回调

        //自定义多伦交互回调
        cuiDirectiveListener = new CUIDirectiveListener();

        mDirectiveListenerMap.put(UsecaseAlias.ALARM, alarmDirectiveListener);
        mDirectiveListenerMap.put(UsecaseAlias.APPLAUNCH, appLauncherListener);
        mDirectiveListenerMap.put(UsecaseAlias.CONTACTS, contactsDirectiveListener);
        mDirectiveListenerMap.put(UsecaseAlias.DEVICE_CONTROL, deviceControlListener);
        mDirectiveListenerMap.put(UsecaseAlias.GN_MUSIC, localAudioPlayerListener);
        mDirectiveListenerMap.put(UsecaseAlias.GN_REMOTE, teleControllerListener);
        mDirectiveListenerMap.put(UsecaseAlias.SMS, smsDirectiveListener);
        mDirectiveListenerMap.put(UsecaseAlias.PHONECALL, phoneCallDirectiveListener);

    }

    public  void registerDirectiveListener() {
//        DcsSDK.getInstance().getAsr().registOfflineListener(offlineAsrListener);
//        DcsSDK.getInstance().getAudioRecord().registerRecordListener(voiceInputVolumeListener);

        //初始化对话回调接口
        LogUtil.d("liyh","registerDirectiveListener()");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.audio_player"),"cannot get 'audio_player' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.system"),"cannot get 'system' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.telephone"),"cannot get 'telephone' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.sms"),"cannot get 'sms' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.contacts"),"cannot get 'contacts' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.app_launcher"),"cannot get 'app_launcher' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.web_browser"),"cannot get 'web_browser' device module");
//        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.alert_smartphone"),"cannot get 'alert_smartphone' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.device_control"),"cannot get 'device_control' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.screen"),"cannot get 'screen' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.voiceassist"),"cannot get 'thirdparty_voiceassist' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction"),"cannot get 'custom_user_interaction' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player"),"cannot get 'local_audio_player' device module");

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
        ((OffLineDeviceModule)getDeviceModule("ai.dueros.device_interface.offline")).addOfflineDirectiveListener(offlineAsrListener);
    }

    public void unRegisterDirectiveListener() {
//        DcsSDK.getInstance().getAsr().registAsrVoiceInputListener(null);
//        DcsSDK.getInstance().getAsr().registOfflineListener(null);

        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.audio_player"),"cannot get 'audio_player' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.system"),"cannot get 'system' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.telephone"),"cannot get 'telephone' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.sms"),"cannot get 'sms' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.contacts"),"cannot get 'contacts' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.app_launcher"),"cannot get 'app_launcher' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.web_browser"),"cannot get 'web_browser' device module");
//        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.alert_smartphone"),"cannot get 'alert_smartphone' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.device_control"),"cannot get 'device_control' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.screen"),"cannot get 'screen' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.voiceassist"),"cannot get 'thirdparty_voiceassist' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction"),"cannot get 'custom_user_interaction' device module");
        Preconditions.checkNotNull(getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player"),"cannot get 'local_audio_player' device module");


        ((AudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.audio_player")).removeAudioPlayListener(audioPlayerListener);
        ((SystemDeviceModule)getDeviceModule("ai.dueros.device_interface.system")).removeListener(deviceModuleListener);
        ((PhoneCallDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.telephone")).addDirectiveListener(null);
        ((SmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.sms")).addDirectiveListener(null);
        ((ContactsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.contacts")).removeDirectiveListener(contactsDirectiveListener);
        ((AppLauncherDeviceModule)getDeviceModule("ai.dueros.device_interface.app_launcher")).addAppLauncherDirectiveListener(null);
        ((WebBrowserDeviceModule)getDeviceModule("ai.dueros.device_interface.web_browser")).addDirectiveListener(null);
//        ((AlarmsDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.alert_smartphone")).removeDirectiveLIstener(alarmDirectiveListener);
        ((DeviceControlDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.device_control")).removeDirectiveListener(deviceControlListener);
        ((ScreenDeviceModule)getDeviceModule("ai.dueros.device_interface.screen")).removeScreenListener(screenDirectiveListener);
        ((TeleControllerDeviceModule)getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.voiceassist")).addDirectiveListener(null);
        ((CustomUserInteractionDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction")).setCustomUserInteractionDirectiveListener(null);
        ((LocalAudioPlayerDeviceModule)getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player")).addLocalAudioPlayerListener(null);
//        ((VoiceOutputDeviceModule)getDeviceModule("ai.dueros.device_interface.voice_output")).removeVoiceOutputListener(speakTxtListener);
//        ((TtsOutputDeviceModule)getDeviceModule("ai.dueros.device_interface.tts_output")).removeVoiceOutputListener(speakTxtListener);
        ((OffLineDeviceModule)getDeviceModule("ai.dueros.device_interface.offline")).removeOfflineDirectiveListener(offlineAsrListener);

    }

    public BaseDirectiveListener getDirectiveListener(String usecaseAlias) throws DirectiveListenerNotFoundException {
        if (mDirectiveListenerMap.containsKey(usecaseAlias)) {
            return mDirectiveListenerMap.get(usecaseAlias);
        } else {
            throw new DirectiveListenerNotFoundException();
        }
    }

    public void onDestroy(){

        unRegisterDirectiveListener();
        if (mDirectiveListenerMap != null) {
            mDirectiveListenerMap.clear();
        }

        if(audioPlayerListener != null) {
            audioPlayerListener.onDestroy();
        }
        if(deviceModuleListener != null) {
            deviceModuleListener.onDestroy();
        }

        if(appLauncherListener != null) {
            appLauncherListener.onDestroy();
        }
    }

    private BaseDeviceModule getDeviceModule(String namespace) {
        return mInternalApi.getDeviceModule(namespace);
    }

    public class DirectiveListenerNotFoundException extends Exception {

    }
}
