package com.gionee.gnvoiceassist.directiveListener.voiceinputvolume;

import com.baidu.duer.dcs.systeminterface.BaseAudioRecorder;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;

/**
 * Created by twf on 2017/8/16.
 */

public class VoiceInputVolumeListener implements BaseAudioRecorder.IRecorderListener {
    public static final String TAG = VoiceInputVolumeListener.class.getSimpleName();
    private ScreenRender screenRender;


    public VoiceInputVolumeListener() {
        //TODO 处理音频输入音量的显示
//        screenRender = baseFunction.getScreenRender();
    }

    @Override
    public void onData(byte[] bytes) {

    }

    @Override
    public void onError(String s) {

    }

    public void onVolumeChange(int volume) {
//        LogUtil.d(TAG, "onVolumeChange = " + volume);
        screenRender.renderVoiceInputVolume(volume);
    }

    public void onDestroy() {
        if(screenRender != null) {
            screenRender.onDestroy();
            screenRender = null;
        }
    }
}
