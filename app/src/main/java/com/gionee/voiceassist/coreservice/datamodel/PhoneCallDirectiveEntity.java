package com.gionee.voiceassist.coreservice.datamodel;

import android.text.TextUtils;

import com.gionee.voiceassist.coreservice.sdk.module.phonecall.message.CandidateCalleeNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 1/3/18.
 */

public class PhoneCallDirectiveEntity extends DirectiveEntity {

    private List<String> candidateNames;
    private List<CandidateCalleeNumber> candidateCalleeNumbers;
    private int simSlot = -1;
    private PhoneCallAction action;


    public PhoneCallDirectiveEntity() {
        setType(Type.PHONECALL);
    }

    public List<String> getCandidateNames() {
        return candidateNames;
    }

    public void setCandidateNames(List<String> candidateNames) {
        this.candidateNames = candidateNames;
    }

    public int getSimSlot() {
        return simSlot;
    }

    public void setSimSlot(int simSlot) {
        this.simSlot = simSlot;
    }

    public List<CandidateCalleeNumber> getCandidateCalleeNumbers() {
        return candidateCalleeNumbers;
    }

    public void setCandidateCalleeNumbers(List<CandidateCalleeNumber> candidateCalleeNumbers) {
        this.candidateCalleeNumbers = candidateCalleeNumbers;
    }

    public void setPhoneByName(List<String> candidateNames, String simslot) {
        setAction(PhoneCallAction.BY_NAME);
        setCandidateNames(candidateNames);
        setSimSlot(convertSimslot(simslot));
    }

    public void setPhoneByNumber(CandidateCalleeNumber candidateCalleeNumber, String simSlot) {
        setAction(PhoneCallAction.BY_NUMBER);
        String displayName = candidateCalleeNumber.getDisplayName();
        if (displayName == null) {
            displayName = "";
        }
        CandidateCalleeNumber calleeNumber = new CandidateCalleeNumber();
        calleeNumber.setDisplayName(displayName);
        calleeNumber.setPhoneNumber(candidateCalleeNumber.getPhoneNumber());
        if (candidateCalleeNumbers == null) {
            candidateCalleeNumbers = new ArrayList<>();
        }
        candidateCalleeNumbers.add(calleeNumber);
        this.simSlot = convertSimslot(simSlot);
    }

    public void setPhoneByCalleeSelect(List<CandidateCalleeNumber> candidateCalleeNumbers, String simSlot) {
        setAction(PhoneCallAction.BY_SELECT_CALLEE);
        this.candidateCalleeNumbers = candidateCalleeNumbers;
        this.simSlot = convertSimslot(simSlot);
    }

    public PhoneCallAction getAction() {
        return action;
    }

    public void setAction(PhoneCallAction action) {
        this.action = action;
    }

    /**
     * 区分打电话的发起方式
     */
    public enum PhoneCallAction {
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

    @Override
    public String toString() {
        return "PhoneCallDirectiveEntity{" +
                "candidateNames=" + candidateNames +
                ", candidateCalleeNumbers=" + candidateCalleeNumbers.toString() +
                ", simSlot=" + simSlot +
                ", action=" + action +
                '}';
    }
}
