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

    //用例具体元数据
    private JSONObject metadata;

    public static class Builder {
        private String useCase = "";
        private String action = "";
        private boolean inCustomInteractive = false;
        private boolean shouldRender = false;
        private boolean shouldSpeak = false;
        private RenderEntity renderEntity;
        private String speakText = "";
        private JSONObject metadata;

        public Builder(String usecase) {
            this.useCase = usecase;
        }

        public Builder setAction(String action) {
            this.action = action;
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

        public Builder setMetadata(JSONObject metadata) {
            this.metadata = metadata;
            return this;
        }

        public DirectiveResponseEntity build() {
            return new DirectiveResponseEntity(this);
        }

    }

}
