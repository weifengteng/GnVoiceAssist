package com.gionee.gnvoiceassist.service;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

/**
 * 联系人上传任务。用于接收JobScheduler后台任务调度后，启动{@link ContactsUploadService}进行联系人上传。
 */
@TargetApi(21)
public class ContactsUploadJob extends JobService {

    public static final int CONTACTS_UPLOAD_JOB_ID = 0x2000;
    public static final int CONTACTS_UPLOAD_JOB_DELAYMS = 2000;

    private static final JobInfo JOB_INFO;

    // 联系人变化监听URI
    private static final Uri CONTACTS_TRIGGER_URI = ContactsContract.Contacts.CONTENT_URI;

    static {
        JobInfo.Builder builder = new JobInfo.Builder(CONTACTS_UPLOAD_JOB_ID,
                new ComponentName("com.gionee.gnvoiceassist.service",ContactsUploadService.class.getName()));
        if (Build.VERSION.SDK_INT >= 24) {
            builder.addTriggerContentUri
                    (new JobInfo.TriggerContentUri(CONTACTS_TRIGGER_URI, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS));
        }
        JOB_INFO = builder.build();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent intent = new Intent(ContactsUploadJob.this,ContactsUploadService.class);
        startService(intent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
