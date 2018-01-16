package com.gionee.voiceassist.coreservice.sdk;

import android.content.Context;
import android.util.Log;

import com.baidu.duer.dcs.androidsystemimpl.AudioRecordImpl;
import com.baidu.duer.dcs.androidsystemimpl.wakeup.kitt.KittWakeUpImpl;
import com.baidu.duer.dcs.api.DcsSdkBuilder;
import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.DialogRequestIdHandler;
import com.baidu.duer.dcs.framework.ILoginListener;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.InternalApi;
import com.baidu.duer.dcs.framework.internalapi.IASROffLineConfigProvider;
import com.baidu.duer.dcs.framework.internalapi.IWakeupAgent;
import com.baidu.duer.dcs.framework.internalapi.IWakeupProvider;
import com.baidu.duer.dcs.oauth.api.silent.SilentLoginImpl;
import com.baidu.duer.dcs.offline.asr.bean.ASROffLineConfig;
import com.baidu.duer.dcs.systeminterface.BaseAudioRecorder;
import com.baidu.duer.dcs.systeminterface.BaseWakeup;
import com.baidu.duer.dcs.systeminterface.IMediaPlayer;
import com.baidu.duer.dcs.systeminterface.IOauth;
import com.baidu.duer.dcs.wakeup.WakeUpConfig;
import com.baidu.duer.dcs.wakeup.WakeUpWord;
import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.applauncher.AppLauncherDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.applauncher.IAppLauncher;
import com.gionee.voiceassist.coreservice.sdk.module.applauncher.IAppLauncherImpl;
import com.gionee.voiceassist.coreservice.sdk.module.contacts.ContactsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.CustomCmdDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.offlineasr.OffLineDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.phonecall.PhoneCallDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.reminder.ReminderDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.screen.ScreenDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.screen.extend.card.ScreenExtendDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.sms.SmsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.tvlive.TvLiveDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.webbrowser.WebBrowserDeviceModule;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;
import com.gionee.voiceassist.util.kookong.KookongCustomDataHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 管理SDK的生命周期，取得SDK实例
 */

public class SdkController implements ISdkController {

    //Config SDK
    private static final String WAKEUP_WORD = "";
    private static final boolean ENABLE_WAKEUP = false;

    private static final String TAG = SdkController.class.getSimpleName();
    private static SdkController sInstance;

    private InitStatus mInitStatus = InitStatus.UNINIT;

    private List<SdkInitCallback> exportInitCallbacks;

    private IDcsSdk mDcsSdk;
    private Context mAppCtx;
    private IWakeupAgent.IWakeupAgentListener wakeupAgentListener;

