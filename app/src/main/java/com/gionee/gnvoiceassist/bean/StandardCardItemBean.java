package com.gionee.gnvoiceassist.bean;

/**
 * Created by tengweifeng on 9/22/17.
 */

public class StandardCardItemBean {

    private String type;
    private Image image;
    private String title;
    private String content;
    private Link link;
    private String token;


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class Image{
        public String src;
    }

    public class Link{
        public String url;
        public String anchorText;
    }

    @Override
    public String toString() {
        return "StandardCardItemBean{" +
                "type='" + type + '\'' +
                ", image=" + image +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link=" + link +
                ", token='" + token + '\'' +
                '}';
    }
}
