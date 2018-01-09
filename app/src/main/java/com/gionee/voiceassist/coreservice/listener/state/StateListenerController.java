package com.gionee.voiceassist.coreservice.listener.state;

import com.baidu.duer.dcs.api.IDialogStateListener;
import com.baidu.duer.dcs.framework.IVoiceListener;
import com.baidu.duer.dcs.framework.internalapi.IErrorListener;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.coreservice.CoreService;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.RecognizerState;

import java.util.List;

/**
 * Created by liyingheng on 1/2/18.
 */

public class StateListenerController {

    private static final String TAG = StateListenerController.class.getSimpleName();

    private List<CoreService.StateCallback> mStateCallbacks;
    private IDialogStateListener.DialogState mPreviousDialogState;

    private IDialogStateListener dialogStateListener = new IDialogStateListener() {
        @Override
        public void onDialogStateChanged(DialogState dialogState) {
            LogUtil.d(TAG, "DialogStateListener onDialogStateChanged: " + dialogState);
            RecognizerState state = RecognizerState.IDLE;
            switch (dialogState) {
                case IDLE:
                    state = RecognizerState.IDLE;
                    break;
                case LISTENING:
                    state = RecognizerState.RECORDING;
                    break;
                case THINKING:
                    state = RecognizerState.THINKING;
                    break;
                case SPEAKING:
                    state = RecognizerState.SPEAKING;
                    break;
            }
            for (CoreService.StateCallback callback:mStateCallbacks) {
                callback.onRecognizeStateChanged(state);
            }
            if (dialogState != mPreviousDialogState) {
                if (dialogState == DialogState.LISTENING) {
                    RecordController.getInstance().setSDKRecording(true);
                    for (CoreService.StateCallback callback:mStateCallbacks) {
                        callback.onRecordStart();
                    }
                } else if (mPreviousDialogState == DialogState.LISTENING){
                    RecordController.getInstance().setSDKRecording(false);
                    for (CoreService.StateCallback callback:mStateCallbacks) {
                        callback.onRecordStop();
                    }
                }
                mPreviousDialogState = dialogState;
            }
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
        // TODO:
        SdkController.getInstance().getSdkInstance().getVoiceRequest().addDialogStateListener(dialogStateListener);
        SdkController.getInstance().getSdkInternalApi().addErrorListener(errorListener);
    }

    public void release() {
        // TODO:
        SdkController.getInstance().getSdkInternalApi().removeErrorListener(errorListener);
        SdkController.getInstance().getSdkInstance().getVoiceRequest().removeDialogStateListener(dialogStateListener);
    }

}
