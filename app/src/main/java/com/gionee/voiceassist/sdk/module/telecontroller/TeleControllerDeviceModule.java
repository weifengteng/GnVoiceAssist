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
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.sdk.module.telecontroller.message.AskTimePayload;
import com.gionee.voiceassist.sdk.module.telecontroller.message.OperateBluetoothPayload;
import com.gionee.voiceassist.sdk.module.telecontroller.message.OperateFlashLightPayload;
import com.gionee.voiceassist.sdk.module.telecontroller.message.PrintScreenPayload;

import java.util.HashMap;

/**
 * Created by caoyushu01 on 2017/7/25.
 */

public class TeleControllerDeviceModule extends BaseDeviceModule {
    public static final String TAG = TeleControllerDeviceModule.class.getSimpleName();
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
        Payload payload = directive.getPayload();
        if(ApiConstants.Directives.OperatePrintscreen.NAME.equals(headerName)) {
            if(payload instanceof PrintScreenPayload) {
                handlePrintScreenPayload((PrintScreenPayload) payload);
            }
        } else if(ApiConstants.Directives.SearchTime.NAME.equals(headerName)) {
            if(payload instanceof AskTimePayload) {
                handleAskTimePayload((AskTimePayload) payload);
            }
        } else if(ApiConstants.Directives.OperateFlashlight.NAME.equals(headerName)) {
            if(payload instanceof OperateFlashLightPayload) {
                handleOperateFlashLight((OperateFlashLightPayload) payload);
            }
        } else {
            if (teleControllerDirectiveListener != null) {
                teleControllerDirectiveListener.onTeleControllerDirectiveReceived(directive);
            }
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + ApiConstants.Directives.OperatePrintscreen.NAME, PrintScreenPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.SearchTime.NAME, AskTimePayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.OperateBluetooth.NAME, OperateBluetoothPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.OperateFlashlight.NAME, OperateFlashLightPayload.class);
        return map;
    }

    @Override
    public void release () {
        teleControllerDirectiveListener = null;
    }

    private void handlePrintScreenPayload(PrintScreenPayload printScreenPayload) {
        teleControllerDirectiveListener.onPrintScreen(printScreenPayload);
    }

    private void handleAskTimePayload(AskTimePayload askTimePayload) {
        teleControllerDirectiveListener.onAskingTime(askTimePayload);
    }

    private void handleOperateFlashLight(OperateFlashLightPayload operateFlashLightPayload) {
        teleControllerDirectiveListener.onOperateFlashLight(operateFlashLightPayload);
    }

    public void addDirectivieListener (ITeleControllerDirectiveListener listener) {
        teleControllerDirectiveListener = listener;
    }

    public interface ITeleControllerDirectiveListener {
        void onTeleControllerDirectiveReceived(Directive directive);

        void onAskingTime(AskTimePayload askTimePayload);

        void onPrintScreen(PrintScreenPayload printScreenPayload);

        void onOperateFlashLight(OperateFlashLightPayload operateFlashLightPayload);
    }
}
