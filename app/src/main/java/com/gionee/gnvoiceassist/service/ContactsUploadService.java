package com.gionee.gnvoiceassist.service;

import android.app.IntentService;
import android.content.Intent;

import com.gionee.gnvoiceassist.util.ContactProcessor;
import com.gionee.gnvoiceassist.util.Utils;


/**
 * 上传联系人服务。
 * 当第一次打开应用，或每次联系人条目变化的时候，会触发此服务。
 * 第一次打开应用的时候，云端没有本机联系人条目。因此通过判断SharedPreference中的键值，判断是否要上传联系人。
 * 当第一次上传成功后，SharedPreference中的键值会置true。
 *
 * 在{@link GnVoiceService}中，会针对Android系统版本设定触发此服务的方法。
 * 若Android 7.0及以上的版本，则使用Framework中的JobScheduler API{@link android.app.job.JobScheduler}
 * 监听联系人URI变化，触发此服务。
 * 若Android 7.0以下的版本，则通过注册联系人广播监听，触发此服务。
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
