package com.gionee.gnvoiceassist.util;

import com.gionee.gnvoiceassist.statemachine.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by twf on 2017/8/31.
 */

public class SharedData {
    private static SharedData mInstance;
    private String currDirectiveListenerClassName;
    private String desiredScene;
    private List<String> utteranceWords;
    private List<String> utteranceExtraInfo;
    private boolean isStopListenReceiving;
    private Scene currentScene;

    private SharedData(){
        currentScene = Scene.IDLE;
        utteranceWords = new ArrayList<>();
        utteranceExtraInfo = new ArrayList<>();
    }

    public static synchronized SharedData getInstance() {
        if(mInstance == null) {
            synchronized (SharedData.class) {
                if(mInstance == null) {
                    mInstance = new SharedData();
                }
            }
        }

        return mInstance;
    }

    public String getCurrDirectiveListenerClassName() {
        return currDirectiveListenerClassName;
    }

    public void setCurrDirectiveListenerClassName(String currDirectiveListenerClassName) {
        this.currDirectiveListenerClassName = currDirectiveListenerClassName;
    }

    public void clearCurrDirectiveListenerClassName() {
        setCurrDirectiveListenerClassName("");
    }

    public boolean isStopListenReceiving() {
        return isStopListenReceiving;
    }

    public void setStopListenReceiving(boolean stopListenReceiving) {
        isStopListenReceiving = stopListenReceiving;
    }

    public void setCurrentCuiScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Scene getCurrentCuiScene() {
        return currentScene;
    }

    public void setUtteranceWords(List<String> utterances) {
        utteranceWords.clear();
        utteranceWords.addAll(utterances);
    }

    public List<String> getUtteranceWords() {
        return utteranceWords;
    }

    public void setUtteranceExtraInfo(List<String> extraInfo) {
        utteranceExtraInfo.clear();
        utteranceExtraInfo = extraInfo;
    }

    public List<String> getUtteranceExtraInfo() {
        return utteranceExtraInfo;
    }
}
