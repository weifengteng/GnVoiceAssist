package com.gionee.gnvoiceassist.basefunction.kookong;

import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.util.kookong.TeleControlPresenter;

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
        TeleControlPresenter executeVoiceCmdService = new TeleControlPresenter(baseFunction, cmd);
        executeVoiceCmdService.execute();
    }
}
