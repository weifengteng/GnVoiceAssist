package com.gionee.gnvoiceassist.usecase;

import android.content.Context;
import android.widget.Toast;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.ControllerNotSupported;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.AirplaneModeController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.BluetoothController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.FlashlightController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.HotspotController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.LocationController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.MobileDataController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.NfcController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.NoDisturbController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.ScreenshotController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.WifiController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IAirplaneMode;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IBluetooth;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IFlashlight;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IHotspot;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.ILocation;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IMobileData;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.INfc;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.INoDisturb;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IScreenshot;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IWifi;
import com.gionee.gnvoiceassist.message.io.MetadataParser;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.DeviceControlMetadata;
import com.gionee.gnvoiceassist.usecase.annotation.DirectiveResult;
import com.gionee.gnvoiceassist.usecase.annotation.Operation;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.constants.UsecaseConstants.UsecaseAlias;


/**
 * Created by liyingheng on 11/9/17.
 */

public class DeviceControlUseCase extends UseCase {

    private static final String USECASE_DEVICE_CONTROL = UsecaseAlias.DEVICE_CONTROL;

    private Context mAppCtx;
    public DeviceControlUseCase() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    @Override
    public void handleMessage(DirectiveResponseEntity message) {
        DeviceControlMetadata metadata = MetadataParser.toEntity(message.getMetadata(),DeviceControlMetadata.class);
        requestDeviceControl(metadata);
    }

    @Override
    public String getUseCaseName() {
        return USECASE_DEVICE_CONTROL;
    }

    @DirectiveResult
    private void requestDeviceControl(DeviceControlMetadata metadata) {
        boolean state = metadata.getState();
        DeviceControlOperator operator = new DeviceControlOperator();
        switch (metadata.getCommand()) {
            case Constants.JSON_KEY_WIFI:
                operator.operateWifi(state);
                break;
            case Constants.JSON_KEY_BLUETOOTH:
                operator.operateBluetooth(state);
                break;
            case Constants.JSON_KEY_CELLULAR:
                operator.operateMobileData(state);
                break;
            case Constants.JSON_KEY_GPS:
                operator.operateLocation(state);
                break;
            case Constants.JSON_KEY_PHONEMODE:
                operator.operateAirPlaneMode(state);
                break;
            case Constants.FUN_NO_DISTURB:
                operator.operateNoDisturbMode(state);
                break;
            case Constants.FUN_SHUTDOWN:
                operator.operateDeviceShutDown();
                break;
            case Constants.FUN_RESTART:
                operator.operateDeviceReboot();
                break;
            case Constants.JSON_KEY_HOTSPOT:
                try {
                    operator.operateHotspot(state);
                } catch (ControllerNotSupported controllerNotSupported) {
                    controllerNotSupported.printStackTrace();
                }
                break;
            case Constants.JSON_KEY_NFC:
                operator.operateNfc(state);
                break;
        }
    }

    private class DeviceControlOperator {
        public void operateFlashLight(boolean mode) {
            IFlashlight controller = new FlashlightController(mAppCtx);
            controller.setFlashlightEnabled(mode);
        }

        public void operateWifi(boolean mode) {
            IWifi controller = new WifiController(mAppCtx);
            controller.setWifiEnabled(mode);
        }

        public void operateBluetooth(boolean mode) {
            IBluetooth controller = new BluetoothController(mAppCtx);
            controller.setBluetoothEnabled(mode);
        }

        public void operateAirPlaneMode(boolean mode) {
            IAirplaneMode controller = new AirplaneModeController(mAppCtx);
            controller.setAirplaneModeEnabled(mode);
        }

        public void operateNoDisturbMode(boolean mode) {
            //TODO: Throw NOT SUPPORT Exception
            INoDisturb controller = new NoDisturbController(mAppCtx);
            controller.setNoDisturbEnabled(mode);
        }

        public void operateMobileData(boolean mode) {
            //TODO: 考虑无Sim卡的情况
            IMobileData controller = new MobileDataController(mAppCtx);
            controller.setMobileDataEnabled(mode);
        }

        public void operateNfc(boolean mode) {
            INfc controller = new NfcController(mAppCtx);
            controller.setNfcEnabled(mode);
        }

        public void operateVpn(boolean mode) {
            //Not legally support
        }

        public void operateHotspot(boolean mode) throws ControllerNotSupported {
            IHotspot controller = new HotspotController(mAppCtx);
            controller.setHotspotEnabled(mode);
        }

        public void operateLocation(boolean mode) {
            ILocation controller = new LocationController(mAppCtx);
            controller.setLocationEnabled(mode);
        }

        public void operateCaptureScreen() {
            Toast.makeText(mAppCtx,"操作：截屏",Toast.LENGTH_SHORT).show();
            IScreenshot controller = new ScreenshotController(mAppCtx);
            controller.takeScreenshot();
        }

        public void operateDeviceShutDown() {

        }

        public void operateDeviceReboot() {

        }
    }

}
