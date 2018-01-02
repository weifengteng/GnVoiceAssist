package com.gionee.voiceassist.coreservice.sdk.module.customcmd.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/26.
 */

public class AliPayScanPayload extends Payload {
    private String aliPayScan;

    public AliPayScanPayload(@JsonProperty("alipayscan") String aliPayScan) {
        this.aliPayScan = aliPayScan;
    }

    public String getAliPayScan() {
        return aliPayScan;
    }

    public void setAliPayScan(String aliPayScan) {
        this.aliPayScan = aliPayScan;
    }
}
