package com.gionee.voiceassist.coreservice.datamodel.screen;

import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderVoiceInputTextPayload;

/**
 * Created by liyingheng on 1/2/18.
 */

public class TextCardEntity extends ScreenDirectiveEntity {

    private boolean isVoiceInputText;
    private boolean isFinalResult;
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

    public void bind(RenderVoiceInputTextPayload renderVoiceInputTextPayload) {
        setIsVoiceInputText(true);
        setContent(renderVoiceInputTextPayload.text);
        setIsFinalResult(renderVoiceInputTextPayload.type == RenderVoiceInputTextPayload.Type.FINAL);

    }
    public boolean isVoiceInputText() {
        return isVoiceInputText;
    }

    public TextCardEntity setIsVoiceInputText(boolean voiceInputText) {
        isVoiceInputText = voiceInputText;
        return this;
    }

    public boolean isFinalResult() {
        return isFinalResult;
    }

    public TextCardEntity setIsFinalResult(boolean finalResult) {
        isFinalResult = finalResult;
        return this;
    }
}
