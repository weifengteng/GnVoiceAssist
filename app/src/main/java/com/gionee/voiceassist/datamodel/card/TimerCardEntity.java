package com.gionee.voiceassist.datamodel.card;

/**
 * 倒计时卡片数据
 */

public class TimerCardEntity extends CardEntity{

    private long totalTime = 0;
    private long elapseTime = 0;

    public TimerCardEntity() {
        setType(CardType.TIMER_CARD);
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(long elapseTime) {
        this.elapseTime = elapseTime;
    }
}
