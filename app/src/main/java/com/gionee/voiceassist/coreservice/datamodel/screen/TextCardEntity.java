package com.gionee.voiceassist.coreservice.datamodel.screen;

import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;

/**
 * Created by liyingheng on 1/2/18.
 */

public class TextCardEntity extends ScreenDirectiveEntity {

    public TextCardEntity() {
        setCardType(CardType.TEXT_CARD);
    }

    public void bind(RenderCardPayload dcsPayload) {
        if (dcsPayload.type == RenderCardPayload.Type.TextCard) {
            setTitle(dcsPayload.title);
            setContent(dcsPayload.content);
            if (dcsPayload.link != null && dcsPayload.link.url != null) {
                setLink(new Link(dcsPayload.link.url));
            }
        }
    }

}
