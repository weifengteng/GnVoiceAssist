package com.gionee.voiceassist.controller.appcontrol;

import com.gionee.voiceassist.datamodel.card.CardEntity;

/**
 * 屏幕显示事件
 */

public class RenderEvent {

    private CardEntity payload;

    public RenderEvent(CardEntity payload) {
        this.payload = payload;
    }

    public CardEntity getPayload() {
        return payload;
    }
}
