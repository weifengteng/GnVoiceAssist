package com.gionee.voiceassist.coreservice.datamodel.screen.extended;

import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;
import com.gionee.voiceassist.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liyingheng on 3/5/18.
 */

public class WeatherCardEntity extends ScreenExtendedDirectiveEntity {

    private String city;        // 城市名称

    private String county;      // 镇或乡村名称

    private int askingDay;      // 询问星期几，以Calendar的星期常数作为返回结果

    private String askingDate;  // 询问日期, 返回格式为yyyy-MM-dd

    private float currentTemperature;  // 当前气温

    private int currentPM25;     // 当前PM 2.5

    private String currentAirQuality;  // 当前空气质量

    private List<WeatherSummary> weatherSummaries;      // 天气概况

    public WeatherCardEntity(String city, String county, int askingDay, String askingDate, float currentTemperature, int currentPM25, String currentAirQuality, List<WeatherSummary> weatherSummaries) {
        this.city = city;
        this.county = county;
        this.askingDay = askingDay;
        this.askingDate = askingDate;
        this.currentTemperature = currentTemperature;
        this.currentPM25 = currentPM25;
        this.currentAirQuality = currentAirQuality;
        this.weatherSummaries = weatherSummaries;
    }

    @Override
    public ExtendedCardType getCardType() {
        return ExtendedCardType.WEATHER;
    }

    public static class WeatherSummary {
        private String date;    // 日期

        private int day;        //星期几（以Calendar的周几常数作为返回结果

        private int weatherConditionNo;     // 天气状况编号。用来表示不同的天气信息

        private String weatherConditionDescription;     // 天气状况语言描述

        private float highTemperature;      // 最高温度

        private float lowTemperature;       // 最低温度

        private String wind;        // 风向风速信息

        private int pm25;       // pm2.5

        private String airQuality;  // 空气质量指数

        @Override
        public String toString() {
            return "WeatherSummary{" +
                    "date='" + date + '\'' +
                    ", day=" + day +
                    ", weatherConditionNo=" + weatherConditionNo +
                    ", weatherConditionDescription='" + weatherConditionDescription + '\'' +
                    ", highTemperature=" + highTemperature +
                    ", lowTemperature=" + lowTemperature +
                    ", wind='" + wind + '\'' +
                    ", pm25=" + pm25 +
                    ", airQuality='" + airQuality + '\'' +
                    '}';
        }

        public WeatherSummary(String date, int day, int weatherConditionNo, String weatherConditionDescription, float highTemperature, float lowTemperature, String wind, int pm25, String airQuality) {
            this.date = date;
            this.day = day;
            this.weatherConditionNo = weatherConditionNo;
            this.weatherConditionDescription = weatherConditionDescription;
            this.highTemperature = highTemperature;
            this.lowTemperature = lowTemperature;
            this.wind = wind;
            this.pm25 = pm25;
            this.airQuality = airQuality;

        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getAskingDay() {
        return askingDay;
    }

    public void setAskingDay(int askingDay) {
        this.askingDay = askingDay;
    }

    public String getAskingDate() {
        return askingDate;
    }

    public void setAskingDate(String askingDate) {
        this.askingDate = askingDate;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public List<WeatherSummary> getWeatherSummaries() {
        return weatherSummaries;
    }

    public void setWeatherSummaries(List<WeatherSummary> weatherSummaries) {
        this.weatherSummaries = weatherSummaries;
    }

    public int getCurrentPM25() {
        return currentPM25;
    }

    public void setCurrentPM25(int currentPM25) {
        this.currentPM25 = currentPM25;
    }

    public String getCurrentAirQuality() {
        return currentAirQuality;
    }

    public void setCurrentAirQuality(String currentAirQuality) {
        this.currentAirQuality = currentAirQuality;
    }

    public boolean isAskCurrentWeather() {
        Date current = DateUtil.getNow();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(current).equals(askingDate);
    }

    @Override
    public String toString() {
        return "WeatherCardEntity{" +
                "city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", askingDay=" + askingDay +
                ", askingDate='" + askingDate + '\'' +
                ", currentTemperature=" + currentTemperature +
                ", currentPM25=" + currentPM25 +
                ", currentAirQuality='" + currentAirQuality + '\'' +
                ", isAskCurrentWeather=" + isAskCurrentWeather() +
                ", weatherSummaries=" + weatherSummaries +
                '}';
    }
}
