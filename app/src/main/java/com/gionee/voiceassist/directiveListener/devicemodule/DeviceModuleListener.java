package com.gionee.voiceassist.directiveListener.devicemodule;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.duer.dcs.devicemodule.system.SystemDeviceModule;
import com.baidu.duer.dcs.devicemodule.system.message.ExceptionEncounteredPayload;
import com.baidu.duer.dcs.devicemodule.system.message.SetEndPointPayload;
import com.baidu.duer.dcs.devicemodule.system.message.ThrowExceptionPayload;
import com.baidu.duer.dcs.http.HttpConfig;
import com.baidu.duer.dcs.oauth.api.silent.SilentLoginImpl;
import com.baidu.duer.dcs.systeminterface.IOauth;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;

/**
 * Created by twf on 2017/8/18.
 */

public class DeviceModuleListener extends BaseDirectiveListener implements SystemDeviceModule.IDeviceModuleListener {
    public static final String TAG = DeviceModuleListener.class.getSimpleName();
    private static final String CLIENT_ID = "d8ITlI9aeTPaGcxKKsZit8tq";
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
            retryLogin(throwExceptionPayload);
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

    private void retryLogin(final ThrowExceptionPayload exceptionPayload) {
        IOauth iOauthImpl = new SilentLoginImpl(CLIENT_ID);
        iOauthImpl.getToken(new IOauth.IOauthCallbackListener() {

            @Override
            public void onCancel() {
                Toast.makeText(activityContext.getApplicationContext(), activityContext.getResources()
                                .getString(R.string.login_canceled),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String e) {
                String toastString = TextUtils.isEmpty(e)
                        ? activityContext.getResources()
                        .getString(R.string.login_failed) : e;
                Toast.makeText(activityContext.getApplicationContext(), toastString,
                        Toast.LENGTH_SHORT).show();
                ErrorHelper.sendError(ErrorCode.SDK_TOKEN_INVALIDATE, "SDK Token过期。错误信息:" + exceptionPayload.getDescription());
                LogUtil.e(TAG, " ********************  onThrowException = " + exceptionPayload.toString());
            }

            @Override
            public void onSucceed(String s) {
                // 设置accessToken
                HttpConfig.setAccessToken(s);
                Toast.makeText(activityContext.getApplicationContext(),
                        activityContext.getString(R.string.login_succeed),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
