package com.gionee.voiceassist.coreservice.listener.directive;

import android.content.Intent;
import android.net.Uri;

import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.coreservice.datamodel.WebBrowserDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.webbrowser.WebBrowserDeviceModule;
import com.gionee.voiceassist.util.LogUtil;

import java.util.List;

/**
 * Created by twf on 2017/8/16.
 */

public class WebBrowserListener extends BaseDirectiveListener implements WebBrowserDeviceModule.IWebBrowserDirectiveListener {
    public static final String TAG = WebBrowserListener.class.getSimpleName();
    private static final String APP_BROWSER_PACKAGE_NAME = "com.android.browser";

    public WebBrowserListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }


    @Override
    public void webLauncherDirectiveReceived(String url, Directive directive) {
        String rawMessage = directive.rawMessage;
        LogUtil.d("DCSF-----", TAG + " url= " + url);
        WebBrowserDirectiveEntity msg = new WebBrowserDirectiveEntity();
        msg.setUrl(url);
        sendDirective(msg);
    }
}
