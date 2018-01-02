package com.gionee.voiceassist.directiveListener.devicecontrol;

import android.util.Log;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.coreservice.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.Utils;

import java.util.Map;

/**
 * Created by twf on 2017/8/16.
 */

public class DeviceControlListener extends BaseDirectiveListener implements DeviceControlDeviceModule.IDeviceControlDirectiveListener {
    public static final String TAG = DeviceControlListener.class.getSimpleName();
    public DeviceControlListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
    }

    @Override
    public void onDirectiveReceived(Directive directive) {
        Log.d("DCSF", TAG + " ********************   onDirectiveReceived");
        String rawMsg = directive.rawMessage;
        Map<String, String> map = Utils.getControllerFromDeviceControlJson(rawMsg);
        String funcName = map.get(Constants.FUN_OPERATOR);
        String state = map.get(Constants.FUN_STATE);
        boolean openOrClose = state.equals("true");
        iBaseFunction.getDeviceControlOperator().operateOnlineDeviceControlCmd(funcName, openOrClose);
    }

    @Override
    public void onDestroy() {

    }
}
