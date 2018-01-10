package com.gionee.voiceassist.controller.customuserinteraction;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;

/**
 * 多轮交互控制类接口
 *
 * @author twf
 * @date 2018/1/8
 */

public interface ICuiControl {

    /**
     * 开启多轮交互
     * @param payLoadGenerator 多轮交互 payLoadGenerator
     * @param resultCallback 多轮交互结果处理的接口实现
     */
    void startCustomUserInteraction(CustomUserInteractionDeviceModule.PayLoadGenerator payLoadGenerator, ICuiResult resultCallback);

    /**
     * 停止当前多轮交互
     */
    void stopCurrentCustomUserInteraction();

    /**
     * 当前多轮交互是否应该结束
     * @return
     */
    boolean isCUIShouldStop();

    /**
     * 是否正在进行多轮交互
     * @return
     */
    boolean isCustomUserInteractionProcessing();

    /**
     * 销毁资源
     */
    void onDestroy();
}
