package com.gionee.gnvoiceassist.basefunction;

import com.baidu.duer.sdk.speak.SpeakInterface;
import com.gionee.gnvoiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.gnvoiceassist.tts.ISpeakTxtEventListener;
import com.gionee.gnvoiceassist.tts.TxtSpeakManager;

/**
 * Created by tengweifeng on 9/27/17.
 */

public abstract class BasePresenter implements ISpeakTxtEventListener {
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
    public void onSpeakError(SpeakInterface.SpeakTxtResultCode speakTxtResultCode, String s) {

    }

    protected void playAndRenderText(String ttsText, String utterId, ISpeakTxtEventListener listener) {
        TxtSpeakManager.getInstance().playTTS(ttsText, utterId, listener);
    }

    protected void playAndRenderText(String ttsText, String utterId, ISpeakTxtEventListener listener, boolean showPlayTextInScreen) {
        TxtSpeakManager.getInstance().playTTS(ttsText, utterId, listener);
        if(showPlayTextInScreen) {
            screenRender.renderAnswerInScreen(ttsText);
        }
    }

    protected void playAndRenderText(String ttsText) {
        TxtSpeakManager.getInstance().playTTS(ttsText);
    }

    protected void playAndRenderText(String ttsText, boolean showPlayTextInScreen) {
        TxtSpeakManager.getInstance().playTTS(ttsText);
        if(showPlayTextInScreen) {
            screenRender.renderAnswerInScreen(ttsText);
        }
    }

    /**
     * 释放资源
     */
    public abstract void onDestroy();
}
