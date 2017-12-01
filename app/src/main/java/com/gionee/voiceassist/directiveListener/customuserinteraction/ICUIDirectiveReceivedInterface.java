package com.gionee.voiceassist.directiveListener.customuserinteraction;

/**
 * Created by twf on 2017/8/26.
 */

public interface ICUIDirectiveReceivedInterface {

    void handleCUInteractionUnknownUtterance(String id);

    void handleCUInteractionTargetUrl(String id, String url);
}
