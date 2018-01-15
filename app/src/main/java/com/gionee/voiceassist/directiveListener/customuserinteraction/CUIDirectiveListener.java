package com.gionee.voiceassist.directiveListener.customuserinteraction;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.ClickLinkPayload;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.HandleUnknownUtterancePayload;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.T;

/**
 * Created by twf on 2017/8/26.
 */

public class CUIDirectiveListener implements CustomUserInteractionDeviceModule.ICustomUserInteractionListener {
    private static final String TAG = CUIDirectiveListener.class.getSimpleName();

    public CUIDirectiveListener() {
        super();
    }

    @Override
    public void onClickLink(ClickLinkPayload clickLinkPayload) {
        ICUIDirectiveReceivedInterface receivedInterface = CustomUserInteractionManager.getInstance().getCurrInteractionListener();
        String currCUInteractionId = CustomUserInteractionManager.getInstance().getCurrCUInteractionId();
        String url = clickLinkPayload.getUrl();
        if(receivedInterface != null) {
            if(TextUtils.isEmpty(url)) {
                T.showLong("CUIDirectiveListener customUserInteractionDirectiveReceived url is empty!");
                LogUtil.e(TAG, "CUIDirectiveListener customUserInteractionDirectiveReceived url is empty!");
            } else {
                receivedInterface.handleCUInteractionTargetUrl(currCUInteractionId, url);
            }
        }
    }

    @Override
    public void onHandleUnknownUtterance(HandleUnknownUtterancePayload handleUnknownUtterancePayload) {
        ICUIDirectiveReceivedInterface receivedInterface = CustomUserInteractionManager.getInstance().getCurrInteractionListener();
        String currCUInteractionId = CustomUserInteractionManager.getInstance().getCurrCUInteractionId();
        receivedInterface.handleCUInteractionUnknownUtterance(currCUInteractionId);
    }
}
