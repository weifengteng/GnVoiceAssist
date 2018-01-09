package com.gionee.voiceassist.coreservice.listener.state;


import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.coreservice.CoreService;

import java.util.List;

/**
 * Created by twf on 2017/8/26.
 */

public class TtsEventListener extends BaseStateListener implements VoiceOutputDeviceModule.IVoiceOutputListener {
    public static final String TAG = TtsEventListener.class.getSimpleName();

    public TtsEventListener(List<CoreService.StateCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onVoiceOutputStarted() {
        TtsController.getInstance().setPlayingState(true);
        for (CoreService.StateCallback callback:mStateCallbacks) {
            callback.onTtsStart();
        }
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
        for (CoreService.StateCallback callback:mStateCallbacks) {
            callback.onTtsStop();
        }
    }
}
