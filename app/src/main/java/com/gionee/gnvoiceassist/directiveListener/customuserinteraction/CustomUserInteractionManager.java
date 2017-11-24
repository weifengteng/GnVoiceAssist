package com.gionee.gnvoiceassist.directiveListener.customuserinteraction;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.sdk.SdkManagerImpl;
import com.gionee.gnvoiceassist.service.RecognizeManager;
import com.gionee.gnvoiceassist.util.ConnectivityUtils;

import java.util.HashMap;

/**
 * Created by tengweifeng on 9/18/17.
 */
public class CustomUserInteractionManager {
    private static CustomUserInteractionManager ourInstance = new CustomUserInteractionManager();
    private volatile boolean mShouldStopCurrentInteraction = false;
    private volatile boolean mCustomUserInteractionProcessing = false;
    private String mCurrCUInteractionId;
    private HashMap<String, ICUIDirectiveReceivedInterface> mCustomUserInteractionMap = new HashMap<>();
    private String mCustomInteractorMetadata = "";

    public static CustomUserInteractionManager getInstance() {
        return ourInstance;
    }

    private CustomUserInteractionManager() {
    }

    public void startCustomUserInteraction(
            CustomUserInteractionDeviceModule.PayLoadGenerator payLoadGenerator,
            String interactionId,
            ICUIDirectiveReceivedInterface receivedInterface) {
        if(TextUtils.isEmpty(interactionId)) {
            throw new IllegalArgumentException("interactionId can not be empty!");
        }
        if(payLoadGenerator == null) {
            throw new IllegalArgumentException("payload can not be null!");
        }

        if(receivedInterface == null) {
            throw new IllegalArgumentException("receivedInterface can not be null!");
        }
        MaxUpriseCounter.resetUpriseCount();
        this.mShouldStopCurrentInteraction = false;
        this.mCurrCUInteractionId = interactionId;
        mCustomUserInteractionMap.put(mCurrCUInteractionId, receivedInterface);

        //TODO Warning 这里有个对ApplicationContext的引用。尽快处理，避免产生内存泄漏
        if (ConnectivityUtils.isOnline(GnVoiceAssistApplication.getInstance().getApplicationContext())) {
            ((CustomUserInteractionDeviceModule) RecognizeManager.getInstance().getSdkInternalApi()
                    .getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction"))
                    .setCustomInteractionState(CustomClicentContextMachineState.PHONE, payLoadGenerator);
        } else {
            OfflineCustomInteractionProcessor.getInstance().startOfflineCustomInteraction(
                    CustomClicentContextMachineState.PHONE,
                    payLoadGenerator);
        }
        mCustomUserInteractionProcessing = true;
    }

    public boolean shouldStopCurrentInteraction() {
        if(MaxUpriseCounter.isMaxCount()) {
            return true;
        }
        return mShouldStopCurrentInteraction;
    }

    public void setStopCurrentInteraction(boolean shouldStopCurrentInteraction) {
        if(shouldStopCurrentInteraction) {
            mCustomUserInteractionProcessing = false;
        }
        this.mShouldStopCurrentInteraction = shouldStopCurrentInteraction;
    }

    public ICUIDirectiveReceivedInterface getCurrInteractionListener() {
        if(TextUtils.isEmpty(mCurrCUInteractionId)) {
            throw new IllegalStateException("mCurrCUInteractionId is empty!");
        }
        return mCustomUserInteractionMap.get(mCurrCUInteractionId);
    }

    public String getCurrCUInteractionId() {
        return mCurrCUInteractionId;
    }

    public boolean isCustomUserInteractionProcessing() {

        return mCustomUserInteractionProcessing;
    }

    public String getCustomInteractorMetadata() {
        return mCustomInteractorMetadata;
    }

    public void setCustomInteractorMetadata(String metadata) {
        mCustomInteractorMetadata = metadata;
    }

    public void clearCustomInteractorMetadata() {
        mCustomInteractorMetadata = "";
    }
}
