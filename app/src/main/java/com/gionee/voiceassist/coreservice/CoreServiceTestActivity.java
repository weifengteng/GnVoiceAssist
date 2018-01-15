package com.gionee.voiceassist.coreservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.controller.customuserinteraction.ICuiResult;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.AppLaunchDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ContactsDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GioneeCustomDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteTvDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.LocalAudioPlayerDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.PhoneCallDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.SmsDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.WebBrowserDirectiveEntity;
import com.gionee.voiceassist.customlink.CustomLinkSchema;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.RecognizerState;

import java.util.ArrayList;
import java.util.List;

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

        @Override
        public void onRecognizeStateChanged(RecognizerState state) {

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

        @Override
        public void onContactsPayload(ContactsDirectiveEntity payload) {
            showText("ContactsPayload received. " + payload);
        }

        @Override
        public void onPhonecallPayload(PhoneCallDirectiveEntity payload) {
            showText("PhonecallPayload received" + payload);
        }

        @Override
        public void onAppLaunchPayload(AppLaunchDirectiveEntity payload) {
            showText("AppLaunchPayload received" + payload);
            // TODO: 测试内容，待删除
            String appName = payload.getAppName();
            mService.getCUIController().startCustomUserInteraction(getPayloadGenerator(), getCUICallbackResult());

            mService.playTts("没有安装" + appName + ", 确定要下载吗？", "onAppLaunchPayload", new TtsCallback() {
                @Override
                public void onSpeakStart() {

                }

                @Override
                public void onSpeakFinish(String utterId) {
                    if(mService.getCUIController().isCustomUserInteractionProcessing()) {
                        mService.record();
                    }
                }

                @Override
                public void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s) {

                }
            });

        }

        public static final String DOWNLOAD_CONFIRM = "download_confirm";
        public static final String DOWNLOAD_CANCEL = "download_cancel";
        private CustomUserInteractionDeviceModule.PayLoadGenerator getPayloadGenerator() {
            CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator() {
                @Override
                public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState customClicentContextMachineState) {
                    LogUtil.d(TAG, "generateContextPayloadByInteractionState");
                    if (mService.getCUIController().isCUIShouldStop()) {
                        // 达到最大多轮交互次数，跳出自定义多轮交互状态
                        LogUtil.d(TAG, "confirmDownloadOrNot ******************************ShouldStopCurrentInteraction");
                        return new CustomClientContextPayload(null);
                    }
                    Payload payload;
                    ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();

                    // Yes
                    List<String> confrimUtterances = new ArrayList<>();
                    confrimUtterances.add("确定");
                    confrimUtterances.add("下载");
                    confrimUtterances.add("好的");
                    String confirmUrl = CustomLinkSchema.LINK_APP_DOWNLOAD + DOWNLOAD_CONFIRM;
                    CustomClientContextHyperUtterace confirmHyperUtterace = new CustomClientContextHyperUtterace(confrimUtterances, confirmUrl);
                    // No
                    List<String> cancelUtterances = new ArrayList<>();
                    cancelUtterances.add("取消");
                    cancelUtterances.add("取消下载");
                    cancelUtterances.add("不下载");
                    String cancelUrl = CustomLinkSchema.LINK_APP_DOWNLOAD + DOWNLOAD_CANCEL;
                    CustomClientContextHyperUtterace cancelHyperUtterance = new CustomClientContextHyperUtterace(cancelUtterances, cancelUrl);

                    hyperUtterances.add(confirmHyperUtterace);
                    hyperUtterances.add(cancelHyperUtterance);
                    payload = new CustomClientContextPayload(false, hyperUtterances);
                    return payload;
                }
            };

            return generator;
        }

        private ICuiResult getCUICallbackResult() {
            ICuiResult cuiCallbackImpl = new ICuiResult() {
                @Override
                public void handleCUInteractionUnknownUtterance() {
                    String alert = "您要下载还是取消？";
                    if(mService.getCUIController().isCUIShouldStop()) {
                        alert = "太累了,我先休息一下";
                        mService.getCUIController().stopCurrentCustomUserInteraction();
                        mService.playTts(alert);
                    }
                    mService.playTts(alert, "APPDownloadConfirmCUI", new TtsCallback() {
                        @Override
                        public void onSpeakStart() {

                        }

                        @Override
                        public void onSpeakFinish(String utterId) {
                            if(mService.getCUIController().isCustomUserInteractionProcessing()) {
                                mService.record();
                            }
                        }

                        @Override
                        public void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s) {

                        }
                    });

                }

                @Override
                public void handleCUInteractionTargetUrl(String url) {
                    showText("handleCUInteractionTargetUrl: " + url);
                }
            };

            return cuiCallbackImpl;
        }

        @Override
        public void onGioneeCustomCommandPayload(GioneeCustomDirectiveEntity payload) {
            String displayText = "";
            switch (payload.getAction()) {
                case LAUNCH_ALIPAY_SCAN:
                    displayText = "打开支付宝扫一扫";
                    break;
                case LAUNCH_ALIPAY_PAILITAO:
                    displayText = "打开拍立淘";
                    break;
                case LAUNCH_ALIPAY_PAYMENT_CODE:
                    displayText = "打开支付宝付款码";
                    break;
                case LAUNCH_HEARTRATE:
                    displayText = "测试心率";
                    break;
                case SHOW_MOBILE_DEVICE_INFO:
                    displayText = "显示设备信息";
                    break;
                case START_TIMER:
                    displayText = "打开倒计时器";
                    break;
                default:
                    break;
            }
            showText(displayText);
        }

        @Override
        public void onGnRemotePayload(GnRemoteDirectiveEntity payload) {
            showText("GnRemotePayload received " + payload);
        }

        @Override
        public void onGnRemoteTvPayload(GnRemoteTvDirectiveEntity payload) {
            showText("GnRemoteTvPayload received " + payload);
        }

        @Override
        public void onLocalAudioPlayerPayload(LocalAudioPlayerDirectiveEntity payload) {
            showText("LocalAudioPlayerPayload received " + payload);
        }

        @Override
        public void onWebBrowserPayload(WebBrowserDirectiveEntity payload) {
            showText("WebBrowserPayload received " + payload);
        }

        @Override
        public void onReminderPayload(ReminderDirectiveEntity payload) {
            showText("ReminderPayload received " + payload);
        }

        @Override
        public void onSmsSendPayload(SmsDirectiveEntity payload) {
            showText("SmsSendPayload received " + payload);
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
            default:
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
                default:
                    break;
            }
        }
    }


}
