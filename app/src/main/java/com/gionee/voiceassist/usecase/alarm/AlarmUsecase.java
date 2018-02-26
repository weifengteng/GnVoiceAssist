package com.gionee.voiceassist.usecase.alarm;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.coreservice.datamodel.AlarmDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.datamodel.card.AlarmCardEntity;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;

import java.util.ArrayList;

/**
 * Created by liyingheng on 10/17/17.
 */

public class AlarmUsecase extends BaseUsecase {

    public static final String TAG = AlarmUsecase.class.getSimpleName();
    public Context mAppCtx;

    public AlarmUsecase() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        AlarmDirectiveEntity alarmPayload = (AlarmDirectiveEntity) payload;
//        setAlarm(alarmPayload.getHour(), alarmPayload.getMinute(), alarmPayload.getRepeatDays(), "");

        AlarmCardEntity alarmCardEntity = generateFromDirectiveEntity(alarmPayload);
        render(alarmCardEntity);
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    public void setAlarm(int hour, int minute, ArrayList<Integer> triggerDays, String message) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, false)
                .putExtra(AlarmClock.EXTRA_HOUR,hour)
                .putExtra(AlarmClock.EXTRA_MINUTES,minute)
                .putExtra(AlarmClock.EXTRA_DAYS,triggerDays)
                .putExtra(AlarmClock.EXTRA_MESSAGE,message);

        startIntent(intent);
    }

    public void setTimer(long length) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_LENGTH,length)
                .putExtra(AlarmClock.EXTRA_SKIP_UI,false);

        startIntent(intent);
    }

    private void startIntent(Intent intent) {
        if (intent.resolveActivity(mAppCtx.getPackageManager()) != null) {
            mAppCtx.startActivity(intent);
        }
    }

    @Override
    public String getAlias() {
        return "alarm";
    }

    private AlarmCardEntity generateFromDirectiveEntity(AlarmDirectiveEntity alarmDirectiveEntity) {
        AlarmCardEntity alarmCardEntity = new AlarmCardEntity();
        alarmCardEntity.setHour(alarmDirectiveEntity.getHour())
                .setMinute(alarmDirectiveEntity.getMinute())
                .setRepeatDay(alarmDirectiveEntity.getRepeatDays());
        return alarmCardEntity;
    }
}
