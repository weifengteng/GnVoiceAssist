package com.gionee.voiceassist.coreservice.datamodel.screen.extended;

import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;

/**
 * Created by liyingheng on 3/5/18.
 */

public class AirPollutionCardEntity extends ScreenExtendedDirectiveEntity {

    private String city;        // 城市

    private float currentTemperature;  // 当前气温

    private int pm25;       // pm2.5空气质量

    private String airCondition;    // 空气质量评价

    private int day;        // 周几（以Calendar的周几常量返回）

    private String date;    // 日期（以yyyy-MM-dd格式返回）

    private String dateDescription;     // 日期描述

    public AirPollutionCardEntity(String city, float currentTemperature, int pm25, String airCondition, int day, String date, String dateDescription) {
        this.city = city;
        this.currentTemperature = currentTemperature;
        this.pm25 = pm25;
        this.airCondition = airCondition;
        this.day = day;
        this.date = date;
        this.dateDescription = dateDescription;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateDescription() {
        return dateDescription;
    }

    public void setDateDescription(String dateDescription) {
        this.dateDescription = dateDescription;
    }

    @Override
    public ExtendedCardType getCardType() {
        return ExtendedCardType.AIR_POLLUTION;
    }

    @Override
    public String toString() {
        return "AirPollutionCardEntity{" +
                "city='" + city + '\'' +
                ", currentTemperature=" + currentTemperature +
                ", pm25=" + pm25 +
                ", airCondition='" + airCondition + '\'' +
                ", day=" + day +
                ", date='" + date + '\'' +
                ", dateDescription='" + dateDescription + '\'' +
                '}';
    }
}
