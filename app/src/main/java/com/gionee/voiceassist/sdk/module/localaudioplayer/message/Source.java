package com.gionee.voiceassist.sdk.module.localaudioplayer.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Source
{
    private String audioId;
    private String title;
    private String artist;
    private String album;
    private String year;
    private String genre;

    public Source(@JsonProperty("audioId") String audioId, @JsonProperty("title") String title, @JsonProperty("artist") String artist, @JsonProperty("album") String album, @JsonProperty("year") String year, @JsonProperty("genre") String genre)
    {
        this.audioId = audioId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.genre = genre;
    }

    public String getAudioId()
    {
        return this.audioId;
    }

    public void setAudioId(String audioId)
    {
        this.audioId = audioId;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getArtist()
    {
        return this.artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getAlbum()
    {
        return this.album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getYear()
    {
        return this.year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getGenre()
    {
        return this.genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }
}
