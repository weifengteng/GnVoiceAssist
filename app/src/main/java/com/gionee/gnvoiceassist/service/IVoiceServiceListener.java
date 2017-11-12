package com.gionee.gnvoiceassist.service;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.util.Constants;

/**
 * Created by liyingheng on 11/1/17.
 */

public interface IVoiceServiceListener {

    /**
     * 识别引擎状态回调
     * @param state 识别引擎状态
     */
    void onEngineState(Constants.EngineState state);

    /**
     * 语音识别状态回调
     * @param state 语音识别状态
     */
    void onRecognizeState(Constants.RecognitionState state);

    /**
     * 识别结果显示回调
     * @param renderData 结果显示数据源
     */
    void onRenderRequest(RenderEntity renderData);

}
