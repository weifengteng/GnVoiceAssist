package com.gionee.gnvoiceassist.service;

/**
 * Created by liyingheng on 11/4/17.
 */

public interface IDirectiveListenerCallback {

    /**
     *
     */
    void onDirectiveResponse();

    /**
     * 输入音量改变回调
     * @param level 输入音量等级
     */
    void onVoiceInputVolume(int level);

}
