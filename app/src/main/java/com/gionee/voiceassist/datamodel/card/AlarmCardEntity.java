package com.gionee.voiceassist.datamodel.card;

import java.util.ArrayList;

/**
 * 闹钟功能卡片数据
 */

public class AlarmCardEntity extends CardEntity {

    public AlarmCardEntity() {
        setType(CardType.ALARM_CARD);
    }

    private int hour = 0;
    private int minute = 0;
    private ArrayList<Integer> repeatDay;
    private String alarmContent;
    private String triggerCountDownInfo;
    private String alarmDate;
    private String alarmDay;
    private String repeatInfo;

    public int getHour() {
        return hour;
    }

    public AlarmCardEntity setHour(int hour) {
        this.hour = hour;
        return this;
    }

    public int getMinute() {
        return minute;
    }

    public AlarmCardEntity setMinute(int minute) {
        this.minute = minute;
        return this;
    }

    public ArrayList<Integer> getRepeatDay() {
        return repeatDay;
    }

    public AlarmCardEntity setRepeatDay(ArrayList<Integer> repeatDay) {
        this.repeatDay = repeatDay;
        return this;
    }

    public String getAlarmContent() {
        this.alarmContent = "测试闹钟⏰";
        return alarmContent;
    }

    public AlarmCardEntity setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
        return this;
    }

    public String getAlarmTriggerTime() {
        StringBuilder sb = new StringBuilder();
        if(hour >=0 && hour <=9) {
            sb.append(0);
        }
        sb.append(hour);
        sb.append(":");
        if(minute >=0 && hour <= 9) {
            sb.append(0);
        }
        sb.append(minute);
        return sb.toString();
    }

    public String getTriggerCountDownInfo() {
        return "16小时30分钟后";
    }

    public String getAlarmDate() {
        return "2018年10月25日";
    }

    public String getAlarmDay() {
        return "周二";
    }

    public String getRepeatInfo() {
        return "";
    }
}
