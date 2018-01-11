package com.gionee.voiceassist.datamodel.card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 12/21/17.
 */

public class ContactListCardEntity extends CardEntity {

    public ContactListCardEntity() {
        setType(CardType.CONTACT_SELECT_CARD);
    }

    public List<ContactItem> contacts = new ArrayList<>();

    public static class ContactItem {
        public String name = "";
        public String phonenum = "";
        //TODO Add Avatar Drawable

        public ContactItem() {

        }

        public ContactItem(String name, String phonenum) {
            this.name = name;
            this.phonenum = phonenum;
        }
    }

    public List<ContactItem> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactItem> contacts) {
        this.contacts = contacts;
    }

    public void addContactItem(String name, String phonenum) {
        contacts.add(new ContactItem(name, phonenum));
    }
}
