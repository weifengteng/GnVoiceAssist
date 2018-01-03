package com.gionee.voiceassist.coreservice.datamodel;

import java.util.List;

/**
 * Created by liyingheng on 1/3/18.
 */

public class ContactsDirectiveEntity extends DirectiveEntity {

    private String contactsName;
    private List<String> candidateName;
    private String number;
    private ContactsAction action;

    public ContactsDirectiveEntity() {
        setType(Type.CONTACTS);
    }

    public String getContactsName() {
        return contactsName;
    }

    private void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public List<String> getCandidateName() {
        return candidateName;
    }

    private void setCandidateName(List<String> candidateName) {
        this.candidateName = candidateName;
    }

    public String getNumber() {
        return number;
    }

    private void setNumber(String number) {
        this.number = number;
    }

    public ContactsAction getAction() {
        return action;
    }

    public void setAction(ContactsAction action) {
        this.action = action;
    }

    public enum ContactsAction {
        CREATE,
        SEARCH
    }

    public void setCreateContactsInfo(String name, String phonenum) {
        setAction(ContactsAction.CREATE);
        setContactsName(name);
        setNumber(phonenum);
    }

    public void setSearchContactsInfo(List<String> candidateName) {
        setAction(ContactsAction.SEARCH);
        setCandidateName(candidateName);
    }
}
