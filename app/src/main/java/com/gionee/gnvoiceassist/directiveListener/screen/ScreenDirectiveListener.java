package com.gionee.gnvoiceassist.directiveListener.screen;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

//import com.baidu.duer.dcs.devicemodule.screen.ScreenDeviceModule;
//import com.baidu.duer.dcs.devicemodule.screen.message.HtmlPayload;
//import com.baidu.duer.dcs.devicemodule.screen.message.Image;
//import com.baidu.duer.dcs.devicemodule.screen.message.ImageListCardPayload;
//import com.baidu.duer.dcs.devicemodule.screen.message.Link;
//import com.baidu.duer.dcs.devicemodule.screen.message.ListCardItem;
//import com.baidu.duer.dcs.devicemodule.screen.message.ListCardPayload;
//import com.baidu.duer.dcs.devicemodule.screen.message.RenderHintPayload;
//import com.baidu.duer.dcs.devicemodule.screen.message.RenderVoiceInputTextPayload;
//import com.baidu.duer.dcs.devicemodule.screen.message.StandardCardPayload;
//import com.baidu.duer.dcs.devicemodule.screen.message.TextCardPayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.message.io.RenderInfoGenerator;
import com.gionee.gnvoiceassist.message.model.render.TextRenderEntity;
import com.gionee.gnvoiceassist.sdk.module.screen.ScreenDeviceModule;
import com.gionee.gnvoiceassist.sdk.module.screen.message.HtmlPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.Image;
import com.gionee.gnvoiceassist.sdk.module.screen.message.ImageListCardPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.Link;
import com.gionee.gnvoiceassist.sdk.module.screen.message.ListCardItem;
import com.gionee.gnvoiceassist.sdk.module.screen.message.ListCardPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.RenderCardPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.RenderHintPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.RenderVoiceInputTextPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.StandardCardPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.TextCardPayload;
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;
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

public class ScreenDirectiveListener extends BaseDirectiveListener implements ScreenDeviceModule.IScreenListener {
    public static final String TAG = ScreenDirectiveListener.class.getSimpleName();
//    private ScreenRender screenRender;
    private Context mAppCtx;

    public ScreenDirectiveListener(IDirectiveListenerCallback callback) {
        super(callback);
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {
    }

    @Override
    public void onRenderVoiceInputText(RenderVoiceInputTextPayload payload, int id) {
        if(payload instanceof RenderVoiceInputTextPayload) {
            RenderVoiceInputTextPayload renderVoiceInputTextPayload = (RenderVoiceInputTextPayload) payload;
            String asrResult = renderVoiceInputTextPayload.text;
            RenderVoiceInputTextPayload.Type type = renderVoiceInputTextPayload.type;
            LogUtil.d(TAG, "asrResult= " + asrResult + "; id = " + id);
            RenderInfoGenerator.GenerateText builder = new RenderInfoGenerator.GenerateText()
                    .setQuery(true)
                    .setContent(asrResult)
                    .setPartial(true);
            if(type == RenderVoiceInputTextPayload.Type.FINAL) {
//                screenRender.renderQueryInScreen(asrResult);
                builder.setPartial(false);
            }
            TextRenderEntity renderResponse = builder.build();
            mCallback.onRenderResponse(renderResponse);
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
            case TextCard: {
                RenderInfoGenerator.GenerateText builder = new RenderInfoGenerator.GenerateText();
                RenderCardPayload.LinkStructure link = payload.link;
                if (link != null) {
                    builder.setLink(link.anchorText, link.url); //RenderInfoPanel
                }
                builder.setContent(payload.content);        //RenderAnswer
                builder.setQuery(false);
                mCallback.onRenderResponse(builder.build());    //发送渲染信息回Service
                break;
            }
            case StandardCard: {
                //RenderInfoPanel
                RenderInfoGenerator.GenerateStandard builder = new RenderInfoGenerator.GenerateStandard();
                builder.setTitle(payload.title);
                builder.setContent(payload.content);
                if (payload.image != null) {
                    builder.setImage(payload.image.src);
                }
                if (payload.link != null) {
                    builder.setLink(payload.link.anchorText,payload.link.url);
                }
                mCallback.onRenderResponse(builder.build());
                break;
            }
            case ListCard: {
                //RenderInfoPanel
                RenderInfoGenerator.GenerateList builder = new RenderInfoGenerator.GenerateList();
                List<RenderCardPayload.ListItem> listCardItems = payload.list;
                for (RenderCardPayload.ListItem item:listCardItems) {
                    String imageSrc = "";
                    String linkSrc = "";
                    String anchorText = "";
                    if (item.image != null && item.image.src != null) {
                        imageSrc = item.image.src;
                    }
                    if (item.url != null) {
                        linkSrc = item.url;
                    }
                    builder.addItem(item.title,item.content,imageSrc,linkSrc);
                }
                mCallback.onRenderResponse(builder.build());
                break;
            }
            case ImageListCard: {
                //RenderInfoPanel
                RenderInfoGenerator.GenerateImageList builder = new RenderInfoGenerator.GenerateImageList();
                List<RenderCardPayload.ImageStructure> images = payload.imageList;
                for (RenderCardPayload.ImageStructure image : images) {
                    builder.addImage(image.src);
                }
                mCallback.onRenderResponse(builder.build());
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onRenderHint(RenderHintPayload renderHintPayload, int id) {

    }
}
