package com.gionee.voiceassist.usecase.remind;

import java.util.Arrays;

/**
 * Created by liyingheng on 3/13/18.
 */

class Alarm {
    private String id;
    private int hour;
    private int minute;
    private boolean enabled;
    private String daysofweek;
    private String message;
    private String repeatCycle;
    private boolean[] repeatDays;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDaysofweek() {
        return daysofweek;
    }

    public void setDaysofweek(String daysofweek) {
        this.daysofweek = daysofweek;
        boolean[] ret = new boolean[7];
        for (int i = 0; i < 7; i++) {
            ret[i] = ((Integer.valueOf(daysofweek) & (1 << i)) > 0);
        }
        this.repeatDays = ret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRepeatCycle() {
        return repeatCycle;
    }

    public void setRepeatCycle(String repeatCycle) {
        this.repeatCycle = repeatCycle;
    }

    public boolean[] getRepeatDays() {
        return repeatDays;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id='" + id + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", enabled=" + enabled +
                ", daysofweek='" + daysofweek + '\'' +
                ", repeatDays=" + Arrays.toString(repeatDays) +
                ", message='" + message + '\'' +
                ", repeatCycle='" + repeatCycle + '\'' +
                '}';
    }
}
