package com.gionee.voiceassist.coreservice.listener.state;

import com.baidu.duer.dcs.framework.internalapi.IErrorListener;
import com.gionee.voiceassist.coreservice.CoreService;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;

import java.util.List;

/**
 * Created by liyingheng on 1/9/18.
 */

public class ErrorEventListener extends BaseStateListener implements IErrorListener {

    private static final String TAG = ErrorEventListener.class.getSimpleName();

    public ErrorEventListener(List<CoreService.StateCallback> callbacks) {
        super(callbacks);
    }

    @Override
    public void onErrorCode(ErrorCode errorCode) {
        LogUtil.e(TAG, "ErrorListener onErrorCode = " + errorCode);
        ErrorHelper.sendError
                (com.gionee.voiceassist.util.ErrorCode.SDK_UNKNOWN_ERROR, "SDK未知错误：" + errorCode);
    }
}
