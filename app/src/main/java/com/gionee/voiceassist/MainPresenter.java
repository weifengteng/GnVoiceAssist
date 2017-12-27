package com.gionee.voiceassist;

import android.os.Handler;

import com.baidu.duer.dcs.common.util.CommonUtil;
import com.gionee.voiceassist.basefunction.BaseFunctionManager;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.directiveListener.DirectiveListenerManager;
import com.gionee.voiceassist.directiveListener.voiceinput.IVoiceInputEventListener;
import com.gionee.voiceassist.sdk.SdkController;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.SharedData;
import com.gionee.voiceassist.util.kookong.KookongCustomDataHelper;

/**
 * MainActivity与具体业务之间的中间层，沟通MainActivity与底层SDK的媒介。
 * 用于解除Activity与Model层的耦合，并增加可测试性。
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mHostView;
    private static final String TAG = MainPresenter.class.getSimpleName();

    private BaseFunctionManager baseFunctionManager;
    private DirectiveListenerManager mDirectiveListenerManager;
    private SdkController mSdkController;

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
        mHostView.onRecordingEnabled(true);
    }

    @Override
    public void detach() {
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
        if(SharedData.getInstance().isVadReceiving()) {
            RecordController.getInstance().stopRecord();
            return;
        }
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
    }

}
