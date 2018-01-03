package com.gionee.voiceassist.coreservice.listener.directive;

import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.coreservice.datamodel.GioneeCustomDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.CustomCmdDeviceModule;
import com.gionee.voiceassist.util.LogUtil;

import java.util.List;

/**
 * Created by twf on 2017/12/26.
 */

public class CustomCmdDirectiveListener extends BaseDirectiveListener implements CustomCmdDeviceModule.ICustomCmdDirectiveListener{
    public static final String TAG = CustomCmdDirectiveListener.class.getSimpleName();

    public CustomCmdDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onLaunchAliPaymentCode() {
        // TODO:
        LogUtil.d(TAG, "onLaunchAliPaymentCode");
        sendDirective(createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction.LAUNCH_ALIPAY_PAYMENT_CODE));
    }

    @Override
    public void onLaunchAliPayScan() {
        // TODO:
        LogUtil.d(TAG, "onLaunchAliPayScan");
        sendDirective(createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction.LAUNCH_ALIPAY_SCAN));
    }

    @Override
    public void onInstantHeartRate() {
        //TODO:
        LogUtil.d(TAG, "onInstantHeartRate");
        sendDirective(createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction.LAUNCH_HEARTRATE));
    }

    @Override
    public void onShowMobileDeviceInfo() {
        // TODO:
        LogUtil.d(TAG, "onShowMobileDeviceInfo");
        sendDirective(createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction.SHOW_MOBILE_DEVICE_INFO));
    }

    @Override
    public void onLaunchPaiLiTao() {
        // TODO:
        LogUtil.d(TAG, "onLaunchPaiLiTao");
        sendDirective(createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction.LAUNCH_ALIPAY_PAILITAO));
    }

    @Override
    public void onStartTimer() {
        // TODO:
        LogUtil.d(TAG, "onStartTimer");
        sendDirective(createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction.START_TIMER));
    }

    private GioneeCustomDirectiveEntity createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction action) {
        return createPayload(action,"");
    }

    private GioneeCustomDirectiveEntity createPayload(GioneeCustomDirectiveEntity.GioneeCustomAction action, String msg) {
        GioneeCustomDirectiveEntity payload = new GioneeCustomDirectiveEntity();
        payload.setAction(action);
        payload.setMsg(msg);
        return payload;
    }
}
