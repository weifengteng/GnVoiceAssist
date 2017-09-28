package com.gionee.gnvoiceassist.basefunction.recordcontrol;

/**
 * Created by twf on 2017/8/31.
 */

interface IRecordControl {

    void startRecordOfflinePrior();

    void startRecordOfflineOnly();

    void startRecordOnline();

    void stopRecord();

    void cancelRecord();

    void onDestroy();
}
