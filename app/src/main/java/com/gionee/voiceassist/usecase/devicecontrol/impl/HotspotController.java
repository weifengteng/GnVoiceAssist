package com.gionee.voiceassist.usecase.devicecontrol.impl;

import android.content.Context;

import com.gionee.voiceassist.usecase.devicecontrol.ControllerNotSupported;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IHotspot;

/**
 * Created by liyingheng on 10/24/17.
 */

public class HotspotController extends BaseController implements IHotspot {

    public HotspotController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setHotspotEnabled(boolean enabled) throws ControllerNotSupported {
//        WifiManager wifiManager = (WifiManager) mAppCtx.getSystemService(Context.WIFI_SERVICE);
//        boolean wifiEnable = wifiManager.isWifiEnabled();
//
//        if (wifiEnable) {
//            //使用热点需要关闭wifi功能
//            IWifi wifiController = new WifiController(mAppCtx);
//            wifiController.setWifiEnabled(false);
//        }
//        Method method = null;
//        try {
//            WifiConfiguration wifiConfiguration = new WifiConfiguration();
//            method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
//            method.invoke(wifiManager,wifiConfiguration,enabled);
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
        throw new ControllerNotSupported("不支持打开WiFi热点");
    }
}
