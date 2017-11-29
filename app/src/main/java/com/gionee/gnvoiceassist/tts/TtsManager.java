package com.gionee.gnvoiceassist.tts;

import android.text.TextUtils;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.sdk.SdkManager;
import com.gionee.gnvoiceassist.util.Constants;

import java.util.HashMap;

/**
 * Created by tengweifeng on 9/14/17.
 */

public class TtsManager {
    public static TtsManager mInstance;
    private volatile boolean isPlaying;
    private String mCurrUtterId;
    private HashMap<String, TtsCallback> mTtsListenerMap = new HashMap<>();

    private TtsManager() {}

    public enum TtsResultCode {
        OFFLINEENGINE_NOT_AVAILABLE,
        ERROR,
        FINISH;
    }

    public static TtsManager getInstance() {
        if(mInstance == null) {
            synchronized (TtsManager.class) {
                if(mInstance == null) {
                    mInstance = new TtsManager();
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

    public void playTTS(String ttsText, String utterId, TtsCallback listener) {
        if(!TextUtils.isEmpty(utterId)) {
            mCurrUtterId = utterId;
            mTtsListenerMap.put(mCurrUtterId, listener);
        }
        playTTS(ttsText);
    }

    public void playTTS(int resId) {
        playTTS(GnVoiceAssistApplication.getInstance().getApplicationContext().getResources().getString(resId));
    }

    public void playTTS(String ttsText) {
        if(isPlaying()) {
            //TODO: 处理当语音正在播报时，打断语音播报
        }
        switch (GnVoiceAssistApplication.TTS_MODE) {
            case Constants.TTS_MODE_ONLINE:
                SdkManager.getInstance().getSdkInternalApi().speakRequest(ttsText);
                break;
            case Constants.TTS_MODE_OFFLINE:
                SdkManager.getInstance().getSdkInternalApi().speakOfflineQuery(ttsText);
                break;
        }
    }

    public TtsCallback getSpeakTxtCallbackListener() {
        if(TextUtils.isEmpty(mCurrUtterId)) {
            return null;
        }
        TtsCallback listener = mTtsListenerMap.get(mCurrUtterId);
        if(listener != null) {
            mTtsListenerMap.remove(mCurrUtterId);
        }
        return listener;
    }

    public String getCurrUtterId() {
        return mCurrUtterId;
    }
}
