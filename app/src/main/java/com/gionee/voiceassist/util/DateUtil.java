package com.gionee.voiceassist.util;

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

}
