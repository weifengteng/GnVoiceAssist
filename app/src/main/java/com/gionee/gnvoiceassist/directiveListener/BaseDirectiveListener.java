package com.gionee.gnvoiceassist.directiveListener;

import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.ICUIDirectiveReceivedInterface;
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;
import com.gionee.gnvoiceassist.tts.ISpeakTxtEventListener;
import com.gionee.gnvoiceassist.tts.TxtSpeakManager;
import static com.gionee.gnvoiceassist.util.Preconditions.checkNotNull;

/**
 * Created by twf on 2017/8/14.
 */

public abstract class BaseDirectiveListener
        implements ISpeakTxtEventListener, ICUIDirectiveReceivedInterface{
    protected IBaseFunction iBaseFunction;
    protected IDirectiveListenerCallback mCallback;

    public BaseDirectiveListener(IDirectiveListenerCallback callback) {
        mCallback = checkNotNull(callback,"DirectiveListenerCallback cannot be null");
    }

    @Override
    public void onSpeakStart() {

    }

    @Override
    public void onSpeakFinish(String utterId) {

    }

    @Override
    public void onSpeakError(TxtSpeakManager.TxtSpeakResult txtSpeakResult, String s) {

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
//            iBaseFunction.getScreenRender().renderAnswerInScreen(ttsText);
            //TODO 是否需要在此处朗读tts，并将文字显示在界面上
        }
    }

    protected void playTTS(String ttsText) {
        TxtSpeakManager.getInstance().playTTS(ttsText);
    }

    protected void playTTS(String ttsText, boolean showPlayTextInScreen) {
        TxtSpeakManager.getInstance().playTTS(ttsText);
        if(showPlayTextInScreen) {
//            iBaseFunction.getScreenRender().renderAnswerInScreen(ttsText);
            //TODO 是否需要在此处朗读tts，并将文字显示在界面上？
        }
    }

    /**
     * 释放资源
     */
    public abstract void onDestroy();
}
