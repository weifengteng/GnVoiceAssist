/*
 * *
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gionee.voiceassist.sdk.module.alarms.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by caoyushu01 on 2017/7/26.
 */

public class Repeat {
    private String type;
    private List<String> weekly;
    private List<Integer> monthly;
    private List<String> yearly;

    public Repeat (@JsonProperty("type") String type,
                   @JsonProperty("weekly") List<String> weekly,
                   @JsonProperty("monthly") List<Integer> monthly,
                   @JsonProperty("yearly") List<String> yearly) {
        this.type = type;
        this.weekly = weekly;
        this.monthly = monthly;
        this.yearly = yearly;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public List<String> getWeekly () {
        return weekly;
    }

    public void setWeekly (List<String> weekly) {
        this.weekly = weekly;
    }

    public List<Integer> getMonthly () {
        return monthly;
    }

    public void setMonthly (List<Integer> monthly) {
        this.monthly = monthly;
    }

    public List<String> getYearly () {
        return yearly;
    }

    public void setYearly (List<String> yearly) {
        this.yearly = yearly;
    }

}
