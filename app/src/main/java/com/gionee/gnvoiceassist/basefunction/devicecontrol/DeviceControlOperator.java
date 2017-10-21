package com.gionee.gnvoiceassist.basefunction.devicecontrol;

import android.content.Context;
import android.text.TextUtils;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.T;

/**
 * Created by twf on 2017/8/25.
 *
 * 离线快捷指令 功能实现
 * 实现以下离线指令的功能：设置Wifi状态，设置蓝牙、数据流量、飞行模式开关，关机、重启点那个操作。
 */

public abstract class DeviceControlOperator extends BasePresenter implements IDeviceControlOperator {
    public static final String TAG = DeviceControlOperator.class.getSimpleName();

    protected Context mAppCtx;

    public DeviceControlOperator(IBaseFunction baseFunction) {
        super(baseFunction);
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    @Override
    public void onDestroy() {
        // TODO:
    }

    public void operateOfflineDeviceControlCmd(String deviceControlCmdTxt) {
        // TODO: operateOfflineDeviceControlCmd
        T.showShort("operateOfflineDeviceControlCmd: " + deviceControlCmdTxt);
//        if(TextUtils.equals(deviceControlCmdTxt, "打开 wifi")) {
//            operateWifi(true);
//        } else if(TextUtils.equals(deviceControlCmdTxt, "关闭 wifi")) {
//            operateWifi(false);
//        }
        switch (deviceControlCmdTxt) {
            case "打开 wifi":
                operateWifi(true);
                break;
            case "关闭 wifi":
                operateWifi(false);
                break;
            case "打开手电筒":
                operateFlashLight(true);
                break;
            case "关闭手电筒":
                operateFlashLight(false);
                break;
        }

        // TODO:
    }

    public void operateOnlineDeviceControlCmd(String deviceOperator, boolean state) {
        com.baidu.duer.dcs.util.LogUtil.d(TAG, "devcie Operator = " + deviceOperator + " state = " + state);
        switch (deviceOperator) {
            case Constants.JSON_KEY_WIFI:
                operateWifi(state);
                break;
            case Constants.JSON_KEY_BLUETOOTH:
                operateBluetooth(state);
                break;
            case Constants.JSON_KEY_CELLULAR:
                operateMobileData(state);
                break;
            case Constants.JSON_KEY_GPS:
                operateLocation(state);
                break;
            case Constants.JSON_KEY_PHONEMODE:
                operateAirPlaneMode(state);
                break;
            case Constants.FUN_NO_DISTURB:
                operateNoDisturbMode(state);
                break;
            case Constants.FUN_SHUTDOWN:
                operateDeviceShutDown();
                break;
            case Constants.FUN_RESTART:
                operateDeviceReboot();
                break;
        }
    }

}
