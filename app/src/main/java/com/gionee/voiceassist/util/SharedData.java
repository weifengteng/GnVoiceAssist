package com.gionee.voiceassist.util;

import com.gionee.voiceassist.statemachine.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twf on 2017/8/31.
 */

public class SharedData {
    private static SharedData mInstance;
    private String currDirectiveListenerClassName;
    private String desiredScene;
    private boolean isVadReceiving;
    private Scene currentScene;
    private int mLastQueryItemPosition;

    private SharedData(){
        currentScene = Scene.IDLE;
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

    public boolean isVadReceiving() {
        return isVadReceiving;
    }

    public void setVadReceiving(boolean vadReceiving) {
        isVadReceiving = vadReceiving;
        LogUtil.d(SharedData.class, "setVadReceiving = " + isVadReceiving());
    }

    public void setCurrentCuiScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Scene getCurrentCuiScene() {
        return currentScene;
    }

    public int getLastQueryItemPosition() {
        return mLastQueryItemPosition;
    }

    public void setLastQueryItemPosition(int lastQueryItemPosition) {
        this.mLastQueryItemPosition = lastQueryItemPosition;
    }
}
