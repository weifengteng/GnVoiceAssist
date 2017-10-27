package com.gionee.gnvoiceassist.basefunction.devicecontrol.impl;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IWifi;

/**
 * Created by liyingheng on 10/24/17.
 */

public class WifiController extends BaseController implements IWifi {

    public WifiController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setWifiEnabled(boolean enabled) {
        WifiManager wifiManager = (WifiManager) mAppCtx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == enabled) {
            Toast.makeText(mAppCtx,"WiFi已" + (enabled ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
            return;
        }
        wifiManager.setWifiEnabled(enabled);
    }
}
