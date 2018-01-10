package com.gionee.voiceassist.usecase;

import com.gionee.voiceassist.controller.appcontrol.DataController;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;

/**
 * Created by liyingheng on 1/10/18.
 */

public abstract class BaseUsecase {

    public BaseUsecase() {

    }

    public abstract void handleDirective(DirectiveEntity payload);

    //TODO Handle ScreenInteract
//    public abstract void handleScreenInteract();

    public abstract String getAlias();

    //TODO render
    public void render() {

    }


    public void playAndRenderText(String text) {
        DataController.getDataController().getServiceController().playTts(text);
    }

    public void playAndRenderText(String text, String utterId, TtsCallback utteranceCallback) {
        DataController.getDataController().getServiceController().playTts(text, utterId, utteranceCallback);
    }

}
