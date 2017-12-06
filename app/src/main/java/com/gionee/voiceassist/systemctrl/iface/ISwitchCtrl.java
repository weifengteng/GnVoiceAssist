package com.gionee.voiceassist.systemctrl.iface;

/**
 * Created by liyingheng on 12/6/17.
 */

public interface ISwitchCtrl {

    boolean getState();

    void toggle(boolean enabled, Callback callback);

    interface Callback {

        void onSuccess();

        void onFailure(FailureCode code, String reason);

    }

    enum FailureCode {
        NOT_SUPPORT,
        UNKNOWN_FAILURE
    }

}
