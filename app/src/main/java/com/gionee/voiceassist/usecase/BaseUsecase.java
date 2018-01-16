package com.gionee.voiceassist.usecase;

import android.content.Context;

import com.gionee.voiceassist.controller.appcontrol.DataController;
import com.gionee.voiceassist.controller.appcontrol.ScreenController;
import com.gionee.voiceassist.controller.customuserinteraction.ICuiControl;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.util.ContextUtil;

/**
 * Created by liyingheng on 1/10/18.
 */

public abstract class BaseUsecase {

    public BaseUsecase() {

    }

    public abstract void handleDirective(DirectiveEntity payload);

    public abstract void handleUiFeedback(String uri);

    public abstract String getAlias();

    //TODO render
    public void render(CardEntity cardEntity) {
        DataController.getDataController().getScreenController().renderToUi(cardEntity);
    }


    public void playAndRenderText(String text) {
        DataController.getDataController().getServiceController().playTts(text);
    }

    public void playAndRenderText(String text, String utterId, TtsCallback utteranceCallback) {
        DataController.getDataController().getServiceController().playTts(text, utterId, utteranceCallback);
    }


    public ICuiControl getCuiController() {
        return DataController.getDataController().getServiceController().getCUIController();
    }

    public void startRecord() {
        DataController.getDataController().getServiceController().startRecord();
    }

    public Context getAppContext() {
        return ContextUtil.getAppContext();
    }


}
