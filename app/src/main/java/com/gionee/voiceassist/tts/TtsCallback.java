package com.gionee.voiceassist.tts;

/**
 * Created by twf on 2017/8/26.
 */

public interface TtsCallback {

    void onSpeakStart();

    void onSpeakFinish(String utterId);

    void onSpeakError(TtsManager.TtsResultCode ttsResultCode, String s);
}
