package com.gionee.gnvoiceassist.home;

/**
 * Created by liyingheng on 11/2/17.
 */

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mHostView;

    public HomePresenter(HomeContract.View view) {
        mHostView = view;
    }

    @Override
    public void start() {
        // Activity调用onResume()时，执行此方法。
        // 此方法主要在屏幕焦点返回应用时触发。
        // 需要注册与Service的监听器，重新查询此时引擎的状态（引擎是否工作）
        queryEngineState();
    }

    @Override
    public void destroy() {
        // Activity调用onDestroy()时，执行此方法。
        // 主要释放所有与应用界面相关的资源。
        // 包括解除与底层有关的监听器，确保内存被顺利回收。
    }

    @Override
    public void queryEngineState() {

    }

    @Override
    public void fireVoiceRequest() {

    }
}
