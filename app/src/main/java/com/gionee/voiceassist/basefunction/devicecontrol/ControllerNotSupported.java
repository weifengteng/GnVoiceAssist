package com.gionee.voiceassist.basefunction.devicecontrol;

/**
 * Created by liyingheng on 10/24/17.
 */

public class ControllerNotSupported extends Exception {

    public ControllerNotSupported() {
        super();
    }

    public ControllerNotSupported(String message) {
        super(message);
    }
}
