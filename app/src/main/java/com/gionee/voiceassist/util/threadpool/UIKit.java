package com.gionee.voiceassist.util.threadpool;

import android.os.Looper;

import com.baidu.duer.dcs.util.LogUtil;

/**
 * Created by twf on 2017/6/18.
 */
// http://blog.csdn.net/qiujuer/article/details/41599383
public class UIKit {
    public static final String TAG = UIKit.class.getSimpleName();
    private static UIKitHandlerPost mainPoster = null;
    public static final int MAX_MILLIS_EXECUTE_IN_MAIN_THREAD = 20;

    private static UIKitHandlerPost getMainPoster() {
        if(mainPoster == null) {
            synchronized (UIKit.class) {
                if(mainPoster == null) {
                    mainPoster = new UIKitHandlerPost(Looper.getMainLooper(), MAX_MILLIS_EXECUTE_IN_MAIN_THREAD);
                }
            }
        }
        return mainPoster;
    }

    public static void runOnMainThreadAsync(Runnable runnable) {
        LogUtil.d(TAG, "runOnMainThreadAsync");
        if(Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        getMainPoster().async(runnable);
    }

    public static void runOnMainThreadSync(Runnable runnable) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        UIKitSyncPost poster = new UIKitSyncPost(runnable);
        getMainPoster().sync(poster);
        poster.waitRun();
    }

    public static void runOnMainThreadSync(Runnable runnable, int waitTime, boolean cancel) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        UIKitSyncPost poster = new UIKitSyncPost(runnable);
        getMainPoster().sync(poster);
        poster.waitRun(waitTime, cancel);
    }

    public static void dispose() {
        if(mainPoster != null) {
            mainPoster.dispose();
            mainPoster = null;
        }
    }
}
