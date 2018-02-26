package com.gionee.voiceassist.coreservice;

import com.baidu.duer.dcs.framework.internalapi.DcsConfig;
import com.gionee.voiceassist.util.Constants;

/**
 * CoreService的全局控制参数
 */

public class CoreServiceConfig {

    /**
     * 语音识别ASR模式
     */
    public static final int ASR_MODE = DcsConfig.ASR_MODE_ONLINE;

    /**
     * TTS播报模式
     */
    public static final int TTS_MODE = Constants.TTS_MODE_OFFLINE;


}
