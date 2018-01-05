package com.gionee.voiceassist.coreservice.listener.directive;


import android.text.TextUtils;

import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.basefunction.music.GNMusicOperator;
import com.gionee.voiceassist.coreservice.datamodel.LocalAudioPlayerDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer.message.SearchAndPlayMusicPayload;
import com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer.message.SearchAndPlayRadioPayload;
import com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer.message.SearchAndPlayUnicastPayload;
import com.gionee.voiceassist.util.T;

import java.util.List;

/**
 * Created by tengweifeng on 9/29/17.
 */

public class LocalAudioPlayerListener extends BaseDirectiveListener implements LocalAudioPlayerDeviceModule.ILocalAudioPlayerListener {
    private GNMusicOperator mGnMusicOperator;

    public LocalAudioPlayerListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onSearchAndPlayMusic(SearchAndPlayMusicPayload searchAndPlayMusicPayload) {
        if(searchAndPlayMusicPayload != null) {
            // TODO:
            String singer = "";
            String query = searchAndPlayMusicPayload.getQuery();
            String song = searchAndPlayMusicPayload.getSong();
            List<String> singerList = searchAndPlayMusicPayload.getSinger();
            boolean withSinger = false;
            boolean withSong = false;
            if(singerList != null && !singerList.isEmpty()) {
                singer = singerList.get(0);
                withSinger = !TextUtils.isEmpty(singer);
            } else {
                withSinger = false;
            }
            withSong = !TextUtils.isEmpty(song) && !TextUtils.equals(song, "null");

            LocalAudioPlayerDirectiveEntity msg = new LocalAudioPlayerDirectiveEntity();
            if (withSinger && withSong) {
                msg.setSearchSongExact(singer, song);
            } else if (withSinger && !withSong) {
                msg.setSearchSinger(singer);
            } else if (!withSinger && withSong) {
                msg.setSearchSongFuzzy(song);
            } else {
                msg.setPlayRandom();
            }
            sendDirective(msg);
            T.showShort("搜索并播放音乐！ song= " + song + " singer= " + singer);
        }
    }

    @Override
    public void onSearchAndPlayUnicast(SearchAndPlayUnicastPayload searchAndPlayUnicastPayload) {
        T.showShort("搜索并播放有声！");
        // TODO:

    }

    @Override
    public void onSearchAndPlayRadio(SearchAndPlayRadioPayload searchAndPlayRadioPayload) {
        // TODO:
        T.showShort("搜索并播放直播电台！");

    }
}
