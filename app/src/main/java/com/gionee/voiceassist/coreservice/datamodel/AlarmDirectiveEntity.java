package com.gionee.voiceassist.coreservice.datamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liyingheng on 12/29/17.
 */

public class AlarmDirectiveEntity extends DirectiveEntity {

    public AlarmDirectiveEntity() {
        setType(Type.ALARM);
    }

    private int hour = 0;

    private int minute = 0;

    private ArrayList<Integer> repeatDays;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        if (hour >= 0 && hour < 24) {
            this.hour = hour;
        } else {
            throw new IllegalArgumentException("Hour must in 0 to 23");
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if (minute >= 0 && minute < 60) {
            this.minute = minute;
        } else {
            throw new IllegalArgumentException("Minute must in 0 to 59");
        }
    }

    public void setTime(int hour, int minute, ArrayList<Integer> repeatDays) {
        setHour(hour);
        setMinute(minute);
        setRepeatDays(repeatDays);
    }

    public ArrayList<Integer> getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(ArrayList<Integer> repeatDays) {
        this.repeatDays = repeatDays;
    }
}
