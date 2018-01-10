package com.gionee.voiceassist.coreservice.datamodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by liyingheng on 1/5/18.
 */

public class ReminderDirectiveEntity extends DirectiveEntity {

    public ReminderDirectiveEntity() {
        setType(Type.REMINDER);
    }

    private String time;
    private String content;
    private ReminderRepeat repeat;
    private ReminderAction action;


    public static class ReminderRepeat {
        public RepeatType type;
        public List<Integer> weekly = new ArrayList<>();
        public List<Integer> monthly = new ArrayList<>();
        public List<String> yearly = new ArrayList<>();
        public enum RepeatType {
            WEEKLY, MONTHLY, YEARLY, None
        }

        public static ReminderRepeat createWeeklyRepeat(List<String> weekly) {
            ReminderRepeat repeat = new ReminderRepeat();
            repeat.type = RepeatType.WEEKLY;
            List<Integer> weeklyRepeatList = new ArrayList<>();
            for (String weekItem:weekly) {
                switch (weekItem) {
                    case "MO":
                        weeklyRepeatList.add(Calendar.MONDAY);
                        break;
                    case "TU":
                        weeklyRepeatList.add(Calendar.TUESDAY);
                        break;
                    case "WE":
                        weeklyRepeatList.add(Calendar.WEDNESDAY);
                        break;
                    case "TH":
                        weeklyRepeatList.add(Calendar.THURSDAY);
                        break;
                    case "FR":
                        weeklyRepeatList.add(Calendar.FRIDAY);
                        break;
                    case "SA":
                        weeklyRepeatList.add(Calendar.SATURDAY);
                        break;
                    case "SU":
                        weeklyRepeatList.add(Calendar.SUNDAY);
                        break;
                }
            }
            repeat.weekly = weeklyRepeatList;
            return repeat;
        }

        public static ReminderRepeat createMonthlyRepeat(List<Integer> monthly) {
            ReminderRepeat repeat = new ReminderRepeat();
            repeat.type = RepeatType.MONTHLY;
            repeat.monthly = monthly;
            return repeat;
        }

        public static ReminderRepeat createYearlyRepeat(List<String> yearly) {
            ReminderRepeat repeat = new ReminderRepeat();
            repeat.type = RepeatType.YEARLY;
            repeat.yearly = yearly;
            return repeat;
        }

        public static ReminderRepeat createNoRepeat() {
            ReminderRepeat repeat = new ReminderRepeat();
            repeat.type = RepeatType.None;
            return repeat;
        }

        @Override
        public String toString() {
            return "ReminderRepeat{" +
                    "type=" + type +
                    ", weekly=" + weekly +
                    ", monthly=" + monthly +
                    ", yearly=" + yearly +
                    '}';
        }
    }

    public void setCreateReminder(String time, String content, ReminderRepeat repeat) {
        this.time = time;
        this.content = content;
        this.repeat = repeat;
        setAction(ReminderAction.CREATE_REMINDER);
    }

    public void setSearchReminder(String time) {
        this.time = time;
        this.action = ReminderAction.SEARCH_REMINDER;
    }

    public void setDeleteReminder(String time) {
        this.time = time;
        this.action = ReminderAction.DELETE_REMINDER;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public ReminderRepeat getRepeat() {
        return repeat;
    }

    public ReminderAction getAction() {
        return action;
    }

    void setAction(ReminderAction action) {
        this.action = action;
    }

    public enum ReminderAction {
        CREATE_REMINDER,
        SEARCH_REMINDER,
        DELETE_REMINDER
    }

    @Override
    public String toString() {
        return "ReminderDirectiveEntity{" +
                "time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", repeat=" + repeat +
                ", action=" + action +
                '}';
    }
}
