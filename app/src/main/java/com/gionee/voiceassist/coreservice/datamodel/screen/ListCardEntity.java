package com.gionee.voiceassist.coreservice.datamodel.screen;

import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 1/2/18.
 */

public class ListCardEntity extends ScreenDirectiveEntity {

    private List<ListItem> items = new ArrayList<>();

    public ListCardEntity() {
        setCardType(CardType.LIST_CARD);
    }

    @Override
    public void bind(RenderCardPayload dcsPayload) {

    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    public static class ListItem {
        public String title;
        public String description;
        public String url;
        public String imgSrc;
    }

}
