package com.gionee.voiceassist.util;

import com.gionee.voiceassist.basefunction.error.ErrorHandler;
import com.gionee.voiceassist.util.event.ErrorEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 注册错误信息监听，传递错误信息的帮助类
 */

public class ErrorHelper {

    private static final String TAG = ErrorHandler.class.getSimpleName();

    private ErrorHandler errorHandler;

    /**
     * 向应用内报告异常消息
     * @param errorCode 错误码
     * @param errorDetail 错误详细信息
     */
    public static void sendError(ErrorCode errorCode, String errorDetail) {
        ErrorEvent event = new ErrorEvent(errorCode, errorDetail);
        if (EventBus.getDefault() != null) {
            EventBus.getDefault().post(event);
        } else {
            LogUtil.e(TAG, "sendError FAILED! " + "ErrorHandler not instantiate.");
        }
    }

    /**
     * 注册异常集中处理组件
     */
    public void registerErrorHandler() {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler();
        }
        EventBus.getDefault().register(errorHandler);
    }

    /**
     * 注销异常集中处理组件
     */
    public void unregisterErrorHandler() {
        EventBus.getDefault().unregister(errorHandler);
    }

}
