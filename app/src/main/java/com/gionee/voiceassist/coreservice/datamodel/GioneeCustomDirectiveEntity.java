package com.gionee.voiceassist.coreservice.datamodel;

/**
 * Created by liyingheng on 1/3/18.
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
        START_TIMER
    }

    public GioneeCustomAction getAction() {
        return action;
    }

    public void setAction(GioneeCustomAction action) {
        this.action = action;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
