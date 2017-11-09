package com.gionee.gnvoiceassist.message.model.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 11/7/17.
 */

public class AlarmMetadata extends Metadata {

    private int hour;

    private int minute;

    private ArrayList<Integer> triggerDays;

    private String message;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public ArrayList<Integer> getTriggerDays() {
        return triggerDays;
    }

    public void setTriggerDays(ArrayList<Integer> triggerDays) {
        this.triggerDays = triggerDays;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
