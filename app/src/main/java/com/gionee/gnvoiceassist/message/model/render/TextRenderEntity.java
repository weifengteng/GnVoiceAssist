package com.gionee.gnvoiceassist.message.model.render;

/**
 * Created by liyingheng on 11/7/17.
 */

public class TextRenderEntity extends RenderEntity {

    public TextRenderEntity() {
        setType(Type.TextCard);
    }

    //部分查询回调
    private boolean partial;

    //对话识别码
    private String conversationId;

    //是否为查询原文字
    private boolean queryText;

    public boolean isPartial() {
        return partial;
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isQueryText() {
        return queryText;
    }

    public void setQueryText(boolean queryText) {
        this.queryText = queryText;
    }
}
