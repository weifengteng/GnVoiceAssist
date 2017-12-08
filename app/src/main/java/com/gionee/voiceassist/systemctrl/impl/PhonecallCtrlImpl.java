package com.gionee.voiceassist.systemctrl.impl;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import com.gionee.voiceassist.systemctrl.iface.IPhonecallCtrl;
import com.gionee.voiceassist.util.LogUtil;

import java.util.List;

/**
 * Created by liyingheng on 12/6/17.
 */

public class PhonecallCtrlImpl extends BaseCtrlImpl implements IPhonecallCtrl {
    @Override
    public void makeCall(String number, String simId) {

    }

    @Override
    public boolean needChooseSim() {
        return false;
    }

    @Override
    public boolean dualSimAvailable() {
        return false;
    }

    @Override
    public boolean dualSimInserted() {
        return false;
    }

    @Override
    public int defaultSimId() {
        return getSimCount(mAppCtx);
    }

    public int getSimCount(Context ctx) {
        List<SubscriptionInfo> list = SubscriptionManager.from(ctx).getActiveSubscriptionInfoList();
        if(null == list) {
            LogUtil.e("liyh","SimCardUtil getSimCount sim count = 0 and null == list");
            return -1;
        }
        LogUtil.d("liyh","SimCardUtil getSimCount sim count = " + list.size());
        return list.size();
    }
}
