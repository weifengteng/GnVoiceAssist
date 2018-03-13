package com.gionee.voiceassist.usecase.remind;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.AlarmClock;


import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.gionee.voiceassist.util.ContextUtil.getAppContext;

/**
 * Created by liyingheng on 3/13/18.
 */

public class AlarmDao {

    private static final String TAG = AlarmDao.class.getSimpleName();
    private Uri alarmGeneralUri = new Uri.Builder()
            .scheme("content")
            .authority("com.android.deskclock")
            .appendPath("alarm")
            .build();
    private String[] PROJECTION_COL = new String[]{
            "_id",
            "hour",
            "minutes",
            "repeatrecycle",
            "enabled",
            "daysofweek",
            "message"};

    void performCreateAlarm(int hour, int minute, List<Integer> triggerDays, String message) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR,hour)
                .putExtra(AlarmClock.EXTRA_MINUTES,minute)
                .putExtra(AlarmClock.EXTRA_DAYS,(ArrayList<Integer>)triggerDays)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                .putExtra(AlarmClock.EXTRA_MESSAGE,message);

        if (intent.resolveActivity(getAppContext().getPackageManager()) != null) {
            getAppContext().startActivity(intent);
        }
    }

    List<Alarm> queryAlarmWithTime(int hour, int minute) {
        final ContentResolver cr = getAppContext().getContentResolver();
        String SELECTION = "hour = ? AND minutes = ? ";
        Cursor c = cr.query(alarmGeneralUri, PROJECTION_COL, SELECTION, new String[]{String.valueOf(hour), String.valueOf(minute)}, null);
        List<Alarm> matchAlarms = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                matchAlarms.add(parseAlarmCursor(c));
            }
            c.close();
        }
        return matchAlarms;
    }

    List<Alarm> queryAlarmWithTimespan(int[] startTime, int[] endTime) {
        final ContentResolver cr = getAppContext().getContentResolver();
        String SELECTION = "hour >= ? AND hour <= ? ";
        Cursor c = cr.query(
                alarmGeneralUri,
                PROJECTION_COL,
                SELECTION,
                new String[]{String.valueOf(startTime[0]), String.valueOf(endTime[0])},
                null);
        List<Alarm> matchAlarms = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                matchAlarms.add(parseAlarmCursor(c));
            }
            c.close();
        }
        Iterator<Alarm> alarmIterator = matchAlarms.iterator();
        while (alarmIterator.hasNext()) {
            Alarm alarm = alarmIterator.next();
            if (!isAlarmInTimespan(alarm, startTime, endTime)) {
                alarmIterator.remove();
            }
        }
        return matchAlarms;
    }

    boolean isAlarmInTimespan(Alarm alarms, int[] startTs, int[] endTs) {
        return true;
    }

    List<Alarm> queryAllAlarm() {
        final ContentResolver cr = getAppContext().getContentResolver();
        Cursor c = cr.query(alarmGeneralUri, PROJECTION_COL, null, null, null);
        List<Alarm> matchAlarms = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                matchAlarms.add(parseAlarmCursor(c));
            }
            c.close();
        }
        return matchAlarms;
    }

    List<Alarm> queryAlarmWithMessage(String msg) {
        final ContentResolver cr = getAppContext().getContentResolver();
        String SELECTION = "message = ? ";
        Cursor c = cr.query(alarmGeneralUri, PROJECTION_COL, SELECTION, new String[]{msg}, null);
        List<Alarm> matchAlarms = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                matchAlarms.add(parseAlarmCursor(c));
            }
            c.close();
        }
        return matchAlarms;
    }

    Alarm parseAlarmCursor(Cursor c) {
        String id = c.getString(c.getColumnIndex(PROJECTION_COL[0]));
        int h = c.getInt(c.getColumnIndex(PROJECTION_COL[1]));
        int minutes = c.getInt(c.getColumnIndex(PROJECTION_COL[2]));
        String repeatCycle = c.getString(c.getColumnIndex(PROJECTION_COL[3]));
        boolean enable = c.getInt(c.getColumnIndex(PROJECTION_COL[4])) == 1;
        String daysOfWeek = c.getString(c.getColumnIndex(PROJECTION_COL[5]));
        String message = c.getString(c.getColumnIndex(PROJECTION_COL[6]));

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setHour(h);
        alarm.setMinute(minutes);
        alarm.setRepeatCycle(repeatCycle);
        alarm.setEnabled(enable);
        alarm.setDaysofweek(daysOfWeek);
        alarm.setMessage(message);
        LogUtil.d(TAG, "Query Alarm = " + alarm);
        return alarm;
    }

    void cancelAlarmWithTime(final int hour, final int minute, boolean uiPerform) {
        // TODO: Migrate AsyncTask to ThreadPool for better control of thread
        final ContentResolver cr = getAppContext().getContentResolver();

        new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... id) {
                List<Alarm> matchAlarms = queryAlarmWithTime(hour, minute);
                if (!matchAlarms.isEmpty()) {
                    ArrayList<ContentProviderOperation> providerOperations = new ArrayList<>();
                    for (Alarm alarm:matchAlarms) {
                        ContentValues cv = new ContentValues();
                        cv.put("enabled", false);
                        ContentProviderOperation operation = ContentProviderOperation
                                .newUpdate(alarmGeneralUri
                                        .buildUpon()
                                        .appendPath(alarm.getId())
                                        .build())
                                .withValues(cv)
                                .build();
                        providerOperations.add(operation);
                    }
                    try {
                        cr.applyBatch("com.android.deskclock", providerOperations);
                    } catch (RemoteException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute(hour, minute);
    }

    void cancelAlarmWithMessage(String msg) {
        final ContentResolver cr = getAppContext().getContentResolver();

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... msgs) {
                List<Alarm> matchAlarms = queryAlarmWithMessage(msgs[0]);
                if (!matchAlarms.isEmpty()) {
                    ArrayList<ContentProviderOperation> providerOperations = new ArrayList<>();
                    for (Alarm alarm:matchAlarms) {
                        ContentValues cv = new ContentValues();
                        cv.put("enabled", false);
                        ContentProviderOperation operation = ContentProviderOperation
                                .newUpdate(alarmGeneralUri
                                        .buildUpon()
                                        .appendPath(alarm.getId())
                                        .build())
                                .withValues(cv)
                                .build();
                        providerOperations.add(operation);
                    }
                    try {
                        cr.applyBatch("com.android.deskclock", providerOperations);
                    } catch (RemoteException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute(msg);
    }

    void deleteAlarmWithTime(final int hour, final int minute) {
        // TODO: Migrate AsyncTask to ThreadPool for better control of thread
        final ContentResolver cr = getAppContext().getContentResolver();

        new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... id) {
                List<Alarm> matchAlarms = queryAlarmWithTime(hour, minute);
                if (!matchAlarms.isEmpty()) {
                    ArrayList<ContentProviderOperation> providerOperations = new ArrayList<>();
                    for (Alarm alarm:matchAlarms) {
                        ContentValues cv = new ContentValues();
                        cv.put("enabled", false);
                        ContentProviderOperation operation = ContentProviderOperation
                                .newDelete(alarmGeneralUri)
                                .withSelection("_id = ?", new String[]{alarm.getId()})
                                .build();
                        providerOperations.add(operation);
                    }
                    try {
                        cr.applyBatch("com.android.deskclock", providerOperations);
                    } catch (RemoteException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute(hour, minute);
    }

    void deleteAlarmWithMessage(String msg) {
        final ContentResolver cr = getAppContext().getContentResolver();

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... msgs) {
                List<Alarm> matchAlarms = queryAlarmWithMessage(msgs[0]);
                if (!matchAlarms.isEmpty()) {
                    ArrayList<ContentProviderOperation> providerOperations = new ArrayList<>();
                    for (Alarm alarm:matchAlarms) {
                        ContentValues cv = new ContentValues();
                        cv.put("enabled", false);
                        ContentProviderOperation operation = ContentProviderOperation
                                .newDelete(alarmGeneralUri)
                                .withSelection("_id = ?", new String[]{alarm.getId()})
                                .build();
                        providerOperations.add(operation);
                    }
                    try {
                        cr.applyBatch("com.android.deskclock", providerOperations);
                    } catch (RemoteException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        LogUtil.e(TAG, "Error canceling alarms");
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute(msg);
    }

}
