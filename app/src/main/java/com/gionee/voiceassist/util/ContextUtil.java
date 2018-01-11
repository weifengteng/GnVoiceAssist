package com.gionee.voiceassist.util;

import android.content.Context;

import com.gionee.voiceassist.GnVoiceAssistApplication;

/**
 * Created by liyingheng on 1/11/18.
 */

public class ContextUtil {

    public static Context getAppContext() {
        return GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

}
