package com.gionee.voiceassist.coreservice.datamodel;

import com.gionee.voiceassist.coreservice.sdk.module.sms.message.CandidateRecipient;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.CandidateRecipientNumber;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.SendSmsByNamePayload;

import java.util.List;

/**
 * 发短信垂类对应的数据实体类
 * @author liyingheng
 * @date 1/5/18
 */

public class SmsDirectiveEntity extends DirectiveEntity {

    private List<CandidateRecipient> candidateNames;
    private List<CandidateRecipientNumber> candidateRecipients;
    private String messageContent;
    private String simSlot;
    private String useCarrier = "";
    private SmsAction action;

    public SmsDirectiveEntity() {
        setType(Type.SMS);
    }

    public List<CandidateRecipient> getCandidateRecipients() {
        return candidateNames;
    }

    public List<CandidateRecipientNumber> getCandidateRecipientNumbers() {
        return candidateRecipients;
    }

    public String getSimSlot() {
        return simSlot;
    }

    public String getUseCarrier() {
        return useCarrier;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public SmsAction getAction() {
        return action;
    }

    public enum SmsAction {
        BY_NAME,
        BY_NUMBER,
        SELECT_RECIPIENT
    }

    public SmsDirectiveEntity setCandidateNames(List<CandidateRecipient> candidateNames) {
        this.candidateNames = candidateNames;
        return this;
    }

    public SmsDirectiveEntity setCandidateRecipients(List<CandidateRecipientNumber> candidateRecipients) {
        this.candidateRecipients = candidateRecipients;
        return this;
    }

    public SmsDirectiveEntity setMessageContent(String messageContent) {
        this.messageContent = messageContent;
        return this;
    }

    public SmsDirectiveEntity setSimSlot(String simSlot) {
        this.simSlot = simSlot;
        return this;
    }

    public SmsDirectiveEntity setUseCarrier(String useCarrier) {
        this.useCarrier = useCarrier;
        return this;
    }

    public SmsDirectiveEntity setAction(SmsAction action) {
        this.action = action;
        return this;
    }

    @Override
    public String toString() {
        return "SmsDirectiveEntity{" +
                "candidateNames=" + candidateNames +
                ", candidateRecipients=" + candidateRecipients +
                ", messageContent='" + messageContent + '\'' +
                ", simSlot=" + simSlot +
                ", useCarrier='" + useCarrier + '\'' +
                ", action=" + action +
                '}';
    }
}
