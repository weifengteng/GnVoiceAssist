package com.gionee.gnvoiceassist.directiveListener.audioplayer;


import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.music.GNMusicOperator;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.LocalAudioPlayerDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayMusicPayload;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayRadioPayload;
import com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message.SearchAndPlayUnicastPayload;
import com.gionee.gnvoiceassist.util.T;

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
            String query = searchAndPlayMusicPayload.getQuery();
            String song = searchAndPlayMusicPayload.getSong();
            String singer = searchAndPlayMusicPayload.getSinger().get(0);
            mGnMusicOperator.playMusic(singer, song);
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
