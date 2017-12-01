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
package com.gionee.voiceassist.sdk.module.applauncher.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by caoyushu01 on 2017/7/21.
 */

public class AppInfo {
    private String appName;
    private String packageName;
    private String versionCode;
    private String versionName;

    public AppInfo (@JsonProperty("appName") String appName,
                    @JsonProperty("packageName") String packageName,
                    @JsonProperty("versionCode") String versionCode,
                    @JsonProperty("versionName") String versionName) {
        this.appName = appName;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }

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

    public String getVersionCode () {
        return versionCode;
    }

    public void setVersionCode (String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName () {
        return versionName;
    }

    public void setVersionName (String versionName) {
        this.versionName = versionName;
    }
}
