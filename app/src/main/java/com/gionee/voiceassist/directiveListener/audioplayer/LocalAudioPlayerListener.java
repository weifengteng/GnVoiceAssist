package com.gionee.voiceassist.directiveListener.audioplayer;


import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.basefunction.music.GNMusicOperator;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.voiceassist.sdk.module.localaudioplayer.message.SearchAndPlayMusicPayload;
import com.gionee.voiceassist.sdk.module.localaudioplayer.message.SearchAndPlayRadioPayload;
import com.gionee.voiceassist.sdk.module.localaudioplayer.message.SearchAndPlayUnicastPayload;
import com.gionee.voiceassist.util.T;

import java.util.List;

/**
 * Created by tengweifeng on 9/29/17.
 */

public class LocalAudioPlayerListener extends BaseDirectiveListener implements LocalAudioPlayerDeviceModule.ILocalAudioPlayerListener {
    private GNMusicOperator mGnMusicOperator;

    public LocalAudioPlayerListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
        mGnMusicOperator = iBaseFunction.getGNMusicOperattor();
    }

    @Override
    public void onSearchAndPlayMusic(SearchAndPlayMusicPayload searchAndPlayMusicPayload) {
        if(searchAndPlayMusicPayload != null) {
            // TODO:
            String singer = "";
            String query = searchAndPlayMusicPayload.getQuery();
            String song = searchAndPlayMusicPayload.getSong();
            List<String> singerList = searchAndPlayMusicPayload.getSinger();
            if(singerList != null && !singerList.isEmpty()) {
                singer = singerList.get(0);
            }
            mGnMusicOperator.procMusicAction(singer, song);
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

    @Override
    public void onDestroy() {
        mGnMusicOperator.onDestroy();
        mGnMusicOperator = null;
    }
}
