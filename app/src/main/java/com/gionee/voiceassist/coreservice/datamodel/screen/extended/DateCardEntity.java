package com.gionee.voiceassist.coreservice.datamodel.screen.extended;

import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;

import java.util.Date;

/**
 * Created by liyingheng on 3/5/18.
 */

public class DateCardEntity extends ScreenExtendedDirectiveEntity {

    private Date time;

    private int day;        // 周几

    public DateCardEntity(Date time, int day) {
        this.time = time;
        this.day = day;
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

    @Override
    public String toString() {
        return "DateCardEntity{" +
                "time=" + time +
                ", day=" + day +
                '}';
    }
}
