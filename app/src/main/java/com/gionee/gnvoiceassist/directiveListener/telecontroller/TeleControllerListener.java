package com.gionee.gnvoiceassist.directiveListener.telecontroller;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.telecontroller.TeleControllerDeviceModule;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.util.Utils;

/**
 * Created by twf on 2017/8/14.
 */

public class TeleControllerListener extends BaseDirectiveListener implements TeleControllerDeviceModule.ITeleControllerDirectiveListener{
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
    public void onTeleControllerOnlineDirectiveReceived(Directive directive) {
        String msg = directive.rawMessage;

        LogUtil.d("DCSF", "TeleControllerListener: " + msg);
        customCmd = Utils.getCustomDirectiveCmdFromJson(msg);
        customCmd = customCmd.replace("null", "");
        LogUtil.d("DCSF", "TeleControllerListener customCmd: " + customCmd);
        if(TextUtils.equals(customCmd, "打开手电筒") ||
                TextUtils.equals(customCmd, "关闭手电筒") ||
                TextUtils.equals(customCmd, "截屏") ||
                TextUtils.equals(customCmd, "截一下屏")
                ) {
            if(iBaseFunction != null) {
                iBaseFunction.getDeviceControlOperator().operateOfflineDeviceControlCmd(customCmd);
            }
        } else if(TextUtils.equals(customCmd, "现在几点了") ||
                TextUtils.equals(customCmd, "现在几点") ||
                TextUtils.equals(customCmd, "几点了") ||
                TextUtils.equals(customCmd, "查一下现在几点了") ||
                TextUtils.equals(customCmd, "查一下几点了")) {
            if(iBaseFunction != null) {
                iBaseFunction.getTimerQuery().queryNowTime();
            }
        } else {
                if(iBaseFunction != null) {
                    iBaseFunction.getKookongOperator().executeVoiceCmd(customCmd);
                }
        }
//        KookongExecuteVoiceCmdService executeVoiceCmdService = new KookongExecuteVoiceCmdService(customCmd);
//        executeVoiceCmdService.execute();
    }
}
