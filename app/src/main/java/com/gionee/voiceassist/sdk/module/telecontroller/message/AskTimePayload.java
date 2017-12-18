package com.gionee.voiceassist.sdk.module.telecontroller.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/15.
 */

public class AskTimePayload extends Payload {

    private String askAction;
    private String nowParams;
    private String whatParams;

    public AskTimePayload(@JsonProperty("search")String askAction, @JsonProperty("now")String nowParams, @JsonProperty("what")String whatParams) {
        this.askAction = askAction;
        this.nowParams = nowParams;
        this.whatParams = whatParams;
    }

    public String getAskAction() {
        return askAction;
    }

    public void setAskAction(String askAction) {
        this.askAction = askAction;
    }

    public String getNowParams() {
        return nowParams;
    }

    public void setNowParams(String nowParams) {
        this.nowParams = nowParams;
    }

    public String getWhatParams() {
        return whatParams;
    }

    public void setWhatParams(String whatParams) {
        this.whatParams = whatParams;
    }
}
