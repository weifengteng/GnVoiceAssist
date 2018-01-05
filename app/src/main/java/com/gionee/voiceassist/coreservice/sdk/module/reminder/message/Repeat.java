package com.gionee.voiceassist.coreservice.sdk.module.reminder.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by liyingheng on 1/5/18.
 */

public class Repeat {
    private String type;
    private List<String> weekly;
    private List<Integer> monthly;
    private List<String> yearly;

    public Repeat(
            @JsonProperty("type")String type,
            @JsonProperty("weekly")List<String> weekly,
            @JsonProperty("monthly")List<Integer> monthly,
            @JsonProperty("yearly")List<String> yearly) {
        this.type = type;
        this.weekly = weekly;
        this.monthly = monthly;
        this.yearly = yearly;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getWeekly() {
        return weekly;
    }

    public void setWeekly(List<String> weekly) {
        this.weekly = weekly;
    }

    public List<Integer> getMonthly() {
        return monthly;
    }

    public void setMonthly(List<Integer> monthly) {
        this.monthly = monthly;
    }

    public List<String> getYearly() {
        return yearly;
    }

    public void setYearly(List<String> yearly) {
        this.yearly = yearly;
    }
}
