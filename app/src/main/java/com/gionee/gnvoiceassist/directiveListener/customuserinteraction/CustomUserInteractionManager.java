package com.gionee.gnvoiceassist.directiveListener.customuserinteraction;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.gionee.gnvoiceassist.basefunction.MaxUpriseCounter;
import com.gionee.gnvoiceassist.sdk.SdkManagerImpl;
import com.gionee.gnvoiceassist.statemachine.Scene;
import com.gionee.gnvoiceassist.util.SharedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tengweifeng on 9/18/17.
 */
public class CustomUserInteractionManager {
    private static CustomUserInteractionManager ourInstance = new CustomUserInteractionManager();
    private volatile boolean mShouldStopCurrentInteraction = false;
    private volatile boolean mCustomUserInteractionProcessing = false;
    private String mCurrCUInteractionId;
    private HashMap<String, ICUIDirectiveReceivedInterface> mCustomUserInteractionMap = new HashMap<>();

    private volatile Scene offlineCuiScene;
    private volatile String answerWord = "";
    private volatile List<String> utteranceWords = new ArrayList<>();
    private volatile List<String> utteranceExtraInfo = new ArrayList<>();

    public static CustomUserInteractionManager getInstance() {
        return ourInstance;
    }

    private CustomUserInteractionManager() {
        offlineCuiScene = Scene.IDLE;
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
        ((CustomUserInteractionDeviceModule) SdkManagerImpl.getInstance().getInternalApi()
                .getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction"))
                .setCustomInteractionState(CustomClicentContextMachineState.PHONE, payLoadGenerator);
        mCustomUserInteractionProcessing = true;
    }

    public void startOfflineCustomUserInteraction(Scene scene, String answerWord, List<String> utterances, List<String> utteranceExtraInfo) {
        MaxUpriseCounter.resetUpriseCount();
        setOfflineCuiScene(scene);
        setOfflineCuiAnswerWord(answerWord);
        setOfflineCuiUtteranceWord(utterances);
        setOfflineCuiUtteranceExtraInfo(utteranceExtraInfo);
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
            stopOfflineCui();
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

    public Scene getOfflineCuiScene() {
        return offlineCuiScene;
    }

    public String getOfflineCuiAnswerWord() {
        return answerWord;
    }

    public List<String> getOfflineCuiUtteranceWord() {
        return utteranceWords;
    }

    public List<String> getOfflineCuiUtteranceExtraInfo() {
        return utteranceExtraInfo;
    }

    public synchronized void setOfflineCuiAnswerWord(String answerWord) {
        this.answerWord = answerWord;
    }

    public synchronized void setOfflineCuiScene(Scene scene) {
        offlineCuiScene = scene;
    }

    public synchronized void setOfflineCuiUtteranceWord(List<String> utterances) {
        utteranceWords.clear();
        utteranceWords.addAll(utterances);
    }

    public synchronized void setOfflineCuiUtteranceExtraInfo(List<String> extraInfo) {
        utteranceExtraInfo.clear();
        utteranceExtraInfo.addAll(extraInfo);
    }

    private void stopOfflineCui() {
        offlineCuiScene = Scene.IDLE;
        answerWord = "";
        utteranceExtraInfo.clear();
        utteranceWords.clear();
    }
}
