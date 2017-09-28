package com.gionee.gnvoiceassist.basefunction.devicecontrol;

/**
 * Created by twf on 2017/8/25.
 */

public interface IDeviceControlOperator {

    void operateOfflineDeviceControlCmd(String deviceControlCmdTxt);

    void operateOnlineDeviceControlCmd(String deviceOperator, boolean state);

    void operateFlashLight(boolean mode);

    void operateWifi(boolean mode);

    void operateBluetooth(boolean mode);

    void operateAirPlaneMode(boolean mode);

    void operateNoDisturbMode(boolean mode);

    void operateMobileData(boolean mode);

    void operateLocation(boolean mode);

    void operateCaptureScreen();

    void operateDeviceShutDown();

    void operateDeviceReboot();
}
