package com.gionee.voiceassist.systemctrl.impl.sysswitch;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;

import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BaseCtrlImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liyingheng on 12/6/17.
 */

public class NotdisturbSwitchImpl extends BaseCtrlImpl implements ISwitchCtrl {
    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {
        NotificationManager notificationManager = (NotificationManager) mAppCtx.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.setZenMode(enabled ? Settings.Global.ZEN_MODE_ALARMS : Settings.Global.ZEN_MODE_OFF, null, "By Gionee VoiceAssist");

//            Method doNotDisturbModeMethod = notificationManager.getClass().getMethod("setZenMode", int.class, Uri.class, String.class);
//            doNotDisturbModeMethod.invoke(notificationManager, enabled ? 3:0, null, "By Gionee VoiceAssist");
        notificationManager.setZenMode(enabled ? 3:0, null, "Test");
        callback.onSuccess();
    }
}
