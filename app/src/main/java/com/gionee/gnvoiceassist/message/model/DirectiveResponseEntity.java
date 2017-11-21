package com.gionee.gnvoiceassist.message.model;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

import org.json.JSONObject;

/**
 * 解析结果返回数据结构
 */

public class DirectiveResponseEntity {

    private DirectiveResponseEntity(Builder builder) {
        usecase = builder.useCase;
        action = builder.action;
        inCustomInteractive = builder.inCustomInteractive;
        shouldRender = builder.shouldRender;
        shouldSpeak = builder.shouldSpeak;
        renderContent = builder.renderEntity;
        speakText = builder.speakText;
        metadata = builder.metadata;
    }

    public DirectiveResponseEntity(
            String usecase,
            String action,
            String subAction,
            boolean inCustomInteractive,
            boolean shouldRender,
            boolean shouldSpeak,
            RenderEntity renderContent,
            String speakText,
            String metadata,
            CUIEntity customInteract) {
        this.usecase = usecase;
        this.action = action;
        this.subAction = subAction;
        this.inCustomInteractive = inCustomInteractive;
        this.shouldRender = shouldRender;
        this.shouldSpeak = shouldSpeak;
        this.renderContent = renderContent;
        this.speakText = speakText;
        this.metadata = metadata;
        this.customInteract = customInteract;
    }

    //用例场景
    private String usecase;

    //命令
    private String action;

    //二级命令（如确认/取消）
    private String subAction;

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

    //用例具体元数据(json格式)
    private String metadata;

    //多轮交互请求信息
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

    public String getSubAction() {
        return subAction;
    }

    public void setSubAction(String subAction) {
        this.subAction = subAction;
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

    public static class Builder {
        private String useCase = "";
        private String action = "";
        private String subAction = "";
        private boolean inCustomInteractive = false;
        private boolean shouldRender = false;
        private boolean shouldSpeak = false;
        private RenderEntity renderEntity;
        private String speakText;
        private String metadata;
        private String customInteract;

        public Builder(String usecase) {
            this.useCase = usecase;
            action = "";
            subAction = "";
            speakText = "";
            metadata = "";
            customInteract = "";
        }

        public Builder setAction(String action) {
            this.action = action;
            return this;
        }

        public Builder setSubAction(String subAction) {
            this.subAction = subAction;
            return this;
        }

        public Builder setInCustomInteractive(boolean inCustomInteractive) {
            this.inCustomInteractive = inCustomInteractive;
            return this;
        }

        public Builder setShouldRender (boolean shouldRender) {
            this.shouldRender = shouldRender;
            return this;
        }

        public Builder setShouldSpeak(boolean shouldSpeak) {
            this.shouldSpeak = shouldSpeak;
            return this;
        }

        public Builder setRenderEntity(RenderEntity render) {
            this.renderEntity = render;
            return this;
        }

        public Builder setMetadata(String metadata) {
            this.metadata = metadata;
            return this;
        }

        public DirectiveResponseEntity build() {
            return new DirectiveResponseEntity(this);
        }

    }

}
