package com.gionee.voiceassist.coreservice.sdk.module.customcmd.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/26.
 */

public class StartTimerPayload extends Payload {
    private String startTimer;

    public StartTimerPayload(@JsonProperty("starttimer") String startTimer) {
        this.startTimer = startTimer;
    }

    public String getStartTimer() {
        return startTimer;
    }

    public void setStartTimer(String startTimer) {
        this.startTimer = startTimer;
    }
}
