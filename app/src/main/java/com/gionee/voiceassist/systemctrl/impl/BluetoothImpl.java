package com.gionee.voiceassist.systemctrl.impl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.widget.Toast;

import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;

/**
 * Created by liyingheng on 12/6/17.
 */

public class BluetoothImpl extends BaseCtrlImpl implements ISwitchCtrl {

    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {
        BluetoothAdapter bluetoothAdapter = ((BluetoothManager) mAppCtx.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE))
                .getAdapter();
        if (bluetoothAdapter.isEnabled() == enabled) {
            Toast.makeText(mAppCtx,"蓝牙已" + (enabled ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
//            return;
        }
        if (enabled) {
            bluetoothAdapter.enable();
        } else {
            bluetoothAdapter.disable();
        }
        callback.onSuccess();
    }
}
