package com.gionee.voiceassist.coreservice.listener.directive;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.telecontroller.TeleControllerDeviceModule;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;

import java.util.List;

/**
 * Created by twf on 2017/8/14.
 */

public class GnRemoteListener extends BaseDirectiveListener implements TeleControllerDeviceModule.ITeleControllerDirectiveListener{
    private static final String TAG = GnRemoteListener.class.getSimpleName();
    private String customCmd;

    public GnRemoteListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }


    @Override
    public void onTeleControllerDirectiveReceived(Directive directive) {
        // KooKongCmd
        String msg = directive.rawMessage;
        customCmd = Utils.getCustomDirectiveCmdFromJson(msg);
        customCmd = customCmd.replace("null", "");
        LogUtil.d(TAG, "GnRemoteListener customCmd: " + customCmd);

        GnRemoteDirectiveEntity parsemsg = new GnRemoteDirectiveEntity();
        parsemsg.setCommand(customCmd);
        sendDirective(parsemsg);

//        if(iBaseFunction != null) {
//            iBaseFunction.getKookongOperator().executeVoiceCmd(customCmd);
//        }
    }
}
