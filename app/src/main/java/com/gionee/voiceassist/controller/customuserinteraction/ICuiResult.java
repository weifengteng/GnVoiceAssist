package com.gionee.voiceassist.controller.customuserinteraction;

/**
 * 多轮交互结果回调接口
 * @author twf
 * @date 2018/1/8
 */

public interface ICuiResult {

    /**
     * 处理多轮交互中 Query 未命中预设 utterance 的情况
     */
    void handleCUInteractionUnknownUtterance();

    /**
     * 处理多轮交互中 Query 命中预设 utterance 的情况
     * @param url 服务器返回的识别后的参数
     */
    void handleCUInteractionTargetUrl(String url);
}
