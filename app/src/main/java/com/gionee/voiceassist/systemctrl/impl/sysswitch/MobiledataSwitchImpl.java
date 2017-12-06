package com.gionee.voiceassist.systemctrl.impl.sysswitch;

import android.content.Context;
import android.net.ConnectivityManager;

import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.BaseCtrlImpl;
import com.gionee.voiceassist.util.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liyingheng on 12/6/17.
 */

public class MobiledataSwitchImpl extends BaseCtrlImpl implements ISwitchCtrl {

    private static final String TAG = MobiledataSwitchImpl.class.getSimpleName();

    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void toggle(boolean enabled, Callback callback) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mAppCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            //反射调用ConnectivityManager的内部方法setMobileDataEnabled()
            Class ownerClass = connectivityManager.getClass();
            //设置目标类的参数数量
            Class[] argsClass = new Class[1];
            //设置目标类的参数类型
            argsClass[0] = boolean.class;
            Method method = ownerClass.getMethod("setMobileDataEnabled",argsClass);
            method.invoke(connectivityManager, enabled);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LogUtil.e(TAG,"移动数据设置错误：" + e.toString());
            e.printStackTrace();
        }
    }
}
