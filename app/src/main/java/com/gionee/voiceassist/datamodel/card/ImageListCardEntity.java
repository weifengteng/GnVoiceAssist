package com.gionee.voiceassist.datamodel.card;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片列表卡片数据
 */

public class ImageListCardEntity extends CardEntity {

    public ImageListCardEntity() {
        setType(CardType.IMAGE_LIST_CARD);
    }

    private List<ImageItem> imageItems = new ArrayList<>();

    public class ImageItem {
        public ImageItem(String imgSrc) {
            this.imgSrc = imgSrc;
        }
        public String imgSrc = "";
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    public void addImageItem(String imgSrc) {
        imageItems.add(new ImageItem(imgSrc));
    }
}
