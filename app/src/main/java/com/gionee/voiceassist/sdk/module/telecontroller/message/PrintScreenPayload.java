package com.gionee.voiceassist.sdk.module.telecontroller.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/15.
 */

public class PrintScreenPayload extends Payload {
    private String query;

    public PrintScreenPayload(@JsonProperty("printscreen") String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
