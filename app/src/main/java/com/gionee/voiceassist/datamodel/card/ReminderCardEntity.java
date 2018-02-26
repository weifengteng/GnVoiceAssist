package com.gionee.voiceassist.datamodel.card;

import com.gionee.voiceassist.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author twf
 * @date 2018/1/29
 */

public class ReminderCardEntity extends CardEntity {

    public ReminderCardEntity() {
        setType(CardType.REMINDER_CARD);
    }

    private Date reminderDate;
    private String reminderContent;
    private ArrayList<Integer> repeatDay;

    public Date getReminderDate() {
        return reminderDate;
    }

    public ReminderCardEntity setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
        return this;
    }

    public String getReminderContent() {
        return reminderContent;
    }

    public ReminderCardEntity setReminderContent(String reminderContent) {
        this.reminderContent = reminderContent;
        return this;
    }

    public String getReminderHour() {
        String reminderHour = DateUtil.getFormattedDate(DateUtil.FORMAT_HHMM, reminderDate);
        return reminderHour;
    }

    public String getTriggerCountDownInfo() {
        return DateUtil.getTimeLeaving(reminderDate);
    }

    public String getReminderDateStr() {
        String str = DateUtil.getFormattedDate(DateUtil.FORMAT_YMD_CN, reminderDate);
        return str;
    }

    public String getReminderDayOfWeek() {
        String weekOfDay = DateUtil.getWeek(reminderDate);
        return weekOfDay;
    }

    public String getRepeatInfo() {
        return "";
    }

}
