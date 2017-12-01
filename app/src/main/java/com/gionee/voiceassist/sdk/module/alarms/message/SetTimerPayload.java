package com.gionee.voiceassist.sdk.module.alarms.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SetTimerPayload
        extends Payload
{
    private int length;
    private String message;

    public SetTimerPayload(@JsonProperty("length") int length, @JsonProperty("message") String message)
    {
        this.length = length;
        this.message = message;
    }

    public int getLength()
    {
        return this.length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
