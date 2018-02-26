package com.gionee.voiceassist.datamodel.card;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速开关卡片实体
 */

public class QuickSettingCardEntity extends CardEntity {

    //actionFeedback的URL格式： quicksetting://[settingAlias]/[enable]
    //action点击URL：          quicksetting://[settingAlias]/clicked

    private int settingIcon = -1;
    private String settingName = "";    //操作名称（如蓝牙、WiFi）
    private String settingAlias = "";   //操作别名（如bluetooth、wifi）
    private List<QuickSettingItem> optionNodes = new ArrayList<>();
    private List<QuickSettingObserver> observers = new ArrayList<>();

    public QuickSettingCardEntity(String settingName, String settingAlias) {
        setType(CardType.QUICKSETTING_CARD);
        this.settingName = settingName;
        this.settingAlias = settingAlias;
    }

    public int getSettingIcon() {
        return settingIcon;
    }

    public void setSettingIcon(int settingIcon) {
        this.settingIcon = settingIcon;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingAlias() {
        return settingAlias;
    }

    public void setSettingAlias(String settingAlias) {
        this.settingAlias = settingAlias;
    }

    public List<QuickSettingItem> getOptionNodes() {
        return optionNodes;
    }

    public void setOptionNodes(List<QuickSettingItem> optionNodes) {
        this.optionNodes = optionNodes;
    }

    public void addOptionNode(String optionName, String optionDescription, String optionAlias, QuickSettingState optionState) {
        optionNodes.add(new QuickSettingItem(optionName, optionDescription, optionAlias, optionState));
    }

    public void addObserver(QuickSettingObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(QuickSettingObserver observer) {
        if (observer != null && observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    public void updateNodeState(String optionAlias, QuickSettingState state) {
        // traverse nodes to update
        QuickSettingItem node = traverseNode(optionAlias);
        node.optionState = state;
        for (QuickSettingObserver observer:observers) {
            observer.onStateChanged(optionAlias, state);
        }
    }

    public void updateNodeDescription(String optionAlias, String desc) {
        // traverse nodes to update
        QuickSettingItem node = traverseNode(optionAlias);
        node.optionDescription = desc;
        for (QuickSettingObserver observer:observers) {
            observer.onDescriptionChanged(optionAlias, desc);
        }
    }

    private QuickSettingItem traverseNode(String optionAlias) {
        for (QuickSettingItem node:optionNodes) {
            if (node.optionAlias.equals(optionAlias)) {
                return node;
            } else {
                QuickSettingItem subNode = traverseNode(optionAlias);
                if (subNode != null) {
                    return subNode;
                }
            }
        }
        return null;
    }

    public interface QuickSettingObserver {
        void onStateChanged(String optionAlias, QuickSettingState state);
        void onDescriptionChanged(String optionAlias, String description);
    }

    public enum QuickSettingState {
        ENABLED,
        DISABLED,
        PROCESSING
    }

    public static class QuickSettingItem {
        public String optionName = "";  //选项名称
        public String optionDescription = "";   //选项描述
        public String optionAlias = ""; //选项别名
        public QuickSettingState optionState;   // 选项状态 （what if null?）
        private List<QuickSettingItem> subOptionNodes = new ArrayList<>();

        public QuickSettingItem(String optionName, String optionDescription, String optionAlias, QuickSettingState optionState) {
            this.optionName = optionName;
            this.optionDescription = optionDescription;
            this.optionAlias = optionAlias;
            this.optionState = optionState;
        }

        public List<QuickSettingItem> getSubOptionNodes() {
            return subOptionNodes;
        }

        public void setSubOptionNodes(List<QuickSettingItem> subOptionNodes) {
            this.subOptionNodes = subOptionNodes;
        }
    }

    public static class QuickSettingCardBuilder {

        private String settingName;
        private String settingAlias;
        private List<QuickSettingItem> optionNodes;

        public QuickSettingCardBuilder(String settingName, String settingAlias) {
            this.settingName = settingName;
            this.settingAlias = settingAlias;
            optionNodes = new ArrayList<>();
        }

        public QuickSettingCardBuilder addNode(
                String optionName,
                String optionDescription,
                String optionAlias,
                QuickSettingState state) {
            optionNodes.add(new QuickSettingItem(optionName, optionDescription, optionAlias, state));
            return this;
        }

        public QuickSettingCardEntity build() {
            QuickSettingCardEntity instance = new QuickSettingCardEntity(settingName, settingAlias);
            instance.setOptionNodes(optionNodes);
            return instance;
        }
    }
}
