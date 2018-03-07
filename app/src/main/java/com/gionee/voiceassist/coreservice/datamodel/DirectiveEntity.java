package com.gionee.voiceassist.coreservice.datamodel;

import java.io.Serializable;

/**
 * 语音助手服务消息的基类
 */

public class DirectiveEntity implements Serializable {

    private Type type;

    public Type getType() {
        return type;
    }

    void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        ALARM,
        PHONECALL,
        SMS,
        SCREEN,
        SCREEN_EXTENDED,
        APPLAUNCH,
        CONTACTS,
        GIONEE_CUSTOM_COMMAND,
        LOCAL_AUDIOPLAYER,
        GN_REMOTE,
        GN_REMOTE_TV,
        WEBBROWSER,
        REMINDER,
        DEVICE_CONTROL
    }

}
