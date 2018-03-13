package com.gionee.voiceassist.datamodel.card;

/**
 * @author twf
 * @date 2018/3/13
 */

public class WorldTimeQueryCardEntity extends CardEntity {

    private String worldTime;

    public WorldTimeQueryCardEntity() {
        setType(CardType.WORLD_TIME_QUERY_CARD);
    }

    public String getQueryDate() {
        // TODO:
        return "2018-03-12";
    }

    public String getTimeZoneCity() {
        // TODO:
        return "巴塞罗那";
    }

    public String getTimeDiff() {
        // TODO:
        return "-7小时";
    }

    public String getHourAndMinute() {
        // TODO:

        return "02:48";
    }
}
