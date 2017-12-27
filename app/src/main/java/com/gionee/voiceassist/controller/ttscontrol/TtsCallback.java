package com.gionee.voiceassist.controller.ttscontrol;

/**
 * Created by twf on 2017/8/26.
 */

public interface TtsCallback {

    void onSpeakStart();

    void onSpeakFinish(String utterId);

    void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s);
}
