package com.gionee.voiceassist.usecase.remind;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.LogUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liyingheng on 1/10/18.
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
    public String getAlias() {
        return "reminder";
    }

    private void fireCreateReminder(ReminderDirectiveEntity payload) {
        String timeStr = payload.getTime().replace("T", " ");
        String content = payload.getContent();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ssX");
        Date time = new Date();
        try {
            time = formatter.parse(timeStr);
        } catch (ParseException e) {
            LogUtil.e(TAG, "解析时间错误");
            e.printStackTrace();
        }
        if (content.startsWith("闹钟")) {
            createAlarm(time, payload.getRepeat().weekly);
        } else {
            createSchedule(time, content);
        }
    }

    private void fireSearchReminder(ReminderDirectiveEntity payload) {

    }

    private void fireDeleteReminder(ReminderDirectiveEntity payload) {

    }

    private void createSchedule(Date time, String name) {
        LogUtil.d(TAG, "**ACTION PERFORM** createSchedule. time = " + time + ";name = " + name);
    }

    private void createAlarm(Date time, List<Integer> repeat) {
        LogUtil.d(TAG, "**ACTION PERFORM** createAlarm. time = " + time);
    }
}
