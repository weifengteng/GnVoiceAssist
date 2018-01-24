package com.gionee.voiceassist.usecase.customcommand;

import android.content.Intent;
import android.net.Uri;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.ContextUtil;

/**
 * Created by liyingheng on 1/24/18.
 */

public class PailitaoUsecase extends BaseUsecase {
    @Override
    public void handleDirective(DirectiveEntity payload) {
        firePailitao();
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "custom.pailitao";
    }

    private void firePailitao() {
        String pailitao_uri = "pailitao://pailitaoview";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pailitao_uri));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setClassName("com.gionee.sidebar",
//                "com.gionee.sidebar.alibaba.CameraExampleActivity_1");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) != null) {
            playAndRenderText("正在打开拍立淘");
            ContextUtil.getAppContext().startActivity(intent);
        } else {
            playAndRenderText("无法打开拍立淘");
        }
    }
}
