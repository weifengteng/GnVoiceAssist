package com.gionee.gnvoiceassist.directiveListener.alarm;

import com.baidu.duer.dcs.devicemodule.alarms.AlarmsDeviceModule;
import com.baidu.duer.dcs.devicemodule.alarms.message.SetAlarmPayload;
import com.baidu.duer.dcs.devicemodule.alarms.message.SetTimerPayload;
import com.baidu.duer.dcs.devicemodule.alarms.message.ShowAlarmsPayload;
import com.baidu.duer.dcs.devicemodule.alarms.message.ShowTimersPayload;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;

/**
 * Created by twf on 2017/8/16.
 */

public class AlarmDirectiveListener extends BaseDirectiveListener implements AlarmsDeviceModule.IAlarmDirectiveListener {
    public static final String TAG = AlarmDirectiveListener.class.getSimpleName();

    public AlarmDirectiveListener(IBaseFunction baseFunction) {
        super(baseFunction);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSetAlarmDirectiveReceived(SetAlarmPayload setAlarmPayload) {
        LogUtil.d("DCSF-----", TAG + " onSetAlarmDirectiveReceived: ");

    }

    @Override
    public void onShowAlarmsDirectiveReceived(ShowAlarmsPayload showAlarmsPayload) {
        LogUtil.d("DCSF-----", TAG + " onShowAlarmsDirectiveReceived: ");
    }

    @Override
    public void onSetTimerDirectiveReceived(SetTimerPayload setTimerPayload) {
        LogUtil.d("DCSF-----", TAG + " onSetTimerDirectiveReceived: ");
    }

    @Override
    public void onShowTimersDirectiveReceived(ShowTimersPayload showTimersPayload) {
        LogUtil.d("DCSF-----", TAG + " onShowTimerDirectiveReceived: ");
    }
}
