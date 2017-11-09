package com.gionee.gnvoiceassist.message.model;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

import org.json.JSONObject;

/**
 * 具体用例返回的信息结构
 */

public class UsecaseResponseEntity {

    public UsecaseResponseEntity(String usecase,
                                 String action,
                                 boolean inCustomInteractive,
                                 boolean shouldRender,
                                 boolean shouldSpeak,
                                 RenderEntity renderContent,
                                 String speakText,
                                 boolean shouldOperateAfterSpeak,
                                 String metadata,
                                 CUIEntity customInteract) {
        this.usecase = usecase;
        this.action = action;
        this.inCustomInteractive = inCustomInteractive;
        this.shouldRender = shouldRender;
        this.shouldSpeak = shouldSpeak;
        this.renderContent = renderContent;
        this.speakText = speakText;
        this.shouldOperateAfterSpeak = shouldOperateAfterSpeak;
        this.metadata = metadata;
        this.customInteract = customInteract;
    }

    //用例场景
    private String usecase;

    //命令
    private String action;

    //是否为自定义交互指令
    private boolean inCustomInteractive;

    //是否需要显示到界面
    private boolean shouldRender;

    //是否需要朗读
    private boolean shouldSpeak;

    //需要显示在界面的内容
    private RenderEntity renderContent;

    //需要朗读的文字
    private String speakText;

    //是否在结束时才执行操作
    private boolean shouldOperateAfterSpeak;

    //用例具体元数据
    private String metadata;

    //自定义交互元数据
    private CUIEntity customInteract;

    public String getUsecase() {
        return usecase;
    }

    public void setUsecase(String usecase) {
        this.usecase = usecase;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isInCustomInteractive() {
        return inCustomInteractive;
    }

    public void setInCustomInteractive(boolean inCustomInteractive) {
        this.inCustomInteractive = inCustomInteractive;
    }

    public boolean isShouldRender() {
        return shouldRender;
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    public boolean isShouldSpeak() {
        return shouldSpeak;
    }

    public void setShouldSpeak(boolean shouldSpeak) {
        this.shouldSpeak = shouldSpeak;
    }

    public RenderEntity getRenderContent() {
        return renderContent;
    }

    public void setRenderContent(RenderEntity renderContent) {
        this.renderContent = renderContent;
    }

    public String getSpeakText() {
        return speakText;
    }

    public void setSpeakText(String speakText) {
        this.speakText = speakText;
    }

    public boolean isShouldOperateAfterSpeak() {
        return shouldOperateAfterSpeak;
    }

    public void setShouldOperateAfterSpeak(boolean shouldOperateAfterSpeak) {
        this.shouldOperateAfterSpeak = shouldOperateAfterSpeak;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public CUIEntity getCustomInteract() {
        return customInteract;
    }

    public void setCustomInteract(CUIEntity customInteract) {
        this.customInteract = customInteract;
    }
}
