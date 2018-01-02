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
package com.gionee.voiceassist.coreservice.sdk.module.telecontroller.message;

import com.baidu.duer.dcs.framework.message.Payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by caoyushu01 on 2017/7/25.
 */

public class OperateBluetoothPayload extends Payload {
    private String operate;
    private String bluetooth;

    public OperateBluetoothPayload (@JsonProperty("operate") String operate, @JsonProperty("bluetooth") String bluetooth) {
        this.operate = operate;
        this.bluetooth = bluetooth;
    }

    public String getOperate () {
        return operate;
    }

    public void setOperate (String operate) {
        this.operate = operate;
    }

    public String getBluetooth () {
        return bluetooth;
    }

    public void setBluetooth (String bluetooth) {
        this.bluetooth = bluetooth;
    }
}
