package com.gionee.voiceassist;

import com.gionee.voiceassist.datamodel.card.CardEntity;

/**
 * MainActivity与MainPresenter之间的契约类。通过接口定义了View和Presenter之间
 * 的通信内容。
 */

public interface MainContract {

    interface View {
        /**
         * 卡片信息回调
         * @param card 卡片实体
         */
        void showCard(CardEntity card);

        /**
         * 录音功能可用状态改变
         * @param enabled 录音功能是否可用
         */
        void onRecordingEnabled(boolean enabled);

        /**
         * 录音状态改变回调
         * @param recording
         */
        void onVoiceRecording(boolean recording);

        /**
         * 打开帮助页面回调
         */
        void showHelpPage();

        /**
         * 关闭帮助页面回调
         */
        void hideHelpPage();
    }

    interface Presenter {
        /**
         * 连接Presenter
         */
        void attach();

        /**
         * 断开Presenter
         */
        void detach();

        /**
         * 触发录音开始/结束
         */
        void launchRecord();

        /**
         * 打开帮助页面
         */
        void openHelp();

        /**
         * 关闭帮助页面
         */
        void closeHelp();
    }

}
