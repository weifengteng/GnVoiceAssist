package com.gionee.gnvoiceassist.message.io;

import com.gionee.gnvoiceassist.util.JsonUtil;

import java.lang.reflect.Type;

/**
 * Created by liyingheng on 11/8/17.
 */

public class MetadataParser {

    public static <T> T toEntity(String metadata, Type type) {
        return JsonUtil.fromJson(metadata,type);
    }

    public static String toMetadata(Object data) {
        return JsonUtil.toJson(data);
    }

}
