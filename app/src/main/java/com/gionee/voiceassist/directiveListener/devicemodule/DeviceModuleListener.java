package com.gionee.voiceassist.directiveListener.devicemodule;

import android.app.Activity;
import android.content.Context;

import com.baidu.duer.dcs.devicemodule.system.SystemDeviceModule;
import com.baidu.duer.dcs.devicemodule.system.message.ExceptionEncounteredPayload;
import com.baidu.duer.dcs.devicemodule.system.message.SetEndPointPayload;
import com.baidu.duer.dcs.devicemodule.system.message.ThrowExceptionPayload;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.voiceassist.GNOAuthActivity;
import com.gionee.voiceassist.MainActivity;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.ErrorHelper;

/**
 * Created by twf on 2017/8/18.
 */

public class DeviceModuleListener extends BaseDirectiveListener implements SystemDeviceModule.IDeviceModuleListener {
    private Context activityContext;

    public DeviceModuleListener(IBaseFunction baseFunction) {
        super(baseFunction);
        this.activityContext = baseFunction.getMainActivity();
    }

    @Override
    public void onSetEndpoint(SetEndPointPayload setEndPointPayload) {

    }

    @Override
    public void onThrowException(ThrowExceptionPayload throwExceptionPayload) {
        if(throwExceptionPayload.getCode() == ThrowExceptionPayload.Code.UNAUTHORIZED_REQUEST_EXCEPTION) {
            GNOAuthActivity.startActivity(activityContext);
            if(activityContext instanceof MainActivity) {
                Activity mainActivity = (Activity) activityContext;
                mainActivity.finish();
            }
            ErrorHelper.sendError(ErrorCode.SDK_TOKEN_INVALIDATE, "SDK Token过期。错误信息:" + throwExceptionPayload.getDescription());
            LogUtil.e("DCSF", " ********************  onThrowException by TWF = " + throwExceptionPayload.toString());
        }
    }

    @Override
    public void onExceptionEncountered(ExceptionEncounteredPayload exceptionEncounteredPayload) {
        ErrorHelper.sendError(ErrorCode.SDK_INTERNAL_ERROR, "SDK内部错误。错误信息："
                + exceptionEncounteredPayload.getError().message);
    }

    @Override
    public void onDestroy() {
        activityContext = null;
    }
}
