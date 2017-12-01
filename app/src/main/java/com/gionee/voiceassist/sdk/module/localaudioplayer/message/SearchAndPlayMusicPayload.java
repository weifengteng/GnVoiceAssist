package com.gionee.voiceassist.sdk.module.localaudioplayer.message;

import com.baidu.duer.dcs.framework.message.Payload;
import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchAndPlayMusicPayload extends Payload implements Serializable {
    private String query;
    private List<String> singer;
    private String song;
    private String album;
    private List<String> tag;

    public SearchAndPlayMusicPayload(@JsonProperty("query") String query, @JsonProperty("singer") List<String> singer, @JsonProperty("song") String song, @JsonProperty("album") String album, @JsonProperty("tag") List<String> tag)
    {
        this.query = query;
        this.singer = singer;
        this.song = song;
        this.album = album;
        this.tag = tag;
    }

    public String getQuery()
    {
        return this.query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public List<String> getSinger()
    {
        return this.singer;
    }

    public void setSinger(List<String> singer)
    {
        this.singer = singer;
    }

    public String getSong()
    {
        return this.song;
    }

    public void setSong(String song)
    {
        this.song = song;
    }

    public String getAlbum()
    {
        return this.album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public List<String> getTag()
    {
        return this.tag;
    }

    public void setTag(List<String> tag)
    {
        this.tag = tag;
    }
}
