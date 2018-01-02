/*
 * *
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gionee.voiceassist.coreservice.sdk.module.webbrowser;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Header;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.coreservice.sdk.module.webbrowser.message.LaunchBrowserPayload;

import java.util.HashMap;

/**
 * Created by caoyushu01 on 2017/7/21.
 */

public class WebBrowserDeviceModule extends BaseDeviceModule {

    private IWebBrowser mWebBrowserImpl;

    private IWebBrowserDirectiveListener mDirectiveListener;

    public WebBrowserDeviceModule (IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
    }

    @Override
    public ClientContext clientContext () {
        String namespace = ApiConstants.NAMESPACE;
        String name = ApiConstants.Events.WebState.NAME;
        Header header = new Header(namespace, name);
        Payload payload = new Payload();
        return new ClientContext(header, payload);
    }

    @Override
    public void handleDirective (Directive directive) throws HandleDirectiveException {
        String headerName = directive.getName();
        String url = "";
        if (ApiConstants.Directives.LaunchBrowser.NAME.equals(headerName)) {
            url = ((LaunchBrowserPayload) directive.getPayload()).getUrl();
        } else {
            String message = "webBrowser cannot handle the directive";
            throw (new HandleDirectiveException(
                    HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, message));
        }
        if (mDirectiveListener != null) {
            mDirectiveListener.webLauncherDirectiveReceived(url, directive);
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + com.gionee.voiceassist.coreservice.sdk.module.webbrowser.ApiConstants.Directives.LaunchBrowser.NAME, com.gionee.voiceassist.coreservice.sdk.module.webbrowser.message.LaunchBrowserPayload.class);
        return map;
    }

    @Override
    public void release () {
        mDirectiveListener = null;
    }

    public void addDirectiveListener(IWebBrowserDirectiveListener directiveListener){
        mDirectiveListener = directiveListener;
    }

    // 打开网页指令监听器，用于Android应用层监听打开网页指令的到来
    public interface IWebBrowserDirectiveListener {
        void webLauncherDirectiveReceived(String url, Directive directive);
    }
}
