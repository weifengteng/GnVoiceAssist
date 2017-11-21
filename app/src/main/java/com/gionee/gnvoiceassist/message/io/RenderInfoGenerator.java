package com.gionee.gnvoiceassist.message.io;

import com.gionee.gnvoiceassist.message.model.render.ImageListRenderEntity;
import com.gionee.gnvoiceassist.message.model.render.ListRenderEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.message.model.render.StandardRenderEntity;
import com.gionee.gnvoiceassist.message.model.render.TextRenderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于生成RenderEntity的Builder类
 */

public class RenderInfoGenerator {

    private RenderInfoGenerator(){

    }

    /**
     * 生成TextRenderEntity生成器
     */
    public static class GenerateText {

        String title;
        String content;
        RenderEntity.LinkModel link;
        boolean query;
        boolean partial;
        String conversationId;


        public GenerateText() {
            title = "";
            content = "";
            query = false;
            link = new RenderEntity.LinkModel();
        }

        public GenerateText setTitle(String title) {
            this.title = title;
            return this;
        }

        public GenerateText setContent(String content) {
            this.content = content;
            return this;
        }

        public GenerateText setLink(String anchorText, String url) {
            link.anchorText = anchorText;
            link.url = url;
            return this;
        }

        public GenerateText setQuery(boolean query) {
            this.query = query;
            return this;
        }

        public GenerateText setPartial(boolean partial) {
            this.partial = partial;
            return this;
        }

        public GenerateText setConversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public TextRenderEntity build() {
            TextRenderEntity entity = new TextRenderEntity();
            entity.setTitle(title);
            entity.setContent(content);
            entity.setLink(link);
            entity.setConversationId(conversationId);
            entity.setPartial(partial);
            entity.setQueryText(query);
            return entity;
        }

    }

    /**
     * 生成StandardRenderEntity
     */
    public static class GenerateStandard {
        String title;
        String content;
        RenderEntity.LinkModel link;
        RenderEntity.ImageModel image;

        public GenerateStandard() {
            link = new RenderEntity.LinkModel();
            image = new RenderEntity.ImageModel();
        }

        public GenerateStandard setTitle(String title) {
            this.title = title;
            return this;
        }

        public GenerateStandard setContent(String content) {
            this.content = content;
            return this;
        }

        public GenerateStandard setLink(String anchorText, String url) {
            link.anchorText = anchorText;
            link.url = url;
            return this;
        }

        public GenerateStandard setImage(String imgSrc) {
            image.src = imgSrc;
            return this;
        }

        public StandardRenderEntity build() {
            final StandardRenderEntity entity = new StandardRenderEntity();
            entity.setTitle(title);
            entity.setContent(content);
            entity.setLink(link);
            entity.setImage(image);
            return entity;
        }

    }

    /**
     * 生成ListRenderEntity
     */
    public static class GenerateList {
        List<RenderEntity.ListItemModel> listItems;
        public GenerateList() {
            listItems = new ArrayList<>();
        }

        public GenerateList addItem(String title, String content, String imageSrc, String url) {
            RenderEntity.ListItemModel item = new RenderEntity.ListItemModel();
            item.title = title;
            item.content = content;
            item.image.src = imageSrc;
            item.url = url;
            listItems.add(item);
            return this;
        }

        public ListRenderEntity build() {
            final ListRenderEntity entity = new ListRenderEntity();
            entity.setList(listItems);
            return entity;
        }
    }

    /**
     * 生成ImageListRender
     */
    public static class GenerateImageList {
        List<RenderEntity.ImageModel> images;
        public GenerateImageList() {
            images = new ArrayList<>();
        }

        public GenerateImageList addImage(String src) {
            RenderEntity.ImageModel image = new RenderEntity.ImageModel();
            image.src = src;
            images.add(image);
            return this;
        }

        public ImageListRenderEntity build() {
            ImageListRenderEntity entity = new ImageListRenderEntity();
            entity.setImageList(images);
            return entity;
        }

    }

}
