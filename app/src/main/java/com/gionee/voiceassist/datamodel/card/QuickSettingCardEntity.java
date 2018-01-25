package com.gionee.voiceassist.datamodel.card;

/**
 * 快速开关卡片实体
 */

public class QuickSettingCardEntity extends CardEntity {

    //actionFeedback的URL格式： quicksetting://[actionAlias]/[enable]
    //action点击URL：          quicksetting://[actionAlias]/clicked

    //App打开信息

    //操作对应的url

    //选项开关信息

    //选项当前状态

    //操作名称
    private String actionName = "";

    //操作别名
    private String actionAlias = "";

    //状态
    private boolean enabled = false;

    //当前操作状态
    private QuickSettingState actionState = QuickSettingState.DISABLED;

    public QuickSettingCardEntity(String actionName, String actionAlias) {
        this.actionName = actionName;
        this.actionAlias = actionAlias;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionAlias() {
        return actionAlias;
    }

    public void setActionAlias(String actionAlias) {
        this.actionAlias = actionAlias;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void updateActionState(QuickSettingState state) {

    }

    public interface QuickSettingObserver {
        void onStateChanged();
    }

    public enum QuickSettingState {
        ENABLED,
        DISABLED,
        PROCESSING
    }
}
