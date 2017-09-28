package com.gionee.gnvoiceassist.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;

/**
 * Created by twf on 2017/8/16.
 */

public class T {

    private T() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    private static boolean isShow = true;

    public static void showShort(String message) {
        if(isShow) {
            showToastInUIThread(message, Toast.LENGTH_SHORT);
        }
    }

    public static void showLong(String message) {
        if(isShow) {
            showToastInUIThread(message, Toast.LENGTH_LONG);
        }
    }

    public static void showShort(int messageId) {
        showShort(GnVoiceAssistApplication.getInstance().getResources().getString(messageId));
    }

    public static void showLong(int messageId) {
        showLong(GnVoiceAssistApplication.getInstance().getResources().getString(messageId));
    }

    private static void showToastInUIThread(final String text, final int duration) {
        if(Looper.myLooper() != Looper.getMainLooper()) {
            LogUtil.d("DCSF", "Not UI Thread");
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GnVoiceAssistApplication.getInstance(), text, duration).show();
                }
            });
        } else {
            LogUtil.d("DCSF", "UI Thread");
            Toast.makeText(GnVoiceAssistApplication.getInstance(), text, duration).show();
        }
    }


}
