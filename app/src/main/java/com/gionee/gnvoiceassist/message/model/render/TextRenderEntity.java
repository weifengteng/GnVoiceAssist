package com.gionee.gnvoiceassist.message.model.render;

/**
 * Created by liyingheng on 11/7/17.
 */

public class TextRenderEntity extends RenderEntity {

    //部分查询回调
    private boolean partical;

    //对话识别码
    private long conversationId;

    //是否为查询原文字
    private boolean queryText;

    public boolean isPartical() {
        return partical;
    }

    public void setPartical(boolean partical) {
        this.partical = partical;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isQueryText() {
        return queryText;
    }

    public void setQueryText(boolean queryText) {
        this.queryText = queryText;
    }
}
