package com.gionee.voiceassist.coreservice.listener.directive;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;

import java.util.List;

/**
 * Created by liyingheng on 12/29/17.
 */

public class BaseDirectiveListener {

    private List<DirectiveListenerController.DirectiveCallback> mCallbacks;

    public BaseDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        mCallbacks = callbacks;
    }

    void sendDirective(DirectiveEntity directiveMsg) {
        for (DirectiveListenerController.DirectiveCallback callback:mCallbacks) {
            callback.onDirectiveMessage(directiveMsg);
        }
    }

}
