package com.gionee.voiceassist.usecase.quicksetting;

import com.gionee.voiceassist.coreservice.datamodel.DeviceControlDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.usecase.Usecase;
import com.gionee.voiceassist.util.LogUtil;

/**
 * Created by liyingheng on 1/26/18.
 */

@Usecase
public class QuickSettingUsecase extends BaseUsecase {

    private static final String TAG = QuickSettingUsecase.class.getSimpleName();

    @Override
    public void handleDirective(DirectiveEntity payload) {
        fireQuickSetting((DeviceControlDirectiveEntity) payload);
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "quicksetting";
    }

    private void fireQuickSetting(DeviceControlDirectiveEntity payload) {
        String funcName = payload.getFunctionName();
        boolean enabled = payload.isState();
        LogUtil.d(TAG, "fireQuickSetting. Function name : " + funcName + "; enabled : " + enabled);
        switch (funcName) {
            case "flashlight":
                break;

        }
    }
}
