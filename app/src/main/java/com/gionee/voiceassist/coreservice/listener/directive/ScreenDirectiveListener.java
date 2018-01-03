package com.gionee.voiceassist.coreservice.listener.directive;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gionee.voiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.ImagelistCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.ListCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.StandardCardEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.TextCardEntity;
import com.gionee.voiceassist.coreservice.sdk.module.screen.ScreenDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.HtmlPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderHintPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderVoiceInputTextPayload;
import com.gionee.voiceassist.util.LogUtil;

import java.util.List;

/**
 * Created by twf on 2017/8/14.
 */

public class ScreenDirectiveListener extends BaseDirectiveListener implements ScreenDeviceModule.IScreenListener {
    public static final String TAG = ScreenDirectiveListener.class.getSimpleName();
    private boolean isLastAsrRoundComplete = true;

    public ScreenDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }


    @Override
    public void onRenderVoiceInputText(RenderVoiceInputTextPayload payload) {
        if(payload instanceof RenderVoiceInputTextPayload) {
            RenderVoiceInputTextPayload renderVoiceInputTextPayload = (RenderVoiceInputTextPayload) payload;
            String asrResult = renderVoiceInputTextPayload.text;
            RenderVoiceInputTextPayload.Type type = renderVoiceInputTextPayload.type;
            LogUtil.d(TAG, "asrResult= " + asrResult);
            // 实时显示识别结果
            if(isLastAsrRoundComplete) {
//                screenRender.renderQueryInScreen(asrResult);
                isLastAsrRoundComplete = false;
            } else {
//                screenRender.modifyLastTextInScreen(asrResult);
                if(type == RenderVoiceInputTextPayload.Type.FINAL) {
                    isLastAsrRoundComplete = true;
//                    screenRender.setAsrResult(asrResult);
                }
            }

            /*// 只显示最终结果
            if(type == RenderVoiceInputTextPayload.Type.FINAL) {
                screenRender.renderQueryInScreen(asrResult);
            }*/
        }
    }

    @Override
    public void onHtmlPayload(HtmlPayload htmlPayload, int id) {

    }

    @Override
    public void onRenderCard(RenderCardPayload payload, int id) {
        String rawMessage = payload.content;
        RenderCardPayload.Type cardType = payload.type;
        ObjectMapper mapper = new ObjectMapper();


        ScreenDirectiveEntity cardPayload;
        switch (cardType) {
            case TextCard:
                cardPayload = new TextCardEntity();
                break;
            case StandardCard:
                //TODO
                cardPayload = new StandardCardEntity();
                break;
            case ListCard:
                cardPayload = new ListCardEntity();
                break;
            case ImageListCard:
                //TODO
                cardPayload = new ImagelistCardEntity();
                break;
            default:
                cardPayload = new TextCardEntity();
                break;
        }
        cardPayload.bind(payload);
        sendDirective(cardPayload);
    }

    @Override
    public void onRenderHint(RenderHintPayload renderHintPayload, int id) {

    }
}
