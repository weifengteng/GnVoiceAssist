/*
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
package com.gionee.voiceassist.coreservice.sdk.module.screen;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Event;
import com.baidu.duer.dcs.framework.message.Header;
import com.baidu.duer.dcs.framework.message.MessageIdHeader;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.common.util.FileUtil;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.HtmlPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.LinkClickedPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderHintPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderVoiceInputTextPayload;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Screen模块处理并执行服务下发的指令，如HtmlView指令，以及发送事件，如LinkClicked事件
 * <p>
 * Created by wuruisheng on 2017/5/31.
 */
public class ScreenDeviceModule extends BaseDeviceModule {
    private List<IScreenListener> listeners;

    public ScreenDeviceModule(IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
        this.listeners = new CopyOnWriteArrayList<IScreenListener>();
    }

    @Override
    public ClientContext clientContext() {
        return null;
    }

    @Override
    public void handleDirective(Directive directive) throws HandleDirectiveException {
        String name = directive.header.getName();
        if (name.equals(ApiConstants.Directives.HtmlView.NAME)) {
            handleHtmlPayload(directive.getPayload(), directive.id);
        } else if (name.equals(ApiConstants.Directives.RenderVoiceInputText.NAME)) {
            fireRenderVoiceInputText((RenderVoiceInputTextPayload) directive.getPayload());
        } else if (name.equals(ApiConstants.Directives.RenderCard.NAME)) {
            fireOnRenderCard((RenderCardPayload) directive.getPayload(), directive.id);
        } else if (name.equals(ApiConstants.Directives.RenderHint.NAME)) {
            fireOnRenderHint((RenderHintPayload) directive.getPayload(), directive.id);
        } else {
            String message = "VoiceOutput cannot handle the directive";
            throw (new HandleDirectiveException(
                    HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, message));
        }
    }

    @Override
    public void release() {
        listeners.clear();
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + ApiConstants.Directives.HtmlView.NAME, HtmlPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.RenderVoiceInputText.NAME, RenderVoiceInputTextPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.RenderCard.NAME, RenderCardPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.RenderHint.NAME, RenderHintPayload.class);
        return map;
    }

    private void handleHtmlPayload(Payload payload, int id) {
        if (payload instanceof HtmlPayload) {
            HtmlPayload htmlPayload = (HtmlPayload) payload;
            fireOnHtmlView(htmlPayload, id);
        }
    }

    private void sendLinkClickedEvent(String url) {
        String name = ApiConstants.Events.LinkClicked.NAME;
        Header header = new MessageIdHeader(getNameSpace(), name);

        LinkClickedPayload linkClickedPayload = new LinkClickedPayload(url);
        Event event = new Event(header, linkClickedPayload);
        if (messageSender != null) {
            messageSender.sendEvent(event);
        }
    }

    public void addScreenListener(IScreenListener listener) {
        listeners.add(listener);
    }

    public void removeScreenListener(IScreenListener listener) {
        listeners.remove(listener);
    }

    private void fireOnHtmlView(HtmlPayload payload, int id) {
        for (IScreenListener listener : listeners) {
            listener.onHtmlPayload(payload, id);
        }
    }

    private void fireRenderVoiceInputText(RenderVoiceInputTextPayload payload) {
        if (payload.type == RenderVoiceInputTextPayload.Type.FINAL) {
            FileUtil.appendStrToFileNew("ASR-FINAL-RESULT:" + payload.text
                    + "," + System.currentTimeMillis() + "\n");
        }

        for (IScreenListener listener : listeners) {
            listener.onRenderVoiceInputText(payload);
        }
    }

    private void fireOnRenderCard(RenderCardPayload renderCardPayload, int id) {
        for (IScreenListener listener : listeners) {
            listener.onRenderCard(renderCardPayload, id);
        }
    }

    private void fireOnRenderHint(RenderHintPayload payload, int id) {
        for (IScreenListener listener : listeners) {
            listener.onRenderHint(payload, id);
        }
    }

    public interface IScreenListener {
        /**
         * 接收到RenderVoiceInputText指令时回调
         *
         * @param payload 内容
         */
        void onRenderVoiceInputText(RenderVoiceInputTextPayload payload);

        /**
         * 接收到HtmlView指令时回调
         *
         * @param htmlPayload 内容
         * @param id          统计id（ 语音请求的id）
         */
        void onHtmlPayload(HtmlPayload htmlPayload, int id);


        /**
         * 接收到RenderCard指令回调
         *
         * @param renderCardPayload
         * @param id
         */
        void onRenderCard(RenderCardPayload renderCardPayload, int id);

        /**
         * 接收到Hint指令
         *
         * @param renderHintPayload
         * @param id
         */
        void onRenderHint(RenderHintPayload renderHintPayload, int id);
    }
}