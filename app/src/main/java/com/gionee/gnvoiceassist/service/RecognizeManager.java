package com.gionee.gnvoiceassist.service;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.duer.dcs.androidsystemimpl.AudioRecordImpl;
import com.baidu.duer.dcs.androidsystemimpl.phonecall.IPhoneCallImpl;
import com.baidu.duer.dcs.androidsystemimpl.sms.ISmsImpl;
import com.baidu.duer.dcs.api.IASROffLineConfigProvider;
import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.devicemodule.contacts.ContactsDeviceModule;
import com.baidu.duer.dcs.devicemodule.phonecall.PhoneCallDeviceModule;
import com.baidu.duer.dcs.devicemodule.sms.SmsDeviceModule;
import com.baidu.duer.dcs.devicemodule.ttsoutput.TtsOutputDeviceModule;
import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.InternalApi;
import com.baidu.duer.dcs.framework.internalApi.DcsConfig;
import com.baidu.duer.dcs.framework.internalApi.IDcsRequestBodySentListener;
import com.baidu.duer.dcs.framework.internalApi.IErrorListener;
import com.baidu.duer.dcs.framework.message.DcsRequestBody;
import com.baidu.duer.dcs.oauth.api.credentials.BaiduOauthClientCredentialsImpl;
import com.baidu.duer.dcs.offline.asr.bean.ASROffLineConfig;
import com.baidu.duer.dcs.systeminterface.IAudioRecorder;
import com.baidu.duer.dcs.systeminterface.IOauth;
import com.gionee.gnvoiceassist.DirectiveListenerManager;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.directiveListener.location.LocationHandler;
import com.gionee.gnvoiceassist.directiveListener.voiceinput.AsrVoiceInputListener;
import com.gionee.gnvoiceassist.directiveListener.voiceinput.IVoiceInputEventListener;
import com.gionee.gnvoiceassist.directiveListener.voiceinputvolume.VoiceInputVolumeListener;
import com.gionee.gnvoiceassist.sdk.SdkManagerImpl;
import com.gionee.gnvoiceassist.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.applauncher.AppLauncherDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.applauncher.IAppLauncher;
import com.gionee.gnvoiceassist.sdk.module.applauncher.IAppLauncherImpl;
import com.gionee.gnvoiceassist.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.offlineasr.OffLineDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.screen.ScreenDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.screen.extend.card.ScreenExtendDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.webbrowser.WebBrowserDeviceModule;
import com.gionee.gnvoiceassist.tts.SpeakTxtListener;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.Constants.EngineState;
import com.gionee.gnvoiceassist.util.T;
import com.gionee.gnvoiceassist.util.Utils;
import com.gionee.gnvoiceassist.util.kookong.KookongCustomDataHelper;
import com.gionee.gnvoiceassist.util.Preconditions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上层服务与SDK层交互的接口。
 * 对SDK的生命周期进行管理，调用SDK实现相应的操作。
 */

public class RecognizeManager {

    private static final String TAG = RecognizeManager.class.getSimpleName();
    private static final int MSG_INNER_ENGINEINIT_BEGIN = 0x1000;
    private static final int MSG_INNER_ENGINEINIT_SUCCESS = 0x1001;
    private static final int MSG_INNER_OFFLINECOMMAND_GENERATED = 0x1002;
    private static final int MSG_INNER_DIRECTIVE_RECEIVED = 0x1003;


    private static RecognizeManager sINSTANCE;
    private IDcsSdk mDcsSdk;

    private InitEngineTask mInitTask;
    private LoadOfflineCommandTask mLoadOfflineCommandTask;

    //各种监听器
    private IErrorListener errorListener;
    private IDcsRequestBodySentListener requestBodySentListener;
    private LocationHandler locationHandler;
    private SpeakTxtListener ttsListener;
    private AsrVoiceInputListener asrVoiceInputListener;
    private IVoiceInputEventListener voiceInputEventListener;
    private VoiceInputVolumeListener voiceInputVolumeListener;
    private DirectiveListenerManager directiveListenerManager;

    //回调
    private IDirectiveListenerCallback mDirectiveCallback;

    //消息传递
    private RecognizeManagerHandler mLocalHandler;
    private List<IRecognizeManagerCallback> mExportCallbacks;

    //状态
    private EngineState mEngineStatus = EngineState.UNINIT;


    private RecognizeManager() {
        updateEngineState(EngineState.UNINIT);
        mLocalHandler = new RecognizeManagerHandler(this);
        mExportCallbacks = new ArrayList<>();
    }

