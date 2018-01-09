package com.gionee.voiceassist.coreservice.listener.state;

import com.baidu.duer.dcs.api.IDialogStateListener;
import com.gionee.voiceassist.controller.recordcontrol.RecordController;
import com.gionee.voiceassist.coreservice.CoreService;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.RecognizerState;

import java.util.List;

/**
 * Created by liyingheng on 1/9/18.
 */

public class DialogStateEventListener extends BaseStateListener implements IDialogStateListener {

    private static final String TAG = DialogStateEventListener.class.getSimpleName();
    private DialogState mPreviousDialogState;
    public DialogStateEventListener(List<CoreService.StateCallback> callbacks) {
        super(callbacks);
    }

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
}
