/*
 * *
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gionee.gnvoiceassist.sdk.module.alarms;


import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.SetAlarmPayload;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.SetTimerPayload;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.ShowAlarmsPayload;
import com.gionee.gnvoiceassist.sdk.module.alarms.message.ShowTimersPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlarmsDeviceModule extends BaseDeviceModule {
    private List<IAlarmDirectiveListener> listeners = new ArrayList();

    public AlarmsDeviceModule(IMessageSender messageSender)
    {
        super("ai.dueros.device_interface.android.alerts", messageSender);
    }

    public ClientContext clientContext()
    {
        return null;
    }

    public void handleDirective(Directive directive)
            throws HandleDirectiveException
    {
        String headerName = directive.getName();
        try
        {
            if (ApiConstants.Directives.SetAlarm.NAME.equals(headerName))
            {
                SetAlarmPayload payload = (SetAlarmPayload)directive.getPayload();
                fireSetAlarm(payload);
            }
            else if (ApiConstants.Directives.ShowAlarms.NAME.equals(headerName))
            {
                ShowAlarmsPayload payload = (ShowAlarmsPayload)directive.getPayload();
                fireShowAlarms(payload);
            }
            else if (ApiConstants.Directives.SetTimer.NAME.equals(headerName))
            {
                SetTimerPayload payload = (SetTimerPayload)directive.getPayload();
                fireSetTimer(payload);
            }
            else if (ApiConstants.Directives.ShowTimers.NAME.equals(headerName))
            {
                ShowTimersPayload payload = (ShowTimersPayload)directive.getPayload();
                fireShowTimers(payload);
            }
            else
            {
                String message = "alarm cannot handle the directive";
                throw new HandleDirectiveException(HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, message);
            }
        }
        catch (HandleDirectiveException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.alarms.ApiConstants.Directives.SetAlarm.NAME, com.gionee.gnvoiceassist.sdk.module.alarms.message.SetAlarmPayload.class);
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.alarms.ApiConstants.Directives.SetTimer.NAME, com.gionee.gnvoiceassist.sdk.module.alarms.message.SetTimerPayload.class);
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.alarms.ApiConstants.Directives.ShowAlarms.NAME, com.gionee.gnvoiceassist.sdk.module.alarms.message.ShowAlarmsPayload.class);
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.alarms.ApiConstants.Directives.ShowTimers.NAME, com.gionee.gnvoiceassist.sdk.module.alarms.message.ShowTimersPayload.class);
        return map;
    }

    public void addDirectiveListener(IAlarmDirectiveListener listener)
    {
        this.listeners.add(listener);
    }

    public void removeDirectiveLIstener(IAlarmDirectiveListener listener)
    {
        if ((this.listeners != null) && (this.listeners.contains(listener))) {
            this.listeners.remove(listener);
        }
    }

    public void release() {
        listeners.clear();
    }

    private void fireSetAlarm(SetAlarmPayload payload)
    {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((IAlarmDirectiveListener)this.listeners.get(i)).onSetAlarmDirectiveReceived(payload);
        }
    }

    private void fireShowAlarms(ShowAlarmsPayload payload)
    {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((IAlarmDirectiveListener)this.listeners.get(i)).onShowAlarmsDirectiveReceived(payload);
        }
    }

    private void fireSetTimer(SetTimerPayload payload)
    {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((IAlarmDirectiveListener)this.listeners.get(i)).onSetTimerDirectiveReceived(payload);
        }
    }

    private void fireShowTimers(ShowTimersPayload payload)
    {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((IAlarmDirectiveListener)this.listeners.get(i)).onShowTimersDirectiveReceived(payload);
        }
    }

    public interface IAlarmDirectiveListener
    {
        public abstract void onSetAlarmDirectiveReceived(SetAlarmPayload paramSetAlarmPayload);

        public abstract void onShowAlarmsDirectiveReceived(ShowAlarmsPayload paramShowAlarmsPayload);

        public abstract void onSetTimerDirectiveReceived(SetTimerPayload paramSetTimerPayload);

        public abstract void onShowTimersDirectiveReceived(ShowTimersPayload paramShowTimersPayload);
    }
}
