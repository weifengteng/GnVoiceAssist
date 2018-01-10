package com.gionee.voiceassist;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.controller.appcontrol.DataController;
import com.gionee.voiceassist.controller.appcontrol.IRecognizerStateListener;
import com.gionee.voiceassist.controller.appcontrol.IRenderListener;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.PermissionsChecker;
import com.gionee.voiceassist.util.RecognizerState;
import com.gionee.voiceassist.widget.HomeRecyclerView;
import com.gionee.voiceassist.widget.HomeRecyclerViewAdapter;
import com.gionee.voiceassist.widget.RippleLayout;

import java.lang.ref.WeakReference;

/**
 * 没有启动唤醒功能
 */
public class HomeActivity extends GNBaseActivity implements View.OnClickListener, TtsCallback{
    public static final String TAG = HomeActivity.class.getSimpleName();
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

    private TextView tvTip;
    private RippleLayout btnRecord;
    private ImageButton btnHelp;
    private HomeRecyclerView rv;
    private HomeRecyclerViewAdapter rvAdapter;
    private LinearLayoutManager rvLayoutManager;
    private ObjectAnimator mRotationor;
    private ImageView anim_outside;
    private LinearLayout llHelpCommand;
    private ExpandableListView lvHelpCommand;


    private static Handler mMainHandler;
    private ErrorHelper mErrorHelper;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private boolean needInitFramework = true;

    private static final int MSG_RECOSTATE_ENGINE_INITING = 0x1000;
    private static final int MSG_RECOSTATE_ENGINE_INITSUCCESS = 0x1001;
    private static final int MSG_RECOSTATE_ENGINE_INITFAILED = 0x1002;
    private static final int MSG_RECOSTATE_RECORD_START = 0x1100;
    private static final int MSG_RECOSTATE_RECORD_STOP = 0x1101;
    private static final int MSG_RECOSTATE_TTS_START = 0x1102;
    private static final int MSG_RECOSTATE_TTS_STOP = 0x1103;
    private static final int MSG_RECOSTATE_STATE_CHANGED = 0x1104;

    public void showHelpPage() {
        //                LogUtil.i(TAG,"onClick btnHelp visibility" + llHelpCommand.getVisibility());
        //TODO Implement click btnHelp operate
//                if(llHelpCommand.getVisibility() == View.GONE){
//                    visibleListView();
//                }else{
//                    llHelpCommand.setVisibility(View.GONE);
//                    sv.setVisibility(View.VISIBLE);
//                }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        DataController.getDataController().onCreate();
    }

    @Override
    protected void onResume() {
        LogUtil.d(TAG, "onResume");
        // 缺少权限时, 进入权限配置页面
        if (checkPermission(PERMISSIONS)) {
            if(needInitFramework) {
                getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        initHandler();
                        initData();
                        DataController.getDataController().onResume();
                    }
                });
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataController.getDataController().onPause();
        if(btnRecord.isRippleAnimationRunning()){
            btnRecord.stopRippleAnimation();
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

    private IRecognizerStateListener recoStateListener = new IRecognizerStateListener() {
        @Override
        public void onRecordStart() {
            updateStartRecordingUI();
        }

        @Override
        public void onRecordStop() {
            updateStopRecordingUI();
        }

        @Override
        public void onTtsStart() {

        }

        @Override
        public void onTtsStop() {

        }

        @Override
        public void onInitStart() {
            btnRecord.setEnabled(false);
        }

        @Override
        public void onInitFinished() {
            btnRecord.setEnabled(true);
            playWelcomeWord();
        }

        @Override
        public void onInitFailed() {
            btnRecord.setEnabled(false);
        }

        @Override
        public void onStateChanged(RecognizerState state) {

        }
    };

    private IRenderListener renderListener = new IRenderListener() {

    };

    private void initView() {
        tvTip = (TextView)findViewById(R.id.tip);
        tvTip.setVisibility(View.VISIBLE);
        btnHelp = (ImageButton)findViewById(R.id.help);
        lvHelpCommand = (ExpandableListView)findViewById(R.id.home_listview);
        btnRecord = (RippleLayout)findViewById(R.id.ripple_layout);
        btnRecord.setEnabled(false);
//        rv = (HomeRecyclerView) findViewById(R.id.rv);
//        rvAdapter = new HomeRecyclerViewAdapter(this);
//        rvLayoutManager = new LinearLayoutManager(this);
//        rv.setAdapter(rvAdapter);
//        rv.setLayoutManager(rvLayoutManager);
        anim_outside = (ImageView)findViewById(R.id.anim_outside);
        mRotationor = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotation_animator);
        mRotationor.setTarget(anim_outside);
        llHelpCommand = (LinearLayout)findViewById(R.id.help_command);
        btnHelp.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
    }

