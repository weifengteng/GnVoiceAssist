package com.gionee.gnvoiceassist.home;

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

import com.baidu.duer.dcs.util.CommonUtil;
import com.gionee.gnvoiceassist.GNBaseActivity;
import com.gionee.gnvoiceassist.PermissionsActivity;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.message.model.render.TextRenderEntity;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.PermissionsChecker;
import com.gionee.gnvoiceassist.util.SharedData;
import com.gionee.gnvoiceassist.widget.HomeRecyclerView;
import com.gionee.gnvoiceassist.widget.HomeRecyclerViewAdapter;
import com.gionee.gnvoiceassist.widget.HomeScrollView;
import com.gionee.gnvoiceassist.widget.RippleLayout;

import java.lang.ref.WeakReference;
import static com.gionee.gnvoiceassist.util.Preconditions.checkNotNull;

/**
 * 没有启动唤醒功能
 */
public class HomeActivity extends GNBaseActivity implements View.OnClickListener, HomeContract.View {
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

    private TextView tvWelcomeTip;
    private RippleLayout btnRecord;
    private ImageButton btnHelp;
    private LinearLayout llResultScrollView;
    private HomeScrollView mScrollView;
    private HomeRecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mRecyclerViewLayoutManager;
    private LinearLayout llHelpCommandTips;
    private ExpandableListView lvHelpCommandTips;
    private ImageView ivAnimOutside;
    private ObjectAnimator mRotationor;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static Handler mMainHandler;
    private boolean mRecording = false;     //是否正在录音
    private HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initData();
        initHandler();
        initView();
    }

    @Override
    protected void onResume() {
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            mPresenter.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPause");
        if(btnRecord.isRippleAnimationRunning()){
            btnRecord.stopRippleAnimation();
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
                //TODO: 可以开始初始化Presenter
            }

        }
    }

    private void initView() {
        tvWelcomeTip = (TextView)findViewById(R.id.tip);
        tvWelcomeTip.setVisibility(View.GONE);
        btnHelp = (ImageButton)findViewById(R.id.help);
        lvHelpCommandTips = (ExpandableListView)findViewById(R.id.home_listview);
        llResultScrollView = (LinearLayout)findViewById(R.id.sv_ll);
        mScrollView = (HomeScrollView) findViewById(R.id.sv);
        btnRecord = (RippleLayout)findViewById(R.id.ripple_layout);
        mRecyclerView = (HomeRecyclerView) findViewById(R.id.rv);
        mAdapter = new HomeRecyclerViewAdapter(this);
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerView.setVisibility(View.VISIBLE);      //TODO 当RecyclerView内容为空时，显示WelcomeWord
        ivAnimOutside = (ImageView)findViewById(R.id.anim_outside);
        mRotationor = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotation_animator);
        mRotationor.setTarget(ivAnimOutside);
        llHelpCommandTips = (LinearLayout)findViewById(R.id.help_command);
        btnHelp.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
        mAdapter.setOnItemClickedListener(new HomeRecyclerViewAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(View view) {

            }

            @Override
            public void onOptionClicked(int position, View view) {
                
            }
        });
    }

    private void initData() {
        mPresenter = new HomePresenter(this,HomeActivity.this);
        mPermissionsChecker = new PermissionsChecker(this);
    }

    private void initHandler() {
        mMainHandler = new MainHandler(this);
    }

    /*public void setUIByClick(int clickId){
        switch (clickId) {
            case  DataKit.HOME_MSG_VOICE_STOP:
                tvWelcomeTip.setVisibility(View.GONE);
                Log.e("setUIByClick stopRippleAnimation");
                btnHelp.setVisibility(View.VISIBLE);
                btnRecord.stopRippleAnimation();
                mVoiceStatus = VoiceStatus.INPUT;
                break;
            case  DataKit.HOME_MSG_VOICE_START:
                Log.e("setUIByClick startRippleAnimation");
                btnRecord.startRippleAnimation();
                if(myService != null && myService.needShowTip() && llHelpCommandTips.getVisibility() != View.VISIBLE){
                    Log.e("setUIByClick needShowTip");
                    tvWelcomeTip.setVisibility(View.VISIBLE);
                }
                btnHelp.setVisibility(View.GONE);
//                llHelpCommandTips.setVisibility(View.GONE);
                break;
        }
    }*/


    //处理按下回退键、按下Home键、按下应用切换键的停止录音逻辑
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
        }
        mPresenter.destroy();
    }

    @Override
    public void onRecognizeStateChanged(Constants.RecognitionState state) {
        Message msg = mMainHandler.obtainMessage(Constants.MSG_UPDATE_RECOGNIZE_STATE,state);
        mMainHandler.sendMessage(msg);
    }

    @Override
    public void onEngineState(Constants.EngineState state) {
        if (mMainHandler != null) {
            Message msg = mMainHandler.obtainMessage(Constants.MSG_UPDATE_ENGINE_STATE,state);
            mMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void onResult(RenderEntity renderData) {
        Message message = new Message();
        if (renderData.getType() == RenderEntity.Type.TextCard && ((TextRenderEntity)renderData).isQueryText()) {
            message.what = Constants.MSG_SHOW_QUERY;
        } else {
            message.what = Constants.MSG_SHOW_ANSWER;
        }
        message.obj = renderData;
        mMainHandler.sendMessage(message);
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
//                LogUtil.i(TAG,"onClick btnHelp llHelpCommandTips.getVisibility() = " + llHelpCommandTips.getVisibility());
                //TODO Implement click btnHelp operate
//                if(llHelpCommandTips.getVisibility() == View.GONE){
//                    visibleListView();
//                }else{
//                    llHelpCommandTips.setVisibility(View.GONE);
//                    mScrollView.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.ripple_layout :
                LogUtil.e(TAG, "onClick ripple_layout");

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
                if(CommonUtil.isFastDoubleClick()) {
                    return;
                }
                startVoiceCommand();
                break;
            default:
                break;
        }
    }

    private void addView(String recordResult,String backResult,View view) {

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

        llHelpCommandTips.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        //Implement RecyclerView for better performance
//        mRecyclerView.setVisibility(View.VISIBLE);
        llResultScrollView.addView(showView);
//        mAdapter.addChildView(showView);
        final int measuredHeight = llResultScrollView.getMeasuredHeight();
        LogUtil.d(TAG, "VoiceHomeActivity measuredHeight  = " + measuredHeight);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0,measuredHeight);
            }
        });
    }

    private void updateRecognizeState(Constants.RecognitionState state) {
        switch (state){
            case LISTENING:
                updateRecordingStateUi(true);
                break;
            case THINKING:
                updateRecordingStateUi(false);
                //todo: implement thinking ui
                break;
            case SPEAKING:
                updateRecordingStateUi(false);
                break;
            case IDLE:
                updateRecordingStateUi(false);
                break;
        }
    }

    /**
     * 更新录音按钮状态UI
     * @param recording 是否正在录音
     */
    private void updateRecordingStateUi(boolean recording) {
        if (recording != mRecording) {  //录音状态发生改变
            if (recording) {
                updateStartRecordingUI();
            } else {
                updateStopRecordingUI();
            }
            mRecording = recording;
        }
    }

    private void updateStartRecordingUI() {
//        startTimeStopListen = System.currentTimeMillis();
        SharedData.getInstance().setStopListenReceiving(true);
        LogUtil.i(TAG, "setUIByClick startRippleAnimation");
        btnRecord.startRippleAnimation();
        /*if(myService != null && myService.needShowTip() && llHelpCommandTips.getVisibility() != View.VISIBLE){
            LogUtil.e(TAG, "setUIByClick needShowTip");
            tvWelcomeTip.setVisibility(View.VISIBLE);
        }*/
        btnHelp.setVisibility(View.GONE);
    }

    private void updateStopRecordingUI() {
        SharedData.getInstance().setStopListenReceiving(false);
        tvWelcomeTip.setVisibility(View.GONE);
        btnHelp.setVisibility(View.VISIBLE);
        btnRecord.stopRippleAnimation();
    }

    private void renderAnswer(RenderEntity answer) {
        mAdapter.addResultItem(answer);
    }

    private void renderQuery(RenderEntity query) {
        mAdapter.addQueryItem(query);
    }

    private void startVoiceCommand() {
        mPresenter.fireVoiceRequest();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        //Do-nothing
    }

    static class MainHandler extends Handler {
        private WeakReference<Activity> activityRef;

        public MainHandler(HomeActivity activity) {
            activityRef = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HomeActivity homeActivity = null;
            if(activityRef != null) {
                homeActivity = (HomeActivity) activityRef.get();
            }

            switch (msg.what) {
                case Constants.MSG_SHOW_QUERY:
                    if (homeActivity != null) {
                        homeActivity.renderQuery((RenderEntity) msg.obj);
                    }
//                    String text = String.valueOf(msg.obj);
//                    if(homeActivity != null) {
//                        homeActivity.addView(text, null, null);
//
                    break;
                case Constants.MSG_SHOW_ANSWER:
                    if (homeActivity != null)  {
                        homeActivity.renderAnswer((RenderEntity) msg.obj);
                    }
//                    String answer = String.valueOf(msg.obj);
//                    if(homeActivity != null) {
//                        homeActivity.addView(null, answer, null);
//                    }
                    break;
                case Constants.MSG_UPDATE_ENGINE_STATE:
                    //TODO Do Something when engine state changed
                    Constants.EngineState engineState = (Constants.EngineState) msg.obj;
                    boolean enableRecordButton = false;
                    if (engineState == Constants.EngineState.INITED) {
                        enableRecordButton = true;
                    } else {
                        enableRecordButton = false;
                    }
                    if (homeActivity != null) {
                        homeActivity.btnRecord.setEnabled(enableRecordButton);
                    }
                    break;
                case Constants.MSG_UPDATE_RECOGNIZE_STATE:
                    if (homeActivity != null) {
                        Constants.RecognitionState state = (Constants.RecognitionState) msg.obj;
                        homeActivity.updateRecognizeState(state);
                    }
                    break;
//                case Constants.MSG_SHOW_INFO_PANEL:
//                    View infoPanel = (View) msg.obj;
//                    if(homeActivity != null) {
//                        homeActivity.addView(null, null, infoPanel);
//                    }
//                    break;
            }
        }
    }
}
