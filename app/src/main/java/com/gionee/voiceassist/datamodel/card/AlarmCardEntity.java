package com.gionee.voiceassist.datamodel.card;

/**
 * 闹钟功能卡片数据
 */

public class AlarmCardEntity extends CardEntity {

    public AlarmCardEntity() {
        setType(CardType.ALARM_CARD);
    }

    private int hour = 0;
    private int minute = 0;
    private int[] repeatDay = {};

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int[] getRepeatDay() {
        return repeatDay;
    }

    public void setRepeatDay(int[] repeatDay) {
        this.repeatDay = repeatDay;
    }
}
