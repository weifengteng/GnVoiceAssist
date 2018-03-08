package com.gionee.voiceassist.view.viewholder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.extend.WeatherCard;

/**
 * Created by liyingheng on 1/15/18.
 */

public class WeatherCardViewHolder extends BaseViewHolder {

    private ImageView ivFeatureTileImg;
    private TextView tvFeatureCurrentTemp;
    private TextView tvFeatureWeatherCond;
    private TextView tvFeatureAllTemp;
    private TextView tvWeatherCondition;
    private TextView tvRealtimeTemp;
    private TextView tvAllTemp;
    private TextView tvWind;
    private TextView tvAirQuality;

    public WeatherCardViewHolder(View itemView) {
        super(itemView);
        tvFeatureCurrentTemp = (TextView) itemView.findViewById(R.id.tv_feature_current_temperature);
        tvFeatureWeatherCond = (TextView) itemView.findViewById(R.id.tv_feature_weather_condition);
        tvFeatureAllTemp = (TextView) itemView.findViewById(R.id.tv_feature_weather_all_temperature);
        tvWeatherCondition = (TextView) itemView.findViewById(R.id.tv_weather_desc);
        tvRealtimeTemp = (TextView) itemView.findViewById(R.id.tv_realtime_temperature);
        tvAllTemp = (TextView) itemView.findViewById(R.id.tv_all_temperature);
        tvWind = (TextView) itemView.findViewById(R.id.tv_wind);
        tvAirQuality = (TextView) itemView.findViewById(R.id.tv_air_condition);
        ivFeatureTileImg = (ImageView) itemView.findViewById(R.id.iv_feature_tile_img);
    }

    @Override
    public void bind(CardEntity payload) {
        WeatherCard card = (WeatherCard) payload;
        bindText(tvFeatureCurrentTemp, card.isAskCurrentWeather() ? card.getCurrentTemp():null, null);
        bindText(tvFeatureWeatherCond, card.getWeatherCondition(), null);
        bindText(tvFeatureAllTemp, card.getLowTemp() + " - " + card.getHighTemp(), null);
        bindText(tvWeatherCondition, card.getWeatherCondition(), null);
        bindText(tvRealtimeTemp, card.isAskCurrentWeather() ? card.getCurrentTemp():null, "实时");
        bindText(tvAllTemp, card.getLowTemp() + " - " + card.getHighTemp(), "温度");
        bindText(tvWind, card.getWind(), "风力");
        bindText(tvAirQuality, card.getAirCondition(), "空气质量指数");
    }

    public static WeatherCardViewHolder newInstance(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_extended_weather, parent, false);
        return new WeatherCardViewHolder(v);
    }

    private void bindText(TextView view, String text, String rowTitle) {
        if (text == null || TextUtils.isEmpty(text)) {
            view.setVisibility(View.GONE);
            return;
        }
        String prefix = (rowTitle != null && !TextUtils.isEmpty(rowTitle)) ? rowTitle + ":" : "";
        view.setVisibility(View.VISIBLE);
        view.setText(prefix+ text);
    }
}
