package com.gionee.voiceassist.datamodel.card;

/**
 * Created by liyingheng on 12/19/17.
 */

public class StopwatchCardEntity extends CardEntity {

    private long totalTime = 0;

    public StopwatchCardEntity() {
        setType(CardType.STOPWATCH_CARD);
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
