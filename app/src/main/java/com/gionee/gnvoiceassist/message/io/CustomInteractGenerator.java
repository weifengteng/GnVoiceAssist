package com.gionee.gnvoiceassist.message.io;

import com.gionee.gnvoiceassist.message.model.CUIEntity;
import com.gionee.gnvoiceassist.util.JsonUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于生成自定义交互Entity的帮助类
 */

public class CustomInteractGenerator {

    private String interactionId;
    private List<CUIEntity.Command> commandSet;

    public CustomInteractGenerator(String interactionId) {
        this.interactionId = interactionId;
        commandSet = new ArrayList<>();
    }

    public CustomInteractGenerator addCommand(String url, String... utterances) {
        CUIEntity.Command command = new CUIEntity.Command();
        command.setUrl(url);
        command.setUtterance(new ArrayList<>(Arrays.asList(utterances)));
        commandSet.add(command);
        return this;
    }

    public CUIEntity generateEntity() {
        return new CUIEntity(interactionId,commandSet);
    }

    public String generateJsonStr() {
        return JsonUtil.toJson(new CUIEntity(interactionId,commandSet));
    }

}
