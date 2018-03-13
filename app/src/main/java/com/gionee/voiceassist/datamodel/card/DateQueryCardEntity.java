package com.gionee.voiceassist.datamodel.card;

/**
 * @author twf
 * @date 2018/3/12
 */

public class DateQueryCardEntity extends CardEntity {

    private String date;

    public DateQueryCardEntity() {
        setType(CardType.DATE_QUERY_CARD);
    }

    public String getDateFestival() {
        // TODO:
        return "劳动节";
    }

    public String getLunarDate() {
        // TODO:
        return "丁酉年 四月初六";
    }

    public String getYearAndWeek() {
        // TODO:
        return "2018年 星期六";
    }

    public String getMonthAndDay() {
        // TODO:
        return "05/01";
    }
}
