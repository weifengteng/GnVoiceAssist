package com.gionee.voiceassist.statemachine;

import com.gionee.voiceassist.statemachine.scene.BaseScene;

/**
 * Created by twf on 2017/8/31.
 */

public class SelectScene  extends BaseScene{

    public SelectScene() {
        targetConfirmCmdArray = new String[] {"确定"};
        targetCancelCmdArray = new String[] {"取消"};
    }
}
