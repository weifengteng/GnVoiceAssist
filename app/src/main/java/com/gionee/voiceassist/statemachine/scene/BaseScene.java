package com.gionee.voiceassist.statemachine.scene;

import android.text.TextUtils;

/**
 * Created by twf on 2017/9/1.
 */

public class BaseScene implements IScene {
    public String[] targetConfirmCmdArray;
    public String[] targetCancelCmdArray;

    @Override
    public boolean isMatch(String userCmd) {
        return (isConfirm(userCmd) || isCancel(userCmd));
    }

    @Override
    public boolean isConfirm(String userCmd) {
        for(String targetConfirmCmd : targetConfirmCmdArray) {
            if(TextUtils.equals(userCmd, targetConfirmCmd)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCancel(String userCmd) {
        for(String targetCancelCmd : targetCancelCmdArray) {
            if(TextUtils.equals(userCmd, targetCancelCmd)) {
                return true;
            }
        }
        return false;
    }
}
