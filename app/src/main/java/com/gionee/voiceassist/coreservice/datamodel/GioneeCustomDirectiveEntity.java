package com.gionee.voiceassist.coreservice.datamodel;

/**
 * 金立自定义命令（如拍立淘、支付宝扫一扫）场景Payload
 */

public class GioneeCustomDirectiveEntity extends DirectiveEntity {

    private GioneeCustomAction action;
    private String msg;

    public GioneeCustomDirectiveEntity() {
        setType(Type.GIONEE_CUSTOM_COMMAND);
    }

    public enum GioneeCustomAction {
        LAUNCH_ALIPAY_SCAN,
        LAUNCH_ALIPAY_PAYMENT_CODE,
        LAUNCH_ALIPAY_PAILITAO,
        LAUNCH_HEARTRATE,
        SHOW_MOBILE_DEVICE_INFO,
        START_TIMER,
        START_STOPWATCH,
        OPERATE_FLASHLIGHT
    }

    /**
     * 取得自定义命令的相应动作
     * @return 自定义命令动作
     */
    public GioneeCustomAction getAction() {
        return action;
    }

    public void setAction(GioneeCustomAction action) {
        this.action = action;
    }

    /**
     * 取得自定义命令的附加信息
     * @return 自定义命令附加信息
     */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GioneeCustomDirectiveEntity{" +
                "action=" + action +
                ", msg='" + msg + '\'' +
                '}';
    }
}
