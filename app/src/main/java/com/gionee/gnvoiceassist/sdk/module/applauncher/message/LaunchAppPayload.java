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
package com.gionee.gnvoiceassist.sdk.module.applauncher.message;

import com.baidu.duer.dcs.framework.message.Payload;

/**
 * Created by caoyushu01 on 2017/7/20.
 */

public class LaunchAppPayload extends Payload {

    // 以下三个可选字段参数至少返回一个
    // 可选字段，应用的名称
    private String appName;

    // 可选字段，应用的包名
    private String packageName;

    // 可选字段，打开应用指定功能
    private String deepLink;

    // 打开这个应用的token
    private String token;

    public String getAppName () {
        return appName;
    }

    public void setAppName (String appName) {
        this.appName = appName;
    }

    public String getPackageName () {
        return packageName;
    }

    public void setPackageName (String packageName) {
        this.packageName = packageName;
    }

    public String getDeepLink () {
        return deepLink;
    }

    public void setDeepLink (String deepLink) {
        this.deepLink = deepLink;
    }

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }
}
