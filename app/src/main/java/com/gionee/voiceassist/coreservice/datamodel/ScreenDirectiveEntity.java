package com.gionee.voiceassist.coreservice.datamodel;

import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;

/**
 * Created by liyingheng on 12/29/17.
 */

public abstract class ScreenDirectiveEntity extends DirectiveEntity {

    public ScreenDirectiveEntity() {
        setType(Type.SCREEN);
    }

    private CardType cardType;

    private String title;

    private String content;

    private Link link;

    public CardType getCardType() {
        return cardType;
    }

    protected void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public enum CardType {
        TEXT_CARD,
        STANDARD_CARD,
        LIST_CARD,
        IMAGE_LIST_CARD
    }

    public static class Link {
        public String src;
        public String anchor;
        public Link(String src) {
            this.src = src;
        }
    }

    public abstract void bind(RenderCardPayload dcsPayload);

}
