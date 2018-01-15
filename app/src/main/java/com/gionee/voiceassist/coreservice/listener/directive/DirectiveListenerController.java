package com.gionee.voiceassist.coreservice.listener.directive;

import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.InternalApi;
import com.gionee.voiceassist.coreservice.datamodel.AppLaunchDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteTvDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.applauncher.AppLauncherDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.contacts.ContactsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.CustomCmdDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.phonecall.PhoneCallDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.reminder.ReminderDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.screen.ScreenDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.sms.SmsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.tvlive.TvLiveDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.webbrowser.WebBrowserDeviceModule;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 12/29/17.
 */

public class DirectiveListenerController {

    private static final String TAG = DirectiveListenerController.class.getSimpleName();

    private boolean listenerInited = false;
    private boolean listenerInstalled = false;

    private AlarmDirectiveListener alarmListener;
    private ScreenDirectiveListener screenListener;
    private ContactsDirectiveListener contactsListener;
    private PhoneCallDirectiveListener phonecallListener;
    private SmsSendDirectiveListener smsSendDirectiveListener;
    private CustomCmdDirectiveListener customCommandListener;
    private AppLauncherListener applaunchListener;
    private GnRemoteListener gnRemoteListener;
    private GnRemoteTvDirectiveListener gnRemoteTvListener;
    private LocalAudioPlayerListener localAudioPlayerListener;
    private WebBrowserListener webBrowserListener;
    private ReminderDirectiveListener reminderListener;

    private List<DirectiveCallback> mSubscribers = new ArrayList<>();

    public DirectiveListenerController() {

    }

    public void init() {
        if (!listenerInited) {
            initListener();
        }
        LogUtil.d(TAG, "DirectiveListener registering...");
        registerListener();
    }

    public void destroy() {
        unregisterListener();
    }

    /**
     * 外界订阅Directive解析后的消息
     * subscribe回调中，需要返回解析好的Directive消息
     */
    public void subscribe(DirectiveCallback directiveCallback) {
        if (!mSubscribers.contains(directiveCallback)) {
            mSubscribers.add(directiveCallback);
        }
    }


    public void unsubscribe(DirectiveCallback directiveCallback) {
        if (mSubscribers.contains(directiveCallback)) {
            mSubscribers.remove(directiveCallback);
        }
    }

    /**
     * 初始化所有DirectiveListener
     */
    private void initListener() {
        alarmListener = new AlarmDirectiveListener(mSubscribers);
        screenListener = new ScreenDirectiveListener(mSubscribers);
        contactsListener = new ContactsDirectiveListener(mSubscribers);
        phonecallListener = new PhoneCallDirectiveListener(mSubscribers);
        smsSendDirectiveListener = new SmsSendDirectiveListener(mSubscribers);
        customCommandListener = new CustomCmdDirectiveListener(mSubscribers);
        applaunchListener = new AppLauncherListener(mSubscribers);
        gnRemoteListener = new GnRemoteListener(mSubscribers);
        gnRemoteTvListener = new GnRemoteTvDirectiveListener(mSubscribers);
        localAudioPlayerListener = new LocalAudioPlayerListener(mSubscribers);
        webBrowserListener = new WebBrowserListener(mSubscribers);
        reminderListener = new ReminderDirectiveListener(mSubscribers);
        listenerInited = true;
    }

    private void registerListener() {
        ((AlarmsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.android.alerts"))
                .addDirectiveListener(alarmListener);

        ((ScreenDeviceModule) getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.screen"))
                .addScreenListener(screenListener);

        ((CustomCmdDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.customcmd"))
                .addDirectiveListener(customCommandListener);

        ((ContactsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.extensions.contacts"))
                .addContactsListener(contactsListener);

        ((PhoneCallDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.extensions.telephone"))
                .addPhoneCallListener(phonecallListener);

        ((SmsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.extensions.sms"))
                .addSmsListener(smsSendDirectiveListener);

        ((AppLauncherDeviceModule) getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.app_launcher"))
                .addAppLauncherDirectiveListener(applaunchListener);

        ((TeleControllerDeviceModule) getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.kookong"))
                .addDirectivieListener(gnRemoteListener);

        ((TvLiveDeviceModule) getDeviceModule("ai.dueros.device_interface.tv_live"))
                .addDirectiveListener(gnRemoteTvListener);

        ((LocalAudioPlayerDeviceModule) getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player"))
                .addLocalAudioPlayerListener(localAudioPlayerListener);

        ((WebBrowserDeviceModule) getDeviceModule("ai.dueros.device_interface.web_browser"))
                .addDirectiveListener(webBrowserListener);

        ((ReminderDeviceModule) getDeviceModule("ai.dueros.device_interface.extensions.alert_nlu"))
                .addDirectiveListener(reminderListener);

        listenerInstalled = true;
    }

    private void unregisterListener() {
        ((AlarmsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.android.alerts"))
                .removeDirectiveLIstener(alarmListener);
        ((ScreenDeviceModule) getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.screen"))
                .removeScreenListener(screenListener);
        ((CustomCmdDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.customcmd"))
                .removeDirectiveListener(customCommandListener);

        ((ContactsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.extensions.contacts"))
                .removeContactsListener(contactsListener);

        ((PhoneCallDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.extensions.telephone"))
                .removePhoneCallListener(phonecallListener);

        ((SmsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.extensions.sms"))
                .removeSmsListener(smsSendDirectiveListener);

        ((AppLauncherDeviceModule) getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.app_launcher"))
                .addAppLauncherDirectiveListener(null);

        ((TeleControllerDeviceModule) getDeviceModule("ai.dueros.device_interface.thirdparty.gionee.kookong"))
                .addDirectivieListener(null);

        ((TvLiveDeviceModule) getDeviceModule("ai.dueros.device_interface.tv_live"))
                .addDirectiveListener(null);

        ((LocalAudioPlayerDeviceModule) getDeviceModule("ai.dueros.device_interface.extensions.local_audio_player"))
                .addLocalAudioPlayerListener(null);

        ((WebBrowserDeviceModule) getDeviceModule("ai.dueros.device_interface.web_browser"))
                .addDirectiveListener(null);

        ((ReminderDeviceModule) getDeviceModule("ai.dueros.device_interface.extensions.alert_nlu"))
                .removeDirectiveListener(reminderListener);

        listenerInstalled = false;
    }

    public interface DirectiveCallback {
        void onDirectiveMessage(DirectiveEntity msg);
    }

    private InternalApi getSdkInternalApi() {
        return Preconditions.checkNotNull(SdkController.getInstance().getSdkInternalApi());
    }

    private BaseDeviceModule getDeviceModule(String namespace) {
        return getSdkInternalApi().getDeviceModule(namespace);
    }
}