    public static synchronized RecognizeManager getInstance() {
        if (sINSTANCE == null) {
            sINSTANCE = new RecognizeManager();
        }
        return sINSTANCE;
    }

    /**
     * 对语音识别相关组件进行初始化
     */
    public void init() {
        // 初始化完成的工作：
        // SDK初始化、SDK中DeviceModule的装入、SDK基础监听器(DialogStateListener、ErrorListener、
        // 语音识别状态、TTS状态）的注册、位置组件的注册
        mLocalHandler.sendEmptyMessage(MSG_INNER_ENGINEINIT_BEGIN);
    }

    /**
     * 释放语音识别相关组件
     */
    public void release() {
        // 释放（销毁）完成的工作：
        // SDK中DeviceModule的销毁、SDK基础监听器(DialogStateListener、ErrorListener、
        // 语音识别状态、TTS状态）的注销、位置组件的注销。最后释放SDK。
        releaseDeviceModule();
        unregisterEssentialListener();
        unregisterDirectiveListener();
        //若AsyncTask未完成工作，打断
        if (mInitTask != null && mInitTask.getStatus() != AsyncTask.Status.FINISHED) {
            mInitTask.cancel(true);
        }
        releaseSdk();
        // 释放Handler
        if (mLocalHandler != null) {
            mLocalHandler.removeCallbacksAndMessages(null);
            mLocalHandler = null;
        }
    }


    public void startRecord() {
        // true代表自动听音模式，即用户结束说话后，能识别句尾并自动停止
        if (mEngineStatus == EngineState.INITED) {
            //TODO 判断状态的if()语句，日后通过Annotation去掉
            mDcsSdk.getVoiceRequest().beginVoiceRequest(true);
        }
    }

    /**
     * 打断语音识别
     * @param abortRecognize 是否上传打断前的识别结果
     */
    public void abortRecord(boolean abortRecognize) {
        if (abortRecognize) {
            mDcsSdk.getVoiceRequest().cancelVoiceRequest();
        } else {
            mDcsSdk.getVoiceRequest().endVoiceRequest();
        }
    }

    /**
     * 播报TTS
     * @param text 需要播报的文字
     */
    public void startTts (String text) {
        if (mEngineStatus == EngineState.INITED) {
            SdkManagerImpl.getInstance().getInternalApi().speakOfflineQuery(text);
        }
    }

    /**
     * 播报TTS
     * @param text 需要播报的文字
     * @param utteranceId
     */
    public void startTts(String text, int utteranceId) {
        // TODO 处理UtteranceId。UtteranceId主要用作区分具体用途的TTS播报用。
        // 如在多轮交互下，播完tts后还会自动开始录音。
        if (mEngineStatus == EngineState.INITED) {
            SdkManagerImpl.getInstance().getInternalApi().speakOfflineQuery(text);
        }
    }

    /**
     * 外部传入离线语音指令
     */
    public void updateOfflineCommand(JSONObject offlineCommand) {
        //TODO 更新离线命令
        injectOfflineSlot(offlineCommand);
    }

    /**
     * 查询语音识别引擎状态
     * @return 引擎当前状态。INITED已初始化，INITING正在初始化，UNINIT未初始化
     */
    public EngineState getEngineStatus() {
        return mEngineStatus;
    }

    /**
     * 注册外界与RecognizeManager的回调监听
     * @param callback
     */
    public void addCallback(IRecognizeManagerCallback callback) {
        if (!mExportCallbacks.contains(callback)) {
            mExportCallbacks.add(callback);
        }
    }

    /**
     * 注销外界与RecognizeManager的回调监听
     * @param callback
     */
    public void removeCallback(IRecognizeManagerCallback callback) {
        if (!mExportCallbacks.contains(callback)) {
            mExportCallbacks.remove(callback);
        }
    }

    public void setDirectiveCallback(IDirectiveListenerCallback callback) {
        mDirectiveCallback = callback;
    }

    private void initSdk() {
        if (mEngineStatus == EngineState.UNINIT) {
            updateEngineState(EngineState.INITING);
            if (mInitTask == null) {
                mInitTask = new InitEngineTask();
            }
            mInitTask.execute();
        }
    }

    private void initSuccess() {
        registerEssentialListener();
        registerDirectiveListener();
        loadOfflineAsrSlots();
        updateEngineState(EngineState.INITED);
        mInitTask = null;
    }

