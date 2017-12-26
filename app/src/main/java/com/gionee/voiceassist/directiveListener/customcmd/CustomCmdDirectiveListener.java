package com.gionee.voiceassist.directiveListener.customcmd;

import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.sdk.module.customcmd.CustomCmdDeviceModule;
import com.gionee.voiceassist.util.LogUtil;

/**
 * Created by twf on 2017/12/26.
 */

public class CustomCmdDirectiveListener extends BaseDirectiveListener implements CustomCmdDeviceModule.ICustomCmdDirectiveListener{
    public static final String TAG = CustomCmdDirectiveListener.class.getSimpleName();
    public CustomCmdDirectiveListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {

    }

    @Override
    public void onLaunchAliPaymentCode() {
        // TODO:
        LogUtil.d(TAG, "onLaunchAliPaymentCode");
    }

    @Override
    public void onLaunchAliPayScan() {
        // TODO:
        LogUtil.d(TAG, "onLaunchAliPayScan");
    }

    @Override
    public void onInstantHeartRate() {
        //TODO:

        LogUtil.d(TAG, "onInstantHeartRate");
    }

    @Override
    public void onShowMobileDeviceInfo() {
        // TODO:
        LogUtil.d(TAG, "onShowMobileDeviceInfo");
    }

    @Override
    public void onLaunchPaiLiTao() {
        // TODO:
        LogUtil.d(TAG, "onLaunchPaiLiTao");
    }

    @Override
    public void onStartTimer() {
        // TODO:
        LogUtil.d(TAG, "onStartTimer");
    }
}
