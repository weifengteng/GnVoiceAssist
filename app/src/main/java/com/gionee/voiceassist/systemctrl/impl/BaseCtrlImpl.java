package com.gionee.voiceassist.systemctrl.impl;

import android.content.Context;

import com.gionee.voiceassist.GnVoiceAssistApplication;

/**
 * Created by liyingheng on 12/6/17.
 */

public class BaseCtrlImpl {

    protected Context mAppCtx;

    public BaseCtrlImpl() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

}
