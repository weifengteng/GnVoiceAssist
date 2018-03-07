package com.gionee.voiceassist.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期/时间相关工具类
 */

public class DateUtil {

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

}
