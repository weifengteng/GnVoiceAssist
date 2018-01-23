package com.gionee.voiceassist.usecase.timing;

import android.os.Handler;
import android.os.Looper;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.datamodel.card.CardAction;
import com.gionee.voiceassist.datamodel.card.StopwatchCardEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by liyingheng on 1/22/18.
 */

public class StopwatchUsecase extends BaseUsecase {

    /**
     * 更新间隔时间
     */
    private static final int UPDATE_INTERVAL = 1000;

    private Timer stopwatch;
    private TimerTask stopwatchTask;
    private StopwatchControl stopwatchControl;
    private long totalTime = 0;
    private List<StopwatchCardEntity> activeCards = new ArrayList<>();
    private Handler uiHandler;

    public StopwatchUsecase() {
        uiHandler = new Handler(Looper.getMainLooper());
        stopwatchControl = new StopwatchControl();
        stopwatch = new Timer();
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        fireStartStopwatch();
    }

    @Override
    public void handleUiFeedback(String uri) {
        String action = uri.split("://")[1];
        switch (action) {
            case "stop":
                stopwatchControl.stop();
                break;
        }
    }

    @Override
    public String getAlias() {
        return "stopwatch";
    }

    private void fireStartStopwatch() {
        StopwatchCardEntity cardPayload = new StopwatchCardEntity();
        activeCards.add(cardPayload);
        render(cardPayload);
        if (stopwatchTask == null) {
            stopwatchTask = new TimerTask() {
                @Override
                public void run() {
                    totalTime = totalTime + UPDATE_INTERVAL;
                    updateCounter(totalTime);
                }
            };
            stopwatchControl.start();
            updateState(StopwatchCardEntity.StopwatchState.START);
        } else {
            cardPayload.setState(StopwatchCardEntity.StopwatchState.START);
            playAndRenderText("计时器已开启");
        }
    }

    private void updateCounter(final long totalTime) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (StopwatchCardEntity payload:activeCards) {
                    payload.setTotalTime(totalTime);
                }
            }
        });
    }

    private void updateState(final StopwatchCardEntity.StopwatchState state) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (StopwatchCardEntity payload:activeCards) {
                    payload.setState(state);
                    if (state == StopwatchCardEntity.StopwatchState.STOP) {

                    }
                }
            }
        });
    }

    private class StopwatchControl {

        public void start() {
//            String cardUid = UUID.randomUUID().toString();
//            cardUids.add(cardUid);
//            StopwatchCardEntity cardData = new StopwatchCardEntity();
//            cardData.setCardAction(CardAction.NEW);
//            cardData.setTotalTime(0);
//            cardData.setUid(cardUid);
//            mCallback.onRenderInfo(cardData);

//            updateCounter(totalTime);
            stopwatch.schedule(stopwatchTask, UPDATE_INTERVAL, UPDATE_INTERVAL);

        }

        public void stop() {
            stopwatchTask.cancel();
            updateState(StopwatchCardEntity.StopwatchState.STOP);

//            Iterator<String> iter = cardUids.iterator();
//            while (iter.hasNext()) {
//                String uid = iter.next();
//                StopwatchCardEntity cardData = new StopwatchCardEntity();
//                cardData.setCardAction(CardAction.DELETE);
//                cardData.setTotalTime(0);
//                cardData.setUid(uid);
////                mCallback.onRenderInfo(cardData);
//                iter.remove();
//
//            }
        }

        public void pause() {
            stop();
        }

        public void resume() {

        }
    }
}
