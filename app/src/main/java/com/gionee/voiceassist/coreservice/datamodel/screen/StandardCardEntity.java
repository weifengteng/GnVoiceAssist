package com.gionee.voiceassist.coreservice.datamodel.screen;

import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;

/**
 * Created by liyingheng on 1/2/18.
 */

public class StandardCardEntity extends ScreenDirectiveEntity {

    public StandardCardEntity() {
        setCardType(CardType.STANDARD_CARD);
    }

    @Override
    public void bind(RenderCardPayload dcsPayload) {
        if (dcsPayload.type == RenderCardPayload.Type.StandardCard) {
            setTitle(dcsPayload.title);
            setContent(dcsPayload.content);
            if (dcsPayload.link != null) {

            }
            if (dcsPayload.image != null && dcsPayload.image.src != null) {
                setImageSrc(dcsPayload.image.src);
            }
        }

    }

    private String imageSrc = "";

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

}
