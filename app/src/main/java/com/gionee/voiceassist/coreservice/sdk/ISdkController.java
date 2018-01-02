package com.gionee.voiceassist.coreservice.sdk;

import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.framework.InternalApi;

/**
 * Created by liyingheng on 10/15/17.
 */

public interface ISdkController {
    void init();

    void destroy();

    void addSdkInitCallback(SdkInitCallback callback);

    void removeSdkInitCallback(SdkInitCallback callback);

    IDcsSdk getSdkInstance();

    InternalApi getSdkInternalApi();

    public interface SdkInitCallback {
        void onInitSuccess();
        void onInitFailed(String errorMsg);
    }
}
