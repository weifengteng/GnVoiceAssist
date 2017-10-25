package com.gionee.gnvoiceassist.basefunction.devicecontrol.impl;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IFlashlight;

/**
 * Created by liyingheng on 10/24/17.
 */

public class FlashlightController extends BaseController implements IFlashlight {

    public FlashlightController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setFlashlightEnabled(boolean enabled) {
        CameraManager camManager = (CameraManager) mAppCtx.getSystemService(Context.CAMERA_SERVICE);
        try {
            camManager.setTorchMode("0",enabled);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
