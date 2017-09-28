package com.gionee.gnvoiceassist.basefunction.devicecontrol;

import android.text.TextUtils;

import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.T;

/**
 * Created by twf on 2017/8/25.
 */

public class DeviceControlOperator extends BasePresenter implements IDeviceControlOperator {
    public static final String TAG = DeviceControlOperator.class.getSimpleName();

    public DeviceControlOperator(IBaseFunction baseFunction) {
        super(baseFunction);
    }

    @Override
    public void onDestroy() {
        // TODO:
    }

    @Override
    public void operateOfflineDeviceControlCmd(String deviceControlCmdTxt) {
        // TODO: operateOfflineDeviceControlCmd
        T.showShort("operateOfflineDeviceControlCmd: " + deviceControlCmdTxt);
        if(TextUtils.equals(deviceControlCmdTxt, "打开 wifi")) {
            operateWifi(true);
        } else if(TextUtils.equals(deviceControlCmdTxt, "关闭 wifi")) {
            operateWifi(false);
        }
        // TODO:
    }

    @Override
    public void operateOnlineDeviceControlCmd(String deviceOperator, boolean state) {
        com.baidu.duer.dcs.util.LogUtil.d(TAG, "devcie Operator = " + deviceOperator + " state = " + state);
        switch (deviceOperator) {
            case Constants.FUN_SETWIFI:
                operateWifi(state);
                break;
            case Constants.FUN_SETBLUETOOTH:
                operateBluetooth(state);
                break;
            case Constants.FUN_SETCELLULAR:
                operateMobileData(state);
                break;
            case Constants.FUN_SETGPS:
                operateLocation(state);
                break;
            case Constants.FUN_AIRPLANE_MODE:
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

    @Override
    public void operateFlashLight(boolean mode) {
        // TODO: operateFlashLight
        T.showShort("operateFlashLight" + mode);
    }

    @Override
    public void operateWifi(boolean mode) {
        // TODO: operateWifi
        T.showShort("operateWifi" + mode);
    }

    @Override
    public void operateBluetooth(boolean mode) {
        // TODO: operateBluetooth
        T.showShort("operateBluetooth " + mode);
    }

    @Override
    public void operateAirPlaneMode(boolean mode) {
        // TODO: operateAirPlaneMode
        T.showShort("operateAirPlaneMode" + mode);
    }

    @Override
    public void operateNoDisturbMode(boolean mode) {
        // TODO: operateNoDisturbMode
        T.showShort("operateNoDisturbMode" + mode);
    }

    @Override
    public void operateMobileData(boolean mode) {
        // TODO: operateMobileData
        T.showShort("operateMobileData" + mode);
    }

    @Override
    public void operateLocation(boolean mode) {
        // TODO: operateLocation
        T.showShort("operateLocation" + mode);
    }

    @Override
    public void operateCaptureScreen() {
        // TODO: operateCaptureScreen
        T.showShort("operateCaptureScreen");
    }

    @Override
    public void operateDeviceShutDown() {
        // TODO: operateDeviceShutDown
        T.showShort("operateDeviceShutDown");
    }

    @Override
    public void operateDeviceReboot() {
        // TODO: operateDeviceReboot
        T.showShort("operateDeviceReboot");
    }
}
