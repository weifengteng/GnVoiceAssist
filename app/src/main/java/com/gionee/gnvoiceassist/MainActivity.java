package com.gionee.gnvoiceassist;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.gnvoiceassist.basefunction.BaseFunctionManager;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.contact.ContactObserver;
import com.gionee.gnvoiceassist.directiveListener.audioplayer.IAudioPlayerStateListener;
import com.gionee.gnvoiceassist.directiveListener.voiceinput.IVoiceInputEventListener;
import com.gionee.gnvoiceassist.sdk.ISdkManager;
import com.gionee.gnvoiceassist.sdk.SdkManagerImpl;
import com.gionee.gnvoiceassist.tts.ISpeakTxtEventListener;
import com.gionee.gnvoiceassist.tts.TxtSpeakManager;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.ContactProcessor;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.PermissionsChecker;
import com.gionee.gnvoiceassist.util.SharedData;
import com.gionee.gnvoiceassist.util.Utils;
import com.gionee.gnvoiceassist.util.kookong.KookongCustomDataHelper;
import com.gionee.gnvoiceassist.util.threadpool.ThreadPoolManager;
import com.gionee.gnvoiceassist.widget.HomeScrollView;
import com.gionee.gnvoiceassist.widget.RippleLayout;

import java.lang.ref.WeakReference;

import static com.gionee.gnvoiceassist.util.Utils.doUserActivity;

/**
 * 没有启动唤醒功能
 */
