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
package com.gionee.gnvoiceassist.sdk.module.webbrowser.message;

import com.baidu.duer.dcs.framework.message.Payload;

/**
 * Created by caoyushu01 on 2017/7/21.
 */

public class LaunchBrowserPayload extends Payload {
    // 打开web的url
    private String url = "";
    // 打开url的token
    private String token = "";

    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }
}
