package com.gionee.gnvoiceassist.statemachine;

import com.gionee.gnvoiceassist.statemachine.scene.BaseScene;

/**
 * Created by twf on 2017/8/31.
 */

public class DownloadConfirmScene extends BaseScene{

    public DownloadConfirmScene() {
        targetConfirmCmdArray = new String[] {"确定", "下载"};
        targetCancelCmdArray = new String[] {"取消"};
    }
}
