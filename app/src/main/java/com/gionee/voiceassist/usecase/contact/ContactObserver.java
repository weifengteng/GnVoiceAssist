/**
 * 描述：联系人观察类，用于监听联系人数据库，并发出消息用于本地联系人更新数据
 */
package com.gionee.voiceassist.usecase.contact;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.ContactProcessor;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;

public class ContactObserver extends ContentObserver {
    public static final String TAG = ContactObserver.class.getSimpleName();
    private static final int MSG_UPDATE_CONTACTS = Constants.MSG_UPDATE_CONTACTS;
    private static final int HANDLER_UPDATE_CONTACTS_DELAYMILLIS = 3000;
    private Context mCxt;
    private Handler mHandler;

    /**************************** 构造方法 & Override **************************/
    /**
     * 描述：构造方法
     */
    public ContactObserver() {
        super(null);
//        mHandler = handler;
        mCxt = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    /**
     * 描述：通知更新联系人操作
     */
    @Override
    public void onChange(boolean selfChange) {
    	LogUtil.d(TAG, "ContactObserver onChange selfChange = " + selfChange);
        super.onChange(selfChange);
        if (Utils.isTaskActivite(mCxt, Constants.APP_PACKAGE_NAME)) {
            LogUtil.e(TAG, "ContactObserver onChange voice is activite!");
//            return;
        }

//        if (mHandler.hasMessages(MSG_UPDATE_CONTACTS)) {
//            mHandler.removeMessages(MSG_UPDATE_CONTACTS);
//        }
//        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_UPDATE_CONTACTS), HANDLER_UPDATE_CONTACTS_DELAYMILLIS);
        boolean needupdate = ContactProcessor.getContactProcessor().needUpdateContacts();
        if (needupdate) {
            Utils.uploadContacts();
        }
    }
}