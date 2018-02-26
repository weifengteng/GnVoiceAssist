package com.gionee.voiceassist.datamodel.card;

/**
 * 卡片类型
 */

public enum CardType {
    DEFAULT_CARD(0),
    ANSWER_TEXT_CARD(1),
    STANDARD_CARD(2),
    LIST_CARD(3),
    IMAGE_LIST_CARD(4),
    QUERY_TEXT_CARD(5),
    CONTACT_CONTENT_CARD(11),
    SMS_CONTENT_CARD(12),
    ALARM_CARD(21),
    TIMER_CARD(22),
    STOPWATCH_CARD(23),
    QUICKSETTING_CARD(24),
    CONTACT_SELECT_CARD(31),
    SIM_SELECT_CARD(32);

    public final int code;

    CardType(int code) {
        this.code = code;
    }

    public int code() {return code;}
}
