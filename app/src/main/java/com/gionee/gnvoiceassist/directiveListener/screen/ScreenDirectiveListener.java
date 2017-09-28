package com.gionee.gnvoiceassist.directiveListener.screen;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.baidu.duer.dcs.devicemodule.screen.ScreenDeviceModule;
import com.baidu.duer.dcs.devicemodule.screen.message.HtmlPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.RenderHintPayload;
import com.baidu.duer.dcs.devicemodule.screen.message.RenderVoiceInputTextPayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.screenrender.ScreenRender;
import com.gionee.gnvoiceassist.bean.ImageListBean;
import com.gionee.gnvoiceassist.bean.ListCardItemBean;
import com.gionee.gnvoiceassist.bean.StandardCardItemBean;
import com.gionee.gnvoiceassist.bean.TextCardBean;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.Utils;
import com.gionee.gnvoiceassist.widget.SimpleImageListItem;
import com.gionee.gnvoiceassist.widget.SimpleListCardItem;
import com.gionee.gnvoiceassist.widget.SimpleStandardCardItem;
import com.gionee.gnvoiceassist.widget.SimpleTextCardItem;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
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
        //test code
        /*String testJsonStr = "{\"list\":[{\"img\":\"http\"},{\"img2\":\"https\"}]}";
        try {
            Map<String, Object> testJsonMap = new ObjectMapper().readValue(testJsonStr, Map.class);
            Object obj = testJsonMap.get("list");
            String testListStr = String.valueOf(obj);
            Log.d("DCSF", "onRenderCard: *********************************" + testListStr);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        //test code

        String rawMessage = directive.rawMessage;
        Map<String, Map<String, Object>> headerAndPayloadMap = Utils.getHeaderAndPayloadMap(rawMessage);
        Map<String, Object> payloadMap = headerAndPayloadMap.get(Constants.PAYLOAD);
        String cardType = String.valueOf(payloadMap.get(Constants.TYPE));
        ObjectMapper mapper = new ObjectMapper();

        if(TextUtils.isEmpty(cardType)) {
            LogUtil.d(TAG, "cardType is null, return!");
            return;
        }

        switch (cardType) {
            case Constants.TEXT_CARD:
//                String content = Utils.getValueByKey(rawMessage, Constants.CONTENT);
                TextCardBean textCardBean = null;
                try {
                    String jsonStr = mapper.writeValueAsString(payloadMap);
                    textCardBean = mapper.readValue(jsonStr, TextCardBean.class);
                    LogUtil.d(TAG, "beanList : " + textCardBean.toString());
                } catch (IOException e) {
                    LogUtil.d(TAG, "textcard exception : " + e.toString());
                    e.printStackTrace();
                }

                TextCardBean.Link link = textCardBean.getLink();
                if(link != null) {
                    SimpleTextCardItem stci = new SimpleTextCardItem(mAppCtx, textCardBean){
                        @Override
                        public void onClick() {
                            super.onClick();
                        }
                    };
                    View textCardView = stci.getView();
                    screenRender.renderInfoPanel(textCardView);
                } else {
                    screenRender.renderAnswerInScreen(textCardBean.getContent());
                }

                break;
            case Constants.STANDARD_CARD:
                //TODO
                StandardCardItemBean bean = null;
                try {
                    String jsonStr = mapper.writeValueAsString(payloadMap);
                    bean = mapper.readValue(jsonStr, StandardCardItemBean.class);
                    LogUtil.d(TAG, "beanList : " + bean.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SimpleStandardCardItem ssci = new SimpleStandardCardItem(mAppCtx, bean){
                    @Override
                    public void onClick() {
                        super.onClick();
                    }
                };

                screenRender.renderInfoPanel(ssci.getView());
                break;
            case Constants.LIST_CARD:
                //TODO
                ArrayList<ListCardItemBean> beanList = new ArrayList<>();
                try {
                    String jsonStr = mapper.writeValueAsString(payloadMap.get("list"));
                    beanList = mapper.readValue(jsonStr, new TypeReference<List<ListCardItemBean>>() {});
                    LogUtil.d(TAG, "beanList : " + beanList.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SimpleListCardItem sri =  new SimpleListCardItem(mAppCtx, beanList){

                    @Override
                    public void onClick() {
                        super.onClick();
                    }
                };
                View view = sri.getView();
                screenRender.renderInfoPanel(view);
                break;
            case Constants.IMAGELIST_CARD:
                //TODO
                ArrayList<ImageListBean> imageListBeans = new ArrayList<>();
                try {
                    String jsonStr = mapper.writeValueAsString(payloadMap.get("imageList"));
                    imageListBeans = mapper.readValue(jsonStr, new TypeReference<List<ImageListBean>>() {});
                    LogUtil.d(TAG, "imageListBeans : " + imageListBeans.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SimpleImageListItem sili = new SimpleImageListItem(mAppCtx, imageListBeans) {
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
