package com.gionee.voiceassist.coreservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;

public class CoreServiceTestActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = CoreServiceTestActivity.class.getSimpleName();

    private static final int MSG_SHOW_TEXT = 1;

    private Button btnTestInit;
    private Button btnTestRecord;
    private Button btnTestTts;
    private Button btnTestDirectiveParse;
    private Button btnTestRender;
    private TextView tvResult;


    private UiHandler mUiHandler = new UiHandler();
    private IState mStateControl;
    private CoreService mService;
    private CoreService.CoreServiceBinder mBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                mBinder = (CoreService.CoreServiceBinder) service;
                mBinder.setSceneCallback(sceneCallback);
                mBinder.setStateCallback(stateCallback);
                mService = mBinder.getService();
            }
            Log.d(TAG, "Service Connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service Disconnected!");
        }
    };

    private CoreService.StateCallback stateCallback = new CoreService.StateCallback() {
        @Override
        public void onDirectivePayload(DirectiveEntity payload) {

        }

        @Override
        public void onSdkInit() {
            mStateControl = new InitedState();
            showText("SDK初始化成功");
        }

        @Override
        public void onRecordStart() {
            showText("正在录音");
        }

        @Override
        public void onRecordStop() {
            showText("停止录音");
        }

        @Override
        public void onTtsStart() {
            showText("正在播报TTS");
        }

        @Override
        public void onTtsStop() {
            showText("停止TTS播报");
        }
    };

    private CoreService.SceneCallback sceneCallback = new CoreService.SceneCallback() {
        @Override
        public void onScreenPayload(ScreenDirectiveEntity payload) {
            showText(payload.getContent());
        }

        @Override
        public void onAlarmPayload(AlarmDirectiveEntity payload) {
            showText("设置" + payload.getHour() + "点" + payload.getMinute() + "的闹钟");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_service_test);
        tvResult = (TextView) findViewById(R.id.tv_result);
        btnTestInit = (Button) findViewById(R.id.btn_test_init_state);
        btnTestRecord = (Button) findViewById(R.id.btn_test_record);
        btnTestTts = (Button) findViewById(R.id.btn_test_tts);
        btnTestDirectiveParse = (Button) findViewById(R.id.btn_test_directive_parse);
        btnTestRender= (Button) findViewById(R.id.btn_test_render_info);
        btnTestInit.setOnClickListener(this);
        btnTestRecord.setOnClickListener(this);
        btnTestTts.setOnClickListener(this);
        btnTestDirectiveParse.setOnClickListener(this);
        btnTestRender.setOnClickListener(this);

        //State Control
        mStateControl = new UninitState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(getServiceIntent(), mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    private void showText(String text) {
        Message msg = mUiHandler.obtainMessage(MSG_SHOW_TEXT, text);
        mUiHandler.sendMessage(msg);
    }

    private Intent getServiceIntent() {
        Intent intent = new Intent(CoreServiceTestActivity.this, CoreService.class);
        return intent;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_init_state:
                mService.init();
                break;
            case R.id.btn_test_record:
                mStateControl.record();
                break;
            case R.id.btn_test_tts:
                mStateControl.playTts();
                break;
            case R.id.btn_test_directive_parse:
                mStateControl.directiveParse();
                break;
            case R.id.btn_test_render_info:
                mStateControl.renderInfo();
                break;
        }
    }

    interface IState {
        void record();
        void playTts();
        void directiveParse();
        void renderInfo();
    }

    class UninitState implements IState {

        @Override
        public void record() {
            Toast.makeText(CoreServiceTestActivity.this, "Not inited", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void playTts() {
            Toast.makeText(CoreServiceTestActivity.this, "Not inited", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void directiveParse() {
            Toast.makeText(CoreServiceTestActivity.this, "Not inited", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void renderInfo() {
            Toast.makeText(CoreServiceTestActivity.this, "Not inited", Toast.LENGTH_SHORT).show();
        }
    }

    class InitedState implements IState {

        @Override
        public void record() {
            mService.record();
        }

        @Override
        public void playTts() {
            mService.playTts("你好，你听到这个声音说明你已经OK了");
        }

        @Override
        public void directiveParse() {
            mService.sendTextQuery("设置今天6点30分的闹钟");
        }

        @Override
        public void renderInfo() {
            mService.sendTextQuery("讲个笑话");
        }
    }

    private class UiHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_TEXT:
                    tvResult.setText((String)msg.obj);
                    break;
            }
        }
    }


}
