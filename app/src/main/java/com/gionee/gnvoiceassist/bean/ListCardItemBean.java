package com.gionee.gnvoiceassist.bean;

/**
 * Created by tengweifeng on 9/22/17.
 */

public class ListCardItemBean {

    private Image image;
    private String title;
    private String content;
    private String url;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public class Image{
        public String src;
    }

}
