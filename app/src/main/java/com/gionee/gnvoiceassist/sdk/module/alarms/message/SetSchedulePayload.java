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
package com.gionee.gnvoiceassist.sdk.module.alarms.message;

import com.baidu.duer.dcs.framework.message.Payload;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by caoyushu01 on 2017/7/26.
 */

public class SetSchedulePayload extends Payload {
    private String scheduledTime;
    private String content;
    private Repeat repeat;

    public SetSchedulePayload (@JsonProperty("scheduledTime") String scheduledTime,
                               @JsonProperty("content") String content,
                               @JsonProperty("repeat") Repeat repeat) {
        this.scheduledTime = scheduledTime;
        this.content = content;
        this.repeat = repeat;
    }


    public String getScheduledTime () {
        return scheduledTime;
    }

    public void setScheduledTime (String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public Repeat getRepeat () {
        return repeat;
    }

    public void setRepeat (Repeat repeat) {
        this.repeat = repeat;
    }




}
