package com.gionee.voiceassist.sdk.module.screen.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TextCardPayload extends RenderCardPayload
{
    private String token;
    private String type;
    private String content;
    private Link link;

    public TextCardPayload(@JsonProperty("token") String token, @JsonProperty("type") String type, @JsonProperty("content") String content, @JsonProperty("link") Link link)
    {
        this.token = token;
        this.type = type;
        this.content = content;
        this.link = link;
    }

    public String getToken()
    {
        return this.token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Link getLink()
    {
        return this.link;
    }

    public void setLink(Link link)
    {
        this.link = link;
    }
}
