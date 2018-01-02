package com.gionee.voiceassist.coreservice.listener.directive;

import com.baidu.duer.dcs.framework.InternalApi;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.SdkController;
import com.gionee.voiceassist.coreservice.sdk.module.alarms.AlarmsDeviceModule;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 12/29/17.
 */

public class DirectiveListenerController {

    private static final String TAG = DirectiveListenerController.class.getSimpleName();

    private boolean listenerInited = false;
    private boolean listenerInstalled = false;

    private AlarmDirectiveListener alarmListener;

    private List<DirectiveCallback> mSubscribers = new ArrayList<>();

    public DirectiveListenerController() {

    }

    public void init() {
        if (!listenerInited) {
            initListener();
        }
        LogUtil.d(TAG, "DirectiveListener registering...");
        registerListener();
    }

    public void destroy() {
        unregisterListener();
    }

    /**
     * 外界订阅Directive解析后的消息
     * subscribe回调中，需要返回解析好的Directive消息
     */
    public void subscribe(DirectiveCallback directiveCallback) {
        if (!mSubscribers.contains(directiveCallback)) {
            mSubscribers.add(directiveCallback);
        }
    }


    public void unsubscribe(DirectiveCallback directiveCallback) {
        if (mSubscribers.contains(directiveCallback)) {
            mSubscribers.remove(directiveCallback);
        }
    }

    private void initListener() {
        alarmListener = new AlarmDirectiveListener(mSubscribers);
        listenerInited = true;
    }

    private void registerListener() {
        AlarmsDeviceModule alarmsDeviceModule =
                Preconditions.checkNotNull(((AlarmsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.android.alerts")));
        alarmsDeviceModule.addDirectiveListener(alarmListener);
        listenerInstalled = true;
    }

    private void unregisterListener() {
        ((AlarmsDeviceModule)getSdkInternalApi().getDeviceModule("ai.dueros.device_interface.android.alerts"))
                .removeDirectiveLIstener(alarmListener);
        listenerInstalled = false;
    }

    public interface DirectiveCallback {
        void onDirectiveMessage(DirectiveEntity msg);
    }

    private InternalApi getSdkInternalApi() {
        return Preconditions.checkNotNull(SdkController.getInstance().getSdkInternalApi());
    }
}