    private SdkController() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
        exportInitCallbacks = new ArrayList<>();
    }

    public static synchronized SdkController getInstance() {
        if (sInstance == null) {
            sInstance = new SdkController();
        }
        return sInstance;
    }

    @Override
    public void init() {
        initSdk(mAppCtx);
    }

    @Override
    public void destroy() {
        mDcsSdk.release();
        updateInitStatus(InitStatus.UNINIT);
    }

    @Override
    public void addSdkInitCallback(SdkInitCallback callback) {
        if (!exportInitCallbacks.contains(callback)) {
            exportInitCallbacks.add(callback);
        }
    }

    @Override
    public void removeSdkInitCallback(SdkInitCallback callback) {
        if (exportInitCallbacks.contains(callback)) {
            exportInitCallbacks.remove(callback);
        }
    }

    @Override
    public IDcsSdk getSdkInstance() {
        if (mDcsSdk == null) {
            init();
        }
        return mDcsSdk;
    }

    @Override
    public InternalApi getSdkInternalApi() {
        return ((DcsSdkImpl) mDcsSdk).getInternalApi();
    }

    private void initSdk(final Context context) {
        updateInitStatus(InitStatus.INITING);
        // 第一步，初始化sdk实例
        String clientId = "83kW99iEz0jpGp9hrX981ezGcTaxNzk0";
        String clientSecret = "UTjgedIE5CRZM3CWj2cApLKajeZWotvf";
        String appId = "10290022";
        String apiKey = "bw40xRdDGFclaSIGzgXgNFdG";
        String secretKey = "AkP1XOuGVlrrML7dTs4WqW6bqj8lvv6C";
        //withPid是语音链路，是百度语音组针对不同产品线配置所开放的接口，
        //每个产品立项都会拥有自己的pid编号，或者使用通用的，729是sdk sample的语音配置编号
        BaseAudioRecorder audioRecorder = new AudioRecordImpl();
        mDcsSdk = new DcsSdkBuilder()
                .withClientId(clientId)
                .withOauth(getOauth(clientId))
                .withAudioRecorder(audioRecorder)
                .withPid(729)
                .build();
        getSdkInternalApi().setDebug(true);
        getSdkInternalApi().setAsrMode(GnVoiceAssistApplication.ASR_MODE);

        //二、设置基础功能：DeviceModule、离线功能、唤醒功能
        setupDeviceModule(context);
        setupOffline(appId, apiKey, secretKey);
        setupWakeup();

        // 第三步，将sdk跑起来
        runSdk();
    }

    private IOauth getOauth(String clientId) {
        return new SilentLoginImpl(clientId);
    }

    private void setupDeviceModule(Context context) {
        //初始化应用启动模块AppLauncher
        IAppLauncher appLauncher = new IAppLauncherImpl(context);
        IMessageSender messageSender = ((DcsSdkImpl) mDcsSdk).getInternalApi().getMessageSender();
        AppLauncherDeviceModule appLauncherDeviceModule = new AppLauncherDeviceModule(appLauncher, messageSender);
        mDcsSdk.putDeviceModule(appLauncherDeviceModule);

        //初始化电话模块PhonecallDeviceModule
        PhoneCallDeviceModule phoneCallDeviceModule = new PhoneCallDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(phoneCallDeviceModule);

        //初始化短信模块SmsDeviceModule
        SmsDeviceModule smsDeviceModule = new SmsDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(smsDeviceModule);

        //初始化联系人模块ContactsDeviceModule
        ContactsDeviceModule contactsDeviceModule = new ContactsDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(contactsDeviceModule);

        //初始化浏览器模块WebBrowserDeviceModule
        WebBrowserDeviceModule webBrowserDeviceModule = new WebBrowserDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(webBrowserDeviceModule);

        //初始化闹铃模块AlarmsDeviceModule
        AlarmsDeviceModule alarmsDeviceModule = new AlarmsDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(alarmsDeviceModule);

        //初始化提醒模块ReminderDeviceModule
        ReminderDeviceModule reminderDeviceModule = new ReminderDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(reminderDeviceModule);

        //初始化手机控制模块DeviceControlDeviceModule
        DeviceControlDeviceModule deviceControlDeviceModule = new DeviceControlDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(deviceControlDeviceModule);

        //初始化上屏模块ScreenDeviceModule
        ScreenDeviceModule screenDeviceModule = new ScreenDeviceModule(messageSender);
        ScreenExtendDeviceModule screenExtendDeviceModule = new ScreenExtendDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(screenDeviceModule);
        mDcsSdk.putDeviceModule(screenExtendDeviceModule);

        //初始化telecontrollerDeviceModule
        TeleControllerDeviceModule teleControllerDeviceModule = new TeleControllerDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(teleControllerDeviceModule);

        // 初始化tvLiveDeviceModule
        TvLiveDeviceModule tvLiveDeviceModule = new TvLiveDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(tvLiveDeviceModule);

        // 初始化 CustomCmdDeviceModule
        CustomCmdDeviceModule customCmdDeviceModule = new CustomCmdDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(customCmdDeviceModule);

        //初始化localAudioPlayerDeviceModule
        LocalAudioPlayerDeviceModule localAudioPlayerDeviceModule = new LocalAudioPlayerDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(localAudioPlayerDeviceModule);

        //初始化CustomInteraction多轮交互模块
        DialogRequestIdHandler dialogRequestIdHandler = ((DcsSdkImpl) mDcsSdk).getProvider().getDialogRequestIdHandler();
        CustomUserInteractionDeviceModule customUserInteractionDeviceModule = new CustomUserInteractionDeviceModule(messageSender,dialogRequestIdHandler);
        mDcsSdk.putDeviceModule(customUserInteractionDeviceModule);

        //初始化离线识别模块
        OffLineDeviceModule offLineDeviceModule = new OffLineDeviceModule();
        mDcsSdk.putDeviceModule(offLineDeviceModule);

        getSdkInternalApi().getDeviceModule(com.baidu.duer.dcs.devicemodule.custominteraction.ApiConstants.NAMESPACE);

        LogUtil.d(TAG, "DeviceModule installed!");
    }

    private void setupOffline(String appId, String appKey, String secretKey) {
        final ASROffLineConfig asrOffLineConfig = new ASROffLineConfig();
        asrOffLineConfig.offlineAsrSlots = getOfflineAsrSlots();
        asrOffLineConfig.asrAppId = appId;
        asrOffLineConfig.asrAppKey = appKey;
        asrOffLineConfig.asrSecretKey = secretKey;
        asrOffLineConfig.grammerPath = "assets:///baidu_speech_grammar.bsg";
        IASROffLineConfigProvider asrOffLineConfigProvider = new IASROffLineConfigProvider() {
            @Override
            public ASROffLineConfig getOfflineConfig() {
                return asrOffLineConfig;
            }
        };
        getSdkInternalApi().setAsrOffLineConfigProvider(asrOffLineConfigProvider);
    }

    private void setupWakeup() {
        final BaseWakeup wakeup = new KittWakeUpImpl();
        IWakeupProvider wakeupProvider = new IWakeupProvider() {

            @Override
            public WakeUpConfig wakeUpConfig() {
                return new WakeUpConfig.Builder()
                        .resPath("snowboy/common.res")
                        .umdlPath("snowboy/xiaoduxiaodu_all_11272017.umdl")
                        .sensitivity("0.35,0.35,0.40")
                        .highSensitivity("0.45,0.45,0.55")
                        .build();
            }

            @Override
            public boolean enableWarning() {
                return false;
            }

            @Override
            public String warningSource() {
                return "assets://alarm.mp3";
            }

            @Override
            public boolean wakeAlways() {
                return false;
            }

            @Override
            public BaseWakeup wakeupImpl() {
                return wakeup;
            }

            @Override
            public float volume() {
                return 0;
            }
        };
        getSdkInternalApi().setWakeupProvider(wakeupProvider);
        IWakeupAgent wakeupAgent = getSdkInternalApi().getWakeupAgent();
        if (wakeupAgent != null) {
            wakeupAgentListener = new IWakeupAgent.IWakeupAgentListener() {


                @Override
                public void onInitWakeUpSucceed() {

                }

                @Override
                public void onInitWakeUpFailed(String s) {

                }

                @Override
                public void onWakeupSucceed(WakeUpWord wakeUpWord) {

                }

                @Override
                public void onWarningCompleted() {

                }

                @Override
                public void onWarningError(String s, IMediaPlayer.ErrorType errorType) {

                }
            };
            wakeupAgent.addWakeupAgentListener(wakeupAgentListener);
        }

    }

    private void runSdk() {
        getSdkInternalApi().login(new ILoginListener() {
            @Override
            public void onSucceed(String s) {
                updateInitStatus(InitStatus.INITED);
                fireSdkInitSuccess();
                mDcsSdk.run();

            }

            @Override
            public void onFailed(String s) {
                LogUtil.e(TAG,"SDK Login failed! Message = " + s);
                updateInitStatus(InitStatus.UNINIT);
                fireSdkInitFailed("Login Failed. Message : " + s);
                ErrorHelper.sendError(ErrorCode.SDK_LOGIN_FAILED, "登录失败。原因：" + s);
            }

            @Override
            public void onCancel() {
                LogUtil.w(TAG,"SDK login cancel!");
                fireSdkInitFailed("Login Cancelled.");
                ErrorHelper.sendError(ErrorCode.SDK_LOGIN_CANCELED, "登录被取消");
            }
        });
    }

    private JSONObject getOfflineAsrSlots() {
        long startTimemills = System.currentTimeMillis();
        long endTimemills = 0;
        JSONObject slotJson = new JSONObject();
        try {
            {
                Map<String, String[]> slotMap = KookongCustomDataHelper.getKookongOfflineAsrSlotMap();

                String[] deviceList = slotMap.get(Constants.SLOT_DEVICELIST);
                if(deviceList != null) {
                    JSONArray slotdataArray1 = new JSONArray(deviceList);
                    slotJson.put(Constants.SLOT_DEVICELIST, slotdataArray1);
                }

                String[] customACStateList = slotMap.get(Constants.SLOT_CUSTOMACSTATELIST);
                if(customACStateList != null) {
                    JSONArray slotDataArray2 = new JSONArray(customACStateList);
                    slotJson.put(Constants.SLOT_CUSTOMACSTATELIST, slotDataArray2);
                }

                JSONArray slotdataArray = new JSONArray();
                slotdataArray.put("相机");
                slotdataArray.put("设置");
                slotdataArray.put("相册");
                slotdataArray.put("联系人");
                // 通用识别槽位
                slotJson.put(Constants.SLOT_APPNAME, slotdataArray);
            }
            {
                JSONArray slotdataArray = new JSONArray();
                ArrayList<String> contactNameList = Utils.getAllContacts(GnVoiceAssistApplication.getInstance());
                for(String name : contactNameList) {
                    slotdataArray.put(name);
                }
                // 通用识别槽位
                slotJson.put(Constants.SLOT_CONTACTNAME, slotdataArray);
            }

        } catch (Exception e) {
            ErrorHelper.sendError(ErrorCode.GENERATE_OFFLINESLOT_ERROR, "生成动态离线指令失败。失败原因：" + e);
            e.printStackTrace();
        } finally {
            endTimemills = System.currentTimeMillis();
            Log.i("liyh","getOfflineAsrSlots() duration = " + (endTimemills - startTimemills));
            return slotJson;
        }
    }

    private void updateInitStatus(InitStatus status) {
        mInitStatus = status;
    }

    public InitStatus getInitStatus() {
        return mInitStatus;
    }

    private void fireSdkInitSuccess() {
        Iterator<SdkInitCallback> iterator = exportInitCallbacks.iterator();
        while (iterator.hasNext()) {
            iterator.next().onInitSuccess();
        }
    }

    private void fireSdkInitFailed(String errorMsg) {
        Iterator<SdkInitCallback> iterator = exportInitCallbacks.iterator();
        while (iterator.hasNext()) {
            iterator.next().onInitFailed(errorMsg);
        }
    }

    public enum InitStatus {
        UNINIT,
        INITING,
        INITED
    }

}
