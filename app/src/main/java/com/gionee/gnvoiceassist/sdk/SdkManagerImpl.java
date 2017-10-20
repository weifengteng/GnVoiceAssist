package com.gionee.gnvoiceassist.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.duer.dcs.androidsystemimpl.AudioRecordImpl;
import com.baidu.duer.dcs.androidsystemimpl.custominteraction.ICustomUserInteractionImpl;
import com.baidu.duer.dcs.androidsystemimpl.phonecall.IPhoneCallImpl;
import com.baidu.duer.dcs.androidsystemimpl.player.MediaPlayerImpl;
import com.baidu.duer.dcs.androidsystemimpl.sms.ISmsImpl;
import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.devicemodule.audioplayer.AudioPlayerDeviceModule;
import com.baidu.duer.dcs.devicemodule.contacts.ContactsDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.phonecall.PhoneCallDeviceModule;
import com.baidu.duer.dcs.devicemodule.sms.SmsDeviceModule;
import com.baidu.duer.dcs.devicemodule.system.SystemDeviceModule;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.DialogRequestIdHandler;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.InternalApi;
import com.baidu.duer.dcs.framework.internalApi.IDcsRequestBodySentListener;
import com.baidu.duer.dcs.framework.internalApi.IDirectiveReceivedListener;
import com.baidu.duer.dcs.framework.internalApi.IErrorListener;
import com.baidu.duer.dcs.framework.location.Location;
import com.baidu.duer.dcs.framework.message.DcsRequestBody;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.oauth.api.credentials.BaiduOauthClientCredentialsImpl;
import com.baidu.duer.dcs.systeminterface.IAudioRecorder;
import com.baidu.duer.dcs.systeminterface.IOauth;
import com.gionee.gnvoiceassist.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.applauncher.AppLauncherDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.applauncher.IAppLauncher;
import com.gionee.gnvoiceassist.sdk.module.applauncher.IAppLauncherImpl;
import com.gionee.gnvoiceassist.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.screen.ScreenDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.screen.extend.card.ScreenExtendDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.webbrowser.WebBrowserDeviceModule;
import com.gionee.gnvoiceassist.util.T;

/**
 * Created by liyingheng on 10/15/17.
 */

public class SdkManagerImpl implements ISdkManager {

    //Config SDK
    private static final String WAKEUP_WORD = "";
    private static final boolean ENABLE_WAKEUP = false;

    private static final String TAG = SdkManagerImpl.class.getSimpleName();
    private static SdkManagerImpl sInstance;

    private IDcsSdk mDcsSdk;

    private SdkManagerImpl() {

    }

    public static synchronized SdkManagerImpl getInstance() {
        if (sInstance == null) {
            sInstance = new SdkManagerImpl();
        }

        return sInstance;
    }

    @Override
    public void init(Context context) {
        initSdk(context);
//        initDirectiveReceivedListener();
//        initLocationHandler();
        initErrorListener();
        initSendEventListener();
    }

    @Override
    public void destroy() {
        mDcsSdk.release();
    }

    private void initSdk(Context context) {
        // 第一步初始化sdk
        mDcsSdk = DcsSdkImpl.getInstance();
        IAudioRecorder audioRecorder = new AudioRecordImpl();

        String clientId = "83kW99iEz0jpGp9hrX981ezGcTaxNzk0";
        String clientSecret = "UTjgedIE5CRZM3CWj2cApLKajeZWotvf";
        IOauth oauth = new BaiduOauthClientCredentialsImpl(clientId, clientSecret);
        mDcsSdk.init(clientId, oauth, audioRecorder, ENABLE_WAKEUP, WAKEUP_WORD);

//        ((TtsOutputDeviceModule)getInternalApi().getDeviceModule(ApiConstants.NAMESPACE)).addVoiceOutputListener(new TtsOutputDeviceModule.ITtsOutputListener() {
//            @Override
//            public void onTtsOutputStarted() {
//                T.showShort("onTtsOutputStarted");
//            }
//
//            @Override
//            public void onTtsOutputFinished() {
//                T.showShort("onTtsOutputFinished");
//            }
//        });

        initDeviceModule(context);


        // 第三步，将sdk跑起来
        mDcsSdk.run();


    }

