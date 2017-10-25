package com.gionee.gnvoiceassist.basefunction.devicecontrol.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.IAirplaneMode;

/**
 * Created by liyingheng on 10/24/17.
 */

public class AirplaneModeController extends BaseController implements IAirplaneMode {

    public AirplaneModeController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setAirplaneModeEnabled(boolean state) {
        ContentResolver cr = mAppCtx.getContentResolver();
        boolean enabled = Settings.System.getInt(cr,Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        if (state == enabled) {
            Toast.makeText(mAppCtx,"飞行模式已" + (state ? "打开":"关闭"), Toast.LENGTH_SHORT).show();
            return;
        }
        Settings.System.putInt(cr,Settings.System.AIRPLANE_MODE_ON, state ? 1:0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state",state);
        mAppCtx.sendBroadcast(intent);
    }
}
