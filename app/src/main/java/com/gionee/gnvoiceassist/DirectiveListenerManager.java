package com.gionee.gnvoiceassist;

import com.baidu.duer.sdk.DcsSDK;
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
import com.gionee.gnvoiceassist.tts.SpeakTxtListener;

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
        DcsSDK.getInstance().getSpeak().addTTSStateListener(speakTxtListener);
        DcsSDK.getInstance().getAudioPlayerDeviceModule().addAudioPlayListener(audioPlayerListener);
        DcsSDK.getInstance().getSystemDeviceModule().addModuleListener(deviceModuleListener);
        DcsSDK.getInstance().getPhoneCallDeviceModule().addDirectiveListener(phoneCallDirectiveListener);
        DcsSDK.getInstance().getSmsDeviceModule().addDirectiveListener(smsDirectiveListener);
        DcsSDK.getInstance().getContactsDeviceModule().addDirectiveListener(contactsDirectiveListener);
        DcsSDK.getInstance().getAppLauncherDeviceModule().addDirectiveListener(appLauncherListener);
        DcsSDK.getInstance().getWebBrowserDeviceModule().addDirectiveListener(webBrowserListener);
        DcsSDK.getInstance().getAlarmsDeviceModule().addDirectiveListener(alarmDirectiveListener);
        DcsSDK.getInstance().getDeviceControlDeviceModule().addDirectiveListener(deviceControlListener);
        DcsSDK.getInstance().getScreenDeviceModule().addDirectiveListener(screenDirectiveListener);
        DcsSDK.getInstance().getTeleControllerDeviceModule().addDirectiveListener(teleControllerListener);
        DcsSDK.getInstance().getTvLiveDeviceModule().addDirectiveListener(tvLiveListener);
        DcsSDK.getInstance().getLocation().registerLocationHandler(locationHandler);
        DcsSDK.getInstance().getAsr().registAsrVoiceInputListener(asrVoiceInputListener);
        DcsSDK.getInstance().getAsr().registOfflineListener(offlineAsrListener);
        DcsSDK.getInstance().getAudioRecord().registerRecordListener(voiceInputVolumeListener);
        DcsSDK.getInstance().getCustomUserInteractionDeviceModule().setCustomUserInteractionDirectiveListener(cuiDirectiveListener);
        // 音乐指令相关
        DcsSDK.getInstance().getLocalAudioPlayerDeviceModule().addLocalAudioPlayerListener(localAudioPlayerListener);
    }

    public void unRegisterDirectiveListener() {
        DcsSDK.getInstance().getSpeak().addTTSStateListener(null);
        DcsSDK.getInstance().getAudioPlayerDeviceModule().removeAudioPlayListener(audioPlayerListener);
        DcsSDK.getInstance().getSystemDeviceModule().addModuleListener(null);
        DcsSDK.getInstance().getPhoneCallDeviceModule().addDirectiveListener(null);
        DcsSDK.getInstance().getSmsDeviceModule().addDirectiveListener(null);
        DcsSDK.getInstance().getContactsDeviceModule().removeDirectiveListener(contactsDirectiveListener);
        DcsSDK.getInstance().getAppLauncherDeviceModule().addDirectiveListener(null);
        DcsSDK.getInstance().getWebBrowserDeviceModule().addDirectiveListener(null);
        DcsSDK.getInstance().getAlarmsDeviceModule().removeDirectiveLIstener(alarmDirectiveListener);
        DcsSDK.getInstance().getDeviceControlDeviceModule().removeDirectiveListener(deviceControlListener);
        DcsSDK.getInstance().getScreenDeviceModule().addDirectiveListener(screenDirectiveListener);
        DcsSDK.getInstance().getTeleControllerDeviceModule().addDirectiveListener(null);
        DcsSDK.getInstance().getTvLiveDeviceModule().addDirectiveListener(null);
        DcsSDK.getInstance().getLocation().registerLocationHandler(null);
        DcsSDK.getInstance().getAsr().registAsrVoiceInputListener(null);
        DcsSDK.getInstance().getAsr().registOfflineListener(null);
        DcsSDK.getInstance().getAudioRecord().registerRecordListener(null);
        DcsSDK.getInstance().getCustomUserInteractionDeviceModule().setCustomUserInteractionDirectiveListener(null);
        DcsSDK.getInstance().getLocalAudioPlayerDeviceModule().addLocalAudioPlayerListener(null);
    }

    public void initDirectiveListener() {
        speakTxtListener = new SpeakTxtListener();

        cuiDirectiveListener = new CUIDirectiveListener();

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

        locationHandler = new LocationHandler(baseFunctionManager);

        offlineAsrListener = new OfflineAsrListener(baseFunctionManager);

        asrVoiceInputListener = new AsrVoiceInputListener(baseFunctionManager);

        audioPlayerListener = new AudioPlayerListener(baseFunctionManager);

        deviceModuleListener = new DeviceModuleListener(baseFunctionManager);

        localAudioPlayerListener = new LocalAudioPlayerListener(baseFunctionManager);
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
}
