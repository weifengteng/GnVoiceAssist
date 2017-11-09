package com.gionee.gnvoiceassist.message.model.metadata;

import java.util.List;

/**
 * Created by liyingheng on 11/7/17.
 */

public class PhonecallMetadata extends Metadata{

    //联系人信息
    private List<ContactsMetadata> contacts;

    //卡槽
    private String simSlot;

    //运营商名称
    private String carrier;

    public List<ContactsMetadata> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsMetadata> contacts) {
        this.contacts = contacts;
    }

    public String getSimSlot() {
        return simSlot;
    }

    public void setSimSlot(String simSlot) {
        this.simSlot = simSlot;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

}
