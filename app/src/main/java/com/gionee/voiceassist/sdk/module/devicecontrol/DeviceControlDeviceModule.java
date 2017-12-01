/*
 * *
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gionee.voiceassist.sdk.module.devicecontrol;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by caoyushu01 on 2017/7/26.
 */

public class DeviceControlDeviceModule extends BaseDeviceModule {

    private List<IDeviceControlDirectiveListener> listeners = new ArrayList<>();

    public DeviceControlDeviceModule (IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
    }

    @Override
    public ClientContext clientContext () {
        return null;
    }

    @Override
    public void handleDirective (Directive directive) throws HandleDirectiveException {
        if (listeners == null) {
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDirectiveReceived(directive);
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetAssistiveTouch.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetAssistiveTouchPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetBluetooth.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetBluetoothPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetPortraitLock.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetPortraitLockPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetBrightness.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetBrightnessPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetCellular.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetCellularPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetCellularMode.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetCellularModePayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetGps.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetGpsPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetHotspot.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetHotspot.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetNfc.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetNfcPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetPhoneMode.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetPhoneMode.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetPhonePower.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetPhonePowerPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetSynchronization.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetSynchronizationPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetVibration.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetVibrationPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetVpn.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetVpnPayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.devicecontrol.ApiConstants.Directives.SetWifi.NAME, com.gionee.voiceassist.sdk.module.devicecontrol.message.SetWifiPayload.class);
        return map;
    }

    @Override
    public void release () {
        listeners.clear();
    }

    public void addDirectiveListener (IDeviceControlDirectiveListener listener) {
        if (listeners != null) {
            listeners.add(listener);
        }
    }

    public void removeDirectiveListener (IDeviceControlDirectiveListener listener) {
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }


    public interface IDeviceControlDirectiveListener {
        void onDirectiveReceived(Directive directive);
    }
}
