package com.gionee.voiceassist.datamodel.card;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通列表卡片数据
 */

public class ListCardEntity extends CardEntity {

    public ListCardEntity() {
        setType(CardType.LIST_CARD);
    }

    private List<ListItem> items = new ArrayList<>();

    public static class ListItem {
        public String title;
        public String content;
        public String imgSrc;
        public Link link;
        public ListItem(String title, String content, String imgSrc, String linkSrc, String linkAnchorText) {
            this.title = title;
            this.content = content;
            this.imgSrc = imgSrc;
            this.link = new Link();
            link.src = linkSrc;
            link.anchorText = linkAnchorText;
        }
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    public void addItem(ListItem item) {
        items.add(item);
    }
}
