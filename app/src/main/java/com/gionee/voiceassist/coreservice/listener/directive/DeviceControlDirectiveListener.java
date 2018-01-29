package com.gionee.voiceassist.coreservice.listener.directive;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.coreservice.datamodel.DeviceControlDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.Utils;

import java.util.List;
import java.util.Map;

/**
 * Created by liyingheng on 1/26/18.
 */

public class DeviceControlDirectiveListener extends BaseDirectiveListener
        implements DeviceControlDeviceModule.IDeviceControlDirectiveListener {

    public DeviceControlDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onDirectiveReceived(Directive directive) {
        String rawMsg = directive.rawMessage;
        Map<String, String> map = Utils.getControllerFromDeviceControlJson(rawMsg);
        String funcName = map.get(Constants.FUN_OPERATOR);
        String state = map.get(Constants.FUN_STATE);
        boolean openOrClose = state.equals("true");
        DeviceControlDirectiveEntity payload = new DeviceControlDirectiveEntity(funcName, openOrClose);
        sendDirective(payload);
    }
}
