package com.gionee.gnvoiceassist.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.service.GnVoiceService;
import com.gionee.gnvoiceassist.service.IVoiceServiceListener;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.LogUtil;
import static com.gionee.gnvoiceassist.util.Preconditions.checkNotNull;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by liyingheng on 11/2/17.
 */

public class HomePresenter implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();

    private final HomeContract.View mHostView;
    private Context mContext;
    private boolean mIsBind = false;
    private GnVoiceService.GnVoiceServiceBinder mBinder;
    private IVoiceServiceListener mCallback = new IVoiceServiceListener() {
        @Override
        public void onEngineState(Constants.EngineState state) {
            mHostView.onEngineState(state);
        }

        @Override
        public void onRecognizeState(Constants.RecognitionState state) {
            mHostView.onRecognizeStateChanged(state);
        }

        @Override
        public void onRenderRequest(RenderEntity renderData) {
            mHostView.onResult(renderData);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d(TAG, "GnVoiceService Connected!");
            mBinder = checkNotNull((GnVoiceService.GnVoiceServiceBinder) service);
            mBinder.addCallback(mCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(TAG, "GnVoiceService Disconnected!");
            mBinder.removeCallback(mCallback);
        }
    };




    public HomePresenter(Context context, HomeContract.View view) {
        mHostView = view;
        mContext = context;
    }

    @Override
    public void start() {
        // Activity调用onResume()时，执行此方法。
        // 此方法主要在屏幕焦点返回应用时触发。
        // 需要注册与Service的监听器，重新查询此时引擎的状态（引擎是否工作）
        startService();
    }

    @Override
    public void destroy() {
        // Activity调用onDestroy()时，执行此方法。
        // 主要释放所有与应用界面相关的资源。
        // 包括解除与底层有关的监听器，确保内存被顺利回收。
        destroyService();
    }

    @Override
    public void queryEngineState() {

    }

    @Override
    public void fireVoiceRequest() {
        if (mIsBind && mBinder != null) {
            mBinder.startRecord();
        }
    }

    private void startService() {
        Intent intent = new Intent(mContext, GnVoiceService.class);
        mIsBind = mContext.bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    private void destroyService() {
        mBinder.removeCallback(mCallback);
        mContext.unbindService(mConnection);    //解除与服务的连接

    }

}
