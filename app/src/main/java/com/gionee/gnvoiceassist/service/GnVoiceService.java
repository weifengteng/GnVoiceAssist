package com.gionee.gnvoiceassist.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.basefunction.contact.ContactObserver;
import com.gionee.gnvoiceassist.message.model.CUIEntity;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.TtsEntity;
import com.gionee.gnvoiceassist.message.model.UsecaseResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.Metadata;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.tts.ISpeakTxtEventListener;
import com.gionee.gnvoiceassist.tts.TxtSpeakManager;
import com.gionee.gnvoiceassist.usecase.AlarmUseCase;
import com.gionee.gnvoiceassist.usecase.AppLaunchUseCase;
import com.gionee.gnvoiceassist.usecase.ContactsUseCase;
import com.gionee.gnvoiceassist.usecase.DeviceControlUseCase;
import com.gionee.gnvoiceassist.usecase.GnMusicUseCase;
import com.gionee.gnvoiceassist.usecase.GnRemoteUseCase;
import com.gionee.gnvoiceassist.usecase.PhonecallUseCase;
import com.gionee.gnvoiceassist.usecase.SmsSendUseCase;
import com.gionee.gnvoiceassist.usecase.TimeQueryUseCase;
import com.gionee.gnvoiceassist.usecase.UseCase;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.SoundPlayer;
import com.gionee.gnvoiceassist.util.kookong.KookongCustomDataHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.gionee.gnvoiceassist.util.Preconditions.checkNotNull;

public class GnVoiceService extends Service implements IDirectiveListenerCallback, UseCase.UsecaseCallback{

    private static final String TAG = GnVoiceService.class.getSimpleName();

    private static final int MSG_DIRECTIVE_RECEIVED = 101;
    private static final int MSG_RENDER_RECEIVED = 102;
    private static final int MSG_USECASE_RECEIVED = 103;
    private static final int MSG_TTSREQUEST_RECEIVED = 104;
    private static final int MSG_ENGINE_STATE_CHANGE = 201;
    private static final int MSG_RECORD_STATE_CHANGE = 202;
    private static final int MSG_RECORD_START = 203;
    private static final int MSG_RECORD_STOP = 204;
    private static final int MSG_CONTACT_UPDATE = 301;
    private static final int MSG_ENGINE_INITSUCCESS = 401;
    private static final int MSG_ENGINE_INITFAILURE = 402;
    private static final int MSG_RECOGNIZE_SUCCESS = 403;
    private static final int MSG_RECOGNIZE_FAILED = 404;

    private RecognizeManager mRecognizeManager;
    private ContactObserver mContactObserver;
    private ContentResolver mContentResolver;
    private TelephonyManager mTelephonyManager;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeCallback;
    private GnVoiceServiceBinder mBinder;
    private UsecaseHandler mUsecaseHandler;

    //消息传递
    private List<IVoiceServiceListener> mExportCallbacks;   //对外回调接口
    private IRecognizeManagerCallback mRmCallback = new IRecognizeManagerCallback() {
        @Override
        public void onEngineState(Constants.EngineState state) {
            if (mEngineState != state) {
                mEngineState = state;
                if (mEngineState == Constants.EngineState.INITED) {
                    mLocalHandler.sendEmptyMessage(MSG_ENGINE_INITSUCCESS);
                }
            }
            for(IVoiceServiceListener callback:mExportCallbacks) {
                callback.onEngineState(state);
            }
        }

        @Override
        public void onRecordStart() {
            mLocalHandler.sendEmptyMessage(MSG_RECORD_START);
            mRecording = true;
        }

        @Override
        public void onRecordStop() {
            mLocalHandler.sendEmptyMessage(MSG_RECORD_STOP);
            mRecording = false;
        }

        @Override
        public void onRecordError() {

        }

        @Override
        public void onRecordState(Constants.RecognitionState state) {
            for(IVoiceServiceListener callback:mExportCallbacks) {
                callback.onRecognizeState(state);
            }
        }

        @Override
        public void onTtsStart() {

        }

        @Override
        public void onTtsEnd() {

        }

        @Override
        public void onTtsState(boolean speaking) {

        }

        @Override
        public void onVolume(int volumeLevel) {

        }

        @Override
        public void onResult() {

        }
    };

