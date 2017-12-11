package com.gionee.voiceassist.basefunction.error;

import android.text.TextUtils;

import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.util.event.ErrorEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * 接收并集中处理错误信息
 */

public class ErrorHandler{

    private final String TAG = getClass().getSimpleName();

    /**
     * 接收错误信息
     * @param errEvent 错误信息实体
     */
    @Subscribe
    public void onError(ErrorEvent errEvent) {
        LogUtil.e(TAG, "A error occur! " + "ErrorCode: " + errEvent.getErrorCode()
                + ", ErrorDetail: " + errEvent.getErrorDetail());
        handleError(errEvent.getErrorCode(), errEvent.getErrorDetail());
    }

    /**
     * 取得需要呈现给用户的文字
     * @param errorCode 错误事件
     * @return 提示给用户的文字
     */
    public String getPrompt(ErrorCode errorCode) {
        switch (errorCode) {
            case PERMISSION_ERROR:
                // 请设置权限
                return "没有设置权限";
            case NETWORK_UNAVAILABLE:
            case ASR_OFFLINE_AUTH_FAILED:
            case ASR_OFFLINE_RECOGNIZE_FAILED:
                // 没联网我懂得很少
                return "没联网我懂得很少";
            case NETWORK_TIMEOUT:
            case ASR_ONLINE_RECOGNIZE_FAILED:
                // 走神了，麻烦再说一遍
                return "走神了，麻烦再说一遍";
            case SDK_INIT_FAILED:
            case SDK_LOGIN_FAILED:
            case SDK_NOT_INIT:
            case SDK_WAKEUP_FAILED:
            case TTS_AUTH_FAILED:
            case ASR_RECORD_ERROR:
            case TTS_SPEAK_FAILED:
            case SDK_UNKNOWN_ERROR:
            case SDK_TOKEN_INVALIDATE:
            case INTERNAL_UNKNOWN_ERROR:
            case SDK_INTERNAL_ERROR:
                // 出了点问题
                return "好像出了点问题";
            case AUDIOFOCUS_ERROR:
            case BIND_GN_REMOTE_FAILED:
                //什么都不做
                return "";
        }
        return "";
    }

    /**
     * 处理错误信息
     * @param errCode 错误信息码
     * @param errDetail 错误信息文字描述
     */
    private void handleError(ErrorCode errCode, String errDetail) {
        String promptText = getPrompt(errCode);
        if (!TextUtils.isEmpty(promptText))  T.showShort(promptText);
    }

}
