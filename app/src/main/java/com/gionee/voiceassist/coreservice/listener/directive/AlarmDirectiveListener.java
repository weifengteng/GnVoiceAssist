package com.gionee.voiceassist.coreservice.listener.directive;


import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.message.SetAlarmPayload;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.message.SetTimerPayload;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.message.ShowAlarmsPayload;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.message.ShowTimersPayload;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by twf on 2017/8/16.
 */

public class AlarmDirectiveListener extends BaseDirectiveListener implements AlarmsDeviceModule.IAlarmDirectiveListener {
    public static final String TAG = AlarmDirectiveListener.class.getSimpleName();

    public AlarmDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }


    @Override
    public void onSetAlarmDirectiveReceived(SetAlarmPayload setAlarmPayload) {
        LogUtil.d("DCSF-----", TAG + " onSetAlarmDirectiveReceived: ");
        List<Integer> days = new ArrayList<>();
        for (String daysStr:setAlarmPayload.getDays()) {
            switch (daysStr) {
                case "MON":
                    days.add(Calendar.MONDAY);
                    break;
                case "TUE":
                    days.add(Calendar.TUESDAY);
                    break;
                case "WED":
                    days.add(Calendar.WEDNESDAY);
                    break;
                case "THU":
                    days.add(Calendar.THURSDAY);
                    break;
                case "FRI":
                    days.add(Calendar.FRIDAY);
                    break;
                case "SAT":
                    days.add(Calendar.SATURDAY);
                    break;
                case "SUN":
                    days.add(Calendar.SUNDAY);
                    break;
            }
        }
        AlarmDirectiveEntity msg = new AlarmDirectiveEntity();
        msg.setTime(setAlarmPayload.getHour(), setAlarmPayload.getMinutes(), (ArrayList<Integer>) days);
        sendDirective(msg);

//        mAlarmPresenter.setAlarm(setAlarmPayload.getHour(),setAlarmPayload.getMinutes(), (ArrayList<Integer>) days,setAlarmPayload.getMessage());
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
