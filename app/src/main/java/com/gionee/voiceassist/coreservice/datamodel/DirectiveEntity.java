package com.gionee.voiceassist.coreservice.datamodel;

import java.io.Serializable;

/**
 * Created by liyingheng on 12/29/17.
 */

public class DirectiveEntity implements Serializable {

    private Type type;

    public Type getType() {
        return type;
    }

    void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        ALARM,
        PHONECALL,
        SMS,
        SCREEN
    }

}
