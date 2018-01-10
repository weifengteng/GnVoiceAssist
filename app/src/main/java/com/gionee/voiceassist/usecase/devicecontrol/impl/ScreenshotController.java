package com.gionee.voiceassist.usecase.devicecontrol.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;

import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IScreenshot;

/**
 * Created by liyingheng on 10/24/17.
 */

public class ScreenshotController extends BaseController implements IScreenshot {
    public ScreenshotController(Context ctx) {
        super(ctx);
    }

    private static final String SYSTEM_UI_PACKAGENAME = "com.android.systemui";
    private static final String SYSTEM_UI_SCREENSHOT_SERVICE = "com.android.systemui.screenshot.TakeScreenshotService";
    private static final int GN_SCREENSHOT_TIMEOUT = 2000;
    private Object mScreenshotLock = new Object();
    private Handler mHandler = new Handler();
    private ServiceConnection mScreenshotConnection;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void takeScreenshot() {
        synchronized (mScreenshotLock) {
            if (mScreenshotConnection != null) {
                return;
            }
            ComponentName cn = new ComponentName(SYSTEM_UI_PACKAGENAME, SYSTEM_UI_SCREENSHOT_SERVICE);
            Intent intent = new Intent();
            intent.setComponent(cn);
            ServiceConnection conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    synchronized (mScreenshotLock) {
                        if (mScreenshotConnection != this) {
                            return;
                        }
                        Messenger messenger = new Messenger(service);
                        Message msg = Message.obtain(null, 1);
                        final ServiceConnection myConn = this;
                        Handler h = new Handler(mHandler.getLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                synchronized (mScreenshotLock) {
                                    try {
                                        if (mScreenshotConnection == myConn) {
                                            mAppCtx.unbindService(mScreenshotConnection);
                                            mScreenshotConnection = null;
                                            mHandler.removeCallbacks(mScreenshotTimeout);
                                        }
                                    } catch(Exception e) {

                                    }
                                }
                            }
                        };
                        msg.replyTo = new Messenger(h);
                        msg.arg1 = 1;
                        msg.arg2 = 0;
                        try {
                            messenger.send(msg);
                        } catch (RemoteException e) {
                        }
                    }
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            if (mAppCtx.bindServiceAsUser(intent, conn, Context.BIND_AUTO_CREATE, UserHandle.CURRENT)) {
//                mScreenshotConnection = conn;
//                mHandler.postDelayed(mScreenshotTimeout, GN_SCREENSHOT_TIMEOUT);
//            }
        }
    }

    Runnable mScreenshotTimeout = new Runnable() {
        @Override public void run() {
            synchronized (mScreenshotLock) {
                try {
                    if (mScreenshotConnection != null) {
                        mAppCtx.unbindService(mScreenshotConnection);
                        mScreenshotConnection = null;
                    }
                } catch(Exception e) {
                }
            }
        }
    };
}
