package com.gionee.voiceassist.basefunction.kookong;

import com.gionee.voiceassist.basefunction.BasePresenter;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.util.kookong.TeleControlPresenter;

/**
 * Created by twf on 2017/8/24.
 */

public class KookongOperator extends BasePresenter {

    public KookongOperator(IBaseFunction baseFunction) {
        super(baseFunction);
    }

    @Override
    public void onDestroy() {
        // TODO:
    }

    public void executeVoiceCmd(String cmd) {
        TeleControlPresenter executeVoiceCmdService = new TeleControlPresenter(cmd);
        executeVoiceCmdService.execute();
    }
}