    private void initDeviceModule() {
        //初始化应用启动模块AppLauncher
        IAppLauncher appLauncher = new IAppLauncherImpl
                (GnVoiceAssistApplication.getInstance().getApplicationContext());
        IMessageSender messageSender = ((DcsSdkImpl) mDcsSdk).getInternalApi().getMessageSender();
        AppLauncherDeviceModule appLauncherDeviceModule = new AppLauncherDeviceModule(appLauncher, messageSender);
        mDcsSdk.putDeviceModule(appLauncherDeviceModule);

        //初始化电话模块PhonecallDeviceModule
        PhoneCallDeviceModule phoneCallDeviceModule = new PhoneCallDeviceModule(new IPhoneCallImpl(),messageSender);
        mDcsSdk.putDeviceModule(phoneCallDeviceModule);

        //初始化短信模块SmsDeviceModule
        SmsDeviceModule smsDeviceModule = new SmsDeviceModule(new ISmsImpl(),messageSender);
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

        //初始化localAudioPlayerDeviceModule
        LocalAudioPlayerDeviceModule localAudioPlayerDeviceModule = new LocalAudioPlayerDeviceModule(messageSender);
        mDcsSdk.putDeviceModule(localAudioPlayerDeviceModule);

        //初始化离线识别模块
        OffLineDeviceModule offLineDeviceModule = new OffLineDeviceModule();
        mDcsSdk.putDeviceModule(offLineDeviceModule);
    }

    private void registerEssentialListener() {
        //TODO 初始化基础监听

        //注册SDK事件监听器
        if (requestBodySentListener == null) {
            requestBodySentListener = new IDcsRequestBodySentListener() {
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
            };
        }
        getSdkInternalApi().addRequestBodySentListener(requestBodySentListener);

        //注册SDK错误信息监听器
        if (errorListener == null) {
            errorListener = new IErrorListener() {
                @Override
                public void onErrorCode(ErrorCode errorCode) {
                    T.showShort("SDK出现错误" + errorCode.getMessage());
                }
            };
        }
        getSdkInternalApi().addErrorListener(errorListener);

        //注册SDK位置监听器（LocationHandler）
        locationHandler = new LocationHandler();
        getSdkInternalApi().setLocationHandler(locationHandler);

        //注册在线TTS监听器（SpeakTxtListener）
        ttsListener = new SpeakTxtListener();
        ((VoiceOutputDeviceModule)getSdkInternalApi()
                .getDeviceModule("ai.dueros.device_interface.voice_output"))
                .addVoiceOutputListener(ttsListener);

        //注册离线TTS监听器（AsrVoiceInputListener）
        ((TtsOutputDeviceModule)getSdkInternalApi()
                .getDeviceModule("ai.dueros.device_interface.tts_output"))
                .addVoiceOutputListener(ttsListener);

        //注册声音音量监听器（voiceInputVolumeListener）
        voiceInputVolumeListener = new VoiceInputVolumeListener();

        //注册状态监听器
        asrVoiceInputListener = new AsrVoiceInputListener();
        voiceInputEventListener = new IVoiceInputEventListener() {
            @Override
            public void onVoiceInputStart() {

            }

            @Override
            public void onVoiceInputStop() {

            }
        };
        asrVoiceInputListener.setVoiceInputEventListener(voiceInputEventListener);
        mDcsSdk.getVoiceRequest().addDialogStateListener(asrVoiceInputListener);

        //TODO 如何监听输入声音音量的变化？

    }

    private void registerDirectiveListener() {
        if (directiveListenerManager == null) {
            directiveListenerManager = new DirectiveListenerManager(mDirectiveCallback);
        }
        directiveListenerManager.registerDirectiveListener();
    }

    private void releaseDeviceModule() {
        //AppLauncher
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.app_launcher");
        //PhonecallDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.extensions.telephone");
        //SmsDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.extensions.sms");
        //ContactsDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.extensions.contacts");
        //WebBrowserDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.web_browser");
        //AlarmDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.android.alerts");
        //DeviceControlDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.extensions.device_control");
        //ScreenDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.screen");
        //TelecontrollerDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.thirdparty.gionee.voiceassist");
        //LocalAudioPlayerDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.extensions.local_audio_player");
        //OfflineDeviceModule
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.offline");
    }

    private void unregisterEssentialListener() {
        getSdkInternalApi().setLocationHandler(null);
        getSdkInternalApi().removeErrorListener(errorListener);
        getSdkInternalApi().removeRequestBodySentListener(requestBodySentListener);
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.voice_output");
        getSdkInternalApi().removeDeviceModule("ai.dueros.device_interface.tts_output");
        mDcsSdk.getVoiceRequest().removeDialogStateListener(asrVoiceInputListener);
    }

