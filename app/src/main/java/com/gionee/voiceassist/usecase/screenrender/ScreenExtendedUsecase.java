package com.gionee.voiceassist.usecase.screenrender;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.AirPollutionCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.DateCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.TrafficRestrictionCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.extended.WeatherCardEntity;
import com.gionee.voiceassist.datamodel.card.WorldTimeQueryCardEntity;
import com.gionee.voiceassist.datamodel.card.extend.WeatherCard;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.LogUtil;

/**
 * Created by liyingheng on 3/7/18.
 */

public class ScreenExtendedUsecase extends BaseUsecase {

    private static final String TAG = ScreenExtendedUsecase.class.getSimpleName();

    @Override
    public void handleDirective(DirectiveEntity payload) {
        if (payload instanceof ScreenExtendedDirectiveEntity) {
            LogUtil.d(TAG, "handling directive = " + payload);
            switch (((ScreenExtendedDirectiveEntity) payload).getCardType()) {
                case WEATHER:
                    fireWeatherCard((WeatherCardEntity) payload);
                    break;
                case AIR_POLLUTION:
                    fireAirPollutionCard((AirPollutionCardEntity) payload);
                    break;
                case STOCK:
                    fireStockCard((AirPollutionCardEntity) payload);
                    break;
                case TRAFFIC_RESTRICTION:
                    fireTrafficRestrictionCard((TrafficRestrictionCardEntity) payload);
                    break;
                case DATE:
                    fireDatetimeCard((DateCardEntity) payload);
                    break;
            }
        }
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "screen.extend";
    }

    private void fireWeatherCard(WeatherCardEntity payload) {
        WeatherCard card = new WeatherCard();
        card.setWithWeatherEntity(payload);
        render(card);
    }

    private void fireAirPollutionCard(AirPollutionCardEntity payload) {

    }

    private void fireStockCard(AirPollutionCardEntity payload) {

    }

    private void fireDatetimeCard(DateCardEntity payload) {
        WorldTimeQueryCardEntity card = new WorldTimeQueryCardEntity();
        card.setFormattedDate(payload.getTime());
        render(card);
    }

    private void fireTrafficRestrictionCard(TrafficRestrictionCardEntity payload) {

    }
}
