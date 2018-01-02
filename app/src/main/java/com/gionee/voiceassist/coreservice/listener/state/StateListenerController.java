package com.gionee.voiceassist.coreservice.listener.state;

import com.baidu.dcs.acl.AsrEventStatus;
import com.baidu.duer.dcs.api.IDialogStateListener;
import com.baidu.duer.dcs.devicemodule.voiceinput.VoiceInputDeviceModule;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.IVoiceListener;
import com.baidu.duer.dcs.framework.internalapi.IErrorListener;
import com.gionee.voiceassist.coreservice.CoreService;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;

import java.util.List;

/**
 * Created by liyingheng on 1/2/18.
 */

public class StateListenerController {

    private static final String TAG = StateListenerController.class.getSimpleName();

    private List<CoreService.StateCallback> mStateCallbacks;

    private IDialogStateListener dialogStateListener = new IDialogStateListener() {
        @Override
        public void onDialogStateChanged(DialogState dialogState) {
            LogUtil.d(TAG, "DialogStateListener onDialogStateChanged: " + dialogState);
        }
    };
    private IVoiceListener voiceListener = new IVoiceListener() {
        @Override
        public void onBegin() {
            for (CoreService.StateCallback callback:mStateCallbacks) {
                callback.onRecordStart();
            }
        }

        @Override
        public void onEnd() {
            for (CoreService.StateCallback callback:mStateCallbacks) {
                callback.onRecordStop();
            }
        }
    };
    private IErrorListener errorListener = new IErrorListener() {
        @Override
        public void onErrorCode(ErrorCode errorCode) {
            LogUtil.e(TAG, "ErrorListener onErrorCode = " + errorCode);
            ErrorHelper.sendError
                    (com.gionee.voiceassist.util.ErrorCode.SDK_UNKNOWN_ERROR, "SDK未知错误：" + errorCode);
        }
    };

    public StateListenerController() {

    }

    public void setStateCallbacks(List<CoreService.StateCallback> callbacks) {
        mStateCallbacks = callbacks;
    }

    public void init() {
        SdkController.getInstance().getSdkInternalApi().addVoiceListener(voiceListener);
        SdkController.getInstance().getSdkInternalApi().addErrorListener(errorListener);
    }

    public void release() {
        SdkController.getInstance().getSdkInternalApi().removeVoiceListener(voiceListener);
        SdkController.getInstance().getSdkInternalApi().removeErrorListener(errorListener);
    }

}
