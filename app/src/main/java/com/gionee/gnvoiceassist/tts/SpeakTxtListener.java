package com.gionee.gnvoiceassist.tts;

import com.baidu.duer.sdk.speak.SpeakInterface;

/**
 * Created by twf on 2017/8/26.
 */

public class SpeakTxtListener implements SpeakInterface.IspeakTxtListener {
    public static final String TAG = SpeakTxtListener.class.getSimpleName();

    @Override
    public void onSpeechStart() {
        TxtSpeakManager.getInstance().setPlayingState(true);
    }

    @Override
        public void onSpeechFinish() {
            TxtSpeakManager.getInstance().setPlayingState(false);
            ISpeakTxtEventListener listener = TxtSpeakManager.getInstance().getSpeakTxtCallbackListener();
            if(listener != null) {
                listener.onSpeakFinish(TxtSpeakManager.getInstance().getCurrUtterId());
            } else {
                // TODO:
            }
    }

    @Override
    public void onError(SpeakInterface.SpeakTxtResultCode speakTxtResultCode, String s) {
        TxtSpeakManager.getInstance().setPlayingState(false);
        ISpeakTxtEventListener listener = TxtSpeakManager.getInstance().getSpeakTxtCallbackListener();
        if(listener != null) {
            listener.onSpeakError(speakTxtResultCode, s);
        } else {
            // TODO:
        }
    }
}
