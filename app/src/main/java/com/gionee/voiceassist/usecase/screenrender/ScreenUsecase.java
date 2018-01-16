package com.gionee.voiceassist.usecase.screenrender;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.ImagelistCardEntity;
import com.gionee.voiceassist.datamodel.card.ImageListCardEntity;
import com.gionee.voiceassist.datamodel.card.ListCardEntity;
import com.gionee.voiceassist.datamodel.card.StandardCardEntity;
import com.gionee.voiceassist.datamodel.card.TextCardEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.LogUtil;


/**
 * Created by twf on 2017/8/25.
 */

public class ScreenUsecase extends BaseUsecase{
    private String asrResult;

    public ScreenUsecase() {

    }

//    public void renderQueryInScreen(String asrResult) {
//        Message msg = Message.obtain();
//        msg.what = Constants.MSG_SHOW_QUERY;
//        msg.obj = asrResult;
//        mHandler.sendMessage(msg);
//    }
//
//    public void renderAnswerInScreen(String content) {
//        LogUtil.d("DCSF", "onRenderCard: content= " + content);
//        if(!TextUtils.isEmpty(content) && !"null".equals(content) && !"NULL".equals(content)) {
//            Message msg = Message.obtain();
//            msg.what = Constants.MSG_SHOW_ANSWER;
//            msg.obj = content;
//            mHandler.sendMessage(msg);
//        }
//    }
//
//    public void modifyLastTextInScreen(String content) {
//        if(!TextUtils.isEmpty(content) && !"null".equals(content) && !"NULL".equals(content)) {
//            Message msg = Message.obtain();
//            msg.what = Constants.MSG_MODIFY_LAST_TEXT;
//            msg.obj = content;
//            mHandler.sendMessage(msg);
//        }
//    }
//
//    public void renderInfoPanel(View infoPanel) {
//        if(infoPanel != null) {
//            Message msg = Message.obtain();
//            msg.what = Constants.MSG_SHOW_INFO_PANEL;
//            msg.obj = infoPanel;
//            mHandler.sendMessage(msg);
//        }
//    }
//
//    public void renderVoiceInputVolume(int volume) {
//        Message msg = Message.obtain();
//        msg.what = Constants.MSG_UPDATE_INPUTVOLUME;
//        msg.arg1 = volume;
//        mHandler.sendMessage(msg);
//    }

    public String getAsrResult() {
        return asrResult;
    }

    public void setAsrResult(String asrResult) {
        this.asrResult = asrResult;
    }


    @Override
    public void handleDirective(DirectiveEntity payload) {
        ScreenDirectiveEntity screenPayload = (ScreenDirectiveEntity) payload;
        ScreenDirectiveEntity.CardType cardType = screenPayload.getCardType();
        switch (cardType) {
            case TEXT_CARD:
                fireTextCard((com.gionee.voiceassist.coreservice.datamodel.screen.TextCardEntity) payload);
                break;
            case STANDARD_CARD:
                fireStandardCard((com.gionee.voiceassist.coreservice.datamodel.screen.StandardCardEntity) payload);
                break;
            case LIST_CARD:
                fireListCard((com.gionee.voiceassist.coreservice.datamodel.screen.ListCardEntity) payload);
                break;
            case IMAGE_LIST_CARD:
                fireImageListCard((ImagelistCardEntity) payload);
                break;
        }
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "screen";
    }

    private void fireTextCard(com.gionee.voiceassist.coreservice.datamodel.screen.TextCardEntity payload) {
        TextCardEntity cardEntity = new TextCardEntity();
        cardEntity.setTitle(payload.getTitle());
        cardEntity.setContent(payload.getContent());
        if (payload.getLink() != null) {
            cardEntity.setExtLink(payload.getLink().src, payload.getLink().anchor);
        }
        render(cardEntity);
    }

    private void fireStandardCard(com.gionee.voiceassist.coreservice.datamodel.screen.StandardCardEntity payload) {
        StandardCardEntity cardEntity = new StandardCardEntity();
        cardEntity.setTitle(payload.getTitle());
        cardEntity.setContent(payload.getContent());
        if (payload.getImageSrc() != null) {
            cardEntity.setImgSrc(payload.getImageSrc());
        }
        if (payload.getLink() != null) {
            cardEntity.setExtLink(payload.getLink().src, payload.getLink().anchor);
        }
        render(cardEntity);
    }

    private void fireListCard(com.gionee.voiceassist.coreservice.datamodel.screen.ListCardEntity payload) {
        ListCardEntity cardEntity = new ListCardEntity();
        for (com.gionee.voiceassist.coreservice.datamodel.screen.ListCardEntity.ListItem item:payload.getItems()) {
            String title = item.title;
            String content = item.description;
            String imgSrc = item.imgSrc;
            String link = item.url;
            cardEntity.addItem(new ListCardEntity.ListItem(title, content, imgSrc, link, ""));
        }
        render(cardEntity);
    }

    private void fireImageListCard(ImagelistCardEntity payload) {
        ImageListCardEntity cardEntity = new ImageListCardEntity();
        for (String item:payload.getImages()) {
            cardEntity.addImageItem(item);
        }
        render(cardEntity);
    }
}
