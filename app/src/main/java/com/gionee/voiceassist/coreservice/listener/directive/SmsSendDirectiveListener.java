package com.gionee.voiceassist.coreservice.listener.directive;

import com.gionee.voiceassist.coreservice.datamodel.SmsDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.sms.SmsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.CandidateRecipientNumber;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.SelectRecipientPayload;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.SendSmsByNamePayload;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.SendSmsByNumberPayload;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 发短信垂类监听
 * @author twf
 * @date 2018/1/12
 */

public class SmsSendDirectiveListener extends BaseDirectiveListener implements SmsDeviceModule.ISmsListener {
    public static final String TAG = SmsSendDirectiveListener.class.getSimpleName();

    public SmsSendDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onSendSmsByName(SendSmsByNamePayload payload) {
        LogUtil.d(TAG,"onSendSmsByName(), payload = " + payload);
        sendDirective(generateFromSelectRecipientPayload(payload));
    }

    @Override
    public void onSelectRecipient(SelectRecipientPayload payload) {
        LogUtil.d(TAG,"onSelectRecipient(), payload = " + payload);
        sendDirective(generateFromSelectRecipientPayload(payload));
    }

    @Override
    public void onSendSmsByNumber(SendSmsByNumberPayload payload) {
        LogUtil.d(TAG,"onSendSmsByNumber(), payload = " + payload);
        sendDirective(generateFromSendSmsByNumberPayload(payload));
    }

    private SmsDirectiveEntity generateFromSelectRecipientPayload(SendSmsByNamePayload payload) {
        SmsDirectiveEntity smsDirectiveEntity = new SmsDirectiveEntity();
        smsDirectiveEntity.setAction(SmsDirectiveEntity.SmsAction.BY_NAME)
                .setCandidateNames(payload.getCandidateRecipients())
                .setMessageContent(payload.getMessageContent())
                .setSimSlot(payload.getUseSimIndex())
                .setUseCarrier(payload.getUseCarrier());
        return smsDirectiveEntity;
    }

    private SmsDirectiveEntity generateFromSelectRecipientPayload(SelectRecipientPayload payload) {
        SmsDirectiveEntity smsDirectiveEntity = new SmsDirectiveEntity();
        smsDirectiveEntity.setAction(SmsDirectiveEntity.SmsAction.SELECT_RECIPIENT)
                .setMessageContent(payload.getMessageContent())
                .setUseCarrier(payload.getUseCarrier())
                .setSimSlot(payload.getUseSimIndex())
                .setCandidateRecipients(payload.getCandidateRecipients());
        return smsDirectiveEntity;
    }

    private SmsDirectiveEntity generateFromSendSmsByNumberPayload(SendSmsByNumberPayload payload) {
        SmsDirectiveEntity smsDirectiveEntity = new SmsDirectiveEntity();
        ArrayList<CandidateRecipientNumber> candidateRecipientNumbers = new ArrayList<>();
        candidateRecipientNumbers.add(payload.getRecipient());

        smsDirectiveEntity.setAction(SmsDirectiveEntity.SmsAction.BY_NUMBER)
                .setMessageContent(payload.getMessageContent())
                .setSimSlot(payload.getUseSimIndex())
                .setUseCarrier(payload.getUseCarrier())
                .setCandidateRecipients(candidateRecipientNumbers);

        return smsDirectiveEntity;
    }
}
