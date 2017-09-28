package com.gionee.gnvoiceassist.util;

/**
 * Created by twf on 2017/8/31.
 */

public class SharedData {
    private static SharedData mInstance;
    private String currDirectiveListenerClassName;
    private String desiredScene;
    private boolean isStopListenReceiving;

    private SharedData(){}

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
}
