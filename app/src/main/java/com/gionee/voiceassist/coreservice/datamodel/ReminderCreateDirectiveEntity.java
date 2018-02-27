package com.gionee.voiceassist.coreservice.datamodel;

/**
 * Created by liyingheng on 2/26/18.
 */

public class ReminderCreateDirectiveEntity extends ReminderDirectiveEntity {

    private String time;

    public String getTime() {
        return time;
    }

    public void setCreateReminder(String time, String content, ReminderRepeat repeat) {
        this.time = time;
        this.content = content;
        this.repeat = repeat;
        setAction(ReminderAction.CREATE_REMINDER);
    }

    @Override
    public String toString() {
        return "ReminderCreateDirectiveEntity{" +
                "time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", repeat=" + repeat +
                ", action=" + action +
                '}';
    }
}
