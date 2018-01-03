package com.gionee.voiceassist.coreservice.datamodel;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 1/3/18.
 */

public class PhonecallDirectiveEntity extends DirectiveEntity {

    private List<String> candidateNames;
    private String number;
    private int simslot = -1;
    private PhonecallAction action;


    public PhonecallDirectiveEntity() {
        setType(Type.PHONECALL);
    }

    public List<String> getCandidateNames() {
        return candidateNames;
    }

    public void setCandidateNames(List<String> candidateNames) {
        this.candidateNames = candidateNames;
    }

    public int getSimslot() {
        return simslot;
    }

    public void setSimslot(int simslot) {
        this.simslot = simslot;
    }

    public void setPhoneByName(List<String> candidateNames, String simslot) {
        setAction(PhonecallAction.BY_NAME);
        setCandidateNames(candidateNames);
        setSimslot(convertSimslot(simslot));
    }

    public void setPhoneByNumber(String displayName, String number, String simslot) {
        setAction(PhonecallAction.BY_NUMBER);
        if (displayName == null) {
            displayName = "";
        }
        if (candidateNames == null) {
            candidateNames = new ArrayList<>();
        }
        candidateNames.add(displayName);
        this.number = number;
        this.simslot = convertSimslot(simslot);
    }

    public void setPhoneByCalleeSelect() {
        setAction(PhonecallAction.BY_SELECT_CALLEE);
    }

    public PhonecallAction getAction() {
        return action;
    }

    public void setAction(PhonecallAction action) {
        this.action = action;
    }

    public enum PhonecallAction {
        BY_NAME,
        BY_NUMBER,
        BY_SELECT_CALLEE
    }

    /**
     * 将simslot卡槽编号转换为数字
     * @param simslot 卡槽
     * @return 1为卡一，2为卡二
     */
    private int convertSimslot(String simslot) {
        if (simslot != null && !TextUtils.isEmpty(simslot)) {
            return simslot.equals("2") ? 1:0;
        } else {
            return -1;
        }
    }
}
