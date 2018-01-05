package com.gionee.voiceassist.coreservice.datamodel;

/**
 * 金立遥控场景Payload
 */

public class GnRemoteDirectiveEntity extends DirectiveEntity {

    private String command;

    public GnRemoteDirectiveEntity() {
        setType(Type.GN_REMOTE);
    }

    /**
     * 取得遥控命令
     * @return 遥控命令
     */
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
