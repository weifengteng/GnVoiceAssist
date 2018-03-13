package com.gionee.voiceassist.coreservice.datamodel.screen.extended;

import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;

import java.util.List;

/**
 * Created by liyingheng on 3/5/18.
 */

public class TrafficRestrictionCardEntity extends ScreenExtendedDirectiveEntity {

    private String city;    // 城市

    private int day;        // 周几

    private String date;    // 日期（以yyyy-MM-dd形式返回）

    private String restriction; // 限行号码

    private String restrictionRule; // 限行规则

    private String nextdayRestriction;  // 次日限行

    private List<WeeklyTrafficRestriction> weeklyTrafficRestriction;  // 周限行概况

    public TrafficRestrictionCardEntity(String city, int day, String date, String restriction, String restrictionRule, String nextdayRestriction, List<WeeklyTrafficRestriction> weeklyTrafficRestriction) {
        this.city = city;
        this.day = day;
        this.date = date;
        this.restriction = restriction;
        this.restrictionRule = restrictionRule;
        this.nextdayRestriction = nextdayRestriction;
        this.weeklyTrafficRestriction = weeklyTrafficRestriction;
    }

    @Override
    public ExtendedCardType getCardType() {
        return ExtendedCardType.TRAFFIC_RESTRICTION;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getRestrictionRule() {
        return restrictionRule;
    }

    public void setRestrictionRule(String restrictionRule) {
        this.restrictionRule = restrictionRule;
    }

    public String getNextdayRestriction() {
        return nextdayRestriction;
    }

    public void setNextdayRestriction(String nextdayRestriction) {
        this.nextdayRestriction = nextdayRestriction;
    }

    public static class WeeklyTrafficRestriction {
        public int day; // 周几
        public String restriction;      // 限行

        public WeeklyTrafficRestriction(int day, String restriction) {
            this.day = day;
            this.restriction = restriction;
        }

        @Override
        public String toString() {
            return "WeeklyTrafficRestriction{" +
                    "day=" + day +
                    ", restriction='" + restriction + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TrafficRestrictionCardEntity{" +
                "city='" + city + '\'' +
                ", day=" + day +
                ", date='" + date + '\'' +
                ", restriction='" + restriction + '\'' +
                ", restrictionRule='" + restrictionRule + '\'' +
                ", nextdayRestriction='" + nextdayRestriction + '\'' +
                ", weeklyTrafficRestriction=" + weeklyTrafficRestriction +
                '}';
    }
}
