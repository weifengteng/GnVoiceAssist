package com.gionee.gnvoiceassist.basefunction;

import android.os.Handler;

import com.gionee.gnvoiceassist.MainActivity;
import com.gionee.gnvoiceassist.basefunction.applaunch.AppLaunchPresenter;
import com.gionee.gnvoiceassist.basefunction.contact.ContactsPresenter;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.DeviceControlOperator;
import com.gionee.gnvoiceassist.basefunction.kookong.KookongOperator;
import com.gionee.gnvoiceassist.basefunction.music.GNMusicOperator;
import com.gionee.gnvoiceassist.basefunction.phonecall.PhoneCallPresenter;
import com.gionee.gnvoiceassist.basefunction.recordcontrol.RecordController;
import com.gionee.gnvoiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.gnvoiceassist.basefunction.smssend.SmsSendPresenter;
import com.gionee.gnvoiceassist.basefunction.timequery.TimeQuery;
import com.gionee.gnvoiceassist.directiveListener.audioplayer.IAudioPlayerStateListener;
import com.gionee.gnvoiceassist.directiveListener.voiceinput.IVoiceInputEventListener;

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
    private TimeQuery timeQuery;
    private KookongOperator kookongOperator;
    private IVoiceInputEventListener voiceInputEventListener;
    private IAudioPlayerStateListener audioPlayerStateListener;
    private GNMusicOperator gnMusicOperator;

    public void setHandler(Handler handler) {
        this.mMainHandler = handler;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

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
            deviceControlOperator = new DeviceControlOperator(this);
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
    public void onDestroy() {
        if(mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
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
}
