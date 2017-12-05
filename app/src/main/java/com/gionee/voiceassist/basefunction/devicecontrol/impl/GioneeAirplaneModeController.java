package com.gionee.voiceassist.basefunction.devicecontrol.impl;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.Settings;
import android.widget.Toast;

import com.gionee.voiceassist.basefunction.devicecontrol.sysinterface.IAirplaneMode;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.T;

/**
 * Created by liyingheng on 12/1/17.
 */

public class GioneeAirplaneModeController extends BaseController implements IAirplaneMode{

    private static final String TAG = GioneeAirplaneModeController.class.getSimpleName();
    private Context mCtx;

    public GioneeAirplaneModeController(Context ctx) {
        super(ctx);
        mCtx = ctx;
    }


    @Override
    public void setAirplaneModeEnabled(boolean isEnable) {
//        boolean curState = Settings.System.getInt(mCtx.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false;
//        LogUtil.d("","FocusFunction setairplanMode isEnable = " + isEnable + ", state = " + curState);
//        if(isEnable != curState) {
//            Settings.Global.putInt(mCtx.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, isEnable ? 1 : 0);
//            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
//            intent.putExtra("state", isEnable);
//            mCtx.sendBroadcastAsUser(intent, UserHandle.ALL);
//            T.showShort("操作金立飞行模式成功");

//        }
    }
}
