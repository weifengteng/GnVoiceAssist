package com.gionee.gnvoiceassist.message.model.metadata;

import com.gionee.gnvoiceassist.util.JsonUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by liyingheng on 11/9/17.
 */

public abstract class Metadata implements Serializable{

    public String toJson() {
        return JsonUtil.toJson(this);
    }

}
