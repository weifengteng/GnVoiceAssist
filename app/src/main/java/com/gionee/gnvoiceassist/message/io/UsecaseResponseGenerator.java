package com.gionee.gnvoiceassist.message.io;


import android.text.TextUtils;

import com.gionee.gnvoiceassist.message.model.CUIEntity;
import com.gionee.gnvoiceassist.message.model.UsecaseResponseEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

import org.json.JSONObject;

/**
 * 生成Usecase回复信息的工具类。实际上是一个Builder。
 */

public class UsecaseResponseGenerator {

    private String usecase;     //用例场景
    private String action;          //命令
    private boolean inCustomInteractive;        //是否为自定义交互指令
    private boolean shouldRender;           //是否需要显示到界面
    private boolean shouldSpeak;            //是否需要朗读
    private RenderEntity renderContent;     //需要显示在界面的内容
    private String speakText;                   //需要朗读的文字
    private boolean shouldOperateAfterSpeak;        //是否在结束时才执行操作
    private String metadata;                    //用例具体元数据
    private CUIEntity customInteract;               //多轮交互元数据

    public UsecaseResponseGenerator(String usecase, String action) {
        this.usecase = usecase;
        this.action = action;
        inCustomInteractive = false;
        shouldRender = false;
        shouldSpeak = false;
        renderContent = null;
        speakText = "";
        shouldOperateAfterSpeak = false;
        metadata = "";
        customInteract = null;
    }

    public UsecaseResponseGenerator setInCustomInteractive(boolean inCustomInteractive) {
        this.inCustomInteractive = inCustomInteractive;
        return this;
    }

    public UsecaseResponseGenerator setRenderContent(RenderEntity renderContent) {
        if (renderContent != null) {
            this.shouldRender = true;
            this.renderContent = renderContent;
        } else {
            this.shouldRender = false;
        }
        return this;
    }

    public UsecaseResponseGenerator setSpeakText(String speakText) {
        if (!TextUtils.isEmpty(speakText)) {
            this.shouldSpeak = true;
            this.speakText = speakText;
        } else {
            this.shouldSpeak = false;
        }
        return this;
    }

    public UsecaseResponseGenerator setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    public UsecaseResponseGenerator setCustomInteract(CUIEntity customInteract) {
        this.customInteract = customInteract;
        return this;
    }

    public UsecaseResponseEntity generateEntity() {
        return new UsecaseResponseEntity(
                usecase,
                action,
                inCustomInteractive,
                shouldRender,
                shouldSpeak,
                renderContent,
                speakText,
                shouldOperateAfterSpeak,
                metadata,
                customInteract);
    }
}
