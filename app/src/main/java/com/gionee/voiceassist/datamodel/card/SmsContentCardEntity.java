package com.gionee.voiceassist.datamodel.card;

/**
 * 信息撰写卡片数据
 */

public class SmsContentCardEntity extends CardEntity {

    public SmsContentCardEntity() {
        setType(CardType.SMS_CONTENT_CARD);
    }

    private String contactName;
    private String messageContent;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
