package com.gionee.voiceassist.datamodel.card;

/**
 * 文字卡片数据
 */

public class TextCardEntity extends CardEntity {

    private boolean userAsr = false;

    public TextCardEntity() {
        setType(CardType.TEXT_CARD);
    }

}
