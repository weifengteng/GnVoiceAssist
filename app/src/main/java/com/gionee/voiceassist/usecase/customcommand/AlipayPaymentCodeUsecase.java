package com.gionee.voiceassist.usecase.customcommand;

import android.content.Intent;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.ContextUtil;

/**
 * Created by liyingheng on 1/25/18.
 */

public class AlipayPaymentCodeUsecase extends BaseUsecase {
    @Override
    public void handleDirective(DirectiveEntity payload) {
        fireAlipayPaymentCode();
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "custom.alipay_paymentcode";
    }

    private void fireAlipayPaymentCode() {
        Intent intent = new Intent();
        intent.setAction("com.gionee.edo.action.alipay");
        ContextUtil.getAppContext().sendBroadcast(intent);
        playAndRenderText("正在尝试显示支付宝付款码");
    }
}
