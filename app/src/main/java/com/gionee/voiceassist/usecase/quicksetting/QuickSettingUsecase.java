package com.gionee.voiceassist.usecase.quicksetting;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.gionee.voiceassist.coreservice.datamodel.DeviceControlDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GioneeCustomDirectiveEntity;
import com.gionee.voiceassist.datamodel.card.QuickSettingCardEntity;
import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BluetoothImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.FlashlightSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.GamemodeSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.HotspotSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.LocationSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.MobiledataSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.NfcSwitchImpl;
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
    private List<QuickSettingCardEntity> locationPayloads;


    public QuickSettingUsecase() {
        mUiHandler = new Handler(Looper.getMainLooper());
        mQsOperator = new QuickSettingOperator();
        wifiPayloads = new ArrayList<>();
        bluetoothPayloads = new ArrayList<>();
        mobileDataPayloads = new ArrayList<>();
        flashlightPayloads = new ArrayList<>();
        locationPayloads = new ArrayList<>();
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        if (payload instanceof DeviceControlDirectiveEntity) {
            fireQuickSetting((DeviceControlDirectiveEntity) payload);
        } else if (payload instanceof GioneeCustomDirectiveEntity) {
            fireCustomQuickSetting((GioneeCustomDirectiveEntity) payload);
        }
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

    /**
     * 处理一般(DeviceControlDirective)类型的快捷开关信息
     * @param payload 快捷开关实体
     */
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
            case "nfc":
                mQsOperator.operateNfc(enabled, true);
                break;
            case "hotspot":
                mQsOperator.operateHotspot(enabled, true);
                break;

        }
    }

    /**
     * 处理自定义特殊(GioneeCustomDirective)类型的快捷开关信息
     * @param payload 快捷开关实体
     */
    private void fireCustomQuickSetting(GioneeCustomDirectiveEntity payload) {
        switch (payload.getAction()) {
            case OPERATE_FLASHLIGHT: {
                if (TextUtils.equals("打开手电筒", payload.getMsg())) {
                    mQsOperator.operateFlashlight(true, true);
                } else if (TextUtils.equals("关闭手电筒", payload.getMsg())){
                    mQsOperator.operateFlashlight(false, true);
                }
            }
        }
    }

    /**
     * 处理快捷开关UI反馈的结果
     * -- IMPORTANT: 当新增快捷开关选项时，务必在这里通过case条件，添加对应别名，处理快捷开关的反馈。 --
     *
     * @param parentAction 快捷开关对应别名alias
     * @param enable 状态：打开(True)或关闭(False)
     */
    private void fireQuickSettingForUiToggle(String parentAction, boolean enable) {
        switch (parentAction) {
            case "wifi":
                mQsOperator.operateWifi(enable, false);
                break;
            case "bluetooth":
                mQsOperator.operateBluetooth(enable, false);
                break;
            case "flashlight":
                mQsOperator.operateFlashlight(enable, false);
                break;
            case "location":
                mQsOperator.operateLocation(enable, false);
                break;
            case "mobiledata":
                mQsOperator.operateMobileData(enable, false);
                break;
            case "nfc":
                mQsOperator.operateNfc(enable, false);
                break;
            case "hotspot":
                mQsOperator.operateHotspot(enable, false);
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

        private void operateLocation(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new LocationSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(locationPayloads,
                            "location",
                            enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity.QuickSettingCardBuilder("位置信息", "bluetooth")
                                .addNode(
                                        "位置信息",
                                        "",
                                        "location",
                                        enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED)
                                .build();
                        bluetoothPayloads.add(payload);
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "位置信息");
                        render(payload);
                    }
                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                    if (prompt) {
                        playAndRenderText("操作位置信息失败");
                    }
                }
            });
        }

        private void operateMobileData(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new MobiledataSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(mobileDataPayloads,
                            "mobiledata",
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
                    playAndRenderText("操作移动数据失败");
                }
            });
        }

        private void operateFlashlight(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new FlashlightSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(flashlightPayloads,
                            "flashlight",
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
                    if (prompt) {
                        playAndRenderText((enable ? "打开" : "关闭") + "手电筒失败");
                    }
                }
            });
        }

        private void operateNfc(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new NfcSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(flashlightPayloads,
                            "nfc",
                            enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity.QuickSettingCardBuilder("NFC", "flashlight")
                                .addNode(
                                        "NFC",
                                        "",
                                        "nfc",
                                        enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED)
                                .build();
                        flashlightPayloads.add(payload);
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "NFC");
                        render(payload);
                    }
                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                    if (prompt) {
                        playAndRenderText((enable ? "打开" : "关闭") + "NFC失败, " + reason);
                    }
                }
            });
        }

        private void operateHotspot(final boolean enable, final boolean prompt) {
            ISwitchCtrl operator = new HotspotSwitchImpl();
            operator.toggle(enable, new ISwitchCtrl.Callback() {
                @Override
                public void onSuccess() {
                    updateQuickSettingState(flashlightPayloads,
                            "hotspot",
                            enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED);
                    if (prompt) {
                        QuickSettingCardEntity payload = new QuickSettingCardEntity.QuickSettingCardBuilder("热点", "flashlight")
                                .addNode(
                                        "热点",
                                        "",
                                        "hotspot",
                                        enable ? QuickSettingCardEntity.QuickSettingState.ENABLED : QuickSettingCardEntity.QuickSettingState.DISABLED)
                                .build();
                        flashlightPayloads.add(payload);
                        playAndRenderText("正在" + (enable ? "打开" : "关闭") + "热点");
                        render(payload);
                    }
                }

                @Override
                public void onFailure(ISwitchCtrl.FailureCode code, String reason) {
                    if (prompt) {
                        playAndRenderText((enable ? "打开" : "关闭") + "热点失败");
                    }
                }
            });
        }
    }
}
