package com.gionee.voiceassist.coreservice.sdk.module.screen.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {
    private String url;
    private String anchorText;

    public Link(@JsonProperty("url") String url, @JsonProperty("anchorText") String anchorText)
    {
        this.url = url;
        this.anchorText = anchorText;
    }

    public String getUrl()
    {
        return this.url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getAnchorText()
    {
        return this.anchorText;
    }

    public void setAnchorText(String anchorText)
    {
        this.anchorText = anchorText;
    }
}
