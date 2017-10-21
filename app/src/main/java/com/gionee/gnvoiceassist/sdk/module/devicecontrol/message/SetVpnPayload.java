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

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by caoyushu01 on 2017/7/26.
 */

public class SetVpnPayload extends Payload {
    private String target;
    private Boolean vpn;

    public SetVpnPayload (@JsonProperty("target") String target,
                          @JsonProperty("vpn") Boolean vpn) {
        this.target = target;
        this.vpn = vpn;
    }

    public String getTarget () {
        return target;
    }

    public void setTarget (String target) {
        this.target = target;
    }

    public Boolean getVpn () {
        return vpn;
    }

    public void setVpn (Boolean vpn) {
        this.vpn = vpn;
    }
}
