package com.gionee.gnvoiceassist.basefunction;

import com.baidu.duer.dcs.util.LogUtil;

/**
 * Created by twf on 2017/9/1.
 */

public class MaxUpriseCounter {
    public static final String TAG = MaxUpriseCounter.class.getSimpleName();

    public static final int MAX_UPRISE_COUNT = 2;
    private static int upriseCount = 0;

    public static boolean isMaxCount() {
        LogUtil.d(TAG, "DCSF ******************** isMaxCount upriseCount= " + upriseCount);
        return upriseCount >= MAX_UPRISE_COUNT;
    }

    public static void resetUpriseCount() {
        upriseCount = 0;
        LogUtil.d(TAG, "DCSF ******************** resetUpriseCount upriseCount= " + upriseCount);
    }

    public static void increaseUpriseCount() {
        upriseCount ++;
        LogUtil.d(TAG, "DCSF ******************** increaseUpriseCount upriseCount= " + upriseCount);
    }
}
