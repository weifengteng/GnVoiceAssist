package com.gionee.voiceassist.datamodel.card.extend;

import com.gionee.voiceassist.coreservice.datamodel.screen.extended.WeatherCardEntity;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.CardType;
import com.gionee.voiceassist.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by liyingheng on 3/8/18.
 */

public class WeatherCard extends CardEntity {

    private String date;     // 询问日期
    private String dateDescription;      // 日期描述（今天，明天或周几）
    private String location;        // 地点
    private String locationImg;     // 地点对应的图片
    private int weatherConditionImgRes;     // 天气状况图标资源
    private String weatherCondition;      // 天气状况
    private String wind;                // 风向
    private String highTemp;        // 最高温度
    private String lowTemp;         // 最低温度
    private String currentTemp;     // 实时温度
    private String airCondition;       // 空气污染（PM2.5指数、空气指数等级）


    public WeatherCard() {
        setType(CardType.WEATHER_CARD);
    }

    public void setDate(String date) {
        // 解析date和dateDescription
        this.date = date;
        this.dateDescription = DateUtil.getFriendlyDateDescription(date);
    }

    public void setLocation(String city, String county) {
        // 解析城市和乡村
        this.location = city + "市" + county;
    }

    public void setWeatherCondition(String weatherCondition) {
        // 解析WeatherCondition，得到WeatherCondition文字和天气图标
        this.weatherCondition = weatherCondition;

    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setTemperature(float highTemperature, float lowTemperature) {
        // 解析温度（最高温度和最低温度）
        String highStr = String.valueOf((int) highTemperature);
        String lowStr = String.valueOf((int) lowTemperature);

        this.highTemp = highStr;
        this.lowTemp = lowStr;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemp = String.valueOf((int) currentTemperature);
    }

    public void setAirCondition(int pm25, String airGrade) {
        // 解析PM2.5和空气等级
        airCondition = String.valueOf(pm25) + "," + airGrade;
    }

    public void setWithWeatherEntity(WeatherCardEntity data) {
        Map<String, WeatherCardEntity.WeatherSummary> summaryMap = data.generateWeatherSummaryMap();
        WeatherCardEntity.WeatherSummary askdaySummary = summaryMap.get(data.getAskingDate());
        setDate(data.getAskingDate());
        setLocation(data.getCity(), data.getCounty());
        setCurrentTemperature(data.getCurrentTemperature());
        setTemperature(askdaySummary.highTemperature, askdaySummary.lowTemperature);
        setAirCondition(askdaySummary.pm25, askdaySummary.airQuality);
        setWind(askdaySummary.wind);
        setWeatherCondition(askdaySummary.weatherConditionDescription);
    }

    public String getDate() {
        return date;
    }

    public String getDateDescription() {
        return dateDescription;
    }

    public int getWeatherConditionImgRes() {
        return weatherConditionImgRes;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public String getWind() {
        return wind;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public String getAirCondition() {
        return airCondition;
    }

    public boolean isAskCurrentWeather() {
        String curDateStr = DateUtil.convertDateToStr(new Date(), "yyyy-MM-dd");
        return date.equals(curDateStr);
    }
}
