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

    /**
     * 获得联系人名字
     * @return 联系人名字
     */
    public String getContactsName() {
        return contactsName;
    }

    private void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    /**
     * 获得候选联系人名字列表
     * @return 候选联系人名字列表
     */
    public List<String> getCandidateName() {
        return candidateName;
    }

    private void setCandidateName(List<String> candidateName) {
        this.candidateName = candidateName;
    }

    /**
     * 取得查询的号码
     * @return 联系人号码
     */
    public String getNumber() {
        return number;
    }

    private void setNumber(String number) {
        this.number = number;
    }

    /**
     * 取得联系人查询动作
     * @return 联系人查询动作。CREATE为创建联系人；SEARCH为搜索联系人。
     */
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

    /**
     * 设置创建联系人场景的联系人信息。
     * @param name 联系人名称
     * @param phonenum 联系人电话号码
     */
    public void setCreateContactsInfo(String name, String phonenum) {
        setAction(ContactsAction.CREATE);
        setContactsName(name);
        setNumber(phonenum);
    }

    /**
     * 设置搜索联系人场景的联系人信息。
     * @param candidateName
     */
    public void setSearchContactsInfo(List<String> candidateName) {
        setAction(ContactsAction.SEARCH);
        setCandidateName(candidateName);
    }

    @Override
    public String toString() {
        return "ContactsDirectiveEntity{" +
                "contactsName='" + contactsName + '\'' +
                ", candidateName=" + candidateName +
                ", number='" + number + '\'' +
                ", action=" + action +
                '}';
    }
}
