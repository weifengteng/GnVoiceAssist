package com.gionee.gnvoiceassist.service;

import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

/**
 * Created by liyingheng on 11/4/17.
 */

public interface IDirectiveListenerCallback {

    /**
     * 识别结果回调
     * @param response 识别结果
     */
    void onDirectiveResponse(DirectiveResponseEntity response);

    /**
     * 输入音量改变回调
     * @param level 输入音量等级
     */
    void onVoiceInputVolume(int level);

    /**
     * 屏幕显示渲染回调
     * @param response 屏幕显示结果
     */
    void onRenderResponse(RenderEntity response);

}
