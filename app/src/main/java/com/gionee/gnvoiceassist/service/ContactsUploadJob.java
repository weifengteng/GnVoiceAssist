package com.gionee.gnvoiceassist.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

/**
 * 联系人上传任务。用于接收JobScheduler后台任务调度后，启动{@link ContactsUploadService}进行联系人上传。
 */
@TargetApi(21)
public class ContactsUploadJob extends JobService {

    // 联系人变化监听URI
    private static final Uri CONTACTS_TRIGGER_URI = ContactsContract.Contacts.CONTENT_URI;


    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
