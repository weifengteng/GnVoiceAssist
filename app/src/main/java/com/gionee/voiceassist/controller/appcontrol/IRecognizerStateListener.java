package com.gionee.voiceassist.controller.appcontrol;

import com.gionee.voiceassist.util.RecognizerState;

/**
 * Created by liyingheng on 1/9/18.
 */

public interface IRecognizerStateListener {
    void onRecordStart();
    void onRecordStop();
    void onTtsStart();
    void onTtsStop();
    void onInitStart();
    void onInitFinished();
    void onInitFailed();
    void onStateChanged(RecognizerState state);
}
