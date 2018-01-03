package com.gionee.voiceassist;

import android.content.ContentResolver;
import android.os.Handler;
import android.provider.ContactsContract;

import com.baidu.duer.dcs.common.util.CommonUtil;
import com.gionee.voiceassist.basefunction.BaseFunctionManager;
import com.gionee.voiceassist.basefunction.contact.ContactObserver;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.directiveListener.DirectiveListenerManager;
import com.gionee.voiceassist.directiveListener.voiceinput.IVoiceInputEventListener;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.SharedData;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.util.kookong.KookongCustomDataHelper;

/**
 * MainActivity与具体业务之间的中间层，沟通MainActivity与底层SDK的媒介。
 * 用于解除Activity与Model层的耦合，并增加可测试性。
 */

public class MainPresenter implements MainContract.Presenter {

    public static final String UTTER_ID_WELCOME = "utter_id_welcome";

    private MainContract.View mHostView;
    private static final String TAG = MainPresenter.class.getSimpleName();

    private BaseFunctionManager baseFunctionManager;
    private DirectiveListenerManager mDirectiveListenerManager;
    private SdkController mSdkController;
    private ContactObserver mContactObserver;
    private ContentResolver mContentResolver;

    //监听器
    private IVoiceInputEventListener voiceInputEventListener = new IVoiceInputEventListener() {
        @Override
        public void onVoiceInputStart() {
            mHostView.onVoiceRecording(true);
        }

        @Override
        public void onVoiceInputStop() {
            mHostView.onVoiceRecording(false);
        }
    };

    //TODO 演进过程中的临时变量
    private Handler mMainHandler;
    private MainActivity mMainActivity;

    public MainPresenter(MainContract.View hostView) {
        mHostView = hostView;
    }

    @Override
    public void attach() {
        initSDK();
        initFrameWork();
        KookongCustomDataHelper.bindDataRetriveService();
        registerContentObserver();
        mHostView.onRecordingEnabled(true);
    }

    @Override
    public void detach() {
        unRegisterContentObserver();
        mDirectiveListenerManager.onDestroy();
        baseFunctionManager.onDestroy();
        mSdkController.destroy();
        mMainHandler = null;
        mMainActivity = null;
    }

    @Override
    public void launchRecord() {
        if(CommonUtil.isFastDoubleClick()) {
            return;
        }
        startVoiceCommand();
    }

    @Override
    public void openHelp() {
        mHostView.showHelpPage();
    }

    @Override
    public void closeHelp() {
        mHostView.hideHelpPage();
    }

    //TODO 临时方法。仅作重构过程中过渡使用
    public void setHandler(Handler handler) {
        mMainHandler = handler;
    }

    //TODO 临时方法。仅作重构过程中过渡使用
    public void setMainActivity(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    private void startVoiceCommand() {
        // 退出云端及本地多轮交互场景(权宜之计)
        RecordController.getInstance().stopCustomInteractContext();
        RecordController.getInstance().startRecord();
    }

    private void initFrameWork() {
        baseFunctionManager = new BaseFunctionManager();
        baseFunctionManager.setHandler(mMainHandler);
        baseFunctionManager.setMainActivity(mMainActivity);

        mDirectiveListenerManager = new DirectiveListenerManager(baseFunctionManager);
        mDirectiveListenerManager.initDirectiveListener();
        mDirectiveListenerManager.registerDirectiveListener();
    }

    private void initSDK() {
        mSdkController = SdkController.getInstance();
        mSdkController.init();
        sdkInitSuccess();
    }

    private void sdkInitSuccess() {
        TtsController.getInstance().playTTS("你好", UTTER_ID_WELCOME, new TtsCallback() {
            @Override
            public void onSpeakStart() {

            }

            @Override
            public void onSpeakFinish(String utterId) {
                startVoiceCommand();
            }

            @Override
            public void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s) {

            }
        });
        T.showShort("SDK 初始化成功");
    }

    private void registerContentObserver() {
        if(mContactObserver == null) {
            mContactObserver = new ContactObserver();
        }
        if(mContentResolver == null) {
            mContentResolver = GnVoiceAssistApplication.getInstance().getContentResolver();
        }
        mContentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mContactObserver);
    }

    private void unRegisterContentObserver() {
        mContentResolver.unregisterContentObserver(mContactObserver);
    }

}
