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
package com.gionee.voiceassist.coreservice.sdk.module.tvlive;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;

import java.util.HashMap;

/**
 * Created by caoyushu01 on 2017/7/25.
 * 金立专属TV live服务
 */

public class TvLiveDeviceModule extends BaseDeviceModule {
    private ITvLiveDirectiveListener iTvLiveDirectiveListener;

    public TvLiveDeviceModule (IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
    }

    @Override
    public ClientContext clientContext () {
        return null;
    }

    @Override
    public void handleDirective (Directive directive) throws HandleDirectiveException {
        String headerName = directive.getName();
        if (ApiConstants.Directives.SwitchChannelByName.NAME.equals(headerName)
                || ApiConstants.Directives.SwitchChannelByNumber.NAME.equals(headerName)) {
            if (iTvLiveDirectiveListener != null) {
                iTvLiveDirectiveListener.onTvLiveDirectiveReceived(directive);
            }
        } else {
            String message = "tvlive cannot handle the directive";
            throw (new HandleDirectiveException(
                    HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, message));
        }

    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + com.gionee.voiceassist.coreservice.sdk.module.tvlive.ApiConstants.Directives.SwitchChannelByName.NAME, com.gionee.voiceassist.coreservice.sdk.module.tvlive.message.SwitchChannelByNamePayload.class);
        map.put(getNameSpace() + com.gionee.voiceassist.coreservice.sdk.module.tvlive.ApiConstants.Directives.SwitchChannelByNumber.NAME, com.gionee.voiceassist.coreservice.sdk.module.tvlive.message.SwitchChannelByNumberPayload.class);
        return map;
    }

    @Override
    public void release () {
        iTvLiveDirectiveListener = null;
    }

    public void addDirectiveListener (ITvLiveDirectiveListener liveDirectiveListener) {
        this.iTvLiveDirectiveListener = liveDirectiveListener;
    }

    public interface ITvLiveDirectiveListener {
        void onTvLiveDirectiveReceived(Directive directive);
    }
}
