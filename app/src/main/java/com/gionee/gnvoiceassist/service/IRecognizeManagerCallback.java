package com.gionee.gnvoiceassist.service;

import com.gionee.gnvoiceassist.util.Constants;

/**
 * Created by liyingheng on 11/3/17.
 */

public interface IRecognizeManagerCallback {

    /**
     * 引擎状态改变回调
     * @param state 引擎状态
     */
    void onEngineState(Constants.EngineState state);

    /**
     * 录音开始
     */
    void onRecordStart();

    /**
     * 录音停止
     */
    void onRecordStop();

    /**
     * 录音识别系统错误
     */
    void onRecordError();

    /**
     * 录音识别状态回调
     * @param state
     */
    void onRecordState(Constants.RecognitionState state);

    /**
     * TTS朗读开始回调
     */
    void onTtsStart();

    /**
     * TTS朗读结束回调
     */
    void onTtsEnd();

    /**
     * TTS朗读状态回调
     * @param speaking TTS是否朗读中
     */
    void onTtsState(boolean speaking);

    /**
     * 音量大小回调
     * @param volumeLevel 音量等级
     */
    void onVolume(int volumeLevel);

    /**
     * 语音识别结果回调
     */
    void onResult();

}
