package com.gionee.voiceassist.util.event;

import com.gionee.voiceassist.util.ErrorCode;

/**
 * 错误事件实体
 * <br/>
 * 若应用运行中出现错误时，为了收集错误信息，需要将错误信息事件作为ErrorEvent，
 * 通过EventBus框架传入ErrorHandler中。由ErrorHandler统一对错误进行处理。
 */

public class ErrorEvent {

    private String errorDetail;
    private ErrorCode errorCode;

    public ErrorEvent(ErrorCode errorCode, String errorDetail) {
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
    }

    /**
     * 取得错误事件的详细信息
     * @return 错误详情
     */
    public String getErrorDetail() {
        return errorDetail;
    }

    /**
     * 取得错误事件的错误码
     * @return 错误码
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
