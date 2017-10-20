package com.gionee.gnvoiceassist.util;

import android.util.Log;

/**
 * Created by liyingheng on 10/15/17.
 */

public class LogUtil {

    private static final int LOG_LEVEL = 1;

    private final int OFF = 0;
    private final int ERROR = 1;
    private final int DEBUG = 2;

    public static void e (String tag, String message) {
        Log.e(tag, message);
    }

    public static void d (String tag, String message) {
        Log.d(tag, message);
    }

    public static void w (String tag, String message) {
        Log.w(tag, message);
    }

    public static void i (String tag, String message) {
        Log.i(tag, message);
    }

    public static void v (String tag, String message) {
        Log.v(tag, message);
    }

}
