package com.gionee.voiceassist.sdk.module.localaudioplayer.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liyingheng on 10/16/17.
 */

public class SearchAndPlayUnicastPayload
        extends Payload
{
    private String query;
    private String album;
    private String track;
    private String trackNumber;
    private String artist;
    private String category;
    private String sortType;

    public SearchAndPlayUnicastPayload(@JsonProperty("query") String query, @JsonProperty("album") String album, @JsonProperty("track") String track, @JsonProperty("trackNumber") String trackNumber, @JsonProperty("artist") String artist, @JsonProperty("category") String category, @JsonProperty("sortType") String sortType)
    {
        this.query = query;
        this.album = album;
        this.track = track;
        this.trackNumber = trackNumber;
        this.artist = artist;
        this.category = category;
        this.sortType = sortType;
    }

    public String getQuery()
    {
        return this.query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getAlbum()
    {
        return this.album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getTrack()
    {
        return this.track;
    }

    public void setTrack(String track)
    {
        this.track = track;
    }

    public String getTrackNumber()
    {
        return this.trackNumber;
    }

    public void setTrackNumber(String trackNumber)
    {
        this.trackNumber = trackNumber;
    }

    public String getArtist()
    {
        return this.artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getCategory()
    {
        return this.category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getSortType()
    {
        return this.sortType;
    }

    public void setSortType(String sortType)
    {
        this.sortType = sortType;
    }
}
