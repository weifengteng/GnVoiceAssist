package com.gionee.gnvoiceassist.directiveListener.voiceinput;

import com.baidu.duer.dcs.devicemodule.voiceinput.VoiceInputDeviceModule;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.util.T;

/**
 * Created by twf on 2017/8/17.
 */

public class VoiceInputListener extends BaseDirectiveListener implements VoiceInputDeviceModule.IVoiceInputListener {
    public static final String TAG = VoiceInputListener.class.getSimpleName();
    private IVoiceInputEventListener voiceInputEvent;

    public VoiceInputListener(IVoiceInputEventListener voiceInputEvent) {
        super(null);
        this.voiceInputEvent = voiceInputEvent;
    }

    @Override
    public void onStartRecord() {
        voiceInputEvent.onVoiceInputStart();
    }

    @Override
    public void onFinishRecord() {
        voiceInputEvent.onVoiceInputStop();
    }

    @Override
    public void onSucceed(int i) {
        // TODO:
        LogUtil.d(TAG, "onSucceed   statusCode: " + i);
        if(i != 200) {
            voiceInputEvent.onVoiceInputStop();
            String msg = GnVoiceAssistApplication.getInstance().
                    getResources().getString(R.string.voice_err_msg) + "onSucceed   statusCode: " + i;
            T.showShort(msg);
        }
    }

    @Override
    public void onFailed(String s) {
        // TODO:
        LogUtil.d(TAG, "onFailed    errMsg：" + s);
//        stopRecording();
        voiceInputEvent.onVoiceInputStop();
        String errorMsg = GnVoiceAssistApplication.getInstance().
                getResources().getString(R.string.voice_err_msg) + "onFailed    errMsg：" + s;
        T.showShort(errorMsg);
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {

    }
}
