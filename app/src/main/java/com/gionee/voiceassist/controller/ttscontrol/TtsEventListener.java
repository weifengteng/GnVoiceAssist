package com.gionee.voiceassist.controller.ttscontrol;


import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;

/**
 * Created by twf on 2017/8/26.
 */

public class TtsEventListener implements VoiceOutputDeviceModule.IVoiceOutputListener {
    public static final String TAG = TtsEventListener.class.getSimpleName();

//    @Override
//    public void onTtsStart() {
//        TtsController.getInstance().setPlayingState(true);
//        TtsCallback listener = TtsController.getInstance().getSpeakTxtCallbackListener();
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
//        TtsController.getInstance().setPlayingState(false);
//        TtsCallback listener = TtsController.getInstance().getSpeakTxtCallbackListener();
//        if(listener != null) {
//            listener.onSpeakFinish(TtsController.getInstance().getCurrUtterId());
//        } else {
//            // TODO:
//        }
//    }
//
//    @Override
//    public void onTtsError(String errMsg) {
//        TtsController.getInstance().setPlayingState(false);
//        TtsCallback listener = TtsController.getInstance().getSpeakTxtCallbackListener();
//        if(listener != null) {
//            listener.onSpeakError(TtsController.TtsResultCode.ERROR,errMsg);
//        } else {
//            // TODO:
//        }
//    }

    @Override
    public void onVoiceOutputStarted() {
        TtsController.getInstance().setPlayingState(true);
    }

    @Override
    public void onVoiceOutputFinished() {
        TtsController.getInstance().setPlayingState(false);
        TtsCallback listener = TtsController.getInstance().getSpeakTxtCallbackListener();
        if(listener != null) {
            listener.onSpeakFinish(TtsController.getInstance().getCurrUtterId());
        } else {
            // TODO:
        }
    }

    //TODO Deprecated. Offline TTS should migrate to newer SDK API
    @Deprecated
    public void onTtsOutputStarted() {
        TtsController.getInstance().setPlayingState(true);
    }

    //TODO Deprecated. Offline TTS should migrate to newer SDK API
    @Deprecated
    public void onTtsOutputFinished() {
        TtsController.getInstance().setPlayingState(false);
        TtsCallback listener = TtsController.getInstance().getSpeakTxtCallbackListener();
        if(listener != null) {
            listener.onSpeakFinish(TtsController.getInstance().getCurrUtterId());
        } else {
            // TODO:
        }
    }
}
