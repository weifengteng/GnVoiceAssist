package com.gionee.gnvoiceassist.tts;

import android.text.TextUtils;

import com.baidu.duer.sdk.DcsSDK;
import com.baidu.duer.sdk.speak.SpeakInterface;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;

import java.util.HashMap;

/**
 * Created by tengweifeng on 9/14/17.
 */

public class TxtSpeakManager {
    public static TxtSpeakManager mInstance;
    private volatile boolean isPlaying;
    private String mCurrUtterId;
    private HashMap<String, ISpeakTxtEventListener> mTtsListenerMap = new HashMap<>();

    private TxtSpeakManager() {}

    public static TxtSpeakManager getInstance() {
        if(mInstance == null) {
            synchronized (TxtSpeakManager.class) {
                if(mInstance == null) {
                    mInstance = new TxtSpeakManager();
                }
            }
        }
        return mInstance;
    }

    public void setPlayingState(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public synchronized boolean isPlaying() {
        return isPlaying;
    }

    public void playTTS(String ttsText, String utterId, ISpeakTxtEventListener listener) {
        if(isPlaying()) {
            DcsSDK.getInstance().getSpeak().stopSpeak();
        }
        if(!TextUtils.isEmpty(utterId)) {
            mCurrUtterId = utterId;
            mTtsListenerMap.put(mCurrUtterId, listener);
        }
        DcsSDK.getInstance().getSpeak().speakTxt(ttsText, SpeakInterface.SpeakTxtMixMode.MIX_MODE_DEFAULT);
    }

    public void playTTS(String ttsText) {
        if(isPlaying()) {
            DcsSDK.getInstance().getSpeak().stopSpeak();
        }
        mCurrUtterId = "";
        DcsSDK.getInstance().getSpeak().speakTxt(ttsText, SpeakInterface.SpeakTxtMixMode.MIX_MODE_DEFAULT);
    }

    public void playTTS(int resId) {
        playTTS(GnVoiceAssistApplication.getInstance().getApplicationContext().getResources().getString(resId));
    }

    public ISpeakTxtEventListener getSpeakTxtCallbackListener() {
        if(TextUtils.isEmpty(mCurrUtterId)) {
            return null;
        }
        ISpeakTxtEventListener listener = mTtsListenerMap.get(mCurrUtterId);
        if(listener != null) {
            mTtsListenerMap.remove(mCurrUtterId);
        }
        return listener;
    }

    public String getCurrUtterId() {
        return mCurrUtterId;
    }
}
