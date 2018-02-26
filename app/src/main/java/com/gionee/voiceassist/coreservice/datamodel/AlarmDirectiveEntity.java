package com.gionee.voiceassist.coreservice.datamodel;

import java.util.ArrayList;

/**
 * 闹钟场景Payload
 */

public class AlarmDirectiveEntity extends DirectiveEntity {

    public AlarmDirectiveEntity() {
        setType(Type.ALARM);
    }

    private int hour = 0;

    private int minute = 0;

    private ArrayList<Integer> repeatDays;

    /**
     * 取得小时数
     * @return 小时
     */
    public int getHour() {
        return hour;
    }

    /**
     * 设置小时数
     * @param hour 小时
     */
    public void setHour(int hour) {
        if (hour >= 0 && hour < 24) {
            this.hour = hour;
        } else {
            throw new IllegalArgumentException("Hour must in 0 to 23");
        }
    }

    /**
     * 取得分钟数
     * @return 分钟
     */
    public int getMinute() {
        return minute;
    }

    /**
     * 设置分钟数
     * @param minute 分钟
     */
    public void setMinute(int minute) {
        if (minute >= 0 && minute < 60) {
            this.minute = minute;
        } else {
            throw new IllegalArgumentException("Minute must in 0 to 59");
        }
    }

    /**
     * 一次性地设置时间
     * @param hour 小时
     * @param minute 分钟
     * @param repeatDays 重复日期。传入Calendar类日常量
     */
    public void setTime(int hour, int minute, ArrayList<Integer> repeatDays) {
        setHour(hour);
        setMinute(minute);
        setRepeatDays(repeatDays);
    }

    public ArrayList<Integer> getRepeatDays() {
        return repeatDays;
    }

    /**
     * 设置重复日期
     * @param repeatDays 重复日期。传入Calendar类日常量
     */
    public void setRepeatDays(ArrayList<Integer> repeatDays) {
        this.repeatDays = repeatDays;
    }

    @Override
    public String toString() {
        return "AlarmDirectiveEntity{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", repeatDays=" + repeatDays +
                '}';
    }
}
