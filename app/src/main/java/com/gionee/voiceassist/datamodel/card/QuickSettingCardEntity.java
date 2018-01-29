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

    public interface QuickSettingObserver {
        void onStateChanged(String optionAlias, QuickSettingState state);
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
}
