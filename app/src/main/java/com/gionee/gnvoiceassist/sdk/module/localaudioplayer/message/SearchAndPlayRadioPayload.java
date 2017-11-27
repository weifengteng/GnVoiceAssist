package com.gionee.gnvoiceassist.sdk.module.localaudioplayer.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchAndPlayRadioPayload
        extends Payload
{
    private String type;
    private String query;
    private String modulation;
    private String frequency;
    private String category;
    private String channel;

    public SearchAndPlayRadioPayload(@JsonProperty("type") String type, @JsonProperty("query") String query, @JsonProperty("modulation") String modulation, @JsonProperty("frequency") String frequency, @JsonProperty("category") String category, @JsonProperty("channel") String channel)
    {
        this.type = type;
        this.query = query;
        this.modulation = modulation;
        this.frequency = frequency;
        this.category = category;
        this.channel = channel;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getQuery()
    {
        return this.query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getModulation()
    {
        return this.modulation;
    }

    public void setModulation(String modulation)
    {
        this.modulation = modulation;
    }

    public String getFrequency()
    {
        return this.frequency;
    }

    public void setFrequency(String frequency)
    {
        this.frequency = frequency;
    }

    public String getCategory()
    {
        return this.category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getChannel()
    {
        return this.channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }
}
