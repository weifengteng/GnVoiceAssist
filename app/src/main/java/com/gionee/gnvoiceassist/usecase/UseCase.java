package com.gionee.gnvoiceassist.usecase;

import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.TtsEntity;
import com.gionee.gnvoiceassist.message.model.UsecaseResponseEntity;
import com.gionee.gnvoiceassist.tts.ISpeakTxtEventListener;
import com.gionee.gnvoiceassist.tts.TxtSpeakManager;
import com.gionee.gnvoiceassist.util.LogUtil;

/**
 * Created by liyingheng on 11/8/17.
 */

public abstract class UseCase implements ISpeakTxtEventListener {

    private static final String TAG = UseCase.class.getSimpleName();

    protected UsecaseCallback mCallback;

    public void setCallback(UsecaseCallback callback) {
        mCallback = callback;
    }

    public abstract void handleMessage(DirectiveResponseEntity message);

    public abstract String getUseCaseName();

    public void sendResponse(UsecaseResponseEntity response) {
        if (response.isShouldSpeak()) {
            TtsEntity tts = new TtsEntity(response.getSpeakText(), response.getAction(), null);
            mCallback.onUsecaseResponse(response,tts);
            return;
        }
        mCallback.onUsecaseResponse(response,null);
    }

    public void sendResponse(UsecaseResponseEntity response, ISpeakTxtEventListener ttsProgressListener) {
        if (response.isShouldSpeak()) {
            String text = response.getSpeakText();
            String utterId = response.getAction();
            TtsEntity tts = new TtsEntity();
            tts.setText(text);
            tts.setUtterId(utterId);
            tts.setTtsProgressListener(ttsProgressListener);
            mCallback.onUsecaseResponse(response,tts);
            return;
        }
        mCallback.onUsecaseResponse(response,null);
    }

    public interface UsecaseCallback {

        void onUsecaseResponse(UsecaseResponseEntity response, TtsEntity tts);

    }

    @Override
    public void onSpeakStart() {

    }

    @Override
    public void onSpeakError(TxtSpeakManager.TxtSpeakResult txtSpeakResult, String s) {
        LogUtil.e(TAG,"onTtsSpeakError. msg = " + s);
    }
}
