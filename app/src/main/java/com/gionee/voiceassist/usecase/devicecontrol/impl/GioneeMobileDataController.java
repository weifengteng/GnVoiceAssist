package com.gionee.voiceassist.usecase.devicecontrol.impl;

import android.content.Context;
import android.net.ConnectivityManager;

import com.gionee.voiceassist.usecase.devicecontrol.sysinterface.IMobileData;
import com.gionee.voiceassist.util.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liyingheng on 10/24/17.
 */

public class GioneeMobileDataController extends BaseController implements IMobileData {

    private static final String TAG = GioneeMobileDataController.class.getSimpleName();

    public GioneeMobileDataController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setMobileDataEnabled(boolean mode) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mAppCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            //反射调用ConnectivityManager的内部方法setMobileDataEnabled()
            Class ownerClass = connectivityManager.getClass();
            //设置目标类的参数数量
            Class[] argsClass = new Class[1];
            //设置目标类的参数类型
            argsClass[0] = boolean.class;
            Method method = ownerClass.getMethod("setMobileDataEnabled",argsClass);
            method.invoke(connectivityManager, mode);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LogUtil.e(TAG,"移动数据设置错误：" + e.toString());
            e.printStackTrace();
        }
    }
}
