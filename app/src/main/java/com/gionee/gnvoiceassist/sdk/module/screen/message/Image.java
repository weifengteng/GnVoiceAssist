package com.gionee.gnvoiceassist.sdk.module.screen.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image
{
    private String originPageUrl;
    private String src;

    public Image(@JsonProperty("originPageUrl") String originPageUrl, @JsonProperty("src") String src)
    {
        this.originPageUrl = originPageUrl;
        this.src = src;
    }

    public String getOriginPageUrl()
    {
        return this.originPageUrl;
    }

    public void setOriginPageUrl(String originPageUrl)
    {
        this.originPageUrl = originPageUrl;
    }

    public String getSrc()
    {
        return this.src;
    }

    public void setSrc(String src)
    {
        this.src = src;
    }
}
