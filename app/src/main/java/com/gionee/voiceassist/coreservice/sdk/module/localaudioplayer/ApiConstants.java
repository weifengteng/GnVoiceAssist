package com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer;

/**
 * Created by liyingheng on 10/16/17.
 */

public class ApiConstants
{
    public static final String NAMESPACE = "ai.dueros.device_interface.extensions.local_audio_player";
    public static final String NAME = "LocalAudioPlayerInterface";

    public static final class Events
    {
        public static final class PlaybackState
        {
            public static final String NAME = PlaybackState.class.getSimpleName();
        }
    }

    public static final class Directives
    {
        public static final class SearchAndPlayMusic
        {
            public static final String NAME = SearchAndPlayMusic.class.getSimpleName();
        }

        public static final class SearchAndPlayUnicast
        {
            public static final String NAME = SearchAndPlayUnicast.class.getSimpleName();
        }

        public static final class SearchAndPlayRadio
        {
            public static final String NAME = SearchAndPlayRadio.class.getSimpleName();
        }
    }
}
