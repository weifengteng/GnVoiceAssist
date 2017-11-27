package com.gionee.gnvoiceassist.sdk.module.screen.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liyingheng on 10/16/17.
 */

public class ListCardItem
{
    private String title;
    private String content;
    private String image;
    private String url;

    public ListCardItem(@JsonProperty("title") String title, @JsonProperty("content") String content, @JsonProperty("image") String image, @JsonProperty("url") String url)
    {
        this.title = title;
        this.content = content;
        this.image = image;
        this.url = url;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getImage()
    {
        return this.image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getUrl()
    {
        return this.url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}

