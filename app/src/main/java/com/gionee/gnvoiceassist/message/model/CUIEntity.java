package com.gionee.gnvoiceassist.message.model;

import java.util.List;

/**
 * Created by liyingheng on 11/8/17.
 */

public class CUIEntity {

    public CUIEntity(String interactionId, List<Command> commandSet) {
        this.interactionId = interactionId;
        this.commandSet = commandSet;
    }

    //自定义交互ID
    private String interactionId;

    //指令集
    private List<Command> commandSet;

    public static class Command {

        private String url;

        private List<String> utterance;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getUtterance() {
            return utterance;
        }

        public void setUtterance(List<String> utterance) {
            this.utterance = utterance;
        }
    }

}
