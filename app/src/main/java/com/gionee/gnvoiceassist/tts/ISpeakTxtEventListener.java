package com.gionee.gnvoiceassist.tts;

import com.baidu.duer.sdk.speak.SpeakInterface;

/**
 * Created by twf on 2017/8/26.
 */

public interface ISpeakTxtEventListener {

    void onSpeakStart();

    void onSpeakFinish(String utterId);

    void onSpeakError(SpeakInterface.SpeakTxtResultCode speakTxtResultCode, String s);
}
