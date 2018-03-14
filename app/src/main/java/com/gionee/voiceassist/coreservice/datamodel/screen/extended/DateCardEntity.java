package com.gionee.voiceassist.coreservice.datamodel.screen.extended;

import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;
import com.gionee.voiceassist.util.DateUtil;

import java.util.Date;

/**
 * Created by liyingheng on 3/5/18.
 */

public class DateCardEntity extends ScreenExtendedDirectiveEntity {

    private Date time;

    private int day;        // 周几

    private int timeZone; // 所在时区

    public DateCardEntity(Date time, int day, int timeZone ) {
        this.time = time;
        this.day = day;
        this.timeZone = timeZone;
    }

    @Override
    public ExtendedCardType getCardType() {
        return ExtendedCardType.DATE;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public DateCardEntity setTimeZone(int timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    @Override
    public String toString() {
        return "DateCardEntity{" +
                "time=" + time +
                ", day=" + day +
                ", timeZone=" + timeZone +
                '}';
    }
}
