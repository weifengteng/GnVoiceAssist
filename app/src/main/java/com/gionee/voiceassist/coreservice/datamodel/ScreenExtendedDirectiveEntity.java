package com.gionee.voiceassist.coreservice.datamodel;


/**
 * Created by liyingheng on 3/5/18.
 */

public abstract class ScreenExtendedDirectiveEntity extends DirectiveEntity {

    public ScreenExtendedDirectiveEntity() {
        setType(Type.SCREEN_EXTENDED);
    }

    private String title;

    private String rawData;

    private ExtendedCardType cardType = getCardType();

    public abstract ExtendedCardType getCardType();

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public enum ExtendedCardType {
        WEATHER,
        AIR_POLLUTION,
        TRAFFIC_RESTRICTION,
        STOCK,
        DATE
    }
}
