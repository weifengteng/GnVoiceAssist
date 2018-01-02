package com.gionee.voiceassist.coreservice.sdk.module.screen.message;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListCardPayload extends RenderCardPayload {
    private String token;
    private String type;
    private String title;
    private List<ListCardItem> list;
    private Link link;

    public ListCardPayload(@JsonProperty("token") String token, @JsonProperty("type") String type, @JsonProperty("title") String title, @JsonProperty("list") List<ListCardItem> list, @JsonProperty("link") Link link) {
        this.token = token;
        this.type = type;
        this.title = title;
        this.list = list;
        this.link = link;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ListCardItem> getList() {
        return this.list;
    }

    public void setList(List<ListCardItem> list) {
        this.list = list;
    }

    public Link getLink() {
        return this.link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
