package com.gionee.gnvoiceassist.directiveListener.devicecontrol;

import android.util.Log;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.message.io.DirectiveResponseGenerator;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.DeviceControlMetadata;
import com.gionee.gnvoiceassist.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.Utils;

import java.util.Map;

/**
 * Created by twf on 2017/8/16.
 */

public class DeviceControlListener extends BaseDirectiveListener implements DeviceControlDeviceModule.IDeviceControlDirectiveListener {
    public static final String TAG = DeviceControlListener.class.getSimpleName();

    public DeviceControlListener(IDirectiveListenerCallback callback) {
        super(callback);
    }

    @Override
    public void onDirectiveReceived(Directive directive) {
        Log.d("DCSF", TAG + " ********************   onDirectiveReceived");
        String rawMsg = directive.rawMessage;
        Map<String, String> map = Utils.getControllerFromDeviceControlJson(rawMsg);
        String funcName = map.get(Constants.FUN_OPERATOR);
        String state = map.get(Constants.FUN_STATE);
        boolean openOrClose = state.equals("true");
//        iBaseFunction.getDeviceControlOperator().operateOnlineDeviceControlCmd(funcName, openOrClose);

        DeviceControlMetadata metadata = new DeviceControlMetadata();
        metadata.setCommand(funcName);
        metadata.setState(openOrClose);

        DirectiveResponseEntity response = new DirectiveResponseGenerator("device_control")
                .setAction("device_control")
                .setInCustomInteractive(false)
                .setMetadata(metadata.toJson())
                .build();
        mCallback.onDirectiveResponse(response);
    }

    @Override
    public void onDestroy() {

    }
}
