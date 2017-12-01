package com.gionee.voiceassist.util;

/**
 * Created by liyingheng on 10/26/17.
 */

public class Preconditions {

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
