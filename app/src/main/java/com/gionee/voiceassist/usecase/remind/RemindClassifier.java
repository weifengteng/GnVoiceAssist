package com.gionee.voiceassist.usecase.remind;

import android.text.TextUtils;

import com.gionee.voiceassist.controller.appcontrol.DataController;
import com.gionee.voiceassist.coreservice.datamodel.ReminderCreateDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ReminderDirectiveEntity.ReminderRepeat.RepeatType;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 区分用户查询语句中，对应的是场景是闹钟还是日程提醒
 */

public class RemindClassifier {

    private final String TAG = RemindClassifier.class.getSimpleName();

    public ReminderType classify(ReminderCreateDirectiveEntity entity) {
        /**
         * 闹钟日程区分
         * 1. 循环性要求
         *      a. 循环周期为【每月】、【每年】，且语义不包含【闹钟】、【起床】、【叫醒我】、【叫我】，输出type=日程
         *      b. 循环周期为【每周几】、【每个工作日/周末】、【每周周一到周三】、【每天xx时刻】，输出type=闹钟
         *      c. 每种循环周期+语义为【提醒】，输出type=日程
         * 2. 非循环性需求
         *      a. 语义带有【提醒】字眼，输出type=日程
         *      b. 当语义有【起床】or【叫醒我】or【叫我】or【闹钟】，输出type=闹钟
         *      c. 语义带有【日程】字眼，输出type=日程
         *      d. 语义带有【提醒】字眼，输出type=日程
         */

        ReminderDirectiveEntity.ReminderRepeat repeat = entity.getRepeat();
        String userAsr = DataController.getDataController().getConversationController().getLastUserAsr();
        String content = userAsr + " | " + entity.getContent();
        LogUtil.d(TAG, "提醒内容：" + content);
        if (repeat.type != RepeatType.None) {
            if (isContainStr("提醒")) {
                LogUtil.d(TAG, "每种循环周期+语义为【提醒】，输出type=日程");
                return ReminderType.SCHEDULE;
            } else if ((repeat.type == RepeatType.MONTHLY || repeat.type == RepeatType.YEARLY)
                    && !isContainStr("闹钟", "起床", "叫醒我", "叫我")) {
                LogUtil.d(TAG, "循环周期为【每月】、【每年】，且语义不包含【闹钟】、【起床】、【叫醒我】、【叫我】，输出type=日程");
                return ReminderType.SCHEDULE;
            } else {
                LogUtil.d(TAG, "循环周期为【每周几】、【每个工作日/周末】、【每周周一到周三】、【每天xx时刻】，输出type=闹钟");
                return ReminderType.ALARM;
            }
        } else {
            if (isContainStr(content, "起床","叫醒我","叫我","闹钟")) {
                LogUtil.d(TAG, "当语义有【起床】or【叫醒我】or【叫我】or【闹钟】，输出type=闹钟");
                return ReminderType.ALARM;
            } else if (isContainStr(content, "提醒", "日程")) {
                LogUtil.d(TAG, "语义带有【提醒】【日程】字眼，输出type=日程");
                return ReminderType.SCHEDULE;
            } else {
                LogUtil.d(TAG, "默认提醒状态，输出type=日程");
                return ReminderType.SCHEDULE;
            }
        }
    }

    private String[] containsStr(String content, String... keywords) {
        List<String> containKeywords = new ArrayList<>();
        for (String keyword:keywords) {
            if (content.contains(keyword))
                containKeywords.add(keyword);
        }
        return containKeywords.toArray(new String[containKeywords.size()]);
    }

    private boolean isContainStr(String content, String... keywords) {
        String[] containsWords = containsStr(content, keywords);
        return containsWords.length != 0;
    }


}
