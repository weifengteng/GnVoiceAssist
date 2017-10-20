package com.gionee.gnvoiceassist.basefunction.devicecontrol;

/**
 * Created by twf on 2017/8/25.
 */

public interface IDeviceControlOperator {

//    void operateOfflineDeviceControlCmd(String deviceControlCmdTxt);
//
//    void operateOnlineDeviceControlCmd(String deviceOperator, boolean state);

    /**
     * 操作手电筒
     * @param mode 手电筒开(true)，关(false)
     */
    void operateFlashLight(boolean mode);

    /**
     * 操作WiFi开关
     * @param mode Wifi开、关
     */
    void operateWifi(boolean mode);

    /**
     * 操作蓝牙开关
     * @param mode 蓝牙开、关
     */
    void operateBluetooth(boolean mode);

    /**
     * 控制飞行模式开关
     * @param mode
     */
    void operateAirPlaneMode(boolean mode);

    /**
     * 控制勿扰模式开关
     * @param mode
     */
    void operateNoDisturbMode(boolean mode);

    /**
     * 控制移动数据（流量）开关
     * @param mode
     */
    void operateMobileData(boolean mode);

    void operateNfc(boolean mode);

    void operateVpn(boolean mode);

    void operateHotspot(boolean mode);

    /**
     * 打开关闭位置信息
     * @param mode
     */
    void operateLocation(boolean mode);

    /**
     * 截屏
     */
    void operateCaptureScreen();

    /**
     * 关机
     */
    void operateDeviceShutDown();

    /**
     *
     */
    void operateDeviceReboot();
}
