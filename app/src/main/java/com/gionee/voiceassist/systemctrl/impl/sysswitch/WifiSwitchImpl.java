package com.gionee.voiceassist.systemctrl.impl.sysswitch;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BaseCtrlImpl;

/**
 * Created by liyingheng on 12/6/17.
 */

public class WifiSwitchImpl extends BaseCtrlImpl implements ISwitchCtrl {
    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {
        WifiManager wifiManager = (WifiManager) mAppCtx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == enabled) {
            Toast.makeText(mAppCtx,"WiFi已" + (enabled ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
            return;
        }
        wifiManager.setWifiEnabled(enabled);
    }
}
