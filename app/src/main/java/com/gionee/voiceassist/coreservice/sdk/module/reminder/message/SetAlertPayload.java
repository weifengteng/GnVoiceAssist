package com.gionee.voiceassist.coreservice.sdk.module.reminder.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liyingheng on 1/5/18.
 */

public class SetAlertPayload extends Payload {
    private String scheduledTime;
    private String content;
    private Repeat repeat;

    public SetAlertPayload (
            @JsonProperty("scheduledTime")String scheduledTime,
            @JsonProperty("content")String content,
            @JsonProperty("repeat")Repeat repeat) {
        this.scheduledTime = scheduledTime;
        this.content = content;
        this.repeat = repeat;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }
}
