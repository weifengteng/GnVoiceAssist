package com.gionee.voiceassist.coreservice.sdk.module.customcmd.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/26.
 */

public class InstantHeartRatePayload extends Payload {
    private String heartRate;

    public InstantHeartRatePayload(@JsonProperty("heartrate") String heartRate) {
        this.heartRate = heartRate;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }
}
