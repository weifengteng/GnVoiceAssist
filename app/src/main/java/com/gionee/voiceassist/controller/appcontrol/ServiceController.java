package com.gionee.voiceassist.controller.appcontrol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.coreservice.CoreService;
import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.AppLaunchDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ContactsDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GioneeCustomDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteTvDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.LocalAudioPlayerDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.PhonecallDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.WebBrowserDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.RecognizerState;

import java.util.List;

/**
 * 调度Service的类
 */

public class ServiceController {

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

        }

        @Override
        public void onAlarmPayload(AlarmDirectiveEntity payload) {

        }

        @Override
        public void onContactsPayload(ContactsDirectiveEntity payload) {

        }

        @Override
        public void onPhonecallPayload(PhonecallDirectiveEntity payload) {

        }

        @Override
        public void onAppLaunchPayload(AppLaunchDirectiveEntity payload) {

        }

        @Override
        public void onGioneeCustomCommandPayload(GioneeCustomDirectiveEntity payload) {

        }

        @Override
        public void onGnRemotePayload(GnRemoteDirectiveEntity payload) {

        }

        @Override
        public void onGnRemoteTvPayload(GnRemoteTvDirectiveEntity payload) {

        }

        @Override
        public void onLocalAudioPlayerPayload(LocalAudioPlayerDirectiveEntity payload) {

        }

        @Override
        public void onWebBrowserPayload(WebBrowserDirectiveEntity payload) {

        }

        @Override
        public void onReminderPayload(ReminderDirectiveEntity payload) {

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
                LogUtil.d("liyh", "SDK Init Status = " + initStatus);
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
        GnVoiceAssistApplication.getInstance().unbindService(mServiceConnection);
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

    public void stopTts() {
        //TODO Invoke Service Stop TTS
    }

    public void sendTextQuery(String query) {
        if (mService != null) {
            mService.sendTextQuery(query);
        }
    }

    private Intent getServiceIntent() {
        Intent intent = new Intent(GnVoiceAssistApplication.getInstance().getApplicationContext(), CoreService.class);
        return intent;
    }
}