    private InnerHandler mLocalHandler;
    private InnerPhoneStateListener mPhoneStateListener;

    private Constants.EngineState mEngineState = Constants.EngineState.UNINIT;
    private boolean mRecording = false;

    public GnVoiceService() {
        mBinder = new GnVoiceServiceBinder();
        mExportCallbacks = new ArrayList<>();
        mLocalHandler = new InnerHandler(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return checkNotNull(mBinder, "GnVoiceService中的Binder不能为空");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initEssentialComponent();
        initAdditionalComponent();
        initRecognizeManager();
        requestAudioFocus();    //TODO 处理焦点获取失败的情况
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyEssentialComponent();
        destroyAdditionalComponent();
        destroyRecognizeManager();
        abandonAudioFocus();
        if (mLocalHandler != null) {
            mLocalHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 开始录音
     */
    public void fireRecord() {
        if (!mRecording) {
            playBell(R.raw.ring_start);
            mLocalHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecognizeManager.startRecord();
                }
            },200);
        } else {
            mRecognizeManager.abortRecord(true);
        }
    }

    /**
     * 打断录音
     */
    public void abortRecord() {
        mRecognizeManager.abortRecord(true);
        //TODO 恢复音频焦点

    }

    public void stopRecord() {
        mRecognizeManager.abortRecord(false);
        //TODO 恢复音频焦点

    }

    /**
     * 使用TTS朗读文字
     * @param text
     */
    public void playTts(String text) {
        mRecognizeManager.startTts(text);
    }

    public void playTts(String text, String utterId, ISpeakTxtEventListener ttsProgressCallback) {
        mRecognizeManager.startTts(text,utterId,ttsProgressCallback);
    }

    /**
     * 停止所有TTS朗读
     */
    public void stopTts() {
        mRecognizeManager.abortRecord(true);
    }

    /**
     * 处理RecognizeManager返回的语音识别结果
     * 选择调用HandlerDispatcher分发到具体的Usecase，或显示到View
     */
    public void dispatchRecognizeResult(DirectiveResponseEntity directiveResult) {
        try {
            mUsecaseHandler.sendMessage(directiveResult);
        } catch (Exception e) {
            LogUtil.e(TAG,"dispatchRecognizeResult() 分发消息时出现错误。" + e);
            e.printStackTrace();
        }
    }

    /**
     * 处理Usecase返回的消息
     * @param usecaseResult UseCase返回的实体类
     */
    public void dispatchUsecaseResult(UsecaseResponseEntity usecaseResult) {

        if (usecaseResult.isShouldRender() && usecaseResult.getRenderContent() != null) {
            //取出RenderEntity
            //分发到界面进行渲染
            renderOnActivity(usecaseResult.getRenderContent());
        }

        if (usecaseResult.isInCustomInteractive() && usecaseResult.getCustomInteract() != null) {
            //取出CUIEntity，信息分发到RecognizeManager，让其主持发起多轮交互
            startCustomInteraction(usecaseResult.getMetadata(),usecaseResult.getCustomInteract());
        }
    }

    /**
     * 处理从View层传递的请求
     */
    public void dispatchViewRequest(RenderEntity renderData) {
        //TODO 将底层返回结果返回到界面
        renderOnActivity(renderData);
    }

