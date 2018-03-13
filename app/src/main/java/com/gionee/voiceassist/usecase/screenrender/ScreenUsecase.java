package com.gionee.voiceassist.usecase.screenrender;

import android.os.Handler;
import android.os.HandlerThread;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.ScreenDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.screen.ImagelistCardEntity;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.ImageListCardEntity;
import com.gionee.voiceassist.datamodel.card.ListCardEntity;
import com.gionee.voiceassist.datamodel.card.DateQueryCardEntity;
import com.gionee.voiceassist.datamodel.card.QueryTextCardEntity;
import com.gionee.voiceassist.datamodel.card.StandardCardEntity;
import com.gionee.voiceassist.datamodel.card.AnswerTextCardEntity;
import com.gionee.voiceassist.datamodel.card.WorldTimeQueryCardEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.SharedData;
import com.gionee.voiceassist.view.viewholder.QueryTextCardViewHolder;

import java.util.ArrayList;


/**
 * Created by twf on 2017/8/25.
 */

public class ScreenUsecase extends BaseUsecase{
    private final static String TAG = ScreenUsecase.class.getSimpleName();
    private String asrResult;
    private QueryTextCardEntity queryTextCardEntity;
    private volatile boolean isVoiceInputFinal = true;
    private volatile boolean isAsrResultFrozen;
    private volatile ArrayList<String> partialAsrResultList = new ArrayList<>();

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
        CardEntity cardEntity;
        LogUtil.d("TWF", "fireTextCard: " + payload.getContent());
        if(payload.isVoiceInputText()) {
            if(isVoiceInputFinal) {
                SharedData.getInstance().setLastQueryItemPosition(-1);
                isVoiceInputFinal = false;
                partialAsrResultList.clear();
                // 一条新的 voiceRequest，第一次返回 asr 结果先绑定
                queryTextCardEntity = new QueryTextCardEntity();
                queryTextCardEntity.setContent(payload.getContent());
                queryTextCardEntity.setCallbackBindInterface(new QueryTextCardEntity.IPartialResultCallbackBind() {
                    @Override
                    public void onCallbackBind(final QueryTextCardViewHolder.AsrPartialResultCallback callback) {

                        HandlerThread handlerThread = new HandlerThread("callbackThread");
                        handlerThread.start();
                        Handler handler = new Handler(handlerThread.getLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int count = 0;
                                while (!isVoiceInputFinal || !partialAsrResultList.isEmpty()) {

                                    if(partialAsrResultList.isEmpty()) {
                                        count++;
                                        if(count == 100) {
                                            // 连续超过5秒没有 partial asr result 识别结果，退出。
                                            LogUtil.d("TWF", "partialAsrResultList is empty, count max = " + count + " , break ");
                                            break;
                                        } else {
                                            try {
                                                Thread.sleep(50);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } finally {
                                                continue;
                                            }
                                        }
                                    }
                                    String partialResult = partialAsrResultList.remove(0);
                                    callback.onPartialResult(partialResult);
                                    count = 0;
                                    LogUtil.d("TWF", "update PartialResultCallback " + partialResult);
                                }
                            }
                        });
                    }

                    @Override
                    public void onTextUpdateFreeze(String frozenText) {
                        isAsrResultFrozen = true;
                        SharedData.getInstance().setAsrResult(frozenText);
                    }
                });

                render(queryTextCardEntity);
            } else {
                // 之后返回的结果都以回调形式更新
                partialAsrResultList.add(payload.getContent());
            }

            if(payload.isFinalResult()) {
                if(!isAsrResultFrozen) {
                    SharedData.getInstance().setAsrResult(payload.getContent());
                }
                isVoiceInputFinal = true;
                queryTextCardEntity = null;
            }
        } else {
            cardEntity = new AnswerTextCardEntity();
            cardEntity.setTitle(payload.getTitle());
            cardEntity.setContent(payload.getContent());
            if (payload.getLink() != null) {
                cardEntity.setExtLink(payload.getLink().src, payload.getLink().anchor);
            }
            render(cardEntity);

            // TODO: 针对日期、世界时查询的处理
            if(isDateQueryCmd()) {
                render(resolveDateQueryCardEntity(payload.getContent()));
            } else if (isWorldTimeQueryCmd()) {
                render(resolveWorldTimeQueryCardEntity(payload.getContent()));
            }
        }
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

    private boolean isDateQueryCmd() {
        // TODO: wait for luhong solution
        return false;
    }

    private boolean isWorldTimeQueryCmd() {
        // TODO: wait for luhong solution
        return false;
    }

    private DateQueryCardEntity resolveDateQueryCardEntity(String query) {
        // TODO: resolve DateQueryCardEntity from nlp engine
        DateQueryCardEntity entity = new DateQueryCardEntity();
        return entity;
    }

    private WorldTimeQueryCardEntity resolveWorldTimeQueryCardEntity(String query) {
        // TODO: resolve WorldTimeQueryCardEntity from nlp engine
        return new WorldTimeQueryCardEntity();
    }
}
