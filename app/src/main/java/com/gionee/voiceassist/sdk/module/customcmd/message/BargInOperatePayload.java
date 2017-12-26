package com.gionee.voiceassist.sdk.module.customcmd.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/26.
 */

public class BargInOperatePayload extends Payload {
    private String barginCmd;

    public BargInOperatePayload(@JsonProperty("barginoper")String barginCmd) {
        this.barginCmd = barginCmd;
    }

    public String getBarginCmd() {
        return barginCmd;
    }

    public void setBarginCmd(String barginCmd) {
        this.barginCmd = barginCmd;
    }
}
