package com.gionee.voiceassist.basefunction;

import com.gionee.voiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.voiceassist.tts.TtsCallback;
import com.gionee.voiceassist.tts.TtsManager;

/**
 * Created by tengweifeng on 9/27/17.
 */

public abstract class BasePresenter implements TtsCallback {
    protected IBaseFunction baseFunction;
    protected ScreenRender screenRender;

    public BasePresenter(IBaseFunction baseFunction) {
        if(baseFunction == null) {
            throw new IllegalArgumentException("baseFunction cannot be null!");
        }
        this.baseFunction = baseFunction;
        screenRender = baseFunction.getScreenRender();
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

    protected void playAndRenderText(String ttsText, String utterId, TtsCallback listener) {
        TtsManager.getInstance().playTTS(ttsText, utterId, listener);
    }

    protected void playAndRenderText(String ttsText, String utterId, TtsCallback listener, boolean showPlayTextInScreen) {
        TtsManager.getInstance().playTTS(ttsText, utterId, listener);
        if(showPlayTextInScreen) {
            screenRender.renderAnswerInScreen(ttsText);
        }
    }

    protected void playAndRenderText(String ttsText) {
        TtsManager.getInstance().playTTS(ttsText);
    }

    protected void playAndRenderText(String ttsText, boolean showPlayTextInScreen) {
        TtsManager.getInstance().playTTS(ttsText);
        if(showPlayTextInScreen) {
            screenRender.renderAnswerInScreen(ttsText);
        }
    }

    /**
     * 释放资源
     */
    public abstract void onDestroy();
}
