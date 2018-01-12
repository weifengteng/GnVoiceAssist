package com.gionee.voiceassist.datamodel.card;

/**
 * 联系人卡片数据
 */

public class ContactContentCardEntity extends CardEntity {

    public ContactContentCardEntity() {
        setType(CardType.CONTACT_CONTENT_CARD);
    }

    private String contactName;
    private String contactNumber;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
