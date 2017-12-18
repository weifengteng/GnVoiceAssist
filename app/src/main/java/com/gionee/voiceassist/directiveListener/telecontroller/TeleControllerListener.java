package com.gionee.voiceassist.directiveListener.telecontroller;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.voiceassist.sdk.module.telecontroller.message.AskTimePayload;
import com.gionee.voiceassist.sdk.module.telecontroller.message.OperateFlashLightPayload;
import com.gionee.voiceassist.sdk.module.telecontroller.message.PrintScreenPayload;
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

    @Override
    public void onAskingTime(AskTimePayload askTimePayload) {
        if(iBaseFunction != null) {
            iBaseFunction.getTimerQuery().queryNowTime();
        }
    }

    @Override
    public void onPrintScreen(PrintScreenPayload printScreenPayload) {
        if(iBaseFunction != null) {
            iBaseFunction.getDeviceControlOperator().operateOfflineDeviceControlCmd(printScreenPayload.getQuery());
        }
    }

    @Override
    public void onOperateFlashLight(OperateFlashLightPayload operateFlashLightPayload) {
        if(iBaseFunction != null) {
            iBaseFunction.getDeviceControlOperator().operateOfflineDeviceControlCmd(operateFlashLightPayload.getQuery());
        }
    }
}
