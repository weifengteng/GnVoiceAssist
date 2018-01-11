package com.gionee.voiceassist.datamodel.card;

/**
 * 普通卡片数据
 */

public class StandardCardEntity extends CardEntity {

    public StandardCardEntity() {
        setType(CardType.STANDARD_CARD);
    }

    private String imgSrc = "";

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