    /**
     * 分发TTS播报请求
     * @param ttsData tts数据
     */
    public void dispatchTtsRequest(TtsEntity ttsData, boolean inCustomInteract) {
        //若为发起多轮交互形式的结果，则需要将TtsEntity的回调换地方
        String text = ttsData.getText();
        String utterId = ttsData.getUtterId();
        if (inCustomInteract) {
            //TODO 将ISpeakTxtEventListener匿名实现移动到别的地方
            playTts(text, utterId, new ISpeakTxtEventListener() {
                @Override
                public void onSpeakStart() {

                }

                @Override
                public void onSpeakFinish(String utterId) {
                    mLocalHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fireRecord();
                        }
                    },500);
                }

                @Override
                public void onSpeakError(TxtSpeakManager.TxtSpeakResult txtSpeakResult, String s) {

                }
            });
        }
        if (!TextUtils.isEmpty(ttsData.getUtterId()) && ttsData.getTtsProgressListener() != null) {
            playTts(text,utterId,checkNotNull(ttsData.getTtsProgressListener()));
        }
    }

    private void startCustomInteraction(String metadata, CUIEntity cuiData) {
        mRecognizeManager.startCustomInteraction(metadata,cuiData);
    }

    private synchronized void renderOnActivity(RenderEntity renderData) {
        for (IVoiceServiceListener listener:mExportCallbacks) {
            listener.onRenderRequest(renderData);
        }
    }

    private void initRecognizeManager() {
        if (mRecognizeManager == null) {
            mRecognizeManager = RecognizeManager.getInstance();
        }
        mRecognizeManager.addCallback(mRmCallback);
        mRecognizeManager.setDirectiveCallback(this);
        mRecognizeManager.init();
    }

    private void initEssentialComponent() {
        //TODO 初始化UsecaseHandler
        mUsecaseHandler = new UsecaseHandler();
        mUsecaseHandler.registerUsecase("applaunch",new AppLaunchUseCase(),this);
        mUsecaseHandler.registerUsecase("phonecall",new PhonecallUseCase(),this);
        mUsecaseHandler.registerUsecase("smssend",new SmsSendUseCase(),this);
        mUsecaseHandler.registerUsecase("alarm",new AlarmUseCase(),this);
        mUsecaseHandler.registerUsecase("contacts",new ContactsUseCase(),this);
        mUsecaseHandler.registerUsecase("device_control", new DeviceControlUseCase(),this);
        mUsecaseHandler.registerUsecase("gn_remote",new GnRemoteUseCase(),this);
        mUsecaseHandler.registerUsecase("gn_music",new GnMusicUseCase(),this);
        mUsecaseHandler.registerUsecase("time_query",new TimeQueryUseCase(),this);

        // 初始化ContentResolver
        mContentResolver = getContentResolver();    //是否要判断ContentResolver为空的情况？是否要使用ApplicationContext？
        // 初始化ContactObserver
        mContactObserver = new ContactObserver(mLocalHandler,GnVoiceService.this.getApplicationContext());
        mContentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI,true,mContactObserver);
        // 初始化Telephony监听器
        mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        // 初始化音频焦点监听
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        mAudioFocusChangeCallback = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                LogUtil.i(TAG, "onAudioFocusChange() focusChange = " + focusChange);
            }
        };

    }

    private void initAdditionalComponent() {
        // 初始化酷控服务
        KookongCustomDataHelper.bindDataRetriveService();
    }

    private void registerBroadcast() {
        //初始化广播接收器
        //需要接收以下广播：开机启动完成


    }

    private void destroyRecognizeManager() {
        if (mRecognizeManager != null) {
            mRecognizeManager.release();
        }
        mRecognizeManager.setDirectiveCallback(null);
        mRecognizeManager.removeCallback(mRmCallback);
    }

    private void destroyEssentialComponent() {
        //TODO 注销UsecaseDispatcher

        // 注销ContentResolver（不需要）

        //TODO 注销ContactObserver
        mContentResolver.unregisterContentObserver(mContactObserver);
        //TODO 注销Telephony监听器
        mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        mPhoneStateListener = null;
        mTelephonyManager = null;
        //TODO 注销音频焦点监听

    }

    private void destroyAdditionalComponent() {
        //注销酷控服务（不需要）
    }

    private void unregisterBroadcast() {
        //注销广播接收器

    }

    private void startUpdateContacts() {
        Intent intent = new Intent(GnVoiceService.this,ContactsUploadService.class);
        startService(intent);
    }

    private boolean requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus
                (mAudioFocusChangeCallback,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);

        return (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }

    private boolean abandonAudioFocus() {
        int result = mAudioManager.abandonAudioFocus(mAudioFocusChangeCallback);
        return (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }

    private void playBell(final int resId) {
        SoundPlayer soundPlayer = SoundPlayer.getInstance();
        if(null != soundPlayer) {
            LogUtil.d(TAG, "GnVoiceService playBell resId=" + resId);
            soundPlayer.playMusicSound(resId);
        }
    }

    @Override
    public void onDirectiveResponse(DirectiveResponseEntity response) {
        Message msg = mLocalHandler.obtainMessage(MSG_DIRECTIVE_RECEIVED,response);
        mLocalHandler.sendMessage(msg);
    }

    @Override
    public void onVoiceInputVolume(int level) {

    }

    @Override
    public void onRenderResponse(RenderEntity response) {
        Message msg = mLocalHandler.obtainMessage(MSG_RENDER_RECEIVED, response);
        mLocalHandler.sendMessage(msg);
    }

    @Override
    public void onUsecaseResponse(UsecaseResponseEntity response, TtsEntity tts) {
        Message responseMsg = mLocalHandler.obtainMessage(MSG_USECASE_RECEIVED,response);
        mLocalHandler.sendMessage(responseMsg);
        if (tts != null) {
            Message ttsMsg = mLocalHandler.obtainMessage
                    (MSG_TTSREQUEST_RECEIVED,response.isInCustomInteractive()?1:0,-1,tts);
            mLocalHandler.sendMessage(ttsMsg);
        }
    }

    /**
     * VoiceService的通信接口
     * 用于与建立连接的客户端进行通信
     */
    public class GnVoiceServiceBinder extends Binder {

        public void addCallback(IVoiceServiceListener listener) {
            if (!mExportCallbacks.contains(checkNotNull(listener))) {
                mExportCallbacks.add(checkNotNull(listener));
            }
        }

        public void removeCallback(IVoiceServiceListener listener) {
            if (!mExportCallbacks.contains(checkNotNull(listener))) {
                mExportCallbacks.remove(listener);
            }
        }

        public void startRecord() {
            fireRecord();
        }

        public void abortRecord() {
            GnVoiceService.this.abortRecord();
        }

        public void stopRecord() {
            GnVoiceService.this.stopRecord();
        }

        public void sendRequestMessage() {

        }
    }

    private static class InnerHandler extends Handler{

        private WeakReference<GnVoiceService> mVoiceService;

        InnerHandler(GnVoiceService service) {
            mVoiceService = new WeakReference<GnVoiceService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            GnVoiceService service = mVoiceService.get();
            switch (msg.what) {
                case MSG_CONTACT_UPDATE:
                    service.startUpdateContacts();
                    break;
                case MSG_RENDER_RECEIVED:
                    service.dispatchViewRequest((RenderEntity) msg.obj);
                    break;
                case MSG_DIRECTIVE_RECEIVED:
                    service.dispatchRecognizeResult((DirectiveResponseEntity) msg.obj);
                    break;
                case MSG_USECASE_RECEIVED:
                    service.dispatchUsecaseResult((UsecaseResponseEntity) msg.obj);
                    break;
                case MSG_TTSREQUEST_RECEIVED:
                    service.dispatchTtsRequest((TtsEntity) msg.obj, msg.arg1 == 1);
                    break;
                case MSG_RECORD_START:
                    break;
                case MSG_RECORD_STOP:
                    service.playBell(R.raw.ring_stop);
                    break;
                case MSG_ENGINE_INITSUCCESS:
                    service.playBell(R.raw.welcome);   //"您好"一词是一进入应用就播，还是引擎初始化后再播？
                    break;
            }
        }
    }

    private class InnerPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //TODO 当电话来电时，打断语音识别
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //打断语音识别
                    abortRecord();
                    // 是否需要退出界面？
                    break;
                default:
                    break;

            }
        }

    }

}
