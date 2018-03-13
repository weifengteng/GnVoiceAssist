package com.gionee.voiceassist.usecase.remind;

import android.content.Intent;
import android.provider.CalendarContract;

import java.util.Date;

import static com.gionee.voiceassist.util.ContextUtil.getAppContext;

/**
 * Created by liyingheng on 3/13/18.
 */

public class ScheduleDao {

    void performCreateSchedule(Date startTime, Date endTime, String title) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTime())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTime());
        if (intent.resolveActivity(getAppContext().getPackageManager()) != null) {
            getAppContext().startActivity(intent);
        }
    }

}
