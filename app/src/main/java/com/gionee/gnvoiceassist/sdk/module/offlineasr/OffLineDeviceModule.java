/*
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
package com.gionee.gnvoiceassist.sdk.module.offlineasr;

import android.util.Log;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.OffLineAsrDirective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 离线识别的DeviceModule
 * <p>
 * Created by guxiuzhong@baidu.com on 2017/9/26.
 */
public class OffLineDeviceModule extends BaseDeviceModule {
    public static final String TAG = "OffLineDeviceModule";

    private List<IOfflineDirectiveListener> listeners = new ArrayList<>();

    public OffLineDeviceModule() {
        // 名字固定的
        super(ApiConstants.NAMESPACE);
    }

    @Override
    public ClientContext clientContext() {
        return null;
    }

    @Override
    public void handleDirective(Directive directive) throws HandleDirectiveException {
        // 数据正常的，所有的指令名字都叫OffLineAsrRecognitionResult
        if (directive.getName().equals("OffLineAsrRecognitionResult")) {
            OffLineAsrDirective offLineAsrDirective = (OffLineAsrDirective) directive;
            Log.d(TAG, "handleDirective: " + offLineAsrDirective.type);
            Log.d(TAG, "handleDirective: " + offLineAsrDirective.offLineData);
            for (IOfflineDirectiveListener listener:listeners) {
                listener.onDirectiveReceived((OffLineAsrDirective) directive);
            }
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        return null;
    }



    @Override
    public void release() {
        listeners.clear();
    }

    public void addOfflineDirectiveListener(IOfflineDirectiveListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeOfflineDirectiveListener(IOfflineDirectiveListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public interface IOfflineDirectiveListener {
        void onDirectiveReceived(OffLineAsrDirective directive);
    }
}