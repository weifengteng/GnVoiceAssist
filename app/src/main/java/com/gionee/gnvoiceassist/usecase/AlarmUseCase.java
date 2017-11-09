package com.gionee.gnvoiceassist.usecase;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.message.io.MetadataParser;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.AlarmMetadata;
import com.gionee.gnvoiceassist.usecase.annotation.Operation;

import java.util.ArrayList;

/**
 * Created by liyingheng on 11/9/17.
 */

public class AlarmUseCase extends UseCase {

    private static final String USECASE_ALIAS = "alarm";

    public static final String ACTION_REQUEST_ALARM = "request_alarm";
    public static final String ACTION_REQUEST_TIMER = "request_timer";
    public static final String SUBACTION_SET = "set";
    public static final String SUBACTION_CONFIRM = "confirm";
    public static final String SUBACTION_CANCEL = "cancel";

    private Context mAppCtx;

    public AlarmUseCase() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    @Override
    public void handleMessage(DirectiveResponseEntity message) {
        AlarmMetadata metadata = MetadataParser.toEntity(message.getMetadata(),AlarmMetadata.class);
        switch (message.getAction()) {
            case ACTION_REQUEST_ALARM:
                //闹钟
                requestAlarm(message.getSubAction(),metadata);
                break;
            case ACTION_REQUEST_TIMER:
                //倒计时器
                break;
        }
    }

    @Override
    public String getUseCaseName() {
        return USECASE_ALIAS;
    }

    private void requestAlarm(String subAction, AlarmMetadata metadata) {
        switch (subAction) {
            case SUBACTION_SET:
                //设置闹钟
                setAlarm(
                        metadata.getHour(),
                        metadata.getMinute(),
                        metadata.getTriggerDays(),
                        metadata.getMessage()
                );
                break;
            case SUBACTION_CANCEL:
                //取消闹钟
                break;
        }
    }

    private void requestTimer(String subAction, AlarmMetadata metadata) {
        switch (subAction) {
            case SUBACTION_SET:
                //设置计时器
                break;
            case SUBACTION_CANCEL:
                //取消计时器
                break;
        }
    }

    @Operation("set_alarm")
    public void setAlarm(int hour, int minute, ArrayList<Integer> triggerDays, String message) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR,hour)
                .putExtra(AlarmClock.EXTRA_MINUTES,minute)
                .putExtra(AlarmClock.EXTRA_DAYS,triggerDays)
                .putExtra(AlarmClock.EXTRA_MESSAGE,message);

        if (intent.resolveActivity(mAppCtx.getPackageManager()) != null) {
            mAppCtx.startActivity(intent);
        }
    }

    @Operation("set_timer")
    public void setTimer(long length) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_LENGTH,length)
                .putExtra(AlarmClock.EXTRA_SKIP_UI,false);

        if (intent.resolveActivity(mAppCtx.getPackageManager()) != null) {
            mAppCtx.startActivity(intent);
        }
    }
}
