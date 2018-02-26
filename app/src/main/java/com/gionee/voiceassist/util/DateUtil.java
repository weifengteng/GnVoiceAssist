package com.gionee.voiceassist.util;

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
