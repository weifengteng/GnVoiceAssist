package com.gionee.voiceassist.controller.appcontrol;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * 控制/取得当前对话信息
 */

public class ConversationController {

    private Stack<String> userAsr;
    private Stack<String> serverResultText;

    public ConversationController() {
        userAsr = new Stack<>();
        serverResultText = new Stack<>();
    }

    public void addUserAsr(String asr) {
        userAsr.add(asr);
    }

    public void addServerResult(String result) {
        serverResultText.add(result);
    }

    public String getLastUserAsr() {
        return userAsr.peek();
    }

    public String getLastServerResult() {
        return serverResultText.peek();
    }

}
