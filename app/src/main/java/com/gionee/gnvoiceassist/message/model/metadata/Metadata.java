package com.gionee.gnvoiceassist.message.model.metadata;

import com.gionee.gnvoiceassist.util.JsonUtil;
import com.google.gson.Gson;

/**
 * Created by liyingheng on 11/9/17.
 */

public abstract class Metadata {

    public String toJson() {
        return JsonUtil.toJson(this);
    }

}
