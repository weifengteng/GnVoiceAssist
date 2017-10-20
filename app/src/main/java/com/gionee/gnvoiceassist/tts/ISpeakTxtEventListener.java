package com.gionee.gnvoiceassist.tts;

/**
 * Created by twf on 2017/8/26.
 */

public interface ISpeakTxtEventListener {

    void onSpeakStart();

    void onSpeakFinish(String utterId);

    void onSpeakError(TxtSpeakManager.TxtSpeakResult txtSpeakResult, String s);
}
