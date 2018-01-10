package com.gionee.voiceassist.coreservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.InternalApi;
import com.gionee.voiceassist.controller.customuserinteraction.CuiController;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.AppLaunchDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ContactsDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GioneeCustomDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteTvDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.LocalAudioPlayerDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.PhonecallDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.WebBrowserDirectiveEntity;
import com.gionee.voiceassist.coreservice.listener.directive.DirectiveListenerController;
import com.gionee.voiceassist.coreservice.listener.state.StateListenerController;
import com.gionee.voiceassist.coreservice.sdk.ISdkController;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.RecognizerState;

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
    private CuiController mCuiController;
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

    public void playTts(String ttsText, String utterId, TtsCallback callback) {
        TtsController.getInstance().playTTS(ttsText, utterId, callback);
    }

    public SdkController.InitStatus getInitStatus() {
        return SdkController.getInstance().getInitStatus();
    }

    public CuiController getCUIController() {
        if(mCuiController == null) {
            mCuiController = new CuiController();
        }
        return mCuiController;
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
                break;
            case CONTACTS:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onContactsPayload((ContactsDirectiveEntity) directivePayload);
                }
                break;
            case PHONECALL:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onPhonecallPayload((PhonecallDirectiveEntity) directivePayload);
                }
                break;
            case SMS:
                for (SceneCallback callback:mExportSceneCallbacks) {

                }
                break;
            case APPLAUNCH:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onAppLaunchPayload((AppLaunchDirectiveEntity) directivePayload);
                }
                break;
            case GIONEE_CUSTOM_COMMAND:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onGioneeCustomCommandPayload((GioneeCustomDirectiveEntity) directivePayload);
                }
                break;
            case GN_REMOTE:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onGnRemotePayload((GnRemoteDirectiveEntity) directivePayload);
                }
                break;
            case GN_REMOTE_TV:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onGnRemoteTvPayload((GnRemoteTvDirectiveEntity) directivePayload);
                }
                break;
            case LOCAL_AUDIOPLAYER:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onLocalAudioPlayerPayload((LocalAudioPlayerDirectiveEntity) directivePayload);
                }
                break;
            case WEBBROWSER:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onWebBrowserPayload((WebBrowserDirectiveEntity) directivePayload);
                }
                break;
            case REMINDER:
                for (SceneCallback callback:mExportSceneCallbacks) {
                    callback.onReminderPayload((ReminderDirectiveEntity) directivePayload);
                }
            default:
                break;
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
        void onSdkInit();
        void onRecordStart();
        void onRecordStop();
        void onTtsStart();
        void onTtsStop();
        void onRecognizeStateChanged(RecognizerState state);
    }

    public interface SceneCallback {
        void onScreenPayload(ScreenDirectiveEntity payload);
        void onAlarmPayload(AlarmDirectiveEntity payload);
        void onContactsPayload(ContactsDirectiveEntity payload);
        void onPhonecallPayload(PhonecallDirectiveEntity payload);
        void onAppLaunchPayload(AppLaunchDirectiveEntity payload);
        void onGioneeCustomCommandPayload(GioneeCustomDirectiveEntity payload);
        void onGnRemotePayload(GnRemoteDirectiveEntity payload);
        void onGnRemoteTvPayload(GnRemoteTvDirectiveEntity payload);
        void onLocalAudioPlayerPayload(LocalAudioPlayerDirectiveEntity payload);
        void onWebBrowserPayload(WebBrowserDirectiveEntity payload);
        void onReminderPayload(ReminderDirectiveEntity payload);
    }
}
