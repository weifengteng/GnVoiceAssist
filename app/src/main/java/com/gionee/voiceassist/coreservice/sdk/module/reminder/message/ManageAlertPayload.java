package com.gionee.voiceassist.coreservice.sdk.module.reminder.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liyingheng on 1/5/18.
 */

public class ManageAlertPayload extends Payload {
    private String type;
    private String year;
    private String month;
    private String week;
    private String day;
    private String hour;
    private String minute;
    private String apm;
    private String content;
    private Repeat repeat;

    public ManageAlertPayload(@JsonProperty("type")String type,
                              @JsonProperty("year")String year,
                              @JsonProperty("month")String month,
                              @JsonProperty("week")String week,
                              @JsonProperty("day")String day,
                              @JsonProperty("hour")String hour,
                              @JsonProperty("minute")String minute,
                              @JsonProperty("apm")String apm,
                              @JsonProperty("content")String content,
                              @JsonProperty("repeat")Repeat repeat) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.week = week;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.apm = apm;
        this.content = content;
        this.repeat = repeat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getApm() {
        return apm;
    }

    public void setApm(String apm) {
        this.apm = apm;
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
