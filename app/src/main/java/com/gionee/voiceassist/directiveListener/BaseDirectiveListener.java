package com.gionee.voiceassist.directiveListener;

import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.customuserinteraction.ICUIDirectiveReceivedInterface;
import com.gionee.voiceassist.tts.TtsCallback;
import com.gionee.voiceassist.tts.TtsManager;

/**
 * Created by twf on 2017/8/14.
 */

public abstract class BaseDirectiveListener
        implements TtsCallback, ICUIDirectiveReceivedInterface{
    protected IBaseFunction iBaseFunction;

    public BaseDirectiveListener(IBaseFunction iBaseFunction) {
        if(iBaseFunction == null) {
            throw new IllegalArgumentException("iBaseFunction cannot be null!");
        }
        this.iBaseFunction = iBaseFunction;
    }

    @Override
    public void onSpeakStart() {

    }

    @Override
    public void onSpeakFinish(String utterId) {

    }

    @Override
    public void onSpeakError(TtsManager.TtsResultCode ttsResultCode, String s) {

    }

    public void handleCUInteractionUnknownUtterance(String id) {

    }

    public void handleCUInteractionTargetUrl(String id, String url) {

    }

    /*public abstract void procOtherDirectiveCalling();

    public abstract boolean procRecoError(int error);

    public abstract void stopFocus();*/

    protected void playTTS(String ttsText, String utterId, TtsCallback listener) {
        TtsManager.getInstance().playTTS(ttsText, utterId, listener);
    }

    protected void playTTS(String ttsText, String utterId, TtsCallback listener, boolean showPlayTextInScreen) {
        TtsManager.getInstance().playTTS(ttsText, utterId, listener);
        if(showPlayTextInScreen) {
            iBaseFunction.getScreenRender().renderAnswerInScreen(ttsText);
        }
    }

    protected void playTTS(String ttsText) {
        TtsManager.getInstance().playTTS(ttsText);
    }

    protected void playTTS(String ttsText, boolean showPlayTextInScreen) {
        TtsManager.getInstance().playTTS(ttsText);
        if(showPlayTextInScreen) {
            iBaseFunction.getScreenRender().renderAnswerInScreen(ttsText);
        }
    }

    /**
     * 释放资源
     */
    public abstract void onDestroy();
}
