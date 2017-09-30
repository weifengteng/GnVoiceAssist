package com.gionee.gnvoiceassist.directiveListener.screen;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.baidu.duer.dcs.devicemodule.screen.ScreenDeviceModule;
import com.baidu.duer.dcs.devicemodule.screen.message.HtmlPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.Image;
import com.baidu.duer.dcs.devicemodule.screen.message.ImageListCardPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.Link;
import com.baidu.duer.dcs.devicemodule.screen.message.ListCardItem;
import com.baidu.duer.dcs.devicemodule.screen.message.ListCardPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.RenderHintPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.RenderVoiceInputTextPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.StandardCardPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.TextCardPayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.Utils;
import com.gionee.gnvoiceassist.widget.SimpleImageListItem;
import com.gionee.gnvoiceassist.widget.SimpleListCardItem;
import com.gionee.gnvoiceassist.widget.SimpleStandardCardItem;
import com.gionee.gnvoiceassist.widget.SimpleTextCardItem;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by twf on 2017/8/14.
 */

public class ScreenDirectiveListener extends BaseDirectiveListener implements ScreenDeviceModule.IScreenDirectiveListener {
    public static final String TAG = ScreenDirectiveListener.class.getSimpleName();
    private ScreenRender screenRender;
    private Context mAppCtx;

    public ScreenDirectiveListener(IBaseFunction baseFunction) {
        super(baseFunction);
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
        screenRender = baseFunction.getScreenRender();
    }

    @Override
    public void onRenderVoicePartial(Directive directive) {
        Payload payload = directive.getPayload();
        if(payload instanceof RenderVoiceInputTextPayload) {
            RenderVoiceInputTextPayload renderVoiceInputTextPayload = (RenderVoiceInputTextPayload) payload;
            String asrResult = renderVoiceInputTextPayload.text;
            RenderVoiceInputTextPayload.Type type = renderVoiceInputTextPayload.type;
            LogUtil.d(TAG, "asrResult= " + asrResult);
            if(type == RenderVoiceInputTextPayload.Type.FINAL) {
                screenRender.renderQueryInScreen(asrResult);
            }
        }
    }

    @Override
    public void onRenderCard(Directive directive) {

        String rawMessage = directive.rawMessage;
        Payload payload = directive.getPayload();
        String cardType = Utils.getValueByKey(rawMessage, Constants.TYPE);
        Map<String, Map<String, Object>> headerAndPayloadMap = Utils.getHeaderAndPayloadMap(rawMessage);
        Map<String, Object> payloadMap = headerAndPayloadMap.get(Constants.PAYLOAD);
//        String cardType = String.valueOf(payloadMap.get(Constants.TYPE));
        ObjectMapper mapper = new ObjectMapper();

        if(TextUtils.isEmpty(cardType)) {
            LogUtil.d(TAG, "cardType is null, return!");
            return;
        }

        switch (cardType) {
            case Constants.TEXT_CARD:
                if(payload instanceof TextCardPayload) {
                    TextCardPayload textCardPayload = (TextCardPayload) payload;
                    Link link = textCardPayload.getLink();
                    if(link != null) {
                        SimpleTextCardItem stci = new SimpleTextCardItem(mAppCtx, textCardPayload){
                            @Override
                            public void onClick() {
                                super.onClick();
                            }
                        };
                        View textCardView = stci.getView();
                        screenRender.renderInfoPanel(textCardView);
                    } else {
                        screenRender.renderAnswerInScreen(textCardPayload.getContent());
                    }
                }
                break;
            case Constants.STANDARD_CARD:
                //TODO
                if(payload instanceof StandardCardPayload) {
                    StandardCardPayload standardCardPayload = (StandardCardPayload) payload;
                    SimpleStandardCardItem ssci = new SimpleStandardCardItem(mAppCtx, standardCardPayload){
                        @Override
                        public void onClick() {
                            super.onClick();
                        }
                    };

                    screenRender.renderInfoPanel(ssci.getView());
                }
                break;
            case Constants.LIST_CARD:
                //TODO
                if(payload instanceof ListCardPayload) {
                    ListCardPayload listCardPayload = (ListCardPayload) payload;
                    List<ListCardItem> listCardItems = listCardPayload.getList();
                    SimpleListCardItem sri =  new SimpleListCardItem(mAppCtx, listCardItems){

                        @Override
                        public void onClick() {
                            super.onClick();
                        }
                    };
                    View view = sri.getView();
                    screenRender.renderInfoPanel(view);
                }
                break;
            case Constants.IMAGELIST_CARD:
                //TODO
                if(payload instanceof ImageListCardPayload) {
                    ImageListCardPayload imageListCardPayload = (ImageListCardPayload) payload;
                    List<Image> images = imageListCardPayload.getImageList();
                    SimpleImageListItem sili = new SimpleImageListItem(mAppCtx, images) {
                        @Override
                        public void onClick() {
                            super.onClick();
                        }
                    };
                    screenRender.renderInfoPanel(sili.getView());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRenderHint(RenderHintPayload renderHintPayload) {

    }

    @Override
    public void onHtmlView(HtmlPayload htmlPayload) {

    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {
    }
}
