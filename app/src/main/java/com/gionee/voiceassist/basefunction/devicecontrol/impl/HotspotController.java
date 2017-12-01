package com.gionee.voiceassist.basefunction.devicecontrol.impl;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.gionee.voiceassist.basefunction.devicecontrol.ControllerNotSupported;
import com.gionee.voiceassist.basefunction.devicecontrol.sysinterface.IHotspot;
import com.gionee.voiceassist.basefunction.devicecontrol.sysinterface.IWifi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
