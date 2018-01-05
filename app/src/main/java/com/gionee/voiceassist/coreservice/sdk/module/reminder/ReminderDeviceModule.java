package com.gionee.voiceassist.coreservice.sdk.module.reminder;

import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.voiceassist.coreservice.sdk.module.reminder.message.ManageAlertPayload;
import com.gionee.voiceassist.coreservice.sdk.module.reminder.message.SetAlertPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提醒场景 端能力的DeviceModule
 * 可创建提醒、查询提醒以及删除提醒
 */

public class ReminderDeviceModule extends BaseDeviceModule {

    private List<IReminderDirectiveListener> listeners = new ArrayList<>();

    public ReminderDeviceModule(IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
    }

    @Override
    public ClientContext clientContext() {
        return null;
    }

    @Override
    public void handleDirective(Directive directive) throws HandleDirectiveException {
        String headerName = directive.getName();
        try {
            if (ApiConstants.Directives.SetAlert.NAME.equals(headerName)) {
                //命中SetAlert
                fireSetAlert((SetAlertPayload) directive.getPayload());
            } else if (ApiConstants.Directives.ManageAlert.NAME.equals(headerName)) {
                fireManageAlert((ManageAlertPayload) directive.getPayload());
            } else {
                String msg = "reminder cannot handle the directive";
                throw new HandleDirectiveException(HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, msg);
            }
        } catch (HandleDirectiveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + ApiConstants.Directives.SetAlert.NAME, SetAlertPayload.class);
        map.put(getNameSpace() + ApiConstants.Directives.ManageAlert.NAME, ManageAlertPayload.class);
        return map;
    }

    @Override
    public void release() {
        listeners.clear();
    }

    public void addDirectiveListener(IReminderDirectiveListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeDirectiveListener(IReminderDirectiveListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    private void fireSetAlert(SetAlertPayload payload) {
        for (IReminderDirectiveListener listener:listeners) {
            listener.onSetAlertDirectiveReceived(payload);
        }
    }

    private void fireManageAlert(ManageAlertPayload payload) {
        for (IReminderDirectiveListener listener:listeners) {
            listener.onManageAlertDirectiveReceived(payload);
        }
    }

    public interface IReminderDirectiveListener {
        void onSetAlertDirectiveReceived(SetAlertPayload payload);
        void onManageAlertDirectiveReceived(ManageAlertPayload payload);
    }
}
