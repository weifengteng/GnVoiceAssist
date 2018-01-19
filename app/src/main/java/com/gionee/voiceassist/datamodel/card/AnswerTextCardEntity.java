package com.gionee.voiceassist.datamodel.card;

/**
 * 文字卡片数据
 */

public class AnswerTextCardEntity extends CardEntity {

    private boolean userAsr = false;

    public AnswerTextCardEntity() {
        setType(CardType.ANSWER_TEXT_CARD);
    }

}
