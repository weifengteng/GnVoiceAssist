package com.gionee.voiceassist.coreservice.listener.directive;

import com.gionee.voiceassist.coreservice.datamodel.ReminderCreateDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderManageDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.reminder.ReminderDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.reminder.message.ManageAlertPayload;
import com.gionee.voiceassist.coreservice.sdk.module.reminder.message.SetAlertPayload;

import java.util.List;

/**
 * Created by liyingheng on 1/5/18.
 */

public class ReminderDirectiveListener extends BaseDirectiveListener implements ReminderDeviceModule.IReminderDirectiveListener{
    public ReminderDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onSetAlertDirectiveReceived(SetAlertPayload payload) {
        ReminderCreateDirectiveEntity msg = new ReminderCreateDirectiveEntity();
        ReminderDirectiveEntity.ReminderRepeat repeat = null;
        switch (payload.getRepeat().getType()) {
            case "WEEKLY":
                repeat = ReminderDirectiveEntity.ReminderRepeat.createWeeklyRepeat(payload.getRepeat().getWeekly());
                break;
            case "MONTHLY":
                repeat = ReminderDirectiveEntity.ReminderRepeat.createMonthlyRepeat(payload.getRepeat().getMonthly());
                break;
            case "YEARLY":
                repeat = ReminderDirectiveEntity.ReminderRepeat.createYearlyRepeat(payload.getRepeat().getYearly());
                break;
            case "DAILY":
                break;
            default:
                repeat = ReminderDirectiveEntity.ReminderRepeat.createNoRepeat();
                break;
        }
        msg.setCreateReminder(payload.getScheduledTime(), payload.getContent(), repeat);
        sendDirective(msg);
    }

    @Override
    public void onManageAlertDirectiveReceived(ManageAlertPayload payload) {
        ReminderManageDirectiveEntity msg = new ReminderManageDirectiveEntity();
        ReminderDirectiveEntity.ReminderRepeat repeat = null;
        switch (payload.getRepeat().getType()) {
            case "WEEKLY":
                repeat = ReminderDirectiveEntity.ReminderRepeat.createWeeklyRepeat(payload.getRepeat().getWeekly());
                break;
            case "MONTHLY":
                repeat = ReminderDirectiveEntity.ReminderRepeat.createMonthlyRepeat(payload.getRepeat().getMonthly());
                break;
            case "YEARLY":
                repeat = ReminderDirectiveEntity.ReminderRepeat.createYearlyRepeat(payload.getRepeat().getYearly());
                break;
            case "DAILY":
                break;
            default:
                repeat = ReminderDirectiveEntity.ReminderRepeat.createNoRepeat();
                break;
        }
        switch (payload.getType()) {
            case "SELECT":
                msg.setSearchReminder(payload.getApm(), payload.getDay(), payload.getHour(), payload.getMinute(), payload.getMonth(), repeat);
                break;
            case "DELETE":
                msg.setDeleteReminder(payload.getApm(), payload.getDay(), payload.getHour(), payload.getMinute(), payload.getMonth(), repeat);
                break;
        }
        sendDirective(msg);
    }
}
