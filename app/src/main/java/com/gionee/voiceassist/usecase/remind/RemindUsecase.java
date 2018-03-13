package com.gionee.voiceassist.usecase.remind;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.util.Log;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderCreateDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderManageDirectiveEntity;
import com.gionee.voiceassist.datamodel.card.ReminderCardEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.ContextUtil;
import com.gionee.voiceassist.util.DateUtil;
import com.gionee.voiceassist.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 提醒场景Usecase
 */

public class RemindUsecase extends BaseUsecase {

    private static final String TAG = RemindUsecase.class.getSimpleName();

    private AlarmDao alarmDao;
    private ScheduleDao scheduleDao;

    @Override
    public void handleDirective(DirectiveEntity payload) {
        ReminderDirectiveEntity reminderPayload = (ReminderDirectiveEntity) payload;
        ReminderDirectiveEntity.ReminderAction action = reminderPayload.getAction();
        switch (action) {
            case CREATE_REMINDER:
                fireCreateReminder((ReminderCreateDirectiveEntity) reminderPayload);
                break;
            case SEARCH_REMINDER:
                fireSearchReminder((ReminderManageDirectiveEntity) reminderPayload);
                break;
            case DELETE_REMINDER:
                fireDeleteReminder((ReminderManageDirectiveEntity) reminderPayload);
                break;

        }
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "reminder";
    }

    private void fireCreateReminder(ReminderCreateDirectiveEntity payload) {
        String timeStr = payload.getTime().replace("T", " ");
        String content = payload.getContent();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ssX");
        Date reminderDate = new Date();
        try {
            reminderDate = formatter.parse(timeStr);
        } catch (ParseException e) {
            LogUtil.e(TAG, "解析时间错误");
            e.printStackTrace();
        }
        if (content.startsWith("闹钟")) {
            // TODO: setRepeatRule
            ReminderCardEntity entity = new ReminderCardEntity();
            entity.setReminderDate(reminderDate);
            entity.setReminderContent(content);
            render(entity);

            createAlarm(reminderDate, payload.getRepeat().weekly);
        } else {
            createSchedule(reminderDate, content);
        }
    }

    private void fireSearchReminder(ReminderManageDirectiveEntity payload) {
        LogUtil.d(TAG, "**ACTION PERFORM** searchReminder. payload = " + payload);
    }

    private void fireDeleteReminder(ReminderManageDirectiveEntity payload) {
        LogUtil.d(TAG, "**ACTION PERFORM** deleteReminder. payload = " + payload);
        Date startTime = payload.getTime()[0];
        Date endTime = payload.getTime()[1];
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if (alarmDao == null) {
            alarmDao = new AlarmDao();
        }
        alarmDao.cancelAlarmWithTime(hour, minute, false);
    }

    private void createSchedule(Date time, String name) {
        LogUtil.d(TAG, "**ACTION PERFORM** createSchedule. time = " + time + ";name = " + name);
        if (scheduleDao == null) {
            scheduleDao = new ScheduleDao();
        }
        scheduleDao.performCreateSchedule(time, time, name);
    }

    private void createAlarm(Date time, List<Integer> repeat) {
        LogUtil.d(TAG, "**ACTION PERFORM** createAlarm. time = " + time);
        boolean isRepeat = !repeat.isEmpty();
        boolean isToday = time.before(DateUtil.getTomorrowDate()) && time.after(DateUtil.getTodayDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        if (!isToday) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            repeat.add(day);
        }
        if (alarmDao == null) {
            alarmDao = new AlarmDao();
        }
        alarmDao.performCreateAlarm(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), repeat, "");
    }

}
