package com.gionee.voiceassist.systemctrl.impl;

import com.gionee.voiceassist.systemctrl.iface.IPhonecallCtrl;

/**
 * Created by liyingheng on 12/6/17.
 */

public class PhonecallCtrlImpl extends BaseCtrlImpl implements IPhonecallCtrl {
    @Override
    public void makeCall(String number, String simId) {

    }

    @Override
    public boolean needChooseSim() {
        return false;
    }

    @Override
    public boolean dualSimAvailable() {
        return false;
    }

    @Override
    public boolean dualSimInserted() {
        return false;
    }

    @Override
    public int defaultSimId() {
        return 0;
    }
}
