package com.gionee.voiceassist;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.duer.dcs.common.util.CommonUtil;
import com.gionee.voiceassist.basefunction.BaseFunctionManager;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.basefunction.contact.ContactObserver;
import com.gionee.voiceassist.directiveListener.DirectiveListenerManager;
import com.gionee.voiceassist.directiveListener.audioplayer.IAudioPlayerStateListener;
import com.gionee.voiceassist.directiveListener.voiceinput.IVoiceInputEventListener;
import com.gionee.voiceassist.sdk.ISdkController;
import com.gionee.voiceassist.sdk.SdkController;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.ContactProcessor;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.PermissionsChecker;
import com.gionee.voiceassist.util.SharedData;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.util.Utils;
import com.gionee.voiceassist.util.kookong.KookongCustomDataHelper;
import com.gionee.voiceassist.widget.HomeRecyclerView;
import com.gionee.voiceassist.widget.HomeRecyclerViewAdapter;
import com.gionee.voiceassist.widget.HomeScrollView;
import com.gionee.voiceassist.widget.RippleLayout;

import java.lang.ref.WeakReference;

/**
 * 没有启动唤醒功能
 */
public class MainActivity extends GNBaseActivity implements View.OnClickListener, IVoiceInputEventListener, IAudioPlayerStateListener, TtsCallback {
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
    private static Handler mMainHandler;
    private IBaseFunction baseFunctionManager;
    private ContactObserver mContactObserver;
    private ContentResolver mContentResolver;

    private ISdkController mSdkManager;
    private ErrorHelper mErrorHelper;


    private LinearLayout help_command;
    private LinearLayout sv_ll;
    private ImageView anim_outside;
    private ObjectAnimator mRotationor;
    private HomeScrollView sv;
    private HomeRecyclerView rv;
    private HomeRecyclerViewAdapter rvAdapter;
    private LinearLayoutManager rvLayoutManager;
    private ExpandableListView home_listview;
    private RippleLayout rl;
    private TextView tip;
    private ImageButton help;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private boolean needInitFramework = true;
    private View mLastTextView;

