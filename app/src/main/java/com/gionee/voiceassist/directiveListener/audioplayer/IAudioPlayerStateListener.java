package com.gionee.voiceassist.directiveListener.audioplayer;

/**
 * Created by twf on 2017/8/18.
 */

public interface IAudioPlayerStateListener {
    void onPlaying();

    void onPaused();

    void onStopped();

    void onCompletion();
}
