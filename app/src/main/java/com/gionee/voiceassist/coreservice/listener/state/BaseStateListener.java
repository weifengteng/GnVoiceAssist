package com.gionee.voiceassist.coreservice.listener.state;

import com.gionee.voiceassist.coreservice.CoreService;

import java.util.List;

/**
 * Created by liyingheng on 1/2/18.
 */

public class BaseStateListener {

    List<CoreService.StateCallback> mStateCallbacks;
    public BaseStateListener(List<CoreService.StateCallback> callbacks) {
        mStateCallbacks = callbacks;
    }

}
