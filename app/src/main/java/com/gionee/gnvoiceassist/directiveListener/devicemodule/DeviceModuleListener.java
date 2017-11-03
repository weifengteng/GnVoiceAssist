package com.gionee.gnvoiceassist.directiveListener.devicemodule;

import android.app.Activity;
import android.content.Context;

import com.baidu.duer.dcs.devicemodule.system.SystemDeviceModule;
import com.baidu.duer.dcs.devicemodule.system.message.ExceptionEncounteredPayload;
import com.baidu.duer.dcs.devicemodule.system.message.SetEndPointPayload;
import com.baidu.duer.dcs.devicemodule.system.message.ThrowExceptionPayload;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GNOAuthActivity;
import com.gionee.gnvoiceassist.home.HomeActivity;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;

/**
 * Created by twf on 2017/8/18.
 */

public class DeviceModuleListener extends BaseDirectiveListener implements SystemDeviceModule.IDeviceModuleListener {
    private Context activityContext;

    public DeviceModuleListener(IBaseFunction baseFunction) {
        super(baseFunction);
        this.activityContext = baseFunction.getHomeActivity();
    }

    @Override
    public void onSetEndpoint(SetEndPointPayload setEndPointPayload) {

    }

    @Override
    public void onThrowException(ThrowExceptionPayload throwExceptionPayload) {
        if(throwExceptionPayload.getCode() == ThrowExceptionPayload.Code.UNAUTHORIZED_REQUEST_EXCEPTION) {
            GNOAuthActivity.startActivity(activityContext);
            if(activityContext instanceof HomeActivity) {
                Activity mainActivity = (Activity) activityContext;
                mainActivity.finish();
            }
            LogUtil.e("DCSF", " ********************  onThrowException by TWF = " + throwExceptionPayload.toString());
        }
    }

    @Override
    public void onExceptionEncountered(ExceptionEncounteredPayload exceptionEncounteredPayload) {

    }

    @Override
    public void onDestroy() {
        activityContext = null;
    }
}
