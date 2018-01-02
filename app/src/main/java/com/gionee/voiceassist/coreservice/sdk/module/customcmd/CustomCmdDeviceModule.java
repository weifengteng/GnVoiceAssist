package com.gionee.voiceassist.coreservice.sdk.module.customcmd;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.AliPayScanPayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.AliPaymentCodePayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.BargInOperatePayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.InstantHeartRatePayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.MobileDeviceInfoPayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.OperateFlashLightPayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.PaiLiTaoPayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.PrintScreenPayload;
import com.gionee.voiceassist.coreservice.sdk.module.customcmd.message.StartTimerPayload;
import com.gionee.voiceassist.coreservice.sdk.module.devicecontrol.DeviceControlDeviceModule;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义指令中除了kookong 相关的其他自定义指令
 * Created by twf on 2017/12/26.
 */

public class CustomCmdDeviceModule extends BaseDeviceModule {
    private static String TAG = CustomCmdDeviceModule.class.getSimpleName();
    private ArrayList<BaseDirectiveListener> directiveListeners;

    public CustomCmdDeviceModule(IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
        directiveListeners = new ArrayList<>();
    }

    @Override
    public ClientContext clientContext() {
        return null;
    }

    @Override
    public void handleDirective(Directive directive) throws HandleDirectiveException {
        String headerName = directive.getName();
        Payload payload = directive.getPayload();

        if(ApiConstants.Directives.AliPayPaymentCode.NAME.equals(headerName)) {
            if(payload instanceof AliPaymentCodePayload) {
                handleAliPaymentCodePayload();
            }
        } else if(ApiConstants.Directives.ALiPayScan.NAME.equals(headerName)) {
            if(payload instanceof AliPayScanPayload) {
                handleAliPayScanPayload();
            }
        } else if(ApiConstants.Directives.BarginOper.NAME.equals(headerName)) {
            if(payload instanceof BargInOperatePayload) {
                handleBarginOperPayload();
            }
        } else if(ApiConstants.Directives.InstantHeartRate.NAME.equals(headerName)) {
            if(payload instanceof InstantHeartRatePayload) {
                handleInstantHeartRatePayload();
            }
        } else if(ApiConstants.Directives.MobileDeviceInfo.NAME.equals(headerName)) {
            if(payload instanceof MobileDeviceInfoPayload) {
                handleMobileDeviceInfoPayload();
            }
        } else if(ApiConstants.Directives.OperateFlashlight.NAME.equals(headerName)) {
            if(payload instanceof OperateFlashLightPayload) {
                handleOperateFlashLightPayload();
            }
        } else if(ApiConstants.Directives.PaiLiTao.NAME.equals(headerName)) {
            if(payload instanceof PaiLiTaoPayload) {
                handlePaiLiTaoPayload();
            }
        } else if(ApiConstants.Directives.PrintScreen.NAME.equals(headerName)) {
            if(payload instanceof PrintScreenPayload) {
                handlePrintScreenPayload();
            }
        } else if(ApiConstants.Directives.StartTimer.NAME.equals(headerName)) {
            if(payload instanceof StartTimerPayload) {
                handleStartTimerPayload();
            }
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + ApiConstants.Directives.AliPayPaymentCode.NAME, AliPaymentCodePayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.ALiPayScan.NAME, AliPayScanPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.BarginOper.NAME, BargInOperatePayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.InstantHeartRate.NAME, InstantHeartRatePayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.MobileDeviceInfo.NAME, MobileDeviceInfoPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.OperateFlashlight.NAME, OperateFlashLightPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.PaiLiTao.NAME, PaiLiTaoPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.PrintScreen.NAME, PrintScreenPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.StartTimer.NAME, StartTimerPayload.class);
        return map;
    }

    @Override
    public void release() {
        if(directiveListeners != null) {
            directiveListeners.clear();
            directiveListeners = null;
        }

    }

    public void addDirectiveListener(BaseDirectiveListener directiveListener) {
        directiveListeners.add(directiveListener);
    }

    public void removeDirectiveListener(BaseDirectiveListener directiveListener) {
        directiveListeners.remove(directiveListener);
    }

    public void removeAllDirectiveListener() {
        directiveListeners.clear();
    }

    public void handleAliPaymentCodePayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                ((ICustomCmdDirectiveListener) listener).onLaunchAliPaymentCode();
                break;
            }
        }
    }

    public void handleAliPayScanPayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                ((ICustomCmdDirectiveListener) listener).onLaunchAliPayScan();
                break;
            }
        }
    }

    public void handleBarginOperPayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                // TODO: execute operate bargin query in LocalAudioMusicPlayer DirectiveListener
//                ((ICustomCmdDirectiveListener) listener).onLaunchAliPaymentCode();
                LogUtil.d(TAG, "handleBarginOperPayload");
                break;
            }
        }
    }

    public void handleInstantHeartRatePayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                ((ICustomCmdDirectiveListener) listener).onInstantHeartRate();
                break;
            }
        }
    }

    public void handleMobileDeviceInfoPayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                ((ICustomCmdDirectiveListener) listener).onShowMobileDeviceInfo();
                break;
            }
        }
    }

    public void handleOperateFlashLightPayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                // TODO: execute operate flashflight in DeviceModule DirectiveListener
//                ((ICustomCmdDirectiveListener) listener).onLaunchAliPaymentCode();
                LogUtil.d(TAG, "handleOperateFlashLightPayload");
                break;
            }
        }
    }

    public void handlePaiLiTaoPayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                ((ICustomCmdDirectiveListener) listener).onLaunchPaiLiTao();
                break;
            }
        }
    }

    public void handlePrintScreenPayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof DeviceControlDeviceModule.IDeviceControlDirectiveListener) {
                // TODO: execute printscreen in DeviceModule DirectiveListener
//                ((DeviceControlDeviceModule.IDeviceControlDirectiveListener) listener).onDirectiveReceived(null);
                LogUtil.d(TAG, "handlePrintScreenPayload");
                break;
            }
        }
    }

    public void handleStartTimerPayload() {
        for(BaseDirectiveListener listener : directiveListeners) {
            if(listener instanceof ICustomCmdDirectiveListener) {
                ((ICustomCmdDirectiveListener) listener).onStartTimer();
                break;
            }
        }
    }

    public interface ICustomCmdDirectiveListener {
        void onLaunchAliPaymentCode();
        void onLaunchAliPayScan();
        void onInstantHeartRate();
        void onShowMobileDeviceInfo();
        void onLaunchPaiLiTao();
        void onStartTimer();
    }
}
