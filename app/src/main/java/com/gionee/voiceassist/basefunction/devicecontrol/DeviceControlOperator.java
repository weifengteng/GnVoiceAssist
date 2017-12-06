package com.gionee.voiceassist.basefunction.devicecontrol;

import android.content.Context;
import android.text.TextUtils;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.basefunction.BasePresenter;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.systemctrl.SystemCtrlProvider;
import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.T;

/**
 * Created by twf on 2017/8/25.
 *
 * 离线快捷指令 功能实现
 * 实现以下离线指令的功能：设置Wifi状态，设置蓝牙、数据流量、飞行模式开关，关机、重启点那个操作。
 */

public class DeviceControlOperator extends BasePresenter {
    public static final String TAG = DeviceControlOperator.class.getSimpleName();

    protected Context mAppCtx;
    private SystemCtrlProvider mCtrlProvider;

    public DeviceControlOperator(IBaseFunction baseFunction) {
        super(baseFunction);
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
        mCtrlProvider = new SystemCtrlProvider();
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
        LogUtil.d(TAG, "devcie Operator = " + deviceOperator + " state = " + state);
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
    public void operateFlashLight(boolean mode) {
        mCtrlProvider.getFlashlightSwitch().toggle(mode, new ISwitchCtrl.Callback() {
            @Override
            public void onSuccess() {
                playAndRenderText("手电筒已打开");
            }

            @Override
            public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                switch (code) {
                    case NOT_SUPPORT:
                        playAndRenderText("手电筒不支持");
                        break;
                    case UNKNOWN_FAILURE:
                        playAndRenderText("手电筒打开错误");
                        break;
                }
            }
        });
    }

    /**
     * 操作WiFi开关
     * @param mode Wifi开、关
     */
    public void operateWifi(boolean mode) {
        mCtrlProvider.getWifiSwitch().toggle(mode, new ISwitchCtrl.Callback() {
            @Override
            public void onSuccess() {
                playAndRenderText("WLAN已打开");
            }

            @Override
            public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                switch (code) {
                    case NOT_SUPPORT:
                        playAndRenderText("WiFi不支持");
                        break;
                    case UNKNOWN_FAILURE:
                        playAndRenderText("WiFi打开错误");
                        break;
                }
            }
        });
    }

    /**
     * 操作蓝牙开关
     * @param mode 蓝牙开、关
     */
    public void operateBluetooth(boolean mode) {

    }

    /**
     * 控制飞行模式开关
     * @param mode
     */
    public void operateAirPlaneMode(boolean mode) {
        mCtrlProvider.getAirplaneModeSwitch().toggle(mode, new ISwitchCtrl.Callback() {
            @Override
            public void onSuccess() {
                playAndRenderText("飞行模式已打开");
            }

            @Override
            public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                playAndRenderText("飞行模式打开错误");
            }
        });
    }

    /**
     * 控制勿扰模式开关
     * @param mode
     */
    public void operateNoDisturbMode(boolean mode) {
        mCtrlProvider.getNotDisturbSwitch().toggle(mode, new ISwitchCtrl.Callback() {
            @Override
            public void onSuccess() {
                playAndRenderText("已打开勿扰模式");
            }

            @Override
            public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                playAndRenderText("勿扰模式打开错误");
            }
        });
    }

    /**
     * 控制移动数据（流量）开关
     * @param mode
     */
    public void operateMobileData(final boolean mode) {
        mCtrlProvider.getNotDisturbSwitch().toggle(mode, new ISwitchCtrl.Callback() {
            @Override
            public void onSuccess() {
                playAndRenderText(mode ? "已打开流量":"已关闭流量");
            }

            @Override
            public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                playAndRenderText("开关流量操作错误");
            }
        });
    }

    public void operateNfc(boolean mode) {
        mCtrlProvider.getWifiSwitch().toggle(mode, new ISwitchCtrl.Callback() {
            @Override
            public void onSuccess() {
                playAndRenderText("NFC已打开");
            }

            @Override
            public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                switch (code) {
                    case NOT_SUPPORT:
                        playAndRenderText("不支持NFC");
                        break;
                    case UNKNOWN_FAILURE:
                        playAndRenderText("NFC操作错误");
                        break;
                }
            }
        });
    }

    public void operateVpn(boolean mode) {

    }

    public void operateHotspot(boolean mode) throws ControllerNotSupported {

    }

    /**
     * 打开关闭位置信息
     * @param mode
     */
    public void operateLocation(boolean mode) {

    }

    /**
     * 截屏
     */
    public void operateCaptureScreen() {

    }

    /**
     * 关机
     */
    public void operateDeviceShutDown() {

    }

    /**
     *
     */
    public void operateDeviceReboot() {

    }

}
