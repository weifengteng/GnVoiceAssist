package com.gionee.voiceassist.coreservice.listener.state;

import com.baidu.duer.dcs.api.IDialogStateListener;
import com.baidu.duer.dcs.devicemodule.voiceoutput.VoiceOutputDeviceModule;
import com.baidu.duer.dcs.framework.internalapi.IErrorListener;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.coreservice.CoreService;
import com.gionee.voiceassist.coreservice.sdk.SdkController;

import java.util.List;

/**
 * Created by liyingheng on 1/2/18.
 */

public class StateListenerController {

    private static final String TAG = StateListenerController.class.getSimpleName();
    private boolean listenerInited = false;
    private boolean listenerInstalled = false;
    private List<CoreService.StateCallback> mStateCallbacks;

    private IDialogStateListener dialogStateListener;
    private IErrorListener errorListener;
    private VoiceOutputDeviceModule.IVoiceOutputListener ttsListener;

    public void setStateCallbacks(List<CoreService.StateCallback> callbacks) {
        mStateCallbacks = callbacks;
    }

    public void init() {
        if (!listenerInited) {
            initListener();
        }
        registerListener();
    }

    private void initListener() {
        dialogStateListener = new DialogStateEventListener(mStateCallbacks);
        errorListener = new ErrorEventListener(mStateCallbacks);
        ttsListener = new TtsEventListener(mStateCallbacks);
        listenerInited = true;
    }

    private void registerListener() {
        SdkController.getInstance().getSdkInstance().getVoiceRequest().addDialogStateListener(dialogStateListener);
        SdkController.getInstance().getSdkInternalApi().addErrorListener(errorListener);
        ((VoiceOutputDeviceModule)SdkController.getInstance().getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.voice_output"))
                .addVoiceOutputListener(ttsListener);
        listenerInstalled = true;
    }

    private void unregisterListener() {
        // TODO: 因为这个要调用DeviceModule，因此需要先释放这个，再释放DeviceModule
        ((VoiceOutputDeviceModule)SdkController.getInstance().getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.voice_output"))
                .removeVoiceOutputListener(ttsListener);
        SdkController.getInstance().getSdkInternalApi().removeErrorListener(errorListener);
        SdkController.getInstance().getSdkInstance().getVoiceRequest().removeDialogStateListener(dialogStateListener);
        listenerInstalled = false;
    }

    public void release() {
        unregisterListener();
    }

}
