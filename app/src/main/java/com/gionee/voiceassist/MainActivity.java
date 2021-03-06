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
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.directiveListener.audioplayer.IAudioPlayerStateListener;
import com.gionee.voiceassist.directiveListener.voiceinput.IVoiceInputEventListener;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.PermissionsChecker;
import com.gionee.voiceassist.view.widget.HomeScrollView;
import com.gionee.voiceassist.view.widget.RippleLayout;

import java.lang.ref.WeakReference;

/**
 * 没有启动唤醒功能
 */
public class MainActivity extends GNBaseActivity
        implements View.OnClickListener, IVoiceInputEventListener, IAudioPlayerStateListener, TtsCallback, MainContract.View {
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
    private ErrorHelper mErrorHelper;
    private MainContract.Presenter mPresenter;


    private LinearLayout help_command;
    private LinearLayout sv_ll;
    private ImageView anim_outside;
    private ObjectAnimator mRotationor;
    private HomeScrollView sv;
    private ExpandableListView home_listview;
    private RippleLayout rl;
    private TextView tip;
    private ImageButton help;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private boolean needInitFramework = true;
    private View mLastTextView;

    @Override
    public void showCard(CardEntity card) {

    }

    @Override
    public void onRecordingEnabled(boolean enabled) {
        if (enabled) {
            rl.setEnabled(true);
        } else {
            rl.setEnabled(false);
        }
    }

    @Override
    public void onVoiceRecording(boolean recording) {
        if (recording) {
            updateStartRecordingUI();
        } else {
            updateStopRecordingUI();
        }
    }

    @Override
    public void showHelpPage() {
        //                LogUtil.i(TAG,"onClick help visibility" + help_command.getVisibility());
        //TODO Implement click help operate
//                if(help_command.getVisibility() == View.GONE){
//                    visibleListView();
//                }else{
//                    help_command.setVisibility(View.GONE);
//                    sv.setVisibility(View.VISIBLE);
//                }
    }

    @Override
    public void hideHelpPage() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long startTs = System.currentTimeMillis();
        LogUtil.d(TAG, "onCreate");
        setContentView(R.layout.main_activity_layout);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        long endTs = System.currentTimeMillis();
        LogUtil.i("liyh","onCreate() duration = " + (endTs - startTs));
        mPresenter = new MainPresenter(MainActivity.this);
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
                        mPresenter.attach();
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
                    mPresenter.attach();
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

    private void initData() {
        LogUtil.d("liyh", "MainActivity, initData()");
        if (mErrorHelper == null) {
            mErrorHelper = new ErrorHelper();
        }
        mErrorHelper.registerErrorHandler();
        needInitFramework = false;
    }

    private void initHandler() {
        mMainHandler = new MainHandler(this);
        //TODO 架构调整过程中临时方法。演进过程中会逐步失效。
        ((MainPresenter)mPresenter).setHandler(mMainHandler);
        ((MainPresenter)mPresenter).setMainActivity(this);
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
        LogUtil.d(TAG, "setUIByClick stopRippleAnimation");
    }

    private void updateStartRecordingUI() {
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
                mPresenter.openHelp();
                break;
            case R.id.ripple_layout :
                LogUtil.e(TAG, "onClick ripple_layout");
                mPresenter.launchRecord();
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

        mPresenter.detach();
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

                case Constants.MSG_SHOW_QUERY:
                    String text = String.valueOf(msg.obj);
                    if(mainActivity != null) {
                        mainActivity.addView(text, null, null);
                    }
                    break;
                case Constants.MSG_SHOW_ANSWER:
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
                    View infoPanel = (View) msg.obj;
                    if(mainActivity != null) {
                        mainActivity.addView(null, null, infoPanel);
                    }
                    break;
                case Constants.MSG_UPDATE_INPUTVOLUME:
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
