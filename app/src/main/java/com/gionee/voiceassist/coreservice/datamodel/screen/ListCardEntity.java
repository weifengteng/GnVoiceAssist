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
        if(dcsPayload.type == RenderCardPayload.Type.ListCard && dcsPayload.list != null) {
            for(RenderCardPayload.ListItem item : dcsPayload.list) {
                if(item != null) {
                    addItem(item);
                }
            }
        }
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

    private void addItem(RenderCardPayload.ListItem item) {
        ListItem listItem = new ListItem();
        listItem.title = item.title;
        listItem.description = item.content;
        listItem.url = item.url;
        if(item.image != null) {
            listItem.imgSrc = item.image.src;
        }

        items.add(listItem);
    }
}
