package com.gionee.gnvoiceassist.tts;


import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;
import com.baidu.duer.dcs.offline.tts.ITts;

/**
 * Created by twf on 2017/8/26.
 */

public class SpeakTxtListener implements VoiceOutputDeviceModule.IVoiceOutputListener {
    public static final String TAG = SpeakTxtListener.class.getSimpleName();

//    @Override
//    public void onTtsStart() {
//        TxtSpeakManager.getInstance().setPlayingState(true);
//        ISpeakTxtEventListener listener = TxtSpeakManager.getInstance().getSpeakTxtCallbackListener();
//        listener.onSpeakStart();
//    }
//
//    @Override
//    public void onTtsProgressChanged(int i) {
//
//    }
//
//    @Override
//    public void onTtsFinish() {
//        TxtSpeakManager.getInstance().setPlayingState(false);
//        ISpeakTxtEventListener listener = TxtSpeakManager.getInstance().getSpeakTxtCallbackListener();
//        if(listener != null) {
//            listener.onSpeakFinish(TxtSpeakManager.getInstance().getCurrUtterId());
//        } else {
//            // TODO:
//        }
//    }
//
//    @Override
//    public void onTtsError(String errMsg) {
//        TxtSpeakManager.getInstance().setPlayingState(false);
//        ISpeakTxtEventListener listener = TxtSpeakManager.getInstance().getSpeakTxtCallbackListener();
//        if(listener != null) {
//            listener.onSpeakError(TxtSpeakManager.TxtSpeakResult.RESULT_CODE_ERROR,errMsg);
//        } else {
//            // TODO:
//        }
//    }

    @Override
    public void onVoiceOutputStarted() {
        TxtSpeakManager.getInstance().setPlayingState(true);
    }

    @Override
    public void onVoiceOutputFinished() {
        TxtSpeakManager.getInstance().setPlayingState(false);
        ISpeakTxtEventListener listener = TxtSpeakManager.getInstance().getSpeakTxtCallbackListener();
        if(listener != null) {
            listener.onSpeakFinish(TxtSpeakManager.getInstance().getCurrUtterId());
        } else {
            // TODO:
        }
    }

    //TODO Deprecated. Offline TTS should migrate to newer SDK API
    @Deprecated
    public void onTtsOutputStarted() {
        TxtSpeakManager.getInstance().setPlayingState(true);
    }

    //TODO Deprecated. Offline TTS should migrate to newer SDK API
    @Deprecated
    public void onTtsOutputFinished() {
        TxtSpeakManager.getInstance().setPlayingState(false);
        ISpeakTxtEventListener listener = TxtSpeakManager.getInstance().getSpeakTxtCallbackListener();
        if(listener != null) {
            listener.onSpeakFinish(TxtSpeakManager.getInstance().getCurrUtterId());
        } else {
            // TODO:
        }
    }
}
