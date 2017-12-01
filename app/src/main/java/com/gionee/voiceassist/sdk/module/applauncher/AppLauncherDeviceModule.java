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
package com.gionee.voiceassist.sdk.module.applauncher;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Header;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.sdk.module.applauncher.message.AppInfo;
import com.gionee.voiceassist.sdk.module.applauncher.message.AppLauncherStatePayload;
import com.gionee.voiceassist.sdk.module.applauncher.message.LaunchAppPayload;

import java.util.HashMap;
import java.util.List;

/**
 * 打开应用的module 类
 * Created by lyh on 2017/11/22.
 */

public class AppLauncherDeviceModule extends BaseDeviceModule {

    private IAppLauncher mAppLauncherImpl;

    //TODO: 将Listener改为List承载
    private IAppLauncherDirectiveListener mDirectiveListener;

    public AppLauncherDeviceModule(IAppLauncher appLauncherImpl, IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
        //TODO: 修改参数，使其不需要依赖IAppLauncher（端能力由我们自己实现）
        this.mAppLauncherImpl = appLauncherImpl;
    }

    @Override
    public ClientContext clientContext() {
        String namespace = ApiConstants.NAMESPACE;
        String name = ApiConstants.Events.AppState.NAME;
        Header header = new Header(namespace, name);
        Payload payload = getState();
        return new ClientContext(header, payload);
    }

    @Override
    public void handleDirective(Directive directive) throws HandleDirectiveException {
        String headerName = directive.getName();
        String appName = "";
        String packageName = "";
        String deepLink = "";
        if (ApiConstants.Directives.LaunchApp.NAME.equals(headerName)) {
            appName = ((LaunchAppPayload) directive.getPayload()).getAppName();
            packageName = ((LaunchAppPayload) directive.getPayload()).getPackageName();
            deepLink = ((LaunchAppPayload) directive.getPayload()).getDeepLink();
        } else {
            String message = "launch app cannot handle the directive";
            throw (new HandleDirectiveException(
                    HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, message));
        }

        // 打开优先使用deepLink，然后使用appName，packageName
        if (!TextUtils.isEmpty(deepLink)) {
            if (mDirectiveListener != null) {
                mDirectiveListener.appDeepLinkDirectiveReceived(appName, packageName, deepLink, directive);
            }
        } else if (!TextUtils.isEmpty(appName) || !TextUtils.isEmpty(packageName)) {
            if (mDirectiveListener != null) {
                mDirectiveListener.appLauncherDirectiveReceived(appName, packageName, directive);
            }
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + ApiConstants.Directives.LaunchApp.NAME, LaunchAppPayload.class);
        return map;
    }

    @Override
    public void release() {
        mDirectiveListener = null;
    }

    private AppLauncherStatePayload getState() {
        List<AppInfo> contextInfos = mAppLauncherImpl.getAppList();
        return new AppLauncherStatePayload(contextInfos);
    }

    public void addAppLauncherDirectiveListener(IAppLauncherDirectiveListener mDirectiveListener) {
        this.mDirectiveListener = mDirectiveListener;
    }

    // 应用指令监听器，用于Android应用层监听应用指令的到来
    public interface IAppLauncherDirectiveListener {
        void appLauncherDirectiveReceived(String appName, String packageName, Directive directive);

        void appDeepLinkDirectiveReceived(String appName, String packageName, String deepLink, Directive directive);
    }
}
