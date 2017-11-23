package com.gionee.gnvoiceassist.message.io;

import android.text.TextUtils;

import com.gionee.gnvoiceassist.message.model.CUIEntity;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

/**
 * Created by liyingheng on 11/17/17.
 */

public class DirectiveResponseGenerator {

    private String useCase = "";
    private String action = "";
    private String subAction = "";
    private boolean inCustomInteractive = false;
    private boolean shouldRender = false;
    private boolean shouldSpeak = false;
    private RenderEntity renderEntity;
    private String speakText;
    private String metadata;
    private CUIEntity customInteract;

    public DirectiveResponseGenerator(String usecase) {
        this.useCase = usecase;
        action = "";
        subAction = "";
        speakText = "";
        metadata = "";
        customInteract = null;
    }

    public DirectiveResponseGenerator setAction(String action) {
        this.action = action;
        return this;
    }

    public DirectiveResponseGenerator setSubAction(String subAction) {
        this.subAction = subAction;
        return this;
    }

    public DirectiveResponseGenerator setInCustomInteractive(boolean inCustomInteractive) {
        this.inCustomInteractive = inCustomInteractive;
        return this;
    }

    public DirectiveResponseGenerator setSpeakText(String text) {
        if (text != null && !TextUtils.isEmpty(text)) {
            this.speakText = text;
            this.shouldSpeak = true;
        } else {
            this.shouldSpeak = false;
        }
        return this;
    }

    public DirectiveResponseGenerator setRenderEntity(RenderEntity render) {
        if (render != null) {
            this.renderEntity = render;
            this.shouldRender = true;
        } else {
            this.shouldRender = false;
        }
        return this;
    }

    public DirectiveResponseGenerator setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    public DirectiveResponseGenerator setCustomInteract(CUIEntity customInteract) {
        this.customInteract = customInteract;
        return this;
    }

    public DirectiveResponseEntity build() {
        return new DirectiveResponseEntity(
                useCase,
                action,
                subAction,
                inCustomInteractive,
                shouldRender,
                shouldSpeak,
                renderEntity,
                speakText,
                metadata,
                customInteract);
    }
    
}
