package com.gionee.voiceassist.coreservice.datamodel;

/**
 * Created by liyingheng on 1/5/18.
 */

public class GnRemoteTvDirectiveEntity extends DirectiveEntity {

    private String tvCommand;

    public GnRemoteTvDirectiveEntity() {
        setType(Type.GN_REMOTE_TV);
    }

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
