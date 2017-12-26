package com.gionee.voiceassist.util.kookong;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;

import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.tts.TtsManager;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;
import com.kookong.app.aidl.ManagerCallback;

/**
 * Created by twf on 2017/8/16.
 */

public class TeleControlPresenter extends KookongBaseService.SimpleKookongService {
    private static IBaseFunction mBaseFunction;
    String voiceCmd;

    public TeleControlPresenter(IBaseFunction baseFunction, String voiceCmd){
        this.mBaseFunction = baseFunction;
        this.voiceCmd = voiceCmd;
    }

    @Override
    void updateCustomData() {
        String customACStateList = null;
        try {
            customACStateList = mIKookongManager.getCustomACStateList();
            LogUtil.d("DCSF-Kookong", "onResponse customACStateList= " + customACStateList);
            if(null != customACStateList) {
                mCustomACStateLists = Utils.parseJsonArray(customACStateList, "name");
                if(mCustomACStateLists!=null){
                    for(int i =0;i<mCustomACStateLists.length;i++){
                        LogUtil.d("DCSF-Kookong", "mCustomACStateLists mode = "+mCustomACStateLists[i]);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void execVoiceCommand() {
        if(mCustomACStateLists != null && mCustomACStateLists.length != 0) {
            for(String customState : mCustomACStateLists) {
                if(TextUtils.equals(voiceCmd, customState)) {
                    voiceCmd = "空调" + voiceCmd;
                    LogUtil.d("DCSF-Kookong", "KookongExecuteVoiceCmdService execVoiceCommand voiceCmd = " + voiceCmd);
                }
            }
        }

        try {
            mIKookongManager.execVoiceCommand(voiceCmd, Constants.OUTSIDE_KOOKONG_APP, new ManagerCallback.Stub(){
                @Override
                public void onResponse(String result) throws RemoteException {
                    LogUtil.d("DCSF-Kookong", "onResponse result = " + result);
                    Message msg = Message.obtain();
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String)msg.obj;
            String cmdResult = "error!";
            if(!TextUtils.isEmpty(result)) {
                if(result.contains("ok")) {
                    cmdResult = "ok";
                } else {
                    cmdResult = Utils.parseJson(result, "msg");
                }
            }
            mBaseFunction.getScreenRender().renderAnswerInScreen(cmdResult);
            LogUtil.d("DCSF-Kookong", "kookong result: " + cmdResult);
//            DcsSDK.getInstance().getSpeak().speakTxt(cmdResult,
//                    SpeakInterface.SpeakTxtMixMode.MIX_MODE_MIXTURE_NETWORK);
            //TODO: 将处理结果用tts播报
            TtsManager.getInstance().playTTS(cmdResult);
        }
    };
}
