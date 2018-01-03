package com.gionee.voiceassist.coreservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.InternalApi;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.listener.directive.DirectiveListenerController;
import com.gionee.voiceassist.coreservice.listener.state.StateListenerController;
import com.gionee.voiceassist.coreservice.sdk.ISdkController;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 核心业务Service
 * 提供语义解析
 * 提供TTS播报
 * 提供语音转写服务
 */
public class CoreService extends Service {

    private static final String TAG = CoreService.class.getSimpleName();

    private SdkController mSdkController;
    private StateListenerController mStateController;
    private DirectiveListenerController mDirectiveController;
    private List<StateCallback> mExportStateCallbacks;
    private List<SceneCallback> mExportSceneCallbacks;
    private DirectiveListenerController.DirectiveCallback mDirectiveCallback = new DirectiveListenerController.DirectiveCallback() {
        @Override
        public void onDirectiveMessage(DirectiveEntity msg) {
            dispatchDirectiveMsg(msg);
        }
    };

    private ISdkController.SdkInitCallback mSdkInitCallback = new ISdkController.SdkInitCallback() {
        @Override
        public void onInitSuccess() {
            Iterator<StateCallback> iter = mExportStateCallbacks.iterator();
            while (iter.hasNext()) {
                iter.next().onSdkInit();
            }
        }

        @Override
        public void onInitFailed(String errorMsg) {
            LogUtil.e(TAG, "SDK initialize failed. Reason: " + errorMsg);
        }
    };

    private CoreServiceBinder mCoreBinder;

    public CoreService() {
        //Initialize Core Binder
        mCoreBinder = new CoreServiceBinder();
        //Initialize Export Callbacks
        mExportSceneCallbacks = new ArrayList<>();
        mExportStateCallbacks = new ArrayList<>();
        //Initialize Controller
        mSdkController = SdkController.getInstance();
        mStateController = new StateListenerController();
        mDirectiveController = new DirectiveListenerController();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mCoreBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Register essential callback
        mStateController.setStateCallbacks(mExportStateCallbacks);
        mDirectiveController.subscribe(mDirectiveCallback);
        mSdkController.addSdkInitCallback(mSdkInitCallback);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDirectiveController.unsubscribe(mDirectiveCallback);
        mSdkController.removeSdkInitCallback(mSdkInitCallback);
        destroy();
    }

    public void init() {
        mSdkController.init();
        mStateController.init();
        mDirectiveController.init();

    }

    public void destroy() {
        mDirectiveController.destroy();
        mSdkController.destroy();
    }

    public void record() {
        RecordController.getInstance().startRecord();
    }

    public void sendTextQuery(String text) {
        mSdkController.getSdkInternalApi().sendQuery(text);
    }

    public void playTts(String ttsText) {
        TtsController.getInstance().playTTS(ttsText);
    }

    private void dispatchDirectiveMsg(DirectiveEntity directivePayload) {
        DirectiveEntity.Type payloadType = directivePayload.getType();
        switch (payloadType) {
            //TODO 这样通过hard-code分发肯定是不行的。后期再慢慢地优化这个分发
            case ALARM:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onAlarmPayload((AlarmDirectiveEntity) directivePayload);
                }
                break;
            case SCREEN:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onScreenPayload((ScreenDirectiveEntity) directivePayload);
                }
        }

    }

    private IDcsSdk getSdkInstance() {
        return mSdkController.getSdkInstance();
    }

    private DcsSdkImpl getSdkImplInstance() {
        return (DcsSdkImpl) mSdkController.getSdkInstance();
    }

    private InternalApi getSdkInternalApi() {
        return getSdkImplInstance().getInternalApi();
    }

    public class CoreServiceBinder extends Binder {
        public void setStateCallback(StateCallback callback) {
            if (!mExportStateCallbacks.contains(callback)) {
                mExportStateCallbacks.add(callback);
            }
        }
        public void setSceneCallback(SceneCallback callback) {
            if (!mExportSceneCallbacks.contains(callback)) {
                mExportSceneCallbacks.add(callback);
            }
        }

        public CoreService getService() {
            return CoreService.this;
        }
    }

    public interface StateCallback {
        void onDirectivePayload(DirectiveEntity payload);
        void onSdkInit();
        void onRecordStart();
        void onRecordStop();
        void onTtsStart();
        void onTtsStop();
    }

    public interface SceneCallback {
        void onScreenPayload(ScreenDirectiveEntity payload);
        void onAlarmPayload(AlarmDirectiveEntity payload);
    }
}
