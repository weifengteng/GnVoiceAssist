package com.gionee.voiceassist.basefunction.alarm;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by liyingheng on 10/17/17.
 */

public interface IAlarmPresenter {

    /**
     * 设置闹钟
     * @param hour 时
     * @param minute 分
     * @param triggerDays 每周触发时间（Calendar周几常量）
     * @param message 闹铃名称
     */
    void setAlarm(int hour, int minute, ArrayList<Integer> triggerDays, String message);

    /**
     * 设置定时器
     * @param length 定时器时间长度
     */
    void setTimer(long length);


}
