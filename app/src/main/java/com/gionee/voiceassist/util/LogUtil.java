package com.gionee.voiceassist.util;

import android.os.Debug;
import android.util.Log;

/**
 * Created by twf on 11/27/17.
 */

public class LogUtil {

    public static boolean DEBUG = true;
    private static final String APPNAME = "GVA-";
    private static boolean LOGV_ON = DEBUG;
    private static boolean LOGD_ON = DEBUG;
    private static boolean LOGI_ON = DEBUG;
    private static boolean LOGW_ON = DEBUG;
    private static boolean LOGE_ON = DEBUG;

    public static void setDEBUG(boolean isDebug) {
        DEBUG = isDebug;
        LOGV_ON = true & DEBUG;
        LOGD_ON = true & DEBUG;
        LOGI_ON = true & DEBUG;
        LOGW_ON = true & DEBUG;
        LOGE_ON = true & DEBUG;
    }

    public static boolean getDEBUG() {
        return DEBUG;
    }

    /**
     * 记录相应的log信息v
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if(LOGV_ON) {
            tag = APPNAME + tag;
            Log.v(tag, msg);
        }
    }

    public static void v(Class<?> c, String msg) {
        v(c.getSimpleName(), msg);
    }

    /**
     * 记录相应的log信息i
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if(LOGI_ON) {
            tag = APPNAME + tag;
            Log.i(tag, msg);
        }
    }

    public static void i(Class<?> c, String msg) {
        i(c.getSimpleName(), msg);
    }

    /**
     * 记录相应的log信息d
     * @param c
     * @param msg
     */
    public static void d(Class<?> c, String msg) {
        d(c.getSimpleName(), msg);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if(LOGD_ON) {
            tag = APPNAME + tag;
            if(tr == null) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, msg, tr);
            }
        }
    }

    /**
     * 记录相应的log信息w
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if(LOGW_ON) {
            tag = APPNAME + tag;
            if(tr != null) {
                Log.w(tag, msg, tr);
            } else {
                Log.w(tag, msg);
            }
        }
    }

    public static void w(Class<?> c, String msg) {
        w(c, msg, null);
    }

    public static void w(Class<?> c, String msg, Throwable tr) {
        w(c.getSimpleName(), msg, tr);
    }

    /**
     * 记录相应的log信息e
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if(LOGE_ON) {
            tag = APPNAME + tag;
            if(tr != null) {
                Log.e(tag, msg, tr);
            } else {
                Log.e(tag, msg);
            }
        }
    }

    public static void e(Class<?> c, String msg) {
        e(c, msg, null);
    }

    public static void e(Class<?> c, String msg, Throwable tr) {
        e(c.getSimpleName(), msg, tr);
    }
}
