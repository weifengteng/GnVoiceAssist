package com.gionee.voiceassist.coreservice.datamodel;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;

import java.util.List;

/**
 * Created by liyingheng on 1/5/18.
 */

public class SmsDirectiveEntity extends DirectiveEntity {

    private List<String> candidateNames;
    private String number;
    private int simslot = -1;
    private SmsAction action;

    public SmsDirectiveEntity() {
        setType(Type.SMS);
    }

    public List<String> getCandidateNames() {
        return candidateNames;
    }

    public void setCandidateNames(List<String> candidateNames) {
        this.candidateNames = candidateNames;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getSimslot() {
        return simslot;
    }

    public void setSimslot(int simslot) {
        this.simslot = simslot;
    }

    public SmsAction getAction() {
        return action;
    }

    public void setAction(SmsAction action) {
        this.action = action;
    }


    public enum SmsAction {
        BY_NAME,
        BY_NUMBER
    }

    @Override
    public String toString() {
        return "SmsDirectiveEntity{" +
                "candidateNames=" + candidateNames +
                ", number='" + number + '\'' +
                ", simslot=" + simslot +
                ", action=" + action +
                '}';
    }
}
