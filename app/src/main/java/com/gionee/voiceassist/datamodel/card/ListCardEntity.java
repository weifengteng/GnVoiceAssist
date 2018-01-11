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

    private List<ListItem> item = new ArrayList<>();

    public class ListItem {
        public String title;
        public String content;
        public String imgSrc;
        public Link link;
    }

    public List<ListItem> getItem() {
        return item;
    }

    public void setItem(List<ListItem> item) {
        this.item = item;
    }
}