    private void unregisterDirectiveListener() {
        directiveListenerManager.unRegisterDirectiveListener();
    }

    private void releaseSdk() {
        if (mInitTask != null && mInitTask.getStatus() != AsyncTask.Status.FINISHED) {
            mInitTask.cancel(true);
        }
        updateEngineState(EngineState.UNINIT);
        mDcsSdk.release();
    }

    /**
     * (Asynchronously) Load dynamic offline recognition command. And send a Message to the
     * local Handler to inform when finished.
     */
    private void loadOfflineAsrSlots() {
        if (mLoadOfflineCommandTask == null) {
            mLoadOfflineCommandTask = new LoadOfflineCommandTask();
        }
        mLoadOfflineCommandTask.execute();
    }

    private void updateEngineState(EngineState state) {
        mEngineStatus = state;
        for (IRecognizeManagerCallback callback:mExportCallbacks) {
            callback.onEngineState(state);
        }
    }

    private void injectOfflineSlot(JSONObject offlineSlot) {
        if (mLoadOfflineCommandTask != null) {
            mLoadOfflineCommandTask.cancel(true);
            mLoadOfflineCommandTask = null;
        }
        //TODO 注入动态离线识别语法

    }

    private class InitEngineTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            IAudioRecorder audioRecorder = new AudioRecordImpl();
            String clientId = "83kW99iEz0jpGp9hrX981ezGcTaxNzk0";
            String clientSecret = "UTjgedIE5CRZM3CWj2cApLKajeZWotvf";
            String appId = "10290022";
            String apiKey = "bw40xRdDGFclaSIGzgXgNFdG";
            String secretKey = "AkP1XOuGVlrrML7dTs4WqW6bqj8lvv6C";
            IOauth oauth = new BaiduOauthClientCredentialsImpl(clientId, clientSecret);
            final ASROffLineConfig asrOffLineConfig = new ASROffLineConfig();
//            asrOffLineConfig.offlineAsrSlots = loadOfflineAsrSlots();
            asrOffLineConfig.asrAppId = appId;
            asrOffLineConfig.asrAppKey = apiKey;
            asrOffLineConfig.asrSecretKey = secretKey;

            IASROffLineConfigProvider asrOffLineConfigProvider = new IASROffLineConfigProvider() {
                @Override
                public ASROffLineConfig get() {
                    return asrOffLineConfig;
                }
            };
            mDcsSdk = new DcsSdkImpl.Builder()
                    .oauth(oauth)
                    .clientId(clientId)
                    .audioRecorder(audioRecorder)
                    .asrMode(DcsConfig.ASR_MODE_ONLINE)
                    .asrOffLineConfig(asrOffLineConfigProvider)
                    .build();

            initDeviceModule();


            // 第三步，将sdk跑起来
            mDcsSdk.run();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mLocalHandler.sendEmptyMessage(MSG_INNER_ENGINEINIT_SUCCESS);
        }
    }

    private class LoadOfflineCommandTask extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... voids) {
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
                e.printStackTrace();
            } finally {
                endTimemills = System.currentTimeMillis();
                Log.i("liyh","loadOfflineAsrSlots() duration = " + (endTimemills - startTimemills));
                return slotJson;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Message msg = mLocalHandler.obtainMessage(MSG_INNER_OFFLINECOMMAND_GENERATED,jsonObject);
            mLocalHandler.sendMessage(msg);
        }
    }

    private static class RecognizeManagerHandler extends Handler {

        WeakReference<RecognizeManager> mRecognizeManagerRef;

        RecognizeManagerHandler(RecognizeManager recognizeManager) {
            mRecognizeManagerRef = new WeakReference<RecognizeManager>(recognizeManager);
        }

        @Override
        public void handleMessage(Message msg) {
            RecognizeManager mRecognizeManager = mRecognizeManagerRef.get();
            switch (msg.what) {
                case MSG_INNER_ENGINEINIT_BEGIN:
                    mRecognizeManager.initSdk();
                    break;
                case MSG_INNER_ENGINEINIT_SUCCESS:
                    mRecognizeManager.initSuccess();
                    break;
                case MSG_INNER_OFFLINECOMMAND_GENERATED:
                    mRecognizeManager.injectOfflineSlot((JSONObject) msg.obj);
                    break;
                case MSG_INNER_DIRECTIVE_RECEIVED:
                    break;
            }
        }
    }

    private InternalApi getSdkInternalApi() {
        return ((DcsSdkImpl)Preconditions.checkNotNull(mDcsSdk)).getInternalApi();
    }



}
