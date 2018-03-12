package com.gionee.voiceassist.util;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期/时间相关工具类
 */

public class DateUtil {

    public static final String FORMAT_YMD_CN = "yyyy年MM月dd日";
    public static final String FORMAT_HHMM = "HH:mm";

    public static Date getNow() {
        return new Date();
    }

    public static Date getTodayDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getTomorrowDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    public static long getTodayEndTimestamp() {
        return getTomorrowDate().getTime() - 1;
    }

    public static Date getTodayEndDate() {
        return new Date(getTodayEndTimestamp());
    }

    public static int dayConvert(String dayStr) {
        switch (dayStr) {
            case "MON":
                return Calendar.MONDAY;
            case "TUE":
                return Calendar.TUESDAY;
            case "WED":
                return Calendar.WEDNESDAY;
            case "THU":
                return Calendar.THURSDAY;
            case "FRI":
                return Calendar.FRIDAY;
            case "SAT":
                return Calendar.SATURDAY;
            case "SUN":
                return Calendar.SUNDAY;
            default:
                return -1;

        }
    }

    public static Date convertStrToDate(String timeStr, String format) {
        if (timeStr == null) {
            return new Date();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date time = new Date();
        try {
            time = formatter.parse(timeStr);
        } catch (ParseException e) {
            LogUtil.e("DateUtil", "解析时间错误");
            e.printStackTrace();
        }
        return time;
    }

    public static String convertDateToStr(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getFriendlyDateDescription(String dateStr) {
        Date date = convertStrToDate(dateStr, "yyyy-MM-dd");
        return getFriendlyDateDescription(date);
    }

    public static String getFriendlyDateDescription(Date date) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        long dateInMillis = date.getTime();
        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return "今天";
        } else if ( julianDay == currentJulianDay +1 ) {
            return "明天";
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    /**
     * 根据日期取得星期几
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        return week;
    }

    public static String getFormattedDate(String formateStr, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(formateStr);
        return sdf.format(date);
    }

    /**
     * 获取距离给定日期还有多少小时多少分钟
     * @param date
     * @return
     */
    public static String getTimeLeaving(Date date) {
        long milliSecondsBetween = date.getTime() - getNow().getTime();
        long milliSecondsPerMinute = 1000 * 60;
        long milliSecondsPerHour = milliSecondsPerMinute * 60;

        if(milliSecondsBetween < 0) {
            milliSecondsBetween *= -1;
        }

        long totalHour = milliSecondsBetween / milliSecondsPerHour;
        long totalMinute = (milliSecondsBetween - totalHour * milliSecondsPerHour) / milliSecondsPerMinute;
        String timeRemainingStr;
        if(totalHour == 0) {
            timeRemainingStr = totalMinute + "分钟后";
        } else if(totalMinute == 0) {
            timeRemainingStr = totalHour + "小时后";
        } else {
            timeRemainingStr = totalHour + "小时" + totalMinute + "分钟后";
        }
        return timeRemainingStr;
    }
}
