package com.gionee.voiceassist.datamodel.card;

import com.gionee.voiceassist.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author twf
 * @date 2018/3/13
 */

public class WorldTimeQueryCardEntity extends CardEntity {

    // eg: "2018-03-13T17:29:47+08:00"
    private Date formattedDate;
    private int timeZone;

    public WorldTimeQueryCardEntity() {
        setType(CardType.WORLD_TIME_QUERY_CARD);
    }

    public WorldTimeQueryCardEntity setFormattedDate(Date formattedDate) {
        this.formattedDate = formattedDate;
        return this;
    }

    public WorldTimeQueryCardEntity setTimeZone(int timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public String getQueryDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ymd = sdf.format(formattedDate);
        return ymd;
    }

    public String getTimeZoneCity() {
        // TODO:
        return "City";
    }

    public String getTimeDiff() {
        // TODO:
        StringBuilder sb = new StringBuilder();
        int timeDiff = timeZone - 8;
        if(timeDiff >= 0) {
            sb.append("+");
        }
        sb.append(timeDiff);
        sb.append("小时");
        return sb.toString();
    }

    public String getHourAndMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String hm = sdf.format(formattedDate);
        return hm;
    }
}
