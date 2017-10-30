package com.gionee.gnvoiceassist.directiveListener.voiceinputvolume;

import com.baidu.duer.dcs.systeminterface.IAudioRecorder;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;

/**
 * Created by twf on 2017/8/16.
 */

public class VoiceInputVolumeListener extends BaseDirectiveListener implements IAudioRecorder.IRecorderListener {
    public static final String TAG = VoiceInputVolumeListener.class.getSimpleName();
    private ScreenRender screenRender;


    public VoiceInputVolumeListener(IBaseFunction baseFunction) {
        super(baseFunction);
        screenRender = baseFunction.getScreenRender();
    }

    @Override
    public void onData(byte[] bytes) {

    }

    @Override
    public void onVolumeChange(int volume) {
//        LogUtil.d(TAG, "onVolumeChange = " + volume);
        screenRender.renderVoiceInputVolume(volume);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onDestroy() {
        if(screenRender != null) {
            screenRender.onDestroy();
            screenRender = null;
        }
    }
}