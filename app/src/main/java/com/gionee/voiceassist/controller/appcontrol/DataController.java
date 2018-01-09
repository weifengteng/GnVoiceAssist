package com.gionee.voiceassist.controller.appcontrol;

import com.gionee.voiceassist.util.Preconditions;
import com.gionee.voiceassist.util.RecognizerState;

import java.util.ArrayList;
import java.util.List;

/**
 * Model具体业务的门面。所有对应用操作的调用都从这里调度。
 */

public class DataController {
    private static DataController sInstance;
    private ServiceController mServiceController;
    private ScreenController mScreenController;
    private boolean mAppInForeground = false;

    private List<IRecognizerStateListener> mRecognizerListeners;
    private List<IRenderListener> mRenderListeners;

    //全局状态
    private RecognizerState mRecognizerState = RecognizerState.IDLE;

    private DataController() {
        mServiceController = new ServiceController();
        mScreenController = new ScreenController();
        mRecognizerListeners = new ArrayList<>();
        mRenderListeners = new ArrayList<>();
        mServiceController.setCallback(mRecognizerListeners);
    }

    public static DataController getDataController() {
        if (sInstance == null) {
            synchronized (DataController.class) {
                if (sInstance == null) {
                    sInstance = new DataController();
                }
            }
        }
        return sInstance;
    }

    //Life-cycle monitor
    public void onCreate() {

    }

    public void onDestroy() {

    }

    public void onResume() {
        setAppInForeground(true);
        mServiceController.attachService();
    }

    public void onPause() {
        setAppInForeground(false);
        mServiceController.detachService();
    }

    private void setAppInForeground(boolean inForeground) {
        mAppInForeground = inForeground;
    }

    public boolean isAppInForeground() {
        return mAppInForeground;
    }

    /**
     * 触发录音选项（一般用于点击录音按钮后逻辑）
     */
    public void triggerRecord() {
        //TODO 需要判断是否正在进行录音。若正在录音，则停止录音。
        mServiceController.startRecord();
    }

    public ServiceController getServiceController() {
        return mServiceController;
    }

    public ScreenController getScreenController() {
        return mScreenController;
    }

    /**
     * 添加识别状态监听器
     */
    public void addRecognizerStateListener(IRecognizerStateListener listener) {
        if (!mRecognizerListeners.contains(listener)) {
            mRecognizerListeners.add(Preconditions.checkNotNull(listener));
        }
    }

    /**
     * 移除识别状态监听器
     */
    public void removeRecognizerStateListener(IRecognizerStateListener listener) {
        if (mRecognizerListeners.contains(listener)) {
            mRecognizerListeners.remove(listener);
        }
    }

    public void addRenderListener(IRenderListener listener) {
        if (!mRenderListeners.contains(listener)) {
            mRenderListeners.add(Preconditions.checkNotNull(listener));
        }
    }

    public void removeRenderListener(IRenderListener listener) {
        if (mRenderListeners.contains(listener)) {
            mRenderListeners.remove(listener);
        }
    }

    void updateRecognizerState(RecognizerState state) {
        mRecognizerState = state;
        for (IRecognizerStateListener listener:mRecognizerListeners) {
            listener.onStateChanged(mRecognizerState);
        }
    }

    public RecognizerState getRecognizerState() {
        return mRecognizerState;
    }

}
