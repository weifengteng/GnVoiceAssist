package com.gionee.gnvoiceassist.sdk;

import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.framework.InternalApi;

/**
 * Created by liyingheng on 10/15/17.
 */

public interface ISdkManager {
    void init();

    void destroy();

    IDcsSdk getSdkInstance();

    InternalApi getSdkInternalApi();
}
