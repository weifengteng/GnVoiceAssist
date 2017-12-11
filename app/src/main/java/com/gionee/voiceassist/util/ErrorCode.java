package com.gionee.voiceassist.util;

/**
 * 错误信息码。用于错误集中处理。
 */

public enum ErrorCode {
    PERMISSION_ERROR(101, "没有授予相应的权限"),
    NETWORK_UNAVAILABLE(102, "没有可用的网络连接"),
    NETWORK_TIMEOUT(103, "网络连接超时"),
    BIND_GN_REMOTE_FAILED(104, "无法连接到金立遥控服务"),
    AUDIOFOCUS_ERROR(105, "音频焦点请求异常"),
    GENERATE_OFFLINESLOT_ERROR(106, "生成动态离线指令失败"),
    INTERNAL_UNKNOWN_ERROR(100, "内部未知错误"),
    SDK_NOT_INIT(201, "SDK没有初始化或正在初始化"),
    SDK_INIT_FAILED(202, "SDK初始化失败"),
    SDK_LOGIN_FAILED(203, "SDK登录失败"),
    SDK_LOGIN_CANCELED(204, "SDK登录取消"),
    SDK_WAKEUP_FAILED(205, "SDK唤醒失败"),
    SDK_TOKEN_INVALIDATE(206, "SDK Token令牌失效"),
    SDK_UNKNOWN_ERROR(200, "SDK未知错误"),
    SDK_INTERNAL_ERROR(299, "SDK内部错误"),
    ASR_RECORD_ERROR(211, "SDK录音出错"),
    ASR_OFFLINE_AUTH_FAILED(212, "SDK离线授权出错"),
    ASR_ONLINE_RECOGNIZE_FAILED(213, "SDK在线识别失败"),
    ASR_OFFLINE_RECOGNIZE_FAILED(214, "SDK离线识别失败"),
    TTS_AUTH_FAILED(221, "SDK TTS授权失败"),
    TTS_SPEAK_FAILED(222, "SDK TTS朗读失败");

    /**
     * code的数值范围：1xx为应用内部异常，2xx为SDK异常，3xx为硬件（如唤醒芯片）异常
     */
    public int code;
    public String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