public class MainActivity extends GNBaseActivity implements View.OnClickListener, IVoiceInputEventListener, IAudioPlayerStateListener, ISpeakTxtEventListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String UTTER_ID_WELCOME = "utter_id_welcome";
    private static final int REQUEST_CODE = 0; // 请求码
    private static  final String[] PERMISSIONS = new String[] {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private long startTimeStopListen;
    private static Handler mMainHandler;
    private IBaseFunction baseFunctionManager;
    private ContactObserver mContactObserver;
    private ContentResolver mContentResolver;

    private ISdkManager mSdkManager;


    private LinearLayout help_command;
    private LinearLayout sv_ll;
    private ImageView anim_outside;
    private ObjectAnimator mRotationor;
    private HomeScrollView sv;
    private ExpandableListView home_listview;
    private RippleLayout rl;
    private TextView tip;
    private ImageButton help;
    private VoiceStatus mVoiceStatus = VoiceStatus.INPUT;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private boolean needInitFramework = true;

    public static enum VoiceStatus {
        INPUT,
        RECOG
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long startTs = System.currentTimeMillis();
        LogUtil.d(TAG, "onCreate");
        setContentView(R.layout.home_activity_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initData();
        initHandler();
        initView();
        registerContentObserver();
        KookongCustomDataHelper.bindDataRetriveService();
        long endTs = System.currentTimeMillis();
        LogUtil.i("liyh","onCreate() duration = " + (endTs - startTs));
    }

    @Override
    protected void onResume() {
        LogUtil.d(TAG, "onResume");
        long startTs = System.currentTimeMillis();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            if(needInitFramework) {
                handleUpdateContacts();
                initDuerSDK();
                initFrameWork();
//                Runnable r = new Runnable() {
//                    @Override
//                    public void run() {
//                        Looper.prepare();
//                        handleUpdateContacts();
//                        initDuerSDK();
////                        Looper.loop();
//                        mMainHandler.sendEmptyMessage(Constants.MSG_INIT_SUCCESS);
////                        initFrameWork();
////                        needInitFramework = false;
//                    }
//                };
//                ThreadPoolManager.getInstance().executeTask(r);
//                TxtSpeakManager.getInstance().playTTS("你好", UTTER_ID_WELCOME, this);
//                startVoiceCommand();
                TxtSpeakManager.getInstance().playTTS("您好", UTTER_ID_WELCOME, this);
                needInitFramework = false;
            }
        }
        long endTs = System.currentTimeMillis();
        LogUtil.i("liyh","onResume() duration = " + (endTs - startTs));
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPause");
        if(rl.isRippleAnimationRunning()){
            rl.stopRippleAnimation();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE) {
            if(resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                finish();
            } else if(resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
                LogUtil.d(TAG, "onActivityResult    PERMISSIONS_GRANTED needInitFramework= " + needInitFramework);
                if(needInitFramework) {
                    handleUpdateContacts();
                    initDuerSDK();
//                    initFrameWork();
//                    TxtSpeakManager.getInstance().playTTS("您好", UTTER_ID_WELCOME, this);
//                    needInitFramework = false;
//                    mMainHandler.sendEmptyMessage(Constants.MSG_INIT_SUCCESS);
                }
            }

        }
    }

    private void initView() {
        tip = (TextView)findViewById(R.id.tip);
        tip.setVisibility(View.VISIBLE);
        help = (ImageButton)findViewById(R.id.help);
        home_listview = (ExpandableListView)findViewById(R.id.home_listview);
        sv_ll = (LinearLayout)findViewById(R.id.sv_ll);
        sv = (HomeScrollView) findViewById(R.id.sv);
        rl = (RippleLayout)findViewById(R.id.ripple_layout);
        anim_outside = (ImageView)findViewById(R.id.anim_outside);
        mRotationor = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotation_animator);
        mRotationor.setTarget(anim_outside);
        help_command = (LinearLayout)findViewById(R.id.help_command);
        help.setOnClickListener(this);
        rl.setOnClickListener(this);
    }

    private void initDuerSDK() {
        long startTs = System.currentTimeMillis();
        mSdkManager = SdkManagerImpl.getInstance();
        mSdkManager.init();
        long endTs = System.currentTimeMillis();
        LogUtil.i("liyh","initDuerSDK() duration = " + (endTs - startTs));
    }

    private void initFrameWork() {
        baseFunctionManager = new BaseFunctionManager();
        baseFunctionManager.setHandler(mMainHandler);
        baseFunctionManager.setMainActivity(this);

        DirectiveListenerManager directiveListenerManager = new DirectiveListenerManager(baseFunctionManager);
        directiveListenerManager.initDirectiveListener();
        directiveListenerManager.registerDirectiveListener();
    }

    private void initData() {
        mPermissionsChecker = new PermissionsChecker(this);
        needInitFramework = true;
    }

    private void initHandler() {
        mMainHandler = new MainHandler(this);
    }

    @Override
    public void onVoiceInputStart() {
        LogUtil.d(TAG, "onVoiceInputStart");
        updateStartRecordingUI();
    }

    @Override
    public void onVoiceInputStop() {
        LogUtil.d(TAG, "onVoiceInputStop");
        updateStopRecordingUI();
    }

    private void updateStopRecordingUI() {
        SharedData.getInstance().setStopListenReceiving(false);
//        voiceButton.setText(getResources().getString(R.string.stop_record));
//        long t = System.currentTimeMillis() - startTimeStopListen;
//        textViewTimeStopListen.setText(getResources().getString(R.string.time_record, t));

        tip.setVisibility(View.GONE);
        LogUtil.d(TAG, "setUIByClick stopRippleAnimation");
        help.setVisibility(View.VISIBLE);
        rl.stopRippleAnimation();
        mVoiceStatus = VoiceStatus.INPUT;
    }

    private void updateStartRecordingUI() {
        startTimeStopListen = System.currentTimeMillis();
        SharedData.getInstance().setStopListenReceiving(true);
//        DcsSdkImpl.getInstance().getSystemDeviceModule().getProvider().userActivity();
//        ((SystemDeviceModule)(mDcsSdk.getInternalApi().getDeviceModule("ai.dueros.device_interface.system")))
//                .getProvider().userActivity();
//        voiceButton.setText(getResources().getString(R.string.start_record));
//        textViewTimeStopListen.setText("");
//        textViewRenderVoiceInputText.setText("");

        LogUtil.e(TAG, "setUIByClick startRippleAnimation");
        rl.startRippleAnimation();
        /*if(myService != null && myService.needShowTip() && help_command.getVisibility() != View.VISIBLE){
            LogUtil.e(TAG, "setUIByClick needShowTip");
            tip.setVisibility(View.VISIBLE);
        }*/
        help.setVisibility(View.GONE);
    }

    /*public void setUIByClick(int clickId){
        switch (clickId) {
            case  DataKit.HOME_MSG_VOICE_STOP:
                tip.setVisibility(View.GONE);
                Log.e("setUIByClick stopRippleAnimation");
                help.setVisibility(View.VISIBLE);
                rl.stopRippleAnimation();
                mVoiceStatus = VoiceStatus.INPUT;
                break;
            case  DataKit.HOME_MSG_VOICE_START:
                Log.e("setUIByClick startRippleAnimation");
                rl.startRippleAnimation();
                if(myService != null && myService.needShowTip() && help_command.getVisibility() != View.VISIBLE){
                    Log.e("setUIByClick needShowTip");
                    tip.setVisibility(View.VISIBLE);
                }
                help.setVisibility(View.GONE);
//                help_command.setVisibility(View.GONE);
                break;
        }
    }*/

    public void setStatus(VoiceStatus status) {
        LogUtil.d(TAG, "VoiceHomeActivity setStatus status = " + status);
        mVoiceStatus = status;
        if(mVoiceStatus == VoiceStatus.RECOG) {
            anim_outside.setVisibility(View.VISIBLE);
            rl.stopRippleAnimation();
            mRotationor.start();
        }else{
            anim_outside.setVisibility(View.GONE);
            mRotationor.end();
        }
    }


    @Override
    public void onPlaying() {
//        pauseOrPlayButton.setText(getResources().getString(R.string.audio_playing));
//        isPause = false;
    }

    @Override
    public void onPaused() {
//        pauseOrPlayButton.setText(getResources().getString(R.string.audio_paused));
//        isPause = true;
    }

    @Override
    public void onStopped() {
//        pauseOrPlayButton.setText(getResources().getString(R.string.audio_default));
//        isPause = false;
    }

    @Override
    public void onCompletion() {
//        pauseOrPlayButton.setText(getResources().getString(R.string.audio_default));
//        isPause = false;
    }

    private void addView(String recordResult,String backResult,View view) {
        /*if(myService != null && myService.needShowTip()){
            myService.setNeedShowTip(false);
        }*/
        View showView = view;
        if(!TextUtils.isEmpty(recordResult)){
            showView = View.inflate(this, R.layout.reco_result, null);
            TextView reco_result = (TextView)showView.findViewById(R.id.reco_result);
            reco_result.setText(recordResult);
        }

        if(!TextUtils.isEmpty(backResult)){
            showView = View.inflate(this,R.layout.simple_host_info, null);
            TextView info = (TextView)showView.findViewById(R.id.info);
            info.setText(backResult);
        }

        help_command.setVisibility(View.GONE);
        sv.setVisibility(View.VISIBLE);
        sv_ll.addView(showView);
        final int measuredHeight = sv_ll.getMeasuredHeight();
        LogUtil.d(TAG, "VoiceHomeActivity measuredHeight  = " + measuredHeight);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0,measuredHeight);
            }
        });
    }

    private void updateVoiceInputVolume(int volume) {
        // TODO:
    }

    @Override
    public void onSpeakStart() {

    }

    @Override
    public void onSpeakFinish(String utterId) {
        LogUtil.d(TAG, "onSpeakFinish");
        if(TextUtils.equals(utterId, UTTER_ID_WELCOME)) {
            rl.callOnClick();
        }
    }

    @Override
    public void onSpeakError(TxtSpeakManager.TxtSpeakResult txtSpeakResult, String s) {
        //note by liyh [refactor]: 此处原来还有一个参数是错误信息码，但是引用了百度自身的sdk包的enum。建议使用自己的enum。

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        // TODO:
        switch (v.getId()) {
            case R.id.help :
//                Log.e("onClick help help_command.getVisibility() = " + help_command.getVisibility());
//                if(help_command.getVisibility() == View.GONE){
//                    visibleListView();
//                }else{
//                    help_command.setVisibility(View.GONE);
//                    sv.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.ripple_layout :
                LogUtil.e(TAG, "onClick ripple_layout");
                /*if(rl.isRippleAnimationRunning()){
                    LogUtil.e(TAG, "onClick ripple_layout stopRecognize() ");
                    // TODO:
                    stopCurOperation();
//                    stopRecognize();
                }else{
                    if(!myService.isReco())
                        myService.startReco(new Intent());
                }*/

//            case R.id.voiceBtn:
//                if(CommonUtil.isFastDoubleClick()) {
//                    return;
//                }
//                if(SharedData.getInstance().isStopListenReceiving()) {
//                    baseFunctionManager.getRecordController().stopRecord();
//                    SharedData.getInstance().setStopListenReceiving(false);
//                    return;
//                }
//                SharedData.getInstance().setStopListenReceiving(true);
//                startTimeStopListen = System.currentTimeMillis();
//                // TODO：强制退出云端多轮交互场景(权宜之计)
////                DcsSDK.getInstance().getSystemDeviceModule().sendExitedEvent();
////                ((SystemDeviceModule)
////                        (DcsSdkImpl.getInstance().getInternalApi().getDeviceModule("ai.dueros.device_interface.system")))
////                        .release();
////                baseFunctionManager.getRecordController().startRecordOfflineOnly();
//                baseFunctionManager.getRecordController().startRecordOfflinePrior();
////                baseFunctionManager.getRecordController().startRecordOnline();
//                doUserActivity();
                startVoiceCommand();
                break;
//            case R.id.openLogBtn:
//
//                break;
//            case R.id.previousSongBtn:
//
//                break;
//            case R.id.nextSongBtn:
//
//                break;
//            case R.id.pauseOrPlayBtn:
//
//                break;
            default:
                break;
        }
    }

    /*@Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        LogUtil.d(TAG, "VoiceHomeActivity onKey event.getAction() = " + event.getAction() + ", KeyEvent.ACTION_UP = " + KeyEvent.ACTION_UP
                + ", keyCode = " + keyCode + ", KeyEvent.KEYCODE_BACK = " + KeyEvent.KEYCODE_BACK);
        if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//            stopCurOperation();
            // TODO:
            return true;
        }
        return false;
    }*/

    private void startVoiceCommand() {
        if(SharedData.getInstance().isStopListenReceiving()) {
            baseFunctionManager.getRecordController().stopRecord();
            SharedData.getInstance().setStopListenReceiving(false);
            return;
        }
        SharedData.getInstance().setStopListenReceiving(true);
        startTimeStopListen = System.currentTimeMillis();
        // TODO：强制退出云端多轮交互场景(权宜之计)
//                DcsSDK.getInstance().getSystemDeviceModule().sendExitedEvent();
//                ((SystemDeviceModule)
//                        (DcsSdkImpl.getInstance().getInternalApi().getDeviceModule("ai.dueros.device_interface.system")))
//                        .release();
//                baseFunctionManager.getRecordController().startRecordOfflineOnly();
//        baseFunctionManager.getRecordController().startRecordOfflinePrior();
        baseFunctionManager.getRecordController().startRecordOnline();
    }

    private void registerContentObserver() {
        long startTs = System.currentTimeMillis();
        //TODO: 将联系人变化检测Observer放到单独的类中
        if(mContactObserver == null) {
            mContactObserver = new ContactObserver(mMainHandler, this.getApplicationContext());
        }
        if(mContentResolver == null) {
            mContentResolver = this.getApplicationContext().getContentResolver();
        }
        mContentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mContactObserver);
        long endTs = System.currentTimeMillis();
        LogUtil.i("liyh","registerContentObserver() duration = " + (endTs - startTs));
    }

    private void unRegisterContentObserver() {
        mContentResolver.unregisterContentObserver(mContactObserver);
    }

    private void handleUpdateContacts() {
        LogUtil.d(TAG, "handleUpdateContacts");
        long startTs = System.currentTimeMillis();
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                Utils.uploadContacts();
//                boolean needupdate = ContactProcessor.getContactProcessor().needUpdateContacts();
//            }
//        };

//        ThreadPoolManager.getInstance().executeTask(r);
        Utils.uploadContacts();
        boolean needupdate = ContactProcessor.getContactProcessor().needUpdateContacts();
        long endTs = System.currentTimeMillis();

        LogUtil.i("liyh","handleUpdateContacts() duration = " + (endTs - startTs));
    }

    private void initSuccess() {
        LogUtil.i("liyh","initSuccess()");
        initFrameWork();
        needInitFramework = false;
        TxtSpeakManager.getInstance().playTTS("你好", UTTER_ID_WELCOME, MainActivity.this);
    }


    @Override
    protected void onDestroy() {
        unRegisterContentObserver();
        if(mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        super.onDestroy();
        if(!needInitFramework) {
            mSdkManager.destroy();
        }
    }

    static class MainHandler extends Handler {
        private WeakReference<Activity> activityWeakReference;

        public MainHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = null;
            if(activityWeakReference != null) {
                mainActivity = (MainActivity) activityWeakReference.get();
            }

            switch (msg.what) {

                case Constants.MSG_INIT_SUCCESS:
                    if (mainActivity != null) {
                        mainActivity.initSuccess();
                    }
                    break;
                case Constants.MSG_SHOW_QUERY:
//                    LogUtil.d(TAG, "MainHandler MSG_SHOW_QUERY");
                    String text = String.valueOf(msg.obj);
                    if(mainActivity != null) {
                        mainActivity.addView(text, null, null);
                    }
                    break;
                case Constants.MSG_SHOW_ANSWER:
//                    LogUtil.d(TAG, "MainHandler MSG_SHOW_ANSWER");
                    String answer = String.valueOf(msg.obj);
                    if(mainActivity != null) {
                        mainActivity.addView(null, answer, null);
                    }
                    break;
                case Constants.MSG_SHOW_INFO_PANEL:
//                    LogUtil.d(TAG, "MainHandler MSG_SHOW_INFO_PANEL");
                    View infoPanel = (View) msg.obj;
                    if(mainActivity != null) {
                        mainActivity.addView(null, null, infoPanel);
                    }
                    break;
                case Constants.MSG_UPDATE_CONTACTS:
//                    LogUtil.d(TAG, "MainHandler MSG_UPDATE_CONTACTS");
                    if(mainActivity != null) {
                        mainActivity.handleUpdateContacts();
                    }
                    break;
                case Constants.MSG_UPDATE_INPUTVOLUME:
//                    LogUtil.d(TAG, "MainHandler MSG_UPDATE_INPUTVOLUME");
                    int volume = msg.arg1;
                    if(mainActivity != null) {
                        mainActivity.updateVoiceInputVolume(volume);
                    }
                    break;
            }
        }
    }
}
