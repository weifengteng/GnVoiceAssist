package com.gionee.gnvoiceassist.directiveListener.customuserinteraction;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.gionee.gnvoiceassist.util.LogUtil;

import java.util.List;

/**
 * 离线多轮交互处理器
 * 用于处理离线多轮交互的命令
 */

public class OfflineCustomInteractionProcessor {

    private static final String TAG = OfflineCustomInteractionProcessor.class.getSimpleName();
    private static OfflineCustomInteractionProcessor sINSTANCE;

    private List<CustomClientContextHyperUtterace> mHyperUtteraces;

    private OfflineCustomInteractionProcessor() {

    }

    public static OfflineCustomInteractionProcessor getInstance() {
        if (sINSTANCE == null) {
            sINSTANCE = new OfflineCustomInteractionProcessor();
        }
        return sINSTANCE;
    }

    public void startOfflineCustomInteraction(
            CustomClicentContextMachineState curState,
            CustomUserInteractionDeviceModule.PayLoadGenerator payLoadGenerator) {
        CustomClientContextPayload payload =
                (CustomClientContextPayload) payLoadGenerator.generateContextPayloadByInteractionState(curState);

        mHyperUtteraces = payload.getHyperUtterances();
    }

    public void onOfflineAsrResult(String rawResult) {
        if (mHyperUtteraces != null) {
            CustomClientContextHyperUtterace matchUtterance;
            for (CustomClientContextHyperUtterace hyperUtterace:mHyperUtteraces) {
                for (String utterance:hyperUtterace.getUtterances()) {
                    if (TextUtils.equals(rawResult,utterance)) {
                        matchUtterance = hyperUtterace;
                        replyCuiResult(matchUtterance);
                        return;
                    }
                }
            }
            replyCuiResult(null);
        }
    }

    private void replyCuiResult(CustomClientContextHyperUtterace hyperUtterace) {
        ICUIDirectiveReceivedInterface receivedInterface = CustomUserInteractionManager.getInstance().getCurrInteractionListener();
        String currCUInteractionId = CustomUserInteractionManager.getInstance().getCurrCUInteractionId();
        if (receivedInterface != null) {
            if (hyperUtterace != null && hyperUtterace.getUrl() != null && TextUtils.isEmpty(hyperUtterace.getUrl())) {
                String url = hyperUtterace.getUrl();
                receivedInterface.handleCUInteractionTargetUrl(currCUInteractionId, url);
            } else {
                receivedInterface.handleCUInteractionUnknownUtterance(currCUInteractionId);
            }
        }
    }



}
