package com.gionee.voiceassist.basefunction;

import com.gionee.voiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;

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
    public void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s) {

    }

    protected void playAndRenderText(String ttsText, String utterId, TtsCallback listener) {
        TtsController.getInstance().playTTS(ttsText, utterId, listener);
    }

    protected void playAndRenderText(String ttsText, String utterId, TtsCallback listener, boolean showPlayTextInScreen) {
        TtsController.getInstance().playTTS(ttsText, utterId, listener);
        if(showPlayTextInScreen) {
            screenRender.renderAnswerInScreen(ttsText);
        }
    }

    protected void playTextAndModifyLast(String ttsText, String utterId, TtsCallback listener) {
        TtsController.getInstance().playTTS(ttsText, utterId, listener);
        screenRender.modifyLastTextInScreen(ttsText);
    }

    protected void playAndRenderText(String ttsText) {
        TtsController.getInstance().playTTS(ttsText);
    }

    protected void playAndRenderText(String ttsText, boolean showPlayTextInScreen) {
        TtsController.getInstance().playTTS(ttsText);
        if(showPlayTextInScreen) {
            screenRender.renderAnswerInScreen(ttsText);
        }
    }

    protected void playTextAndModifyLast(String ttsText) {
        TtsController.getInstance().playTTS(ttsText);
        screenRender.modifyLastTextInScreen(ttsText);
    }

    /**
     * 释放资源
     */
    public abstract void onDestroy();
}
