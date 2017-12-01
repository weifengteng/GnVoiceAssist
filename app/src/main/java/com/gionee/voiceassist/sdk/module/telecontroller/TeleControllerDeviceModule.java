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
package com.gionee.voiceassist.sdk.module.telecontroller;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;

import java.util.HashMap;

/**
 * Created by caoyushu01 on 2017/7/25.
 */

public class TeleControllerDeviceModule extends BaseDeviceModule {
    private ITeleControllerDirectiveListener teleControllerDirectiveListener;


    public TeleControllerDeviceModule (IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
    }

    @Override
    public ClientContext clientContext () {
        return null;
    }

    @Override
    public void handleDirective (Directive directive) throws HandleDirectiveException {
        String headerName = directive.getName();
        if (teleControllerDirectiveListener != null) {
            teleControllerDirectiveListener.onTeleControllerDirectiveReceived(directive);
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + com.gionee.voiceassist.sdk.module.telecontroller.ApiConstants.Directives.OperateBluetooth.NAME, com.gionee.voiceassist.sdk.module.telecontroller.message.OperateBluetoothPayload.class);
        return map;
    }

    @Override
    public void release () {
        teleControllerDirectiveListener = null;
    }

    public void addDirectiveListener (ITeleControllerDirectiveListener listener) {
        this.teleControllerDirectiveListener = listener;
    }

    public interface ITeleControllerDirectiveListener {
        void onTeleControllerDirectiveReceived(Directive directive);
    }
}
