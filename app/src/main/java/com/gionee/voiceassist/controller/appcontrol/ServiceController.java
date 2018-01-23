package com.gionee.voiceassist.controller.appcontrol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.controller.customuserinteraction.ICuiControl;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.coreservice.CoreService;
import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.AppLaunchDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ContactsDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GioneeCustomDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteTvDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.LocalAudioPlayerDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.PhoneCallDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.SmsDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.WebBrowserDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.RecognizerState;

import java.util.List;

/**
 * 调度Service的类
 */

public class ServiceController {

    private Handler mMainThreadHandler;
    private List<IRecognizerStateListener> mCallbacks;
    private CoreService mService;
    private CoreService.CoreServiceBinder mServiceBinder;
    private CoreService.StateCallback servStateCallback = new CoreService.StateCallback() {

        @Override
        public void onSdkInit() {
            for (IRecognizerStateListener callback:mCallbacks) {
                callback.onInitFinished();
            }
        }

        @Override
        public void onRecordStart() {
            for (IRecognizerStateListener callback:mCallbacks) {
                callback.onRecordStart();
            }
        }

        @Override
        public void onRecordStop() {
            for (IRecognizerStateListener callback:mCallbacks) {
                callback.onRecordStop();
            }
        }

        @Override
        public void onTtsStart() {
            for (IRecognizerStateListener callback:mCallbacks) {
                callback.onTtsStart();
            }
        }

        @Override
        public void onTtsStop() {
            for (IRecognizerStateListener callback:mCallbacks) {
                callback.onTtsStop();
            }
        }

        @Override
        public void onRecognizeStateChanged(RecognizerState state) {
            DataController.getDataController().updateRecognizerState(state);
        }
    };
    private CoreService.SceneCallback servSceneCallback = new CoreService.SceneCallback() {
        @Override
        public void onScreenPayload(ScreenDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "screen");
        }

        @Override
        public void onAlarmPayload(AlarmDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "alarm");
        }

        @Override
        public void onContactsPayload(ContactsDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "contacts");
        }

        @Override
        public void onPhonecallPayload(PhoneCallDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "phonecall");
        }

        @Override
        public void onAppLaunchPayload(AppLaunchDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "applaunch");
        }

        @Override
        public void onGioneeCustomCommandPayload(GioneeCustomDirectiveEntity payload) {
//            fireDirectiveDispatch(payload, "gionee_custom_command");
            switch (payload.getAction()) {
                case START_STOPWATCH:
                    fireDirectiveDispatch(payload, "stopwatch");
                    break;
            }
        }

        @Override
        public void onGnRemotePayload(GnRemoteDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "gnremote");
        }

        @Override
        public void onGnRemoteTvPayload(GnRemoteTvDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "gnremote");
        }

        @Override
        public void onLocalAudioPlayerPayload(LocalAudioPlayerDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "localplayer");
        }

        @Override
        public void onWebBrowserPayload(WebBrowserDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "webbrowser");
        }

        @Override
        public void onReminderPayload(ReminderDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "reminder");
        }

        @Override
        public void onSmsSendPayload(SmsDirectiveEntity payload) {
            fireDirectiveDispatch(payload, "smsSend");
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                mServiceBinder = (CoreService.CoreServiceBinder) service;
                mServiceBinder.setStateCallback(servStateCallback);
                mServiceBinder.setSceneCallback(servSceneCallback);
                mService = mServiceBinder.getService();
                SdkController.InitStatus initStatus = mService.getInitStatus();
                if (initStatus == SdkController.InitStatus.INITED) {
                    for (IRecognizerStateListener callback:mCallbacks) {
                        callback.onInitFinished();
                    }
                } else if (initStatus == SdkController.InitStatus.UNINIT) {
                    for (IRecognizerStateListener callback:mCallbacks) {
                        callback.onInitFailed();
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //TODO Service Disconnect时，还需要注销监听器
            mService = null;
        }
    };

    ServiceController() {
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 连接Service
     */
    void attachService() {
        GnVoiceAssistApplication.getInstance().bindService(getServiceIntent(), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 断开Service
     */
    void detachService() {
        if(mService != null) {
            GnVoiceAssistApplication.getInstance().unbindService(mServiceConnection);
            mService = null;
        }
    }

    void setCallback(List<IRecognizerStateListener> callbacks) {
        mCallbacks = callbacks;
    }

    public void startRecord() {
        if (mService != null) {
            mService.record();
        }
    }

    public void stopRecord() {
        //TODO Invoke Service Stop Record
    }

    public void playTts(String text) {
        if (mService != null) {
            mService.playTts(text);
        }
    }

    public void playTts(String text, String utterId, TtsCallback callback) {
        if (mService != null) {
            if (!TextUtils.isEmpty(utterId) && callback != null) {
                mService.playTts(text, utterId, callback);
            } else {
                mService.playTts(text);
            }
        }
    }

    public void stopTts() {
        //TODO Invoke Service Stop TTS
    }

    public void sendTextQuery(String query) {
        if (mService != null) {
            mService.sendTextQuery(query);
        }
    }

    public ICuiControl getCUIController() {
        if(mService != null) {
            return mService.getCUIController();
        }
        return null;
    }

    private Intent getServiceIntent() {
        Intent intent = new Intent(GnVoiceAssistApplication.getInstance().getApplicationContext(), CoreService.class);
        return intent;
    }

    private void fireDirectiveDispatch(final DirectiveEntity payload, final String usecaseAlias) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                DataController.getDataController().getUsecaseDispatcher().sendToUsecase(payload, usecaseAlias);
            }
        });
    }
}
