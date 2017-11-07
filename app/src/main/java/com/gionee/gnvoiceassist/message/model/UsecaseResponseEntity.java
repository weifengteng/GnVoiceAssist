package com.gionee.gnvoiceassist.message.model;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

import org.json.JSONObject;

/**
 * 具体用例返回的信息结构
 */

public class UsecaseResponseEntity {

    //用例场景
    private String usecase;

    //命令
    private String action;

    //是否为自定义交互指令
    private boolean inCustomInteractive;

    //是否需要显示到界面
    private boolean shouldRender;

    //是否需要朗读
    private boolean shouldSpeak;

    //需要显示在界面的内容
    private RenderEntity renderContent;

    //需要朗读的文字
    private String speakText;

    //是否在结束时才执行操作
    private boolean shouldOperateAfterSpeak;

    //用例具体元数据
    private JSONObject metadata;

}
