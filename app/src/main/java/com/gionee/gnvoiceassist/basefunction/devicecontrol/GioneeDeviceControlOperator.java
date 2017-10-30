package com.gionee.gnvoiceassist.basefunction.devicecontrol;

import android.widget.Toast;

import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.AirplaneModeController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.BluetoothController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.FlashlightController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.GioneeMobileDataController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.GioneeNoDisturbController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.HotspotController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.LocationController;
import com.gionee.gnvoiceassist.basefunction.devicecontrol.impl.NfcController;
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

/**
 * Created by liyingheng on 10/18/17.
 */

public class GioneeDeviceControlOperator extends DeviceControlOperator {

    public GioneeDeviceControlOperator(IBaseFunction baseFunction) {
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
        INoDisturb controller = new GioneeNoDisturbController(mAppCtx);
        controller.setNoDisturbEnabled(mode);
    }

    @Override
    public void operateMobileData(boolean mode) {
        //TODO: 考虑无Sim卡的情况
        IMobileData controller = new GioneeMobileDataController(mAppCtx);
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