package com.gionee.voiceassist.coreservice.datamodel;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liyingheng on 2/26/18.
 */

public class ReminderManageDirectiveEntity extends ReminderDirectiveEntity {

    private String apm = "";

    private String day = "";

    private String hour = "";

    private String minute = "";

    private String month = "";

    public void setSearchReminder(String apm, String day, String hour, String minute, String month, ReminderRepeat repeat) {
        setAction(ReminderDirectiveEntity.ReminderAction.SEARCH_REMINDER);
        this.apm = apm;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.repeat = repeat;
    }

    public void setDeleteReminder(String apm, String day, String hour, String minute, String month, ReminderRepeat repeat) {
        this.action = ReminderDirectiveEntity.ReminderAction.DELETE_REMINDER;
        this.apm = apm;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.repeat = repeat;
    }

    /**
     * 是否为精确查询时间（即只说了模糊的时间段，没说具体时间）（如今天下午）
     * @return 模糊时间查询
     */
    public boolean hasConcreteTime() {
        return (!TextUtils.isEmpty(hour) || !TextUtils.isEmpty(minute))
                && !hour.startsWith("+") && !minute.startsWith("+");
    }

    /**
     * 是否查询一段时间以后 (如"查询6小时以后的闹钟")
     * @return 查询粗略时间段以后的事件
     */
    public boolean isAfter() {
        return hour.startsWith("+")
                || day.startsWith("+") && !day.equals("+0") && !day.equals("+1") && !day.equals("+2")
                || minute.startsWith("+")
                || month.startsWith("+");
    }

    /**
     * 若为查询粗略时间后的事件，则返回MONTH_AFTER、HOUR_AFTER、MINUTE_AFTER。
     * 若不为查询时间段以后的事件，则返回NONE。
     * @return 查询类型
     */
    public AfterType getAfterType() {
        if (isAfter()) {
            if (month.startsWith("+")) {
                return AfterType.MONTH_AFTER;
            } else if (day.startsWith("+") && !day.equals("+0") && !day.equals("+1") && !day.equals("+2")) {
                return AfterType.DAY_AFTER;
            } else if (hour.startsWith("+")) {
                return AfterType.HOUR_AFTER;
            } else if (minute.startsWith("+")) {
                return AfterType.MINUTE_AFTER;
            } else {
                return AfterType.NONE;
            }
        } else {
            return AfterType.NONE;
        }
    }

    public enum AfterType {
        NONE, DAY_AFTER, MONTH_AFTER, HOUR_AFTER, MINUTE_AFTER
    }

    public Date[] getTime() {
        Date[] dates = new Date[] {null, null};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        if (isAfter()) {
            // After
        } else {
            // Not after

            // 处理今天、明天、后天的日期
            if (!TextUtils.isEmpty(day)) {
                switch (day) {
                    case "+0":
                        cal.setTime(new Date());
                        break;
                    case "+1":
                        cal.setTime(new Date());
                        cal.add(Calendar.DATE, 1);
                        break;
                    case "+2":
                        cal.add(Calendar.DATE, 2);
                        break;
                    default:
                        cal.set(Calendar.DATE, Integer.parseInt(day));
                        break;
                }
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            }
            // 处理有具体时间的查询
            if (hasConcreteTime()) {
                if (!TextUtils.isEmpty(month)) {
                    cal.set(Calendar.MONTH, Integer.parseInt(month));
                }
                if (!TextUtils.isEmpty(hour)) {
                    cal.set(Calendar.HOUR_OF_DAY, apm.equals("pm") ? Integer.parseInt(hour) + 12 : Integer.parseInt(hour));
                }
                if (!TextUtils.isEmpty(minute)) {
                    cal.set(Calendar.MINUTE, Integer.parseInt(minute));
                }
                dates[0] = cal.getTime();
                dates[1] = cal.getTime();
            } else {
                // 查询时间段
                if (!TextUtils.isEmpty(month)) {
                    if (!month.startsWith("+")) {
                        cal.set(Calendar.MONTH, Integer.parseInt(month));
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        dates[0] = cal.getTime();
                        cal.set(Calendar.DAY_OF_MONTH, -1);
                        dates[1] = cal.getTime();
                    } else {
//                        cal.setTime(new Date());
//                        cal.add(Calendar.MONTH, Integer.parseInt(month));
//                        cal.set(Calendar.DAY_OF_MONTH, 1);
//                        cal.set(Calendar.HOUR_OF_DAY, 0);
//                        cal.set(Calendar.MINUTE, 0);
//                        cal.set(Calendar.SECOND, 0);
//                        cal.set(Calendar.MILLISECOND, 0);
//                        dates[0] = cal.getTime();
//                        cal.set(Calendar.DAY_OF_MONTH, -1);
//                        dates[1] = null;
                        dates[0] = null;
                        dates[1] = null;
                    }
                } else if (!TextUtils.isEmpty(apm)) {
                    switch (apm) {
                        case "am":
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            dates[0] = cal.getTime();
                            cal.add(Calendar.HOUR_OF_DAY, 12);
                            dates[1] = cal.getTime();
                            break;
                        case "pm":
                            cal.set(Calendar.HOUR_OF_DAY, 12);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            dates[0] = cal.getTime();
                            cal.add(Calendar.HOUR_OF_DAY, 12);
                            dates[1] = cal.getTime();
                            break;
                    }
                }
            }
        }
        return dates;
    }

    public String getTimeString(Date date) {
        if (date == null) {
            return "null";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return "ReminderManageDirectiveEntity{" +
                "apm='" + apm + '\'' +
                ", day='" + day + '\'' +
                ", hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", content='" + content + '\'' +
                ", month='" + month + '\'' +
                ", repeat=" + repeat +
                ", action=" + action +
                ", afterType=" + getAfterType() +
                ", hasConcreteTime=" + hasConcreteTime() +
                ", startTime=" + getTimeString(getTime()[0]) +
                ", endTime=" + getTimeString(getTime()[1]) +
                '}';
    }
}
