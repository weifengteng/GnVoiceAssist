package com.gionee.voiceassist.controller.customuserinteraction;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.ClickLinkPayload;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.HandleUnknownUtterancePayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.ErrorHelper;

/**
 * 多轮交互管理controller，管理多轮交互的开始、结束、状态维护等
 * @author twf
 * @date 2018/1/8
 */

public class CuiController implements ICuiControl, CustomUserInteractionDeviceModule.ICustomUserInteractionListener {
    private volatile boolean mShouldStopCurrentInteraction = false;
    private volatile boolean mCustomUserInteractionProcessing = false;
    private ICuiResult mCuiResult;
    private CuiRoundCounter mRoundCounter;


    public CuiController() {
        mRoundCounter = new CuiRoundCounter();
        ((CustomUserInteractionDeviceModule) SdkController.getInstance().getSdkInternalApi()
                .getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction"))
                .setCustomUserInteractionListener(this);
    }

    @Override
    public void startCustomUserInteraction(CustomUserInteractionDeviceModule.PayLoadGenerator payLoadGenerator, ICuiResult resultCallback) {
        if(payLoadGenerator == null) {
            throw new IllegalArgumentException("payload can not be null!");
        }
        if(resultCallback == null) {
            throw new IllegalArgumentException("CUIResult callback can not be null!");
        }
        mCuiResult = resultCallback;
        mRoundCounter.resetCount();
        this.mShouldStopCurrentInteraction = false;
        ((CustomUserInteractionDeviceModule) SdkController.getInstance().getSdkInternalApi()
                .getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction"))
                .setCustomInteractionState(CustomClicentContextMachineState.PHONE, payLoadGenerator);
        mCustomUserInteractionProcessing = true;
    }

    @Override
    public void stopCurrentCustomUserInteraction() {
        mShouldStopCurrentInteraction = true;
        mCustomUserInteractionProcessing = false;
    }

    @Override
    public boolean isCUIShouldStop() {
        if(mRoundCounter.isMaxCount()) {
            return true;
        }
        return mShouldStopCurrentInteraction;
    }

    @Override
    public boolean isCustomUserInteractionProcessing() {
        return mCustomUserInteractionProcessing;
    }

    @Override
    public void onDestroy() {
        ((CustomUserInteractionDeviceModule) SdkController.getInstance().getSdkInternalApi()
                .getDeviceModule("ai.dueros.device_interface.extensions.custom_user_interaction"))
                .setCustomUserInteractionListener(null);
        mRoundCounter = null;
    }

    @Override
    public void onClickLink(ClickLinkPayload clickLinkPayload) {
        String url = clickLinkPayload.getUrl();
        if(!TextUtils.isEmpty(url)) {
            // handle targetUrl
            if(mCuiResult != null) {
                mCuiResult.handleCUInteractionTargetUrl(url);
            }
        } else {
            ErrorHelper.sendError(ErrorCode.SDK_INTERNAL_ERROR, "SDK内部错误。错误信息：customUserInteractionDirectiveReceived url is empty!");
        }
    }

    @Override
    public void onHandleUnknownUtterance(HandleUnknownUtterancePayload handleUnknownUtterancePayload) {
        // handle unknown utterance
        if(mCuiResult != null) {
            mRoundCounter.increaseCount();
            mCuiResult.handleCUInteractionUnknownUtterance();
        }
    }

    class CuiRoundCounter {
        public static final int MAX_COUNT = 2;
        private int mUpraiseCount = 0;

        public boolean isMaxCount() {
            return mUpraiseCount > MAX_COUNT;
        }

        public void increaseCount() {
            mUpraiseCount++;
        }

        public void resetCount() {
            mUpraiseCount = 0;
        }
    }
}
