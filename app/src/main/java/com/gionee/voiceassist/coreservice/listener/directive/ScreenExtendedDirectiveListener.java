package com.gionee.voiceassist.coreservice.listener.directive;

import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.AirPollutionCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.DateCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.StockCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.TrafficRestrictionCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.WeatherCardEntity;
import com.gionee.voiceassist.coreservice.sdk.module.screen.extend.card.ScreenExtendDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.screen.extend.card.message.RenderAirQualityPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.extend.card.message.RenderDatePayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.extend.card.message.RenderStockPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.extend.card.message.RenderTrafficRestrictionPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.extend.card.message.RenderWeatherPayload;
import com.gionee.voiceassist.util.DateUtil;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liyingheng on 3/5/18.
 */

public class ScreenExtendedDirectiveListener extends BaseDirectiveListener
        implements ScreenExtendDeviceModule.IRenderExtendListener {

    private static final String TAG = ScreenExtendedDirectiveListener.class.getSimpleName();

    public ScreenExtendedDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onRenderDirective(Directive directive) {
        Payload payload = directive.getPayload();
        ScreenExtendedDirectiveEntity cardEntity = null;
        if (payload instanceof RenderWeatherPayload) {
            // 处理天气卡片
            cardEntity = fireWeatherCard((RenderWeatherPayload) payload);
        } else if (payload instanceof RenderAirQualityPayload) {
            // 处理空气质量卡片
            cardEntity = fireAirPollutionCard((RenderAirQualityPayload) payload);
        } else if (payload instanceof RenderTrafficRestrictionPayload) {
            // 处理限行卡片
            cardEntity = fireTrafficRestrictionCard((RenderTrafficRestrictionPayload) payload);
        } else if (payload instanceof RenderStockPayload) {
            // 处理股票卡片
            cardEntity = fireStockCard((RenderStockPayload) payload);
        } else if (payload instanceof RenderDatePayload) {
            // 处理日期卡片
            cardEntity = fireDateCard((RenderDatePayload) payload);
        }
        if (cardEntity != null) {
            LogUtil.d(TAG, "ScreenExtended directive = " + cardEntity);
            sendDirective(cardEntity);
        }

    }

    private WeatherCardEntity fireWeatherCard(RenderWeatherPayload payload) {
        String city = payload.city;
        String askingDate = payload.askingDate;
        int askingDay = DateUtil.dayConvert(payload.askingDay);

        float currentTemp = 0;
        int currentPM25 = 0;
        String currentAirQuality = "";

        List<WeatherCardEntity.WeatherSummary> summaries = new ArrayList<>();
        for (RenderWeatherPayload.WeatherForecast dailyForecast:payload.weatherForecast) {
            // Convert High Temp
            float highTemp = Float.parseFloat(dailyForecast.highTemperature.substring(0, dailyForecast.highTemperature.length() - 1));
            float lowTemp = Float.parseFloat(dailyForecast.lowTemperature.substring(0, dailyForecast.lowTemperature.length() - 1));
            int day = DateUtil.dayConvert(dailyForecast.day);
            String date = dailyForecast.date;
            String weatherConditionDesc = dailyForecast.weatherCondition;

            WeatherCardEntity.WeatherSummary summary = new WeatherCardEntity.WeatherSummary(
                    date,
                    day,
                    -1,
                    weatherConditionDesc,
                    highTemp,
                    lowTemp,
                    "",
                    -1,
                    "");
            summaries.add(summary);
        }
        WeatherCardEntity entity = new WeatherCardEntity(city, "", askingDay, askingDate, currentTemp, currentPM25, currentAirQuality, summaries);
        return entity;
    }

    private AirPollutionCardEntity fireAirPollutionCard(RenderAirQualityPayload payload) {
        String city = payload.city;
        float currentTemp = Float.parseFloat(payload.currentTemperature.substring(0, payload.currentTemperature.length() - 1));
        int pm25 = Integer.parseInt(payload.pm25);
        String airCondition = payload.airQuality;
        int day = DateUtil.dayConvert(payload.day);
        String date = payload.date;
        String dateDesc = payload.dateDescription;

        return new AirPollutionCardEntity(
                city,
                currentTemp,
                pm25,
                airCondition,
                day,
                date,
                dateDesc);
    }

    private TrafficRestrictionCardEntity fireTrafficRestrictionCard(RenderTrafficRestrictionPayload payload) {
        String city = payload.city;
        int askDay = DateUtil.dayConvert(payload.day);
        String askDate = payload.date;
        String restriction = payload.todayRestriction;
        String restrictionRule = payload.restrictionRule;
        String nextdayRestriction = payload.tomorrowRestriction;
        List<TrafficRestrictionCardEntity.WeeklyTrafficRestriction> weeklyTrafficRestrictions = new ArrayList<>();
        for (RenderTrafficRestrictionPayload.Restriction dailyRestriction:payload.weekRestriction) {
            weeklyTrafficRestrictions.add(
                    new TrafficRestrictionCardEntity.WeeklyTrafficRestriction(
                            DateUtil.dayConvert(dailyRestriction.day),
                            dailyRestriction.restriction)
            );
        }

        return new TrafficRestrictionCardEntity(
                city,
                askDay,
                askDate,
                restriction,
                restrictionRule,
                nextdayRestriction,
                weeklyTrafficRestrictions);

    }

    private StockCardEntity fireStockCard(RenderStockPayload payload) {
        String stockName = payload.name;
        String marketStatus = payload.marketStatus;
        float marketPrice = (float) payload.marketPrice;
        float changeInPercentage = (float) payload.changeInPercentage;
        float changeInPrice = (float) payload.changeInPrice;
        // TODO: 日期信息暂时无法取得，可能需要修改DeviceModule
        Date lastUpdateTime = DateUtil.convertStrToDate(payload.datetime, "yyyy-MM-dd hh:mm:ssX");
        float openPrice = (float) payload.openPrice;
        float prevClosePrice = (float) payload.previousClosePrice;
        float dayHighPrice = (float) payload.dayHighPrice;
        float dayLowPrice = (float) payload.dayLowPrice;
        float priceEarningRatio = (float) payload.priceEarningRatio;
        String marketName = payload.marketName;

        return new StockCardEntity(
                stockName,
                "",
                marketStatus,
                marketPrice,
                changeInPercentage,
                changeInPrice,
                lastUpdateTime,
                openPrice,
                prevClosePrice,
                dayHighPrice,
                dayLowPrice,
                priceEarningRatio,
                marketName);
    }

    private DateCardEntity fireDateCard(RenderDatePayload payload) {
        return new DateCardEntity(
                DateUtil.convertStrToDate(payload.datetime.replace("T", " "), "yyyy-MM-dd hh:mm:ssX"),
                DateUtil.dayConvert(payload.day));
    }



}
