package com.gionee.gnvoiceassist.tts;


import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;

/**
 * Created by twf on 2017/8/26.
 */

public class TtsEventListener implements VoiceOutputDeviceModule.IVoiceOutputListener {
    public static final String TAG = TtsEventListener.class.getSimpleName();

//    @Override
//    public void onTtsStart() {
//        TtsManager.getInstance().setPlayingState(true);
//        TtsCallback listener = TtsManager.getInstance().getSpeakTxtCallbackListener();
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
//        TtsManager.getInstance().setPlayingState(false);
//        TtsCallback listener = TtsManager.getInstance().getSpeakTxtCallbackListener();
//        if(listener != null) {
//            listener.onSpeakFinish(TtsManager.getInstance().getCurrUtterId());
//        } else {
//            // TODO:
//        }
//    }
//
//    @Override
//    public void onTtsError(String errMsg) {
//        TtsManager.getInstance().setPlayingState(false);
//        TtsCallback listener = TtsManager.getInstance().getSpeakTxtCallbackListener();
//        if(listener != null) {
//            listener.onSpeakError(TtsManager.TtsResultCode.ERROR,errMsg);
//        } else {
//            // TODO:
//        }
//    }

    @Override
    public void onVoiceOutputStarted() {
        TtsManager.getInstance().setPlayingState(true);
    }

    @Override
    public void onVoiceOutputFinished() {
        TtsManager.getInstance().setPlayingState(false);
        TtsCallback listener = TtsManager.getInstance().getSpeakTxtCallbackListener();
        if(listener != null) {
            listener.onSpeakFinish(TtsManager.getInstance().getCurrUtterId());
        } else {
            // TODO:
        }
    }

    //TODO Deprecated. Offline TTS should migrate to newer SDK API
    @Deprecated
    public void onTtsOutputStarted() {
        TtsManager.getInstance().setPlayingState(true);
    }

    //TODO Deprecated. Offline TTS should migrate to newer SDK API
    @Deprecated
    public void onTtsOutputFinished() {
        TtsManager.getInstance().setPlayingState(false);
        TtsCallback listener = TtsManager.getInstance().getSpeakTxtCallbackListener();
        if(listener != null) {
            listener.onSpeakFinish(TtsManager.getInstance().getCurrUtterId());
        } else {
            // TODO:
        }
    }
}
