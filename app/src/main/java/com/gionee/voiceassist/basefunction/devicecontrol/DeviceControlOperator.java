package com.gionee.voiceassist.basefunction.devicecontrol;

import android.content.Context;
import android.text.TextUtils;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.basefunction.BasePresenter;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.T;

/**
 * Created by twf on 2017/8/25.
 *
 * 离线快捷指令 功能实现
 * 实现以下离线指令的功能：设置Wifi状态，设置蓝牙、数据流量、飞行模式开关，关机、重启点那个操作。
 */

public abstract class DeviceControlOperator extends BasePresenter {
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
            case "截屏":
                operateCaptureScreen();
                break;
        }

        // TODO:
    }

    public void operateOnlineDeviceControlCmd(String deviceOperator, boolean state) {
        com.baidu.duer.dcs.util.LogUtil.d(TAG, "devcie Operator = " + deviceOperator + " state = " + state);
        try {
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
                case Constants.JSON_KEY_HOTSPOT:
                    operateHotspot(state);
                    break;
                case Constants.JSON_KEY_NFC:
                    operateNfc(state);
                    break;
            }
        } catch (ControllerNotSupported controllerNotSupported) {
            controllerNotSupported.printStackTrace();
        }
    }

    /**
     * 操作手电筒
     * @param mode 手电筒开(true)，关(false)
     */
    public abstract void operateFlashLight(boolean mode);

    /**
     * 操作WiFi开关
     * @param mode Wifi开、关
     */
    public abstract void operateWifi(boolean mode);

    /**
     * 操作蓝牙开关
     * @param mode 蓝牙开、关
     */
    public abstract void operateBluetooth(boolean mode);

    /**
     * 控制飞行模式开关
     * @param mode
     */
    public abstract void operateAirPlaneMode(boolean mode);

    /**
     * 控制勿扰模式开关
     * @param mode
     */
    public abstract void operateNoDisturbMode(boolean mode);

    /**
     * 控制移动数据（流量）开关
     * @param mode
     */
    public abstract void operateMobileData(boolean mode);

    public abstract void operateNfc(boolean mode);

    public abstract void operateVpn(boolean mode);

    public abstract void operateHotspot(boolean mode) throws ControllerNotSupported;

    /**
     * 打开关闭位置信息
     * @param mode
     */
    public abstract void operateLocation(boolean mode);

    /**
     * 截屏
     */
    public abstract void operateCaptureScreen();

    /**
     * 关机
     */
    public abstract void operateDeviceShutDown();

    /**
     *
     */
    public abstract void operateDeviceReboot();

}
