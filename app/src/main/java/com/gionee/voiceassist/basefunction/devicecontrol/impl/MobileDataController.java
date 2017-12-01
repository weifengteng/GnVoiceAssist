package com.gionee.voiceassist.basefunction.devicecontrol.impl;

import android.content.Context;

import com.gionee.voiceassist.basefunction.devicecontrol.sysinterface.IMobileData;

/**
 * Created by liyingheng on 10/24/17.
 */

public class MobileDataController extends BaseController implements IMobileData {

    public MobileDataController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setMobileDataEnabled(boolean enabled) {

    }
}
