package com.gionee.gnvoiceassist.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by liyingheng on 11/8/17.
 */

public class JsonUtil {

    public static String toJson(Object src) {
        Gson gson = new Gson();
        return gson.toJson(src);
    }

    public static <T> T fromJson(String src, Type srcType) {
        Gson gson = new Gson();
        return gson.fromJson(src,srcType);
    }

}
