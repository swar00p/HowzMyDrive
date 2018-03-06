package com.hyrulesoft.android.apps.howzmydrive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    private static final long FIRST_ALARM_OFFSET = 5000;

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent alarmPendingIntent;
        Intent alarmIntent;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(context, AlarmReceiver.class);

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()) ||
            "com.hyrulesoft.apps.android.howzmydrive.ALARM_START".equals(intent.getAction())) {
            // Set the alarm to start at approximately 6:00 a.m.
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis() + FIRST_ALARM_OFFSET);
            alarmPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    cal.getTimeInMillis(),
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES/5, alarmPendingIntent);
        }
    }
}
