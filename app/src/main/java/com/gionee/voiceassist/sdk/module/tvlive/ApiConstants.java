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
package com.gionee.voiceassist.sdk.module.tvlive;

/**
 * Created by caoyushu01 on 2017/7/25.
 */

public class ApiConstants {
    public static final String NAMESPACE = "ai.dueros.device_interface.tv_live";
    public static final String NAME = "TvLiveInterface";

    public static final class Events {

    }

    public static final class Directives {
        public static final class SwitchChannelByName {
            public static final String NAME = SwitchChannelByName.class.getSimpleName();
        }

        public static final class SwitchChannelByNumber {
            public static final String NAME = SwitchChannelByNumber.class.getSimpleName();
        }

    }
}
