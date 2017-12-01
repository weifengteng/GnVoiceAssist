package com.gionee.voiceassist.basefunction.screenrender;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.LogUtil;


/**
 * Created by twf on 2017/8/25.
 */

public class ScreenRender {
    private Handler mHandler;
    private String asrResult;

    public ScreenRender(Handler mainHandler) {
        mHandler = mainHandler;
    }

    public void renderQueryInScreen(String asrResult) {
        this.asrResult = asrResult;
        Message msg = Message.obtain();
        msg.what = Constants.MSG_SHOW_QUERY;
        msg.obj = asrResult;
        mHandler.sendMessage(msg);
    }

    public void renderAnswerInScreen(String content) {
        LogUtil.d("DCSF", "onRenderCard: content= " + content);
        if(!TextUtils.isEmpty(content) && !"null".equals(content) && !"NULL".equals(content)) {
            Message msg = Message.obtain();
            msg.what = Constants.MSG_SHOW_ANSWER;
            msg.obj = content;
            mHandler.sendMessage(msg);
        }
    }

    public void renderInfoPanel(View infoPanel) {
        if(infoPanel != null) {
            Message msg = Message.obtain();
            msg.what = Constants.MSG_SHOW_INFO_PANEL;
            msg.obj = infoPanel;
            mHandler.sendMessage(msg);
        }
    }

    public void renderVoiceInputVolume(int volume) {
        Message msg = Message.obtain();
        msg.what = Constants.MSG_UPDATE_INPUTVOLUME;
        msg.arg1 = volume;
        mHandler.sendMessage(msg);
    }

    public String getAsrResult() {
        return asrResult;
    }

    public void onDestroy() {
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
