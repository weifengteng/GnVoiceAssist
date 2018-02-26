package com.gionee.voiceassist.usecase.quicksetting;

import android.os.Handler;
import android.os.Looper;

import com.gionee.voiceassist.coreservice.datamodel.DeviceControlDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.datamodel.card.QuickSettingCardEntity;
import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BluetoothImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.FlashlightSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.GamemodeSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.LocationSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.MobiledataSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.WifiSwitchImpl;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.usecase.Usecase;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 1/26/18.
 */

@Usecase
public class QuickSettingUsecase extends BaseUsecase {

    private static final String TAG = QuickSettingUsecase.class.getSimpleName();
    private QuickSettingOperator mQsOperator;
    private Handler mUiHandler;
    private List<QuickSettingCardEntity> wifiPayloads;
    private List<QuickSettingCardEntity> bluetoothPayloads;
    private List<QuickSettingCardEntity> mobileDataPayloads;
    private List<QuickSettingCardEntity> flashlightPayloads;


    public QuickSettingUsecase() {
        mUiHandler = new Handler(Looper.getMainLooper());
        mQsOperator = new QuickSettingOperator();
        wifiPayloads = new ArrayList<>();
        bluetoothPayloads = new ArrayList<>();
        mobileDataPayloads = new ArrayList<>();
        flashlightPayloads = new ArrayList<>();
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

    private void updateQuickSettingState(final List<QuickSettingCardEntity> payloads,
                                         final String optionAlias,
                                         final QuickSettingCardEntity.QuickSettingState state) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (QuickSettingCardEntity payload:payloads) {
                    payload.updateNodeState(optionAlias, state);
                }
            }
        });

    }

    private class QuickSettingOperator {
        private void operateWifi(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new WifiSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(
                            wifiPayloads,
                            "wifi",
                            enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity.QuickSettingCardBuilder(
                                "无线局域网",
                                "wifi")
                                .addNode(
                                        "Wi-Fi",
                                        "无线局域网",
                                        "wifi",
                                        enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED)
                                .build();
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "无线局域网");
                        wifiPayloads.add(payload);
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
                    updateQuickSettingState(bluetoothPayloads,
                            "bluetooth",
                            enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity.QuickSettingCardBuilder("蓝牙", "bluetooth")
                                .addNode(
                                        "蓝牙",
                                        "",
                                        "bluetooth",
                                        enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED)
                                .build();
                        bluetoothPayloads.add(payload);
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

        private void operateMobileData(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new MobiledataSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(mobileDataPayloads,
                            "bluetooth",
                            enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity.QuickSettingCardBuilder("移动数据", "mobiledata")
                                .addNode(
                                        "移动数据",
                                        "",
                                        "mobiledata",
                                        enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED)
                                .build();
                        mobileDataPayloads.add(payload);
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "移动数据");
                        render(payload);
                    }
                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                    playAndRenderText("打开移动数据失败");
                }
            });
        }

        private void operateFlashlight(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new FlashlightSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(flashlightPayloads,
                            "bluetooth",
                            enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity.QuickSettingCardBuilder("手电筒", "flashlight")
                                .addNode(
                                        "手电筒",
                                        "",
                                        "flashlight",
                                        enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED)
                                .build();
                        flashlightPayloads.add(payload);
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "手电筒");
                        render(payload);
                    }
                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {

                }
            });
        }
    }
}
