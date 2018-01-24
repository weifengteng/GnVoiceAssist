package com.gionee.voiceassist.controller.appcontrol;

import com.gionee.voiceassist.datamodel.card.CardEntity;

/**
 * 屏幕显示事件
 */

public class RenderEvent {

    private CardEntity payload;
    private int cardPosition = -1;

    public RenderEvent(CardEntity payload) {
        this.payload = payload;
    }

    public CardEntity getPayload() {
        return payload;
    }

    public int getCardPosition() {
        return cardPosition;
    }

    public RenderEvent setCardPosition(int cardPosition) {
        this.cardPosition = cardPosition;
        return this;
    }
}
