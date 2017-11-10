package com.gionee.gnvoiceassist.sdk.module.alarms.message;


import com.baidu.duer.dcs.framework.message.Payload;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SetAlarmPayload
        extends Payload {
    private int hour;
    private int minutes;
    private String message;
    private List<String> days;
    private boolean viberate;

    public SetAlarmPayload(@JsonProperty("hour") int hour, @JsonProperty("minutes") int minutes, @JsonProperty("message") String message, @JsonProperty("days") List<String> days, @JsonProperty("viberate") boolean viberate) {
        this.hour = hour;
        this.minutes = minutes;
        this.message = message;
        this.days = days;
        this.viberate = viberate;
    }

    public int getHour() {
        return this.hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDays() {
        return this.days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public boolean isViberate() {
        return this.viberate;
    }

    public void setViberate(boolean viberate) {
        this.viberate = viberate;
    }
}
