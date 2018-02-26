package com.gionee.voiceassist.systemctrl.impl.sysswitch;

import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;

/**
 * Created by liyingheng on 2/26/18.
 */

public class HotspotSwitchImpl implements ISwitchCtrl {
    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {

    }
}
