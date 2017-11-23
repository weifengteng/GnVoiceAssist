package com.gionee.gnvoiceassist.message.model;

import com.gionee.gnvoiceassist.tts.ISpeakTxtEventListener;

/**
 * Created by liyingheng on 11/22/17.
 */

public class TtsEntity {

    private String text = "";

    private String utterId = "";

    private ISpeakTxtEventListener ttsProgressListener;

    public TtsEntity() {

    }

    public TtsEntity(String text, String utterId, ISpeakTxtEventListener ttsProgressListener) {
        this.text = text;
        this.utterId = utterId;
        this.ttsProgressListener = ttsProgressListener;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUtterId() {
        return utterId;
    }

    public void setUtterId(String utterId) {
        this.utterId = utterId;
    }

    public ISpeakTxtEventListener getTtsProgressListener() {
        return ttsProgressListener;
    }

    public void setTtsProgressListener(ISpeakTxtEventListener ttsProgressListener) {
        this.ttsProgressListener = ttsProgressListener;
    }
}
