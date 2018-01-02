package com.gionee.voiceassist.directiveListener.telecontroller;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.coreservice.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;

/**
 * Created by twf on 2017/8/14.
 */

public class TeleControllerListener extends BaseDirectiveListener implements TeleControllerDeviceModule.ITeleControllerDirectiveListener{
    private static final String TAG = TeleControllerListener.class.getSimpleName();
    private String customCmd;

    public TeleControllerListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {

    }

    @Override
    public void onTeleControllerDirectiveReceived(Directive directive) {
        // KooKongCmd
        String msg = directive.rawMessage;
        customCmd = Utils.getCustomDirectiveCmdFromJson(msg);
        customCmd = customCmd.replace("null", "");
        LogUtil.d(TAG, "TeleControllerListener customCmd: " + customCmd);

        if(iBaseFunction != null) {
            iBaseFunction.getKookongOperator().executeVoiceCmd(customCmd);
        }
    }
}
