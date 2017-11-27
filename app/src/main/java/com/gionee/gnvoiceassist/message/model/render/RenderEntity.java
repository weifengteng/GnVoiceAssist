package com.gionee.gnvoiceassist.message.model.render;

import java.io.Serializable;

/**
 * 界面显示数据结构
 */

public class RenderEntity implements Serializable{

    public enum Type {
        TextCard, StandardCard, ListCard, ImageListCard, ChooseBoxCard, ChooseListCard
    }

    private String title = "";

    private Type type;

    private String content = "";

    private LinkModel link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LinkModel getLink() {
        return link;
    }

    public void setLink(LinkModel link) {
        this.link = link;
    }

    /**
     * 链接数据结构
     */
    public static class LinkModel {
        //链接提示文字
        public String anchorText;
        //链接地址
        public String url;
    }

    /**
     * 图片数据结构
     */
    public static class ImageModel {
        //图片地址
        public String src;
    }

    /**
     * 列表条目数据结构
     */
    public static class ListItemModel {
        //标题
        public String title;
        //内容
        public String content;
        //图片
        public ImageModel image;
        //链接
        public String url;
    }

    /**
     * 选择器条目数据结构
     */
    public static class ChooseItemModel {
        //标题
        public String title;
        //内容
        public String content;
        //元数据
        public String metadata;
    }

}
