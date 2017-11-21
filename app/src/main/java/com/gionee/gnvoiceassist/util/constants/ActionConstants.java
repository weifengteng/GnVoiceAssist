package com.gionee.gnvoiceassist.util.constants;

/**
 * 消息中Action常量集
 * 如果以REQUEST开头，则代表用户请求某一操作。信息流向一般为Service -> UseCase
 * 如果以CUI开头，则代表用户应答多轮交互的操作。信息流一般为Service -> UseCase
 * 如果一QUERY开头，则代表UseCase发起多轮交互的操作。信息流一般为UseCase -> Service
 * 如果以RESULT开头，则代表UseCase的操作结果。信息流为UseCase -> Service
 */

public class ActionConstants {

    /**
     * 应用操作常量
     */
    public class AppLaunchAction {
        /**
         * 请求打开应用
         */
        public static final String ACTION_REQUEST_LAUNCH_APP = "request_launch_app";

        /**
         * 发起询问下载应用多轮交互
         */
        public static final String ACTION_QUERY_DOWNLOAD_APP = "query_download_app";

        /**
         * 用户应答是否下载应用多轮交互
         */
        public static final String ACTION_CUI_DOWNLOAD_APP = "cui_download_app";

        /**
         * 确定下载
         */
        public static final String SUBACTION_CUI_CONFIRM = "confirm";

        /**
         * 取消下载
         */
        public static final String SUBACTION_CUI_CANCEL = "cancel";
    }

    public class PhonecallAction {
        //传入的action
        /**
         * 请求拨打电话
         */
        public static final String ACTION_REQUEST_CALL = "request_call";

        /**
         * 拨打电话结果返回
         */
        public static final String ACTION_RESULT_CALL = "result_call";

        /**
         * 发起询问选择卡槽的多轮交互
         */
        public static final String ACTION_QUERY_SIMSLOT = "query_simslot";

        /**
         * 发起询问选择多号码的多轮交互
         */
        public static final String ACTION_QUERY_MULTI_NUMBER = "query_multi_number";

        /**
         * 用户应答多轮交互选卡多轮交互
         */
        public static final String ACTION_CUI_SIMSLOT = "cui_simslot";

        /**
         * 用户应答多轮交互选择号码的多轮交互
         */
        public static final String ACTION_CUI_MULTI_NUMBER = "cui_multi_number";

    }

    public class SmsAction {

        /**
         * 请求发送信息
         */
        public static final String ACTION_REQUEST_SENDSMS = "request_send_sms";

        /**
         * 发起选择多号码的多轮交互
         */
        public static final String ACTION_QUERY_MULTI_NUMBER = "query_multi_number";

        /**
         * 发起选择卡槽的多轮交互
         */
        public static final String ACTION_QUERY_SIMSLOT = "query_simslot";

        /**
         * 发起确认发送短信的多轮交互
         */
        public static final String ACTION_QUERY_SENDSMS_CONFIRM = "query_sendsms_confirm";

        /**
         * 用户应答选择多号码
         */
        public static final String ACTION_CUI_MULTI_NUMBER = "cui_multi_number";

        /**
         * 用户应答选择卡槽
         */
        public static final String ACTION_CUI_SIMSLOT = "cui_simslot";

        /**
         * 用户应答确认发送短信
         */
        public static final String ACTION_CUI_SENDSMS_CONFIRM = "cui_sendsms_confirm";

    }

}
