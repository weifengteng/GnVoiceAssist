package com.gionee.voiceassist.coreservice.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 1/5/18.
 */

public class LocalAudioPlayerDirectiveEntity extends DirectiveEntity {


    private String singer = "";

    private String song = "";

    private SearchMusicAction action;

    public LocalAudioPlayerDirectiveEntity() {
        setType(Type.LOCAL_AUDIOPLAYER);
    }

    public String getSinger() {
        return singer;
    }

    public String getSong() {
        return song;
    }

    /**
     * 搜索某歌手的所有歌曲
     * @param singer 歌手名
     */
    public void setSearchSinger(String singer) {
        setAction(SearchMusicAction.BY_SINGER);
        setSinger(singer);
    }

    /**
     * 模糊搜索歌曲（只用歌曲名称匹配歌曲）
     * @param song 歌曲名
     */
    public void setSearchSongFuzzy(String song) {
        setAction(SearchMusicAction.BY_SONG_FUZZY);
        setSong(song);
    }

    /**
     * 精确搜索歌曲（通过歌手名+歌曲名匹配歌曲）
     * @param singer 歌手名
     * @param song 歌曲名
     */
    public void setSearchSongExact(String singer, String song) {
        setAction(SearchMusicAction.BY_SONG_EXACT);
        setSinger(singer);
        setSong(song);
    }

    /**
     * 随机播放音乐（歌手名、歌曲名都为空）
     */
    public void setPlayRandom() {
        setAction(SearchMusicAction.PLAY_RANDOM);
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public SearchMusicAction getAction() {
        return action;
    }

    private void setAction(SearchMusicAction action) {
        this.action = action;
    }

    public enum SearchMusicAction {
        PLAY_RANDOM,
        BY_SINGER,
        BY_SONG_FUZZY,
        BY_SONG_EXACT,
        SEARCH_UNICAST,
        SEARCH_RADIO,
    }

    @Override
    public String toString() {
        return "LocalAudioPlayerDirectiveEntity{" +
                "singer='" + singer + '\'' +
                ", song='" + song + '\'' +
                ", action=" + action +
                '}';
    }
}
