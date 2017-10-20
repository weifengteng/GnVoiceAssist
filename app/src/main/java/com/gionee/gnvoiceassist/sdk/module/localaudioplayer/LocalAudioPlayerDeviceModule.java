package com.gionee.gnvoiceassist.sdk.module.localaudioplayer;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Header;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.ClientContextPayload;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayMusicPayload;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayRadioPayload;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayUnicastPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liyingheng on 10/16/17.
 */

public class LocalAudioPlayerDeviceModule extends BaseDeviceModule
{
    private List<ILocalAudioPlayerListener> listeners = new ArrayList();
    private PayloadGenerator payloadGenerator;

    public LocalAudioPlayerDeviceModule(IMessageSender messageSender)
    {
        super("ai.dueros.device_interface.extensions.local_audio_player", messageSender);
    }

    public ClientContext clientContext()
    {
        String namespace = "ai.dueros.device_interface.extensions.local_audio_player";
        String name = ApiConstants.Events.PlaybackState.NAME;
        Header header = new Header(namespace, name);
        Payload payload;
        if (this.payloadGenerator != null) {
            payload = this.payloadGenerator.generateContextPayload();
        } else {
            payload = new Payload();
        }
        return new ClientContext(header, payload);
    }

    public void handleDirective(Directive directive) throws HandleDirectiveException {
        String name = directive.getName();
        Payload payload = directive.getPayload();
        if (ApiConstants.Directives.SearchAndPlayMusic.NAME.equals(name))
        {
            if ((payload instanceof SearchAndPlayMusicPayload)) {
                handleSearchAndPlayMusic((SearchAndPlayMusicPayload)payload);
            }
        }
        else if (ApiConstants.Directives.SearchAndPlayUnicast.NAME.equals(name))
        {
            if ((payload instanceof SearchAndPlayUnicastPayload)) {
                handleSearchAndPlayUnicast((SearchAndPlayUnicastPayload)payload);
            }
        }
        else if (ApiConstants.Directives.SearchAndPlayRadio.NAME.equals(name))
        {
            if ((payload instanceof SearchAndPlayRadioPayload)) {
                handleSearchAndPlayRadio((SearchAndPlayRadioPayload)payload);
            }
        }
        else
        {
            String message = "localAudioPlayer cannot handle the directive";
            throw new HandleDirectiveException(HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, message);
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.localaudioplayer.ApiConstants.Directives.SearchAndPlayMusic.NAME, com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayMusicPayload.class);
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.localaudioplayer.ApiConstants.Directives.SearchAndPlayUnicast.NAME, com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayUnicastPayload.class);
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.localaudioplayer.ApiConstants.Directives.SearchAndPlayRadio.NAME, com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayRadioPayload.class);
        return map;
    }

    public void release()
    {
        this.listeners.clear();
    }

    public void addLocalAudioPlayerListener(ILocalAudioPlayerListener listener)
    {
        this.listeners.add(listener);
    }

    private void handleSearchAndPlayMusic(SearchAndPlayMusicPayload payload)
    {
        for (ILocalAudioPlayerListener listener : this.listeners) {
            listener.onSearchAndPlayMusic(payload);
        }
    }

    private void handleSearchAndPlayUnicast(SearchAndPlayUnicastPayload payload)
    {
        for (ILocalAudioPlayerListener listener : this.listeners) {
            listener.onSearchAndPlayUnicast(payload);
        }
    }

    private void handleSearchAndPlayRadio(SearchAndPlayRadioPayload payload)
    {
        for (ILocalAudioPlayerListener listener : this.listeners) {
            listener.onSearchAndPlayRadio(payload);
        }
    }

    public static abstract interface PayloadGenerator
    {
        public abstract ClientContextPayload generateContextPayload();
    }

    public static abstract interface ILocalAudioPlayerListener
    {
        public abstract void onSearchAndPlayMusic(SearchAndPlayMusicPayload paramSearchAndPlayMusicPayload);

        public abstract void onSearchAndPlayUnicast(SearchAndPlayUnicastPayload paramSearchAndPlayUnicastPayload);

        public abstract void onSearchAndPlayRadio(SearchAndPlayRadioPayload paramSearchAndPlayRadioPayload);
    }
}
