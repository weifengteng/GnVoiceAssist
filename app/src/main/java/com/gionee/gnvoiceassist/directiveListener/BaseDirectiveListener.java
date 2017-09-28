package com.gionee.gnvoiceassist.directiveListener;

import com.baidu.duer.sdk.speak.SpeakInterface;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.ICUIDirectiveReceivedInterface;
import com.gionee.gnvoiceassist.tts.ISpeakTxtEventListener;
import com.gionee.gnvoiceassist.tts.TxtSpeakManager;

/**
 * Created by twf on 2017/8/14.
 */

public abstract class BaseDirectiveListener implements ISpeakTxtEventListener, ICUIDirectiveReceivedInterface{
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
    public void onSpeakError(SpeakInterface.SpeakTxtResultCode speakTxtResultCode, String s) {

    }

    public void handleCUInteractionUnknownUtterance(String id) {

    }

    public void handleCUInteractionTargetUrl(String id, String url) {

    }

    /*public abstract void procOtherDirectiveCalling();

    public abstract boolean procRecoError(int error);

    public abstract void stopFocus();*/

    protected void playTTS(String ttsText, String utterId, ISpeakTxtEventListener listener) {
        TxtSpeakManager.getInstance().playTTS(ttsText, utterId, listener);
    }

    protected void playTTS(String ttsText, String utterId, ISpeakTxtEventListener listener, boolean showPlayTextInScreen) {
        TxtSpeakManager.getInstance().playTTS(ttsText, utterId, listener);
        if(showPlayTextInScreen) {
            iBaseFunction.getScreenRender().renderAnswerInScreen(ttsText);
        }
    }

    protected void playTTS(String ttsText) {
        TxtSpeakManager.getInstance().playTTS(ttsText);
    }

    protected void playTTS(String ttsText, boolean showPlayTextInScreen) {
        TxtSpeakManager.getInstance().playTTS(ttsText);
        if(showPlayTextInScreen) {
            iBaseFunction.getScreenRender().renderAnswerInScreen(ttsText);
        }
    }

    /**
     * 释放资源
     */
    public abstract void onDestroy();
}
