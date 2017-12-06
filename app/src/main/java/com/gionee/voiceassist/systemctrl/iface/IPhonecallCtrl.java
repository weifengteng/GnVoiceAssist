package com.gionee.voiceassist.systemctrl.iface;

/**
 * Created by liyingheng on 12/6/17.
 */

public interface IPhonecallCtrl {

    void makeCall(String number, String simId);

    boolean needChooseSim();

    boolean dualSimAvailable();

    boolean dualSimInserted();

    int defaultSimId();


}
