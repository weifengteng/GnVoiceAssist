package com.gionee.voiceassist.coreservice.datamodel;

/**
 * 金立遥控电视场景Payload
 */

public class GnRemoteTvDirectiveEntity extends DirectiveEntity {

    private String tvCommand;

    public GnRemoteTvDirectiveEntity() {
        setType(Type.GN_REMOTE_TV);
    }

    /**
     * 取得遥控命令
     * @return
     */
    public String getTvCommand() {
        return tvCommand;
    }

    public void setTvCommand(String tvCommand) {
        this.tvCommand = tvCommand;
    }

    @Override
    public String toString() {
        return "GnRemoteTvDirectiveEntity{" +
                "tvCommand='" + tvCommand + '\'' +
                '}';
    }
}
