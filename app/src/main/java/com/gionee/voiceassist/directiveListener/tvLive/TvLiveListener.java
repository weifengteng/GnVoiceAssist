package com.gionee.voiceassist.directiveListener.tvLive;

import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.sdk.module.tvlive.TvLiveDeviceModule;
import com.gionee.voiceassist.util.Utils;

/**
 * Created by twf on 2017/8/14.
 */

public class TvLiveListener extends BaseDirectiveListener implements TvLiveDeviceModule.ITvLiveDirectiveListener {
    public static final String TAG = TvLiveListener.class.getSimpleName();
    String tvCmd;

    public TvLiveListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
    }

    @Override
    public void onTvLiveDirectiveReceived(Directive directive) {
        String rawMsg = directive.rawMessage;
        tvCmd = Utils.getCustomDirectiveCmdFromJson(rawMsg);
        LogUtil.d(TAG, "tvCmd: " + tvCmd + " asrResult= " + iBaseFunction.getScreenRender().getAsrResult());

        if(iBaseFunction != null) {
            iBaseFunction.getKookongOperator().executeVoiceCmd(tvCmd);
        }

//        KookongExecuteVoiceCmdService executeVoiceCmdService = new KookongExecuteVoiceCmdService(tvCmd);
//        executeVoiceCmdService.execute();
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {

    }
}
