package com.gionee.voiceassist.systemctrl.impl.sysswitch;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BaseCtrlImpl;

/**
 * Created by liyingheng on 12/6/17.
 */

public class FlashlightSwitchImpl extends BaseCtrlImpl implements ISwitchCtrl {

    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {
        CameraManager camManager = (CameraManager) mAppCtx.getSystemService(Context.CAMERA_SERVICE);
        try {
            camManager.setTorchMode("0", enabled);
            callback.onSuccess();
        } catch (CameraAccessException e) {
            callback.onFailure(FailureCode.UNKNOWN_FAILURE, "手电筒打开错误: " + e);
            e.printStackTrace();
        }
    }
}
