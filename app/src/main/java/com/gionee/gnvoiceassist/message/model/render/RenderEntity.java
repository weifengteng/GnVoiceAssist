package com.gionee.gnvoiceassist.message.model.render;

/**
 * 界面显示数据结构
 */

public class RenderEntity {

    public enum Type {
        TextCard, StandardCard, ListCard, ImageListCard, ChooseBoxCard, ChooseListCard
    }

    private String title;

    private String content;

    private LinkModel link;

    /**
     * 链接数据结构
     */
    class LinkModel {
        //链接提示文字
        public String anchorText;
        //链接地址
        public String url;
    }

    /**
     * 图片数据结构
     */
    class ImageModel {
        //图片地址
        public String src;
    }

    /**
     * 列表条目数据结构
     */
    class ListItemModel {
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
    class ChooseItemModel {
        //标题
        public String title;
        //内容
        public String content;
        //元数据
        public String metadata;
    }

}
