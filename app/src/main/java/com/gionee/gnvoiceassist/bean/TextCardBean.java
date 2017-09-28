package com.gionee.gnvoiceassist.bean;

/**
 * Created by tengweifeng on 9/22/17.
 */

public class TextCardBean {

    private String type;
    private String content;
    private Link link;
    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class Link{
        public String url;
        public String anchorText;
    }

    @Override
    public String toString() {
        return "TextCardBean{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", link=" + link +
                ", token='" + token + '\'' +
                '}';
    }
}
