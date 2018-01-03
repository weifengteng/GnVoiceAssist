package com.gionee.voiceassist.coreservice.datamodel.screen;

import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 1/2/18.
 */

public class ImagelistCardEntity extends ScreenDirectiveEntity {

    private List<String> images = new ArrayList<>();

    public ImagelistCardEntity() {
        setCardType(CardType.IMAGE_LIST_CARD);
    }

    @Override
    public void bind(RenderCardPayload dcsPayload) {
        if (dcsPayload.type == RenderCardPayload.Type.ImageListCard && dcsPayload.imageList != null) {
            for (RenderCardPayload.ImageStructure item: dcsPayload.imageList) {
                addImage(item.src);
            }
        }
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void addImage(String src) {
        images.add(src);
    }
}
