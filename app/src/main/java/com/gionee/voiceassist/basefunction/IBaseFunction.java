package com.gionee.voiceassist.basefunction;

import android.os.Handler;

import com.gionee.voiceassist.MainActivity;
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

public interface IBaseFunction {

    void setHandler(Handler handler);

    void setMainActivity(MainActivity mainActivity);

    void onDestroy();

    RecordController getRecordController();

    KookongOperator getKookongOperator();

    AppLaunchPresenter getAppLaunchPresenter();

    DeviceControlOperator getDeviceControlOperator();

    ScreenRender getScreenRender();

    TimeQuery getTimerQuery();

    MainActivity getMainActivity();

    PhoneCallPresenter getPhoneCallPresenter();

    SmsSendPresenter getSmsSendPresenter();

    ContactsPresenter getContactsPresenter();

    IAlarmPresenter getAlarmPresenter();

    IVoiceInputEventListener getVoiceInputEventListener();

    IAudioPlayerStateListener getAudioPlayerStateListener();

    GNMusicOperator getGNMusicOperattor();

    OfflineAsrHandler getOfflineAsrHandler();
}
