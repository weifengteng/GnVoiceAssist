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
package com.gionee.gnvoiceassist.sdk.module.tvlive.message;

import com.baidu.duer.dcs.framework.message.Payload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by caoyushu01 on 2017/7/25.
 */

public class SwitchChannelByNamePayload extends Payload {
    // 频道名称，eg:北京卫视，湖南卫视高清频道
    private String channelName;
    // 唯一频道码，eg:bj_ws
    private String channelCode;

    public SwitchChannelByNamePayload (@JsonProperty("channelName") String channelName,
                                       @JsonProperty("channelCode") String channelCode) {
        this.channelName = channelName;
        this.channelCode = channelCode;
    }

    public String getChannelName () {
        return channelName;
    }

    public void setChannelName (String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode () {
        return channelCode;
    }

    public void setChannelCode (String channelCode) {
        this.channelCode = channelCode;
    }
}
