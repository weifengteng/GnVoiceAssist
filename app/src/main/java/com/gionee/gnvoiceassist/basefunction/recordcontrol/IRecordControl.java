package com.gionee.gnvoiceassist.basefunction.recordcontrol;

/**
 * Created by twf on 2017/8/31.
 *
 * 语音识别控制类
 *
 * 用来操作语音识别的开始、停止、取消
 */

interface IRecordControl {

    void startRecordOfflinePrior();

    void startRecordOfflineOnly();

    void startRecordOnline();

    void stopRecord();

    void cancelRecord();

    void onDestroy();
}
