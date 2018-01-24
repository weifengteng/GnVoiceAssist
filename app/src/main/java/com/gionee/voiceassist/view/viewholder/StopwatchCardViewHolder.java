package com.gionee.voiceassist.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.controller.appcontrol.DataController;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.StopwatchCardEntity;
import com.gionee.voiceassist.util.LogUtil;

/**
 * 秒表ViewHolder
 */

public class StopwatchCardViewHolder extends BaseViewHolder {

    private static final String TAG = StopwatchCardViewHolder.class.getSimpleName();

    private TextView tvTimeIndicator;
    private Button btnControl;
    private FrameLayout lytCancelledIndicator;

    private StopwatchCardEntity mPayload;
    private StopwatchCardEntity.StopwatchState mState = StopwatchCardEntity.StopwatchState.STOP;

    private StopwatchCardEntity.StopwatchObserver mObserver = new StopwatchCardEntity.StopwatchObserver() {

        @Override
        public void onStateChanged(StopwatchCardEntity.StopwatchState state) {
            mState = state;
            bindState(state);
        }

        @Override
        public void onDataChanged(long value) {
            tvTimeIndicator.setText(formatTime(value));
        }
    };

    public StopwatchCardViewHolder(View itemView) {
        super(itemView);
        tvTimeIndicator = (TextView) itemView.findViewById(R.id.tv_time_indicator);
        btnControl = (Button) itemView.findViewById(R.id.btn_stopwatch_control);
        lytCancelledIndicator = (FrameLayout) itemView.findViewById(R.id.lyt_cancel);
        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nextStateAction = "";
                switch (mState) {
                    case START:
                        nextStateAction = "stop";
                        break;
                    case PAUSE:
                        nextStateAction = "resume";
                        break;
                }
                DataController.getDataController().getScreenController().uiActionFeedback("stopwatch://" + nextStateAction);
            }
        });
    }

    @Override
    public void bind(CardEntity payload) {
        mPayload = (StopwatchCardEntity) payload;
        bindTotalTime(mPayload);
        bindState(mPayload.getState());
        registerStopwatchObserver(mPayload);
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        if (mPayload != null) {
            unregisterStopwatchObserver(mPayload);
            mPayload = null;
        }
    }

    public static StopwatchCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_stopwatchcard, parent, false);
        return new StopwatchCardViewHolder(itemView);
    }

    private void registerStopwatchObserver(StopwatchCardEntity payload) {
        if (payload == null) {
            LogUtil.e(TAG, "registerStopwatchObserver error. payload is null");
            return;
        }
        payload.addStopwatchObserver(mObserver);
    }

    private void unregisterStopwatchObserver(StopwatchCardEntity payload) {
        if (payload == null) {
            LogUtil.e(TAG, "unregisterStopwatchObserver error. payload is null");
            return;
        }
        payload.removeStopwatchObserver(mObserver);
    }

    private void bindTotalTime(StopwatchCardEntity payload) {
        if (payload == null) {
            LogUtil.e(TAG, "bindTotalTime error. payload is null");
            return;
        }
        String totalTimeStr = formatTime(payload.getTotalTime());
        tvTimeIndicator.setText(totalTimeStr);
    }

    private void bindState(StopwatchCardEntity.StopwatchState state) {
        String nextStateText = "";
        switch (state) {
            case START:
                nextStateText = "暂停";
                lytCancelledIndicator.setVisibility(View.GONE);
                break;
            case PAUSE:
                nextStateText = "恢复";
                lytCancelledIndicator.setVisibility(View.GONE);
                break;
            case STOP:
                nextStateText = "开始";
                lytCancelledIndicator.setVisibility(View.VISIBLE);
//                btnControl.setOnClickListener(null);
                break;
        }
        btnControl.setText(nextStateText);
    }

    private String formatTime(long timeInMills) {
        int hour = (int)(timeInMills / 1000 / 3600);
        int minute = (int)(((timeInMills / 1000) - hour * 3600) / 60);
        int second = (int)((timeInMills / 1000) - (hour * 3600 + 60 * minute));

        String timeStr = String.valueOf(hour) + ":"
                + String.valueOf(minute) + ":"
                + String.valueOf(second);

        return timeStr;

    }
}
