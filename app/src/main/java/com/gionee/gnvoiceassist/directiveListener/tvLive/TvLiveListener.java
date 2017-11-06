package com.gionee.gnvoiceassist.directiveListener.tvLive;

import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.sdk.module.tvlive.TvLiveDeviceModule;
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;
import com.gionee.gnvoiceassist.util.Utils;

/**
 * Created by twf on 2017/8/14.
 */

public class TvLiveListener extends BaseDirectiveListener implements TvLiveDeviceModule.ITvLiveDirectiveListener {
    public static final String TAG = TvLiveListener.class.getSimpleName();
    String tvCmd;

    public TvLiveListener(IDirectiveListenerCallback callback) {
        super(callback);
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
