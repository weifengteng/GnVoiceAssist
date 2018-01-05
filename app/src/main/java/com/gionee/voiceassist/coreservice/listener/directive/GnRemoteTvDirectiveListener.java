package com.gionee.voiceassist.coreservice.listener.directive;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteTvDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.tvlive.TvLiveDeviceModule;
import com.gionee.voiceassist.util.Utils;

import java.util.List;

/**
 * Created by twf on 2017/8/14.
 */

public class GnRemoteTvDirectiveListener extends BaseDirectiveListener implements TvLiveDeviceModule.ITvLiveDirectiveListener {
    public static final String TAG = GnRemoteTvDirectiveListener.class.getSimpleName();
    String tvCmd;

    public GnRemoteTvDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }


    @Override
    public void onTvLiveDirectiveReceived(Directive directive) {
        String rawMsg = directive.rawMessage;
        tvCmd = Utils.getCustomDirectiveCmdFromJson(rawMsg);
//        LogUtil.d(TAG, "tvCmd: " + tvCmd + " asrResult= " + iBaseFunction.getScreenRender().getAsrResult());

        GnRemoteTvDirectiveEntity msg = new GnRemoteTvDirectiveEntity();
        msg.setTvCommand(tvCmd);
        sendDirective(msg);
//        if(iBaseFunction != null) {
//            iBaseFunction.getKookongOperator().executeVoiceCmd(tvCmd);
//        }

//        KookongExecuteVoiceCmdService executeVoiceCmdService = new KookongExecuteVoiceCmdService(tvCmd);
//        executeVoiceCmdService.execute();
    }

}
