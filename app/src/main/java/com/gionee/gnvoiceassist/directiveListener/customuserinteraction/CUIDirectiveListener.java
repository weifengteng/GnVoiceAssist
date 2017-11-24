package com.gionee.gnvoiceassist.directiveListener.customuserinteraction;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.T;

/**
 * Created by twf on 2017/8/26.
 */

public class CUIDirectiveListener implements CustomUserInteractionDeviceModule.CustomUserInteractionDirectiveListener {
    private static final String TAG = CUIDirectiveListener.class.getSimpleName();

    public CUIDirectiveListener() {
        super();
    }

    @Override
    public void customUserInteractionDirectiveReceived(String url, Directive directive) {
        ICUIDirectiveReceivedInterface receivedInterface = CustomUserInteractionManager.getInstance().getCurrInteractionListener();
        String currCUInteractionId = CustomUserInteractionManager.getInstance().getCurrCUInteractionId();
        if(receivedInterface != null) {
            if(directive != null) {
                String directiveName = directive.getName();
                if(TextUtils.equals(directiveName, Constants.HANDLE_UNKNOWN_UTTERANCE)) {
                    receivedInterface.handleCUInteractionUnknownUtterance(currCUInteractionId);
                } else {
                    if(TextUtils.isEmpty(url)) {
                        T.showLong("CUIDirectiveListener customUserInteractionDirectiveReceived url is empty!");
                        LogUtil.e(TAG, "CUIDirectiveListener customUserInteractionDirectiveReceived url is empty!");
                    } else {
                        receivedInterface.handleCUInteractionTargetUrl(currCUInteractionId, url);
                    }
                }
            } else {
                // TODO:
            }
        } else {
            // TODO:
        }
    }

    public void offlineCustomInteractionReceived(String url, String directiveName) {

    }

}
