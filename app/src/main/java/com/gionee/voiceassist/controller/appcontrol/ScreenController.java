package com.gionee.voiceassist.controller.appcontrol;

import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.util.ErrorCode;
import com.gionee.voiceassist.util.ErrorHelper;
import com.gionee.voiceassist.util.LogUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by liyingheng on 1/9/18.
 */

public class ScreenController {

    private static final String TAG = ScreenController.class.getSimpleName();

    public void renderToUi(CardEntity renderPayload) {
        RenderEvent event = new RenderEvent(renderPayload);
        if (EventBus.getDefault() != null) {
            EventBus.getDefault().post(event);
        } else {
            ErrorHelper.sendError(ErrorCode.INTERNAL_UNKNOWN_ERROR, "render to UI failed. Eventbus not initialize.");
        }
    }

    public void uiActionFeedback(String uri) {
        String scheme = uri.split("://")[0];
        DataController.getDataController().getUsecaseDispatcher().uiFeedbackToUsecase(uri, scheme);
    }

}
