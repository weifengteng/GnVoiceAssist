package com.gionee.gnvoiceassist.sdk.module.screen.message;

import com.baidu.duer.dcs.framework.message.Payload;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageListCardPayload extends RenderCardPayload {
    private String token;
    private String type;
    private List<Image> imageList;

    public ImageListCardPayload(@JsonProperty("token") String token, @JsonProperty("type") String type, @JsonProperty("imageList") List<Image> imageList)
    {
        this.token = token;
        this.type = type;
        this.imageList = imageList;
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

    public List<Image> getImageList()
    {
        return this.imageList;
    }

    public void setImageList(List<Image> imageList)
    {
        this.imageList = imageList;
    }
}
