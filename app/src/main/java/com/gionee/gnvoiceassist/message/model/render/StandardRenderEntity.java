package com.gionee.gnvoiceassist.message.model.render;

import org.json.JSONObject;

/**
 * 标准卡片的数据结构
 */

public class StandardRenderEntity extends RenderEntity {

    public StandardRenderEntity() {
        this.setType(Type.StandardCard);
    }

    //图片
    private ImageModel image;

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }
}
