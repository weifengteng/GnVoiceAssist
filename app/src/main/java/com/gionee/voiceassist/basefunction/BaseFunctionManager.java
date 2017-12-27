package com.gionee.voiceassist.basefunction;

import android.os.Build;
import android.os.Handler;

import com.gionee.voiceassist.MainActivity;
import com.gionee.voiceassist.basefunction.alarm.AlarmPresenter;
import com.gionee.voiceassist.basefunction.alarm.IAlarmPresenter;
import com.gionee.voiceassist.basefunction.applaunch.AppLaunchPresenter;
import com.gionee.voiceassist.basefunction.contact.ContactsPresenter;
import com.gionee.voiceassist.basefunction.devicecontrol.DeviceControlOperator;
import com.gionee.voiceassist.basefunction.kookong.KookongOperator;
import com.gionee.voiceassist.basefunction.music.GNMusicOperator;
import com.gionee.voiceassist.basefunction.offlineasr.OfflineAsrHandler;
import com.gionee.voiceassist.basefunction.phonecall.PhoneCallPresenter;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.voiceassist.basefunction.smssend.SmsSendPresenter;
import com.gionee.voiceassist.basefunction.timequery.TimeQuery;
import com.gionee.voiceassist.directiveListener.audioplayer.IAudioPlayerStateListener;
import com.gionee.voiceassist.directiveListener.voiceinput.IVoiceInputEventListener;

/**
 * Created by twf on 2017/8/24.
 */

public class BaseFunctionManager implements IBaseFunction {
    private Handler mMainHandler;
    private MainActivity mainActivity;
    private PhoneCallPresenter phoneCallPresenter;
    private SmsSendPresenter smsSendPresenter;
    private RecordController recordController;
    private DeviceControlOperator deviceControlOperator;
    private AppLaunchPresenter appLaunchPresenter;
    private ContactsPresenter contactsPresenter;
    private ScreenRender screenRender;
    private IAlarmPresenter alarmPresenter;
    private TimeQuery timeQuery;
    private KookongOperator kookongOperator;
    private IVoiceInputEventListener voiceInputEventListener;
    private IAudioPlayerStateListener audioPlayerStateListener;
    private GNMusicOperator gnMusicOperator;
    private OfflineAsrHandler offlineAsrHandler;

    @Override
    public void setHandler(Handler handler) {
        this.mMainHandler = handler;
    }

    @Override
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public RecordController getRecordController() {
        if(recordController == null) {
            recordController = new RecordController();
        }
        return recordController;
    }

    @Override
    public PhoneCallPresenter getPhoneCallPresenter() {
        if(phoneCallPresenter == null) {
            phoneCallPresenter = new PhoneCallPresenter(this);
        }
        return phoneCallPresenter;
    }

    @Override
    public SmsSendPresenter getSmsSendPresenter() {
        if(smsSendPresenter == null) {
            smsSendPresenter = new SmsSendPresenter(this);
        }
        return smsSendPresenter;
    }

    @Override
    public ContactsPresenter getContactsPresenter() {
        if(contactsPresenter == null) {
            contactsPresenter = new ContactsPresenter(this);
        }
        return contactsPresenter;
    }

    @Override
    public IAlarmPresenter getAlarmPresenter() {
        if (alarmPresenter == null) {
            alarmPresenter = new AlarmPresenter(this);
        }
        return alarmPresenter;
    }

    @Override
    public KookongOperator getKookongOperator() {
        if(kookongOperator == null) {
            kookongOperator = new KookongOperator(this);
        }
        return kookongOperator;
    }

    @Override
    public AppLaunchPresenter getAppLaunchPresenter() {
        if(appLaunchPresenter == null) {
            appLaunchPresenter = new AppLaunchPresenter(this);
        }
        return appLaunchPresenter;
    }

    @Override
    public DeviceControlOperator getDeviceControlOperator() {
        if(deviceControlOperator == null) {
            switch (Build.BRAND.toLowerCase()) {
                case "gionee":
                    deviceControlOperator = new DeviceControlOperator(this);
                    break;
                default:
                    deviceControlOperator = new DeviceControlOperator(this);
                    break;
            }
        }
        return deviceControlOperator;
    }

    @Override
    public ScreenRender getScreenRender() {
        if(screenRender == null) {
            screenRender = new ScreenRender(mMainHandler);
        }
        return screenRender;
    }

    @Override
    public TimeQuery getTimerQuery() {
        if(timeQuery == null) {
            timeQuery = new TimeQuery(this);
        }
        return timeQuery;
    }

    @Override
    public IVoiceInputEventListener getVoiceInputEventListener() {
        if(voiceInputEventListener == null && mainActivity != null
                && (mainActivity instanceof IVoiceInputEventListener)) {
            voiceInputEventListener = mainActivity;
        }
        return voiceInputEventListener;
    }

    @Override
    public IAudioPlayerStateListener getAudioPlayerStateListener() {
        if(audioPlayerStateListener == null && mainActivity != null
                && (mainActivity instanceof IAudioPlayerStateListener)) {
            audioPlayerStateListener = mainActivity;
        }
        return audioPlayerStateListener;
    }

    @Override
    public GNMusicOperator getGNMusicOperattor() {
        if(gnMusicOperator == null) {
            gnMusicOperator = new GNMusicOperator(this);
        }
        return gnMusicOperator;
    }

    @Override
    public OfflineAsrHandler getOfflineAsrHandler() {
        if (offlineAsrHandler == null) {
            offlineAsrHandler = new OfflineAsrHandler(this);
        }
        return offlineAsrHandler;
    }

    @Override
    public void onDestroy() {
        if(mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }

        if(audioPlayerStateListener != null) {
            audioPlayerStateListener = null;
        }

        if(voiceInputEventListener != null) {
            voiceInputEventListener = null;
        }

        if(offlineAsrHandler != null) {
            offlineAsrHandler = null;
        }
    }
}
