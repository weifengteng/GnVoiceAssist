package com.gionee.voiceassist.directiveListener.screen;

import android.content.Context;
import android.view.View;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.voiceassist.coreservice.sdk.module.screen.ScreenDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.HtmlPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderHintPayload;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderVoiceInputTextPayload;
import com.gionee.voiceassist.view.viewitem.SimpleImageListItem;
import com.gionee.voiceassist.view.viewitem.SimpleListCardItem;
import com.gionee.voiceassist.view.viewitem.SimpleStandardCardItem;
import com.gionee.voiceassist.view.viewitem.SimpleTextCardItem;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Created by twf on 2017/8/14.
 */

public class ScreenDirectiveListener extends BaseDirectiveListener implements ScreenDeviceModule.IScreenListener {
    public static final String TAG = ScreenDirectiveListener.class.getSimpleName();
    private ScreenRender screenRender;
    private Context mAppCtx;
    private boolean isLastAsrRoundComplete = true;

    public ScreenDirectiveListener(IBaseFunction baseFunction) {
        super(baseFunction);
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
        screenRender = baseFunction.getScreenRender();
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
                screenRender.renderQueryInScreen(asrResult);
                isLastAsrRoundComplete = false;
            } else {
                screenRender.modifyLastTextInScreen(asrResult);
                if(type == RenderVoiceInputTextPayload.Type.FINAL) {
                    isLastAsrRoundComplete = true;
                    screenRender.setAsrResult(asrResult);
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


        switch (cardType) {
            case TextCard:
                RenderCardPayload.LinkStructure link = payload.link;
                if (link != null) {
                    SimpleTextCardItem cardItem = new SimpleTextCardItem(mAppCtx,payload) {
                        @Override
                        public void onClick() {
                            super.onClick();
                        }
                    };
                    View textCardView = cardItem.getView();
                    screenRender.renderInfoPanel(textCardView);
                } else {
                    screenRender.renderAnswerInScreen(payload.content);
                }
                break;
            case StandardCard:
                //TODO
                SimpleStandardCardItem ssci = new SimpleStandardCardItem(mAppCtx,payload) {
                    @Override
                    public void onClick() {
                        super.onClick();
                    }
                };
                screenRender.renderInfoPanel(ssci.getView());
                break;

            case ListCard:
                List<RenderCardPayload.ListItem> listCardItems = payload.list;
                SimpleListCardItem slci = new SimpleListCardItem(mAppCtx, listCardItems) {
                    @Override
                    public void onClick() {
                        super.onClick();
                    }
                };
                View slciView = slci.getView();
                screenRender.renderInfoPanel(slciView);
                break;
            case ImageListCard:
                //TODO
                List<RenderCardPayload.ImageStructure> images = payload.imageList;
                SimpleImageListItem sili = new SimpleImageListItem(mAppCtx, images) {
                    @Override
                    public void onClick() {
                        super.onClick();
                    }
                };
                screenRender.renderInfoPanel(sili.getView());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRenderHint(RenderHintPayload renderHintPayload, int id) {

    }

    @Override
    public void onDestroy() {

    }
}
