package com.gionee.voiceassist.datamodel.card;

/**
 * 卡片数据的基类
 */

public class CardEntity {

    private CardType type = CardType.DEFAULT_CARD;
    private CardAction cardAction = CardAction.NEW;
    private String uid = "";
    private String title = "";
    private String content = "";
    private AppAction openAppAction = new AppAction();
    private Link extLink = new Link();
    private int cardPosition = -1;

    public static class Link {
        public String src = "";
        public String anchorText = "";
    }

    class AppAction {
        String name = "";
        String pkgName = "";
        String action = "";
    }

    protected void setType(CardType type) {
        this.type = type;
    }

    public CardType getType() {
        return type;
    }

    public CardAction getCardAction() {
        return cardAction;
    }

    public void setCardAction(CardAction cardAction) {
        this.cardAction = cardAction;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public AppAction getOpenAppAction() {
        return openAppAction;
    }

    public void setOpenAppAction(AppAction openAppAction) {
        this.openAppAction = openAppAction;
    }

    public Link getExtLink() {
        return extLink;
    }

    public void setExtLink(String src, String anchorText) {
        extLink.src = src;
        extLink.anchorText = anchorText;
    }

    public int getCardPosition() {
        return cardPosition;
    }

    public CardEntity setCardPosition(int cardPosition) {
        this.cardPosition = cardPosition;
        return this;
    }
}
