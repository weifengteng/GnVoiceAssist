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
import com.gionee.gnvoiceassist.util.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liyingheng on 10/18/17.
 */

public class AospDeviceControlOperator extends DeviceControlOperator implements IDeviceControlOperator {

    public AospDeviceControlOperator(IBaseFunction baseFunction) {
        super(baseFunction);
    }

    @Override
    public void operateFlashLight(boolean mode) {
        CameraManager camManager = (CameraManager) mAppCtx.getSystemService(Context.CAMERA_SERVICE);
        try {
            camManager.setTorchMode("0",mode);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void operateWifi(boolean mode) {
        WifiManager wifiManager = (WifiManager) mAppCtx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == mode) {
            Toast.makeText(mAppCtx,"WiFi已" + (mode ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
            return;
        }
        wifiManager.setWifiEnabled(mode);
    }

    @Override
    public void operateBluetooth(boolean mode) {
        BluetoothAdapter bluetoothAdapter = ((BluetoothManager) mAppCtx.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE))
                .getAdapter();
        if (bluetoothAdapter.isEnabled() == mode) {
            Toast.makeText(mAppCtx,"蓝牙已" + (mode ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mode) {
            bluetoothAdapter.enable();
        } else {
            bluetoothAdapter.disable();
        }
    }

    @Override
    public void operateAirPlaneMode(boolean mode) {
        ContentResolver cr = mAppCtx.getContentResolver();
        boolean enabled = Settings.System.getInt(cr,Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        if (mode == enabled) {
            Toast.makeText(mAppCtx,"飞行模式已" + (mode ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
            return;
        }
        Settings.System.putInt(cr,Settings.System.AIRPLANE_MODE_ON, mode ? 1:0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state",mode);
        mAppCtx.sendBroadcast(intent);
    }

    @Override
    public void operateNoDisturbMode(boolean mode) {
        //TODO: Throw NOT SUPPORT Exception
    }

    @Override
    public void operateMobileData(boolean mode) {
        //TODO: 考虑无Sim卡的情况
        ConnectivityManager connectivityManager = (ConnectivityManager) mAppCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            //反射调用ConnectivityManager的内部方法setMobileDataEnabled()
            Class ownerClass = connectivityManager.getClass();
            //设置目标类的参数数量
            Class[] argsClass = new Class[1];
            //设置目标类的参数类型
            argsClass[0] = boolean.class;
            Method method = ownerClass.getMethod("setMobileDataEnabled",argsClass);
            method.invoke(connectivityManager, mode);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LogUtil.e(TAG,"移动数据设置错误：" + e.toString());
            e.printStackTrace();
        }


    }

    @Override
    public void operateNfc(boolean mode) {

    }

    @Override
    public void operateVpn(boolean mode) {

    }

    @Override
    public void operateHotspot(boolean mode) {

    }

    @Override
    public void operateLocation(boolean mode) {

    }

    @Override
    public void operateCaptureScreen() {
        Toast.makeText(mAppCtx,"操作：截屏",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void operateDeviceShutDown() {

    }

    @Override
    public void operateDeviceReboot() {

    }
}
