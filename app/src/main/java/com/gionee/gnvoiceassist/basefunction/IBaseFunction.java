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

    IVoiceInputEventListener getVoiceInputEventListener();

    IAudioPlayerStateListener getAudioPlayerStateListener();

    GNMusicOperator getGNMusicOperattor();
}
