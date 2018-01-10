package com.gionee.voiceassist.usecase.devicecontrol.sysinterface;

import com.gionee.voiceassist.usecase.devicecontrol.ControllerNotSupported;

/**
 * Created by liyingheng on 10/24/17.
 */

public interface IHotspot {

    /**
     * 开关热点
     * @param enabled
     */
    void setHotspotEnabled(boolean enabled) throws ControllerNotSupported;

}
