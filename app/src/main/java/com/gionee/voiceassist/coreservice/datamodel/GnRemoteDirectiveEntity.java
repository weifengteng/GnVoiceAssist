package com.gionee.voiceassist.coreservice.datamodel;

/**
 * Created by liyingheng on 1/5/18.
 */

public class GnRemoteDirectiveEntity extends DirectiveEntity {

    private String command;

    public GnRemoteDirectiveEntity() {
        setType(Type.GN_REMOTE);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "GnRemoteDirectiveEntity{" +
                "command='" + command + '\'' +
                '}';
    }
}
