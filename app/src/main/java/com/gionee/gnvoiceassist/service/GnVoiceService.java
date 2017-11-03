package com.gionee.gnvoiceassist.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.gionee.gnvoiceassist.basefunction.contact.ContactObserver;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.kookong.KookongCustomDataHelper;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnvoiceassist.util.Preconditions.checkNotNull;

public class GnVoiceService extends Service {

    private static final int MSG_ENGINE_STATUS_RESULT = 0x1100;
    private static final int MSG_CONTACT_UPDATE = Constants.MSG_UPDATE_CONTACTS;

    private RecognizeManager mRecognizeManager;
    private ContactObserver mContactObserver;
    private ContentResolver mContentResolver;
    private TelephonyManager mTelephonyManager;
    private GnVoiceServiceBinder mBinder;
    private List<IVoiceServiceListener> mExportCallbacks;

    private InnerHandler mInnerHandler;
    private InnerPhoneStateListener mPhoneStateListener;


    public GnVoiceService() {
        mBinder = new GnVoiceServiceBinder();
        mExportCallbacks = new ArrayList<>();
        mInnerHandler = new InnerHandler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return checkNotNull(mBinder, "GnVoiceService中的Binder不能为空");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initEssentialComponent();
        initAdditionalComponent();
        initRecognizeManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyEssentialComponent();
        destroyAdditionalComponent();
    }

    /**
     * 开始录音
     */
    public void fireRecord() {

    }

    /**
     * 打断录音
     */
    public void abortRecord() {

    }

    /**
     * 使用TTS朗读文字
     * @param text
     */
    public void playTts(String text) {

    }

    /**
     * 停止所有TTS朗读
     */
    public void stopTts() {

    }

    /**
     * 处理RecognizeManager返回的语音识别结果
     * 选择调用HandlerDispatcher分发到具体的Usecase，或显示到View
     */
    public void dispatchRecognizeResult() {

    }

    /**
     * 处理Usecase返回的消息
     */
    public void dispatchUsecaseResult() {

    }

    /**
     * 处理从View层传递的请求
     */
    public void dispatchViewRequest() {

    }

    private void initRecognizeManager() {
        if (mRecognizeManager == null) {
            mRecognizeManager = RecognizeManager.getInstance();
        }
        mRecognizeManager.init();
    }

    private void initEssentialComponent() {
        //TODO 初始化UsecaseDispatcher

        // 初始化ContentResolver
        mContentResolver = getContentResolver();    //是否要判断ContentResolver为空的情况？是否要使用ApplicationContext？
        // 初始化ContactObserver
        mContactObserver = new ContactObserver(mInnerHandler,GnVoiceService.this.getApplicationContext());
        mContentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI,true,mContactObserver);
        // TODO 初始化Telephony监听器
        mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        // TODO 初始化音频播放焦点

    }

    private void initAdditionalComponent() {
        // 初始化酷控服务
        KookongCustomDataHelper.bindDataRetriveService();
    }

    private void registerBroadcast() {
        //初始化广播接收器
        //需要接收以下广播：开机启动完成

        //初始化JobScheduler
        //JobScheduler监测以下场景：联系人变化（当联系人改变时，调用ContactsUploadService进行联系人上传操作）


    }

    private void destroyRecognizeManager() {
        if (mRecognizeManager != null) {
            mRecognizeManager.release();
        }
    }

    private void destroyEssentialComponent() {
        //TODO 注销UsecaseDispatcher

        // 注销ContentResolver（不需要）

        //TODO 注销ContactObserver
        mContentResolver.unregisterContentObserver(mContactObserver);
        //TODO 注销Telephony监听器
        mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        mPhoneStateListener = null;
        mTelephonyManager = null;
        //TODO 注销音频焦点监听

    }

    private void destroyAdditionalComponent() {
        //注销酷控服务（不需要）
    }

    private void unregisterBroadcast() {
        //注销广播接收器

    }

    /**
     * VoiceService的通信接口
     * 用于与建立连接的客户端进行通信
     */
    public class GnVoiceServiceBinder extends Binder {

        public void addCallback(IVoiceServiceListener listener) {
            if (!mExportCallbacks.contains(checkNotNull(listener))) {
                mExportCallbacks.add(checkNotNull(listener));
            }
        }

        public void removeCallback(IVoiceServiceListener listener) {
            if (!mExportCallbacks.contains(checkNotNull(listener))) {
                mExportCallbacks.remove(listener);
            }
        }

        public void startRecord() {

        }

        public void abortRecord() {

        }

        public void stopRecord() {

        }

        public void sendRequestMessage() {

        }
    }

    private static class InnerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    }

    private class InnerPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //TODO 当电话来电时，打断语音识别
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //打断语音识别
                    abortRecord();
                    // 是否需要退出界面？
                    break;
                default:
                    break;

            }
        }

    }

}
