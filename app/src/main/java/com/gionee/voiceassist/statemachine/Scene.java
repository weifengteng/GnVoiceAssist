package com.gionee.voiceassist.statemachine;

/**
 * Created by twf on 2017/8/31.
 */

public enum Scene {

    APPLAUNCH_NEW_WAIT_CONFIRM("", ""),
    WAIT_CHOOSE_PHONE_TYPE_TO_CALL("phonecall", "phonecall"),
    WAIT_CHOOSE_SIM_TO_CALL("phonecall", "phonecall"),

    IDLE("all", "all"),
    STOCK("all", "all"),
    SEARCH("all", "all"),
    CONTACT_SHOWING("all", "all"),
    CATCHED_EXCEPTION("all", "all");

    private final String mFocusToCall;
    private final String mFocusToFire;

    Scene(String focusToCall, String focusToFire) {
        mFocusToCall = focusToCall;
        mFocusToFire = focusToFire;
    }

    public String getmFocusToCall() {
        return mFocusToCall;
    }

    public String getmFocusToFire() {
        return mFocusToFire;
    }
}
