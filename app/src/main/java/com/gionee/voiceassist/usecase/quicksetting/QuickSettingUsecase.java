package com.gionee.voiceassist.usecase.quicksetting;

import com.gionee.voiceassist.coreservice.datamodel.DeviceControlDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.datamodel.card.QuickSettingCardEntity;
import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BluetoothImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.GamemodeSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.LocationSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.MobiledataSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.WifiSwitchImpl;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.usecase.Usecase;
import com.gionee.voiceassist.util.LogUtil;

/**
 * Created by liyingheng on 1/26/18.
 */

@Usecase
public class QuickSettingUsecase extends BaseUsecase {

    private static final String TAG = QuickSettingUsecase.class.getSimpleName();
    private QuickSettingOperator mQsOperator;

    public QuickSettingUsecase() {
        mQsOperator = new QuickSettingOperator();
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        fireQuickSetting((DeviceControlDirectiveEntity) payload);
    }

    @Override
    public void handleUiFeedback(String uri) {
        String action = uri.split("://")[1];
        String parentAction = action.split("/")[0];
        String subAction = action.split("/")[1];
        if (subAction.equals("enable") || subAction.equals("disable")) {
            fireQuickSettingForUiToggle(parentAction, subAction.equals("enable"));
        } else if (subAction.equals("click")){

        }
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
            case "wifi":
                mQsOperator.operateWifi(enabled, true);
                break;
            case "bluetooth":
                mQsOperator.operateBluetooth(enabled, true);
                break;

        }
    }

    private void fireQuickSettingForUiToggle(String parentAction, boolean enable) {
        switch (parentAction) {
            case "wifi":
                mQsOperator.operateWifi(enable, false);
                break;
            case "bluetooth":
                mQsOperator.operateBluetooth(enable, false);
                break;
        }
    }

    private class QuickSettingOperator {
        private void operateWifi(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new WifiSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity("Wi-Fi", "wifi");
                        payload.addOptionNode(
                                "Wi-Fi",
                                "无线局域网",
                                "wifi",
                                enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "无线局域网");
                        render(payload);
                    }
                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                    if (prompt) {
                        playAndRenderText("打开无线局域网时遇到错误");
                    }
                }
            });
        }

        private void operateBluetooth(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new BluetoothImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity("蓝牙", "bluetooth");
                        payload.addOptionNode(
                                "蓝牙",
                                "",
                                "bluetooth",
                                enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "蓝牙");
                        render(payload);
                    }
                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                    if (prompt) {
                        playAndRenderText("打开蓝牙失败");
                    }
                }
            });
        }

        private void operateGamemode(boolean enable) {
            ISwitchCtrl operator = new GamemodeSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {

                }
            });
        }

        private void operateLocation(boolean enable) {
            ISwitchCtrl operator = new LocationSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {

                }
            });
        }

        private void operateMobileData(boolean enable) {
            ISwitchCtrl operator = new MobiledataSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {

                }
            });
        }
    }
}
