package com.gionee.voiceassist.sdk.module.customcmd.message;

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by twf on 2017/12/26.
 */

public class AliPaymentCodePayload extends Payload {
    private String aliPaymentCode;

    public AliPaymentCodePayload(@JsonProperty("alipaypaymentcode") String aliPaymentCode) {
        this.aliPaymentCode = aliPaymentCode;
    }

    public String getAliPaymentCode() {
        return aliPaymentCode;
    }

    public void setAliPaymentCode(String aliPaymentCode) {
        this.aliPaymentCode = aliPaymentCode;
    }
}
