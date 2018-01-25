package com.gionee.voiceassist.usecase.customcommand;

import android.content.Intent;
import android.net.Uri;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.ContextUtil;

/**
 * Created by liyingheng on 1/25/18.
 */

public class AlipayScanUsecase extends BaseUsecase {

    private static String ALIPAY_SCAN_CODE_URL = "alipays://platformapi/startApp?appId=10000007";

    @Override
    public void handleDirective(DirectiveEntity payload) {
        fireAlipayScan();
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "custom.alipayscan";
    }

    private void fireAlipayScan() {
//        Intent intent = new Intent("com.gionee.edo.action.scancode");
//        ContextUtil.getAppContext().sendBroadcast(intent);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ALIPAY_SCAN_CODE_URL));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) != null) {
            playAndRenderText("正在打开支付宝扫一扫");
            ContextUtil.getAppContext().startActivity(intent);
        } else {
            playAndRenderText("无法打开支付宝扫一扫功能，请检查支付宝是否安装");
        }
    }
}