    private DirectiveListenerManager mDirectiveListenerManager;

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
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        long endTs = System.currentTimeMillis();
        LogUtil.i("liyh","onCreate() duration = " + (endTs - startTs));
    }

    @Override
    protected void onResume() {
        LogUtil.d(TAG, "onResume");
        // 缺少权限时, 进入权限配置页面
        if (checkPermission(PERMISSIONS)) {
            LogUtil.d("liyh", "needInitFramework = " + needInitFramework);
            if(needInitFramework) {
                getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        initHandler();
                        initData();
                    }
                });
            }
        }
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

    /**
     * 检查权限的授予情况
     * @param requiredPermissions 需要检查的权限
     * @return 权限是否已被授予。True为全部授予，False为未授予
     */
    private boolean checkPermission(String[] requiredPermissions) {
        if (mPermissionsChecker == null) {
            mPermissionsChecker = new PermissionsChecker(this);
        }
        boolean lackPermissions = mPermissionsChecker.lacksPermissions(requiredPermissions);
        if (lackPermissions) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
            return false;
        }
        return true;
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
                    initHandler();
                    initData();
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
//        rv = (HomeRecyclerView) findViewById(R.id.rv);
//        rvAdapter = new HomeRecyclerViewAdapter(this);
//        rvLayoutManager = new LinearLayoutManager(this);
//        rv.setAdapter(rvAdapter);
//        rv.setLayoutManager(rvLayoutManager);
        anim_outside = (ImageView)findViewById(R.id.anim_outside);
        mRotationor = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotation_animator);
        mRotationor.setTarget(anim_outside);
        help_command = (LinearLayout)findViewById(R.id.help_command);
        help.setOnClickListener(this);
        rl.setOnClickListener(this);
    }

    private void initSDK() {
        mSdkManager = SdkController.getInstance();
        mSdkManager.init();
    }

    private void initFrameWork() {
        baseFunctionManager = new BaseFunctionManager();
        baseFunctionManager.setHandler(mMainHandler);
        baseFunctionManager.setMainActivity(this);

        mDirectiveListenerManager = new DirectiveListenerManager(baseFunctionManager);
        mDirectiveListenerManager.initDirectiveListener();
        mDirectiveListenerManager.registerDirectiveListener();
    }

    private void initData() {
        LogUtil.d("liyh", "MainActivity, initData()");
        if (mErrorHelper == null) {
            mErrorHelper = new ErrorHelper();
        }
        mErrorHelper.registerErrorHandler();
        initSDK();
        initFrameWork();
        KookongCustomDataHelper.bindDataRetriveService();
        registerContentObserver();
        mMainHandler.sendEmptyMessage(Constants.MSG_INIT_SUCCESS);
        handleUpdateContacts();
        needInitFramework = false;
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
        tip.setVisibility(View.GONE);
        help.setVisibility(View.VISIBLE);
        rl.stopRippleAnimation();
        SharedData.getInstance().setVadReceiving(false);
        LogUtil.d(TAG, "setUIByClick stopRippleAnimation");
    }

    private void updateStartRecordingUI() {
        SharedData.getInstance().setVadReceiving(true);
        rl.startRippleAnimation();
        help.setVisibility(View.GONE);
        LogUtil.i(TAG, "setUIByClick startRippleAnimation");
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

    private View addView(String recordResult,String backResult,View view) {

        View showView = view;
        mLastTextView = null;
        if(!TextUtils.isEmpty(recordResult)){
            showView = View.inflate(this, R.layout.reco_result, null);
            TextView reco_result = (TextView)showView.findViewById(R.id.reco_result);
            reco_result.setText(recordResult);
            mLastTextView = reco_result;
        }

        if(!TextUtils.isEmpty(backResult)){
            showView = View.inflate(this,R.layout.simple_host_info, null);
            TextView info = (TextView)showView.findViewById(R.id.info);
            info.setText(backResult);
            mLastTextView = info;
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
        return showView;
    }

    private void modifyView(String text) {
        if(mLastTextView != null && mLastTextView instanceof TextView) {
            ((TextView) mLastTextView).setText(text);
        }
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
            rl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rl.callOnClick();
                }
            },100);
        }
    }

    @Override
    public void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s) {

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
//                LogUtil.i(TAG,"onClick help visibility" + help_command.getVisibility());
                //TODO Implement click help operate
//                if(help_command.getVisibility() == View.GONE){
//                    visibleListView();
//                }else{
//                    help_command.setVisibility(View.GONE);
//                    sv.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.ripple_layout :
                LogUtil.e(TAG, "onClick ripple_layout");
                if(CommonUtil.isFastDoubleClick()) {
                    return;
                }
                startVoiceCommand();
                break;
            default:
                break;
        }
    }

    private void startVoiceCommand() {
        if(SharedData.getInstance().isVadReceiving()) {
            baseFunctionManager.getRecordController().stopRecord();
//            SharedData.getInstance().setVadReceiving(false);
            return;
        }
//        SharedData.getInstance().setVadReceiving(true);
        // 退出云端及本地多轮交互场景(权宜之计)
        baseFunctionManager.getRecordController().stopCustomInteractContext();
        baseFunctionManager.getRecordController().startRecord();
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
        boolean needupdate = ContactProcessor.getContactProcessor().needUpdateContacts();
        if (needupdate) {
            Utils.uploadContacts();
        }
    }

    private void sdkInitSuccess() {
        TtsController.getInstance().playTTS("你好", UTTER_ID_WELCOME, MainActivity.this);
        T.showShort("SDK 初始化成功");
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
        mErrorHelper.unregisterErrorHandler();

        if(mDirectiveListenerManager != null) {
            mDirectiveListenerManager.onDestroy();
            mDirectiveListenerManager = null;
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
                        mainActivity.sdkInitSuccess();
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
                case Constants.MSG_MODIFY_LAST_TEXT:
                    String textTobeModified = String.valueOf(msg.obj);
                    if(mainActivity != null) {
                        mainActivity.modifyView(textTobeModified);
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
                    default:
                        break;
            }
        }
    }
}
