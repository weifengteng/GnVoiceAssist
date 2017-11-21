package com.gionee.gnvoiceassist.message.model.render;

import java.util.List;

/**
 * 图片列表卡片的数据结构
 */

public class ImageListRenderEntity extends RenderEntity {

    public ImageListRenderEntity() {
        this.setType(Type.ImageListCard);
    }

    private List<ImageModel> imageList;

    public List<ImageModel> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageModel> imageList) {
        this.imageList = imageList;
    }
}
