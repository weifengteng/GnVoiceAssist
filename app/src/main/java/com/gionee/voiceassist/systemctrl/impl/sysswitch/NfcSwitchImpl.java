package com.gionee.voiceassist.systemctrl.impl.sysswitch;

import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.RemoteException;

import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BaseCtrlImpl;

/**
 * Created by liyingheng on 12/6/17.
 */

public class NfcSwitchImpl extends BaseCtrlImpl implements ISwitchCtrl {

    @Override
    public boolean getState() {
        NfcManager manager = (NfcManager) mAppCtx.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {
        NfcManager manager = (NfcManager) mAppCtx.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (!isSupportNfcFeature() || adapter == null) {
            // NFC not supported
            callback.onFailure(FailureCode.NOT_SUPPORT, "设备不支持NFC");
            return;
        }
//        try {
//            INfcAdapter nfc = INfcAdapter.Stub.asInterface(ServiceManager.getService(Context.NFC_SERVICE));
//            try {
//                if (enabled) {
//                    nfc.enabled();
//                } else {
//                    nfc.disabled();
//                }
//                callback.onSuccess();
//            } catch (RemoteException e) {
//                callback.onFailure(FailureCode.UNKNOWN_FAILURE, "NFC 打开失败: " + e);
//            }
//        } catch (RemoteException e) {
//            callback.onFailure(FailureCode.UNKNOWN_FAILURE, "绑定NFC服务失败: " + e);
//        }


    }

    private boolean isSupportNfcFeature() {
        PackageManager pManager = mAppCtx.getPackageManager();
        return pManager.hasSystemFeature(PackageManager.FEATURE_NFC);
    }
}
