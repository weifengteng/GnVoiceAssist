package com.gionee.voiceassist.usecase;

import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.usecase.remind.RemindUsecase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;


/**
 * 提醒场景测试
 */

@RunWith(MockitoJUnitRunner.class)
public class ReminderUsecaseTest {

    @Mock RemindUsecase reminderUsecase;

    private ReminderDirectiveEntity createDirectivePayload(
            String time,
            String content,
            int[] monthlyRepeat,
            String[] weeklyRepeat) {
        ReminderDirectiveEntity payload = new ReminderDirectiveEntity();
        ReminderDirectiveEntity.ReminderRepeat repeat;
        if (!(monthlyRepeat.length == 0)) {
            List<Integer> monthlyRepeatList = new ArrayList<>();
            for (int month:monthlyRepeat) {
                monthlyRepeatList.add(month);
            }
            repeat = ReminderDirectiveEntity.ReminderRepeat.createMonthlyRepeat(monthlyRepeatList);
        } else if (!(weeklyRepeat.length == 0)) {
            repeat = ReminderDirectiveEntity.ReminderRepeat.createWeeklyRepeat(Arrays.asList(weeklyRepeat));
        } else {
            repeat = ReminderDirectiveEntity.ReminderRepeat.createNoRepeat();
        }
        payload.setCreateReminder(time, content, repeat);
        return payload;
    }
}
