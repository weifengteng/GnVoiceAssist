package com.gionee.gnvoiceassist.directiveListener.audioplayer;

import com.baidu.duer.dcs.systeminterface.IMediaPlayer;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;

/**
 * Created by twf on 2017/8/18.
 */

public class AudioPlayerListener extends BaseDirectiveListener implements IMediaPlayer.IMediaPlayerListener{
    private IAudioPlayerStateListener iAudioPlayerState;

    public AudioPlayerListener(IBaseFunction baseFunction) {
        super(baseFunction);
        this.iAudioPlayerState = baseFunction.getAudioPlayerStateListener();
    }


    @Override
    public void onInit() {

    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onPlaying() {
        // TODO: onPlaying
        if(iAudioPlayerState != null) {
            iAudioPlayerState.onPlaying();
        }
    }

    @Override
    public void onPaused() {
        // TODO: onPause
        if(iAudioPlayerState != null) {
            iAudioPlayerState.onPaused();
        }
    }

    @Override
    public void onStopped() {
        // TODO: onStopped
        if(iAudioPlayerState != null) {
            iAudioPlayerState.onStopped();
        }
    }

    @Override
    public void onCompletion() {
        // TODO: onCompletion
        if(iAudioPlayerState != null) {
            iAudioPlayerState.onCompletion();
        }
    }

    @Override
    public void onError(String s, IMediaPlayer.ErrorType errorType) {

    }

    @Override
    public void onBufferingUpdate(int i) {

    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingEnd() {

    }

    @Override
    public void onDestroy() {
        iAudioPlayerState = null;
    }
}
