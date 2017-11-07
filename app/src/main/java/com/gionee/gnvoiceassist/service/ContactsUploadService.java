package com.gionee.gnvoiceassist.service;

import android.app.IntentService;
import android.content.Intent;

import com.gionee.gnvoiceassist.util.ContactProcessor;
import com.gionee.gnvoiceassist.util.Utils;


/**
 * 上传联系人服务。
 * 当第一次打开应用，或每次联系人条目变化的时候，会触发此服务。
 *
 * 每次打开应用时，会调用此方法上传一次联系人。同时，注册ContentObserver针对联系人URI进行监听。当联系人发生变化时
 * 会再次触发此服务上传联系人。
 *
 */
public class ContactsUploadService extends IntentService {

    public ContactsUploadService() {
        super("ContactsUploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            uploadContacts();
        }
    }

    private void uploadContacts() {
        Utils.uploadContacts();
        boolean needupdate = ContactProcessor.getContactProcessor().needUpdateContacts();
        long endTs = System.currentTimeMillis();
    }

}
