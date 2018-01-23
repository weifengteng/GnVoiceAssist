package com.gionee.voiceassist.datamodel.card;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liyingheng on 12/19/17.
 */

public class StopwatchCardEntity extends CardEntity {

    private long totalTime = 0;
    private StopwatchState state = StopwatchState.STOP;
    private List<StopwatchObserver> mObservers = new ArrayList<>();

    public StopwatchCardEntity() {
        setType(CardType.STOPWATCH_CARD);
    }

    /**
     * 取得当前计时时间
     * @return 当前计时时间（以毫秒为单位）
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * 更新当前计时时间
     * @param totalTime 当前计时时间（以毫秒为单位）
     */
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        updateTime(totalTime);
    }

    public StopwatchState getState() {
        return state;
    }

    public void setState(StopwatchState state) {
        this.state = state;
    }

    public void updateTime(long timeValue) {
        for (StopwatchObserver observer : mObservers) {
            observer.onDataChanged(timeValue);
        }
    }

    public void addStopwatchObserver(StopwatchObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeStopwatchObserver(StopwatchObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    public interface StopwatchObserver {
        void onStateChanged(StopwatchState state);
        void onDataChanged(long value);
    }

    public enum StopwatchState {
        START,
        PAUSE,
        STOP
    }
}
