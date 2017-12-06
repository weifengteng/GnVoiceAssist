package com.gionee.voiceassist.systemctrl.impl.sysswitch;

import com.gionee.voiceassist.systemctrl.impl.BaseCtrlImpl;
import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;

/**
 * Created by liyingheng on 12/6/17.
 */

public class GamemodeSwitchImpl extends BaseCtrlImpl implements ISwitchCtrl {
    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {

    }
}
