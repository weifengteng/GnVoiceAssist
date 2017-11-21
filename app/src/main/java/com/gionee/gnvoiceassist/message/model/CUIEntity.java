package com.gionee.gnvoiceassist.message.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyingheng on 11/8/17.
 */

public class CUIEntity implements Serializable{

    public CUIEntity(String usecase, String interactionId, List<Command> commandSet) {
        this.usecase = usecase;
        this.interactionId = interactionId;
        this.commandSet = commandSet;
    }

    //发起的usecase
    private String usecase;

    //自定义交互ID
    private String interactionId;

    //指令集
    private List<Command> commandSet;

    public String getUsecase() {
        return usecase;
    }

    public void setUsecase(String usecase) {
        this.usecase = usecase;
    }

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    public List<Command> getCommandSet() {
        return commandSet;
    }

    public void setCommandSet(List<Command> commandSet) {
        this.commandSet = commandSet;
    }

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
