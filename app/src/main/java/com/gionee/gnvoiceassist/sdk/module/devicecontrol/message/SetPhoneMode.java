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
package com.gionee.gnvoiceassist.sdk.module.devicecontrol.message;

import com.baidu.duer.dcs.framework.message.Payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by caoyushu01 on 2017/7/26.
 */

public class SetPhoneMode extends Payload {
    private String target;
    private String mode;
    private Boolean phoneMode;

    public SetPhoneMode (@JsonProperty("target") String target,
                         @JsonProperty("mode") String mode,
                         @JsonProperty("phoneMode") Boolean phoneMode) {
        this.target = target;
        this.mode = mode;
        this.phoneMode = phoneMode;
    }

    public String getTarget () {
        return target;
    }

    public void setTarget (String target) {
        this.target = target;
    }

    public String getMode () {
        return mode;
    }

    public void setMode (String mode) {
        this.mode = mode;
    }

    public Boolean getPhoneMode () {
        return phoneMode;
    }

    public void setPhoneMode (Boolean phoneMode) {
        this.phoneMode = phoneMode;
    }
}
