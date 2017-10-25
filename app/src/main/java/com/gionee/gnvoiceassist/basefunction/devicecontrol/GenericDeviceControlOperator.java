package com.gionee.gnvoiceassist.basefunction.devicecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
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
import com.gionee.gnvoiceassist.util.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
