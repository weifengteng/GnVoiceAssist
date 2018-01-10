package com.gionee.voiceassist.usecase.gnremote;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.GnRemoteTvDirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.kookong.TeleControlPresenter;

/**
 * Created by twf on 2017/8/24.
 */

public class GnRemoteUsecase extends BaseUsecase {

    public GnRemoteUsecase() {

    }

    public void executeVoiceCmd(String cmd) {
        TeleControlPresenter executeVoiceCmdService = new TeleControlPresenter(cmd);
        executeVoiceCmdService.execute();
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        if (payload instanceof GnRemoteDirectiveEntity) {
            GnRemoteDirectiveEntity gnremotePayload = (GnRemoteDirectiveEntity) payload;
            executeVoiceCmd(gnremotePayload.getCommand());
        } else if (payload instanceof GnRemoteTvDirectiveEntity) {
            GnRemoteTvDirectiveEntity gnremoteTvPayload = (GnRemoteTvDirectiveEntity) payload;
            executeVoiceCmd(gnremoteTvPayload.getTvCommand());
        }
    }

    @Override
    public String getAlias() {
        return "gnremote";
    }


}
