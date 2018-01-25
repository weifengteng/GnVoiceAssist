package com.gionee.voiceassist.coreservice.sdk.module.phonecall.message;

import com.baidu.duer.dcs.framework.message.Payload;

/**
 * Created by liyingheng on 1/24/18.
 */

public class TelephoneStatePayload extends Payload {

    public TelephoneStatePayload(TelephoneStatus status, boolean hasSimCard) {
        this.status = status;
        this.hasSimCard = hasSimCard;
    }

    // 设备拨打电话的状态。状态取值：PERMIT允许，REJECT禁用。
    public TelephoneStatus status;

    // 是否有Sim卡
    public boolean hasSimCard;

    public enum TelephoneStatus {
        PERMIT,
        REJECT
    }

}
