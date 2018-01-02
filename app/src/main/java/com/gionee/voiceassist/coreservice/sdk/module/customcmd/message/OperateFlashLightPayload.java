package com.gionee.voiceassist.coreservice.sdk.module.customcmd.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/18.
 */

public class OperateFlashLightPayload extends Payload {
    private String query;

    public OperateFlashLightPayload(@JsonProperty("operateflashlight") String operateCmd) {
        this.query = operateCmd;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
