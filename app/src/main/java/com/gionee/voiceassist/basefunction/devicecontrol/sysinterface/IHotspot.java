package com.gionee.voiceassist.basefunction.devicecontrol.sysinterface;

import com.gionee.voiceassist.basefunction.devicecontrol.ControllerNotSupported;

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
