package com.gionee.voiceassist.usecase.devicecontrol;

import android.widget.Toast;

import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.usecase.devicecontrol.impl.AirplaneModeController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.BluetoothController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.FlashlightController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.HotspotController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.LocationController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.MobileDataController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.NfcController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.NoDisturbController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.ScreenshotController;
import com.gionee.voiceassist.usecase.devicecontrol.impl.WifiController;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IAirplaneMode;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IBluetooth;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IFlashlight;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IHotspot;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.ILocation;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IMobileData;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.INfc;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.INoDisturb;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IScreenshot;
import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IWifi;

/**
 * Created by liyingheng on 10/18/17.
 */

public class GenericDeviceControlOperator extends DeviceControlOperator {

    public GenericDeviceControlOperator(IBaseFunction baseFunction) {
        super(baseFunction);
    }

    @Override
    public void operateFlashLight(boolean mode) {
        IFlashlight controller = new FlashlightController(mAppCtx);
        controller.setFlashlightEnabled(mode);
    }

    @Override
    public void operateWifi(boolean mode) {
        IWifi controller = new WifiController(mAppCtx);
        controller.setWifiEnabled(mode);
    }

    @Override
    public void operateBluetooth(boolean mode) {
        IBluetooth controller = new BluetoothController(mAppCtx);
        controller.setBluetoothEnabled(mode);
    }

    @Override
    public void operateAirPlaneMode(boolean mode) {
        IAirplaneMode controller = new AirplaneModeController(mAppCtx);
        controller.setAirplaneModeEnabled(mode);
    }

    @Override
    public void operateNoDisturbMode(boolean mode) {
        //TODO: Throw NOT SUPPORT Exception
        INoDisturb controller = new NoDisturbController(mAppCtx);
        controller.setNoDisturbEnabled(mode);
    }

    @Override
    public void operateMobileData(boolean mode) {
        //TODO: 考虑无Sim卡的情况
        IMobileData controller = new MobileDataController(mAppCtx);
        controller.setMobileDataEnabled(mode);
    }

    @Override
    public void operateNfc(boolean mode) {
        INfc controller = new NfcController(mAppCtx);
        controller.setNfcEnabled(mode);
    }

    @Override
    public void operateVpn(boolean mode) {
        //Not legally support
    }

    @Override
    public void operateHotspot(boolean mode) throws ControllerNotSupported {
        IHotspot controller = new HotspotController(mAppCtx);
        controller.setHotspotEnabled(mode);
    }

    @Override
    public void operateLocation(boolean mode) {
        ILocation controller = new LocationController(mAppCtx);
        controller.setLocationEnabled(mode);
    }

    @Override
    public void operateCaptureScreen() {
        Toast.makeText(mAppCtx,"操作：截屏",Toast.LENGTH_SHORT).show();
        IScreenshot controller = new ScreenshotController(mAppCtx);
        controller.takeScreenshot();
    }

    @Override
    public void operateDeviceShutDown() {

    }

    @Override
    public void operateDeviceReboot() {

    }
}