    private void initData() {
        if (mErrorHelper == null) {
            mErrorHelper = new ErrorHelper();
        }
        mErrorHelper.registerErrorHandler();
        DataController.getDataController().addRecognizerStateListener(recoStateListener);
        DataController.getDataController().addRenderListener(renderListener);
        needInitFramework = false;
    }

    private void initHandler() {
        mMainHandler = new MainHandler(this);
        //TODO 架构调整过程中临时方法。演进过程中会逐步失效。
//        ((MainPresenter)mPresenter).setHandler(mMainHandler);
//        ((MainPresenter)mPresenter).setMainActivity(this);
    }

    private void updateStopRecordingUI() {
        tvTip.setVisibility(View.GONE);
        btnHelp.setVisibility(View.VISIBLE);
        btnRecord.stopRippleAnimation();
        LogUtil.d(TAG, "setUIByClick stopRippleAnimation");
    }

    private void updateStartRecordingUI() {
        btnRecord.startRippleAnimation();
        btnHelp.setVisibility(View.GONE);
        LogUtil.i(TAG, "setUIByClick startRippleAnimation");
    }

    private void playWelcomeWord() {
        DataController.getDataController().getServiceController().playTts("你好", UTTER_ID_WELCOME, this);
    }

//    private View addView(String recordResult,String backResult,View view) {
//
//        View showView = view;
//        mLastTextView = null;
//        if(!TextUtils.isEmpty(recordResult)){
//            showView = View.inflate(this, R.layout.reco_result, null);
//            TextView reco_result = (TextView)showView.findViewById(R.id.reco_result);
//            reco_result.setText(recordResult);
//            mLastTextView = reco_result;
//        }
//
//        if(!TextUtils.isEmpty(backResult)){
//            showView = View.inflate(this,R.layout.simple_host_info, null);
//            TextView info = (TextView)showView.findViewById(R.id.info);
//            info.setText(backResult);
//            mLastTextView = info;
//        }
//
//        llHelpCommand.setVisibility(View.GONE);
//        sv.setVisibility(View.VISIBLE);
//        sv_ll.addView(showView);
//        final int measuredHeight = sv_ll.getMeasuredHeight();
//        LogUtil.d(TAG, "VoiceHomeActivity measuredHeight  = " + measuredHeight);
//        sv.post(new Runnable() {
//            @Override
//            public void run() {
//                sv.scrollTo(0,measuredHeight);
//            }
//        });
//        return showView;
//    }

//    private void modifyView(String text) {
//        if(mLastTextView != null && mLastTextView instanceof TextView) {
//            ((TextView) mLastTextView).setText(text);
//        }
//    }

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
            btnRecord.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnRecord.callOnClick();
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
                break;
            case R.id.ripple_layout :
                LogUtil.e(TAG, "onClick ripple_layout");
                DataController.getDataController().triggerRecord();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        super.onDestroy();
        mErrorHelper.unregisterErrorHandler();
    }

    static class MainHandler extends Handler {
        private WeakReference<Activity> activityWeakReference;

        public MainHandler(HomeActivity activity) {
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HomeActivity mainActivity = null;
            if(activityWeakReference != null) {
                mainActivity = (HomeActivity) activityWeakReference.get();
            }
            switch (msg.what) {
                case MSG_RECOSTATE_ENGINE_INITING:
                    break;
                case MSG_RECOSTATE_ENGINE_INITSUCCESS:
                    break;
                case MSG_RECOSTATE_ENGINE_INITFAILED:
                    break;
                case MSG_RECOSTATE_RECORD_START:
                    break;
                case MSG_RECOSTATE_RECORD_STOP:
                    break;
                case MSG_RECOSTATE_TTS_START:
                    break;
                case MSG_RECOSTATE_TTS_STOP:
                    break;
                case MSG_RECOSTATE_STATE_CHANGED:
                    break;

            }
        }
    }
}
