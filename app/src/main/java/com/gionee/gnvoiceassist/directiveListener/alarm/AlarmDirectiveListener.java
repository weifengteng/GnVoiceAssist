package com.gionee.gnvoiceassist.directiveListener.alarm;


import com.gionee.gnvoiceassist.basefunction.BaseFunctionManager;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.alarm.IAlarmPresenter;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.message.io.DirectiveResponseGenerator;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.AlarmMetadata;
import com.gionee.gnvoiceassist.message.model.metadata.TimerMetadata;
import com.gionee.gnvoiceassist.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.SetAlarmPayload;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.SetTimerPayload;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.ShowAlarmsPayload;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.ShowTimersPayload;
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;
import com.gionee.gnvoiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by twf on 2017/8/16.
 */

public class AlarmDirectiveListener extends BaseDirectiveListener implements AlarmsDeviceModule.IAlarmDirectiveListener {
    public static final String TAG = AlarmDirectiveListener.class.getSimpleName();
//    public IAlarmPresenter mAlarmPresenter;

    public AlarmDirectiveListener(IDirectiveListenerCallback callback) {
        super(callback);
    }

    @Override
    public void onDestroy() {

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
//        mAlarmPresenter.setAlarm(setAlarmPayload.getHour(),setAlarmPayload.getMinutes(), (ArrayList<Integer>) days,setAlarmPayload.getMessage());
        AlarmMetadata metadata = new AlarmMetadata();
        metadata.setHour(setAlarmPayload.getHour());
        metadata.setMinute(setAlarmPayload.getMinutes());
        metadata.setTriggerDays((ArrayList<Integer>) days);
        metadata.setMessage(setAlarmPayload.getMessage());
        DirectiveResponseEntity response = new DirectiveResponseGenerator("alarm")
                .setAction("request_alarm")
                .setSubAction("set")
                .setInCustomInteractive(false)
                .setMetadata(metadata.toJson())
                .build();
        mCallback.onDirectiveResponse(response);
    }

    @Override
    public void onShowAlarmsDirectiveReceived(ShowAlarmsPayload showAlarmsPayload) {
        LogUtil.d("DCSF-----", TAG + " onShowAlarmsDirectiveReceived: ");
    }

    @Override
    public void onSetTimerDirectiveReceived(SetTimerPayload setTimerPayload) {
        LogUtil.d("DCSF-----", TAG + " onSetTimerDirectiveReceived: ");
        TimerMetadata metadata = new TimerMetadata();
        metadata.setLength(setTimerPayload.getLength());
        metadata.setMessage(setTimerPayload.getMessage());
        DirectiveResponseEntity response = new DirectiveResponseGenerator("alarm")
                .setAction("request_timer")
                .setSubAction("set")
                .setInCustomInteractive(false)
                .setMetadata(metadata.toJson())
                .build();
        mCallback.onDirectiveResponse(response);
    }

    @Override
    public void onShowTimersDirectiveReceived(ShowTimersPayload showTimersPayload) {
        LogUtil.d("DCSF-----", TAG + " onShowTimerDirectiveReceived: ");
    }
}
