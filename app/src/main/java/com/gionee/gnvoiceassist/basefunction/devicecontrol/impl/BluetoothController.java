package com.gionee.gnvoiceassist.basefunction.devicecontrol.impl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.widget.Toast;

import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IBluetooth;

/**
 * Created by liyingheng on 10/24/17.
 */

public class BluetoothController extends BaseController implements IBluetooth {

    public BluetoothController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setBluetoothEnabled(boolean enabled) {
        BluetoothAdapter bluetoothAdapter = ((BluetoothManager) mAppCtx.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE))
                .getAdapter();
        if (bluetoothAdapter.isEnabled() == enabled) {
            Toast.makeText(mAppCtx,"蓝牙已" + (enabled ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
            return;
        }
        if (enabled) {
            bluetoothAdapter.enable();
        } else {
            bluetoothAdapter.disable();
        }
    }
}