    private void initDeviceModule(Context context) {
        //初始化应用启动模块AppLauncher
        IAppLauncher appLauncher = new IAppLauncherImpl(context);
        IMessageSender messageSender = ((DcsSdkImpl) mDcsSdk).getInternalApi().getMessageSender();
        AppLauncherDeviceModule appLauncherDeviceModule = new AppLauncherDeviceModule(appLauncher, messageSender);
        mDcsSdk.setDeviceModule(appLauncherDeviceModule);

        //初始化音频播放器模块AudioPlayerDeviceModule
//        AudioPlayerDeviceModule audioPlayerDeviceModule = new AudioPlayerDeviceModule(new MediaPlayerImpl(),messageSender);
//        mDcsSdk.setDeviceModule(audioPlayerDeviceModule);

        //初始化SystemDeviceModule
//        SystemDeviceModule systemDeviceModule = new SystemDeviceModule(messageSender);
//        mDcsSdk.setDeviceModule(systemDeviceModule);

        //初始化电话模块PhonecallDeviceModule
        PhoneCallDeviceModule phoneCallDeviceModule = new PhoneCallDeviceModule(new IPhoneCallImpl(),messageSender);
        mDcsSdk.setDeviceModule(phoneCallDeviceModule);

        //初始化短信模块SmsDeviceModule
        SmsDeviceModule smsDeviceModule = new SmsDeviceModule(new ISmsImpl(),messageSender);
        mDcsSdk.setDeviceModule(smsDeviceModule);

        //初始化联系人模块ContactsDeviceModule
        ContactsDeviceModule contactsDeviceModule = new ContactsDeviceModule(messageSender);
        mDcsSdk.setDeviceModule(contactsDeviceModule);

        //初始化浏览器模块WebBrowserDeviceModule
        WebBrowserDeviceModule webBrowserDeviceModule = new WebBrowserDeviceModule(messageSender);
        mDcsSdk.setDeviceModule(webBrowserDeviceModule);

        //初始化闹铃模块AlarmsDeviceModule
        AlarmsDeviceModule alarmsDeviceModule = new AlarmsDeviceModule(messageSender);
        mDcsSdk.setDeviceModule(alarmsDeviceModule);

        //初始化手机控制模块DeviceControlDeviceModule
        DeviceControlDeviceModule deviceControlDeviceModule = new DeviceControlDeviceModule(messageSender);
        mDcsSdk.setDeviceModule(deviceControlDeviceModule);

        //初始化上屏模块ScreenDeviceModule
        ScreenDeviceModule screenDeviceModule = new ScreenDeviceModule(messageSender);
        ScreenExtendDeviceModule screenExtendDeviceModule = new ScreenExtendDeviceModule(messageSender);
        mDcsSdk.setDeviceModule(screenDeviceModule);
        mDcsSdk.setDeviceModule(screenExtendDeviceModule);

        //初始化telecontrollerDeviceModule
        TeleControllerDeviceModule teleControllerDeviceModule = new TeleControllerDeviceModule(messageSender);
        mDcsSdk.setDeviceModule(teleControllerDeviceModule);

        //初始化customInteractionDeviceModule
//        CustomUserInteractionDeviceModule customUserInteractionDeviceModule = new CustomUserInteractionDeviceModule(new ICustomUserInteractionImpl(), messageSender, new DialogRequestIdHandler());
//        mDcsSdk.setDeviceModule(customUserInteractionDeviceModule);

        //初始化localAudioPlayerDeviceModule
        LocalAudioPlayerDeviceModule localAudioPlayerDeviceModule = new LocalAudioPlayerDeviceModule(messageSender);
        mDcsSdk.setDeviceModule(localAudioPlayerDeviceModule);

        getInternalApi().getDeviceModule(com.baidu.duer.dcs.devicemodule.custominteraction.ApiConstants.NAMESPACE);
    }

    private void initErrorListener() {
        getInternalApi().addErrorListener(new IErrorListener() {
            @Override
            public void onErrorCode(ErrorCode errorCode) {
//                if (errorCode == ErrorCode.VOICE_REQUEST_FAILED) {
//                    Toast.makeText(MainActivity.this,
//                            getResources().getString(R.string.voice_err_msg),
//                            Toast.LENGTH_SHORT)
//                            .show();
//                } else if (errorCode == ErrorCode.OPEN_MICROPHONE_ERROR) {
////                    Toast.makeText(MainActivity.this, "录音机打开失败，请保证录音权限打开!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void initSendEventListener() {
        getInternalApi().addRequestBodySentListener(new IDcsRequestBodySentListener() {
            @Override
            public void onDcsRequestBody(DcsRequestBody event) {
                String eventName = event.getEvent().getHeader().getName();
                Log.v(TAG, "eventName:" + eventName);

                if (eventName.equals("SpeechStarted")) {
                    // online tts start
//                    T.showLong("SpeechStarted");
                } else if (eventName.equals("SpeechFinished")) {
                    // online tts finish
//                    T.showLong("SpeechFinished");
                }
            }
        });
    }

    private void initDirectiveReceivedListener() {
        getInternalApi().addDirectiveReceivedListener(new IDirectiveReceivedListener() {
            @Override
            public void onDirective(Directive directive) {
                if (!TextUtils.isEmpty(directive.rawMessage)) {
                    Log.v(TAG, "directive-rawMessage:" + directive.rawMessage);
                }
                String name = directive.getName();
                Log.v(TAG, "directive-name:" + name);
                Payload payload = directive.getPayload();
                if ("StopListen".equals(name)) {
//                    voiceButton.setText("点击说话");
                }
            }
        });
    }

    private void initLocationHandler() {
        getInternalApi().setLocationHandler(new Location.LocationHandler() {
            @Override
            public double getLongitude() {
                return 0;
            }

            @Override
            public double getLatitude() {
                return 0;
            }

            @Override
            public String getCity() {
                return null;
            }

            @Override
            public Location.EGeoCoordinateSystem getGeoCoordinateSystem() {
                return Location.EGeoCoordinateSystem.WGS84;
            }
        });
    }


    public InternalApi getInternalApi() {
        return ((DcsSdkImpl) mDcsSdk).getInternalApi();
    }
}
