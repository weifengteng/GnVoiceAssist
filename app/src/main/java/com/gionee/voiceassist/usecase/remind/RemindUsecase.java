package com.gionee.voiceassist.usecase.remind;

import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.CalendarContract;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.datamodel.card.ReminderCardEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.DateUtil;
import com.gionee.voiceassist.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 提醒场景Usecase
 */

public class RemindUsecase extends BaseUsecase {

    private static final String TAG = RemindUsecase.class.getSimpleName();

    @Override
    public void handleDirective(DirectiveEntity payload) {
        ReminderDirectiveEntity reminderPayload = (ReminderDirectiveEntity) payload;
        ReminderDirectiveEntity.ReminderAction action = reminderPayload.getAction();
        switch (action) {
            case CREATE_REMINDER:
                fireCreateReminder(reminderPayload);
                break;
            case SEARCH_REMINDER:
                fireDeleteReminder(reminderPayload);
                break;
            case DELETE_REMINDER:
                fireDeleteReminder(reminderPayload);
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

    private void fireCreateReminder(ReminderDirectiveEntity payload) {
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

    private void fireSearchReminder(ReminderDirectiveEntity payload) {

    }

    private void fireDeleteReminder(ReminderDirectiveEntity payload) {

    }

    private void createSchedule(Date time, String name) {
        LogUtil.d(TAG, "**ACTION PERFORM** createSchedule. time = " + time + ";name = " + name);
        performCreateSchedule(time, time, name);
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
        performCreateAlarm(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), repeat, "");
    }

    private void performCreateAlarm(int hour, int minute, List<Integer> triggerDays, String message) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR,hour)
                .putExtra(AlarmClock.EXTRA_MINUTES,minute)
                .putExtra(AlarmClock.EXTRA_DAYS,(ArrayList<Integer>)triggerDays)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                .putExtra(AlarmClock.EXTRA_MESSAGE,message);

        if (intent.resolveActivity(getAppContext().getPackageManager()) != null) {
            getAppContext().startActivity(intent);
        }
    }

    private void performCreateSchedule(Date startTime, Date endTime, String title) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTime())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTime());
        if (intent.resolveActivity(getAppContext().getPackageManager()) != null) {
            getAppContext().startActivity(intent);
        }
    }
}
