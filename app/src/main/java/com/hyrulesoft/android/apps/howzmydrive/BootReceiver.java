package com.hyrulesoft.android.apps.howzmydrive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    public static final int AM_START = 2;
    public static final int AM_END = 3;
    public static final int PM_START = 4;
    public static final int PM_END = 5;

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent amStartPendingIntent, amEndPendingIntent, pmStartPendingIntent, pmEndPendingIntent;
        Intent amStartIntent, amEndIntent, pmStartIntent, pmEndIntent;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        amStartIntent = new Intent(context, AlarmReceiver.class);
        amStartIntent.putExtra("ALARM_TYPE", AM_START);
        amEndIntent = new Intent(context, AlarmReceiver.class);
        amEndIntent.putExtra("ALARM_TYPE", AM_END);
        pmStartIntent = new Intent(context, AlarmReceiver.class);
        pmStartIntent.putExtra("ALARM_TYPE", PM_START);
        pmEndIntent = new Intent(context, AlarmReceiver.class);
        pmEndIntent.putExtra("ALARM_TYPE", PM_END);

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()) ||
            "com.hyrulesoft.apps.android.howzmydrive.ALARM_START".equals(intent.getAction())) {
            // Set the alarm to start at approximately 6:00 a.m.
            Calendar amStart = Calendar.getInstance();
            amStart.setTimeInMillis(System.currentTimeMillis());
            amStart.set(Calendar.HOUR_OF_DAY, 6);
            amStartPendingIntent = PendingIntent.getBroadcast(context, AM_START, amStartIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    amStart.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, amStartPendingIntent);

            // Set the alarm to start at approximately 10:00 a.m.
            Calendar amEnd = Calendar.getInstance();
            amEnd.setTimeInMillis(System.currentTimeMillis());
            amEnd.set(Calendar.HOUR_OF_DAY, 13);
            amEndPendingIntent = PendingIntent.getBroadcast(context, AM_END, amEndIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    amEnd.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, amEndPendingIntent);

            // Set the alarm to start at approximately 3:00 p.m.
            Calendar pmStart = Calendar.getInstance();
            pmStart.setTimeInMillis(System.currentTimeMillis());
            pmStart.set(Calendar.HOUR_OF_DAY, 16);
            pmStart.set(Calendar.MINUTE, 49);
            pmStartPendingIntent = PendingIntent.getBroadcast(context, PM_START, pmStartIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    pmStart.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pmStartPendingIntent);

            // Set the alarm to start at approximately 7:00 p.m.
            Calendar pmEnd = Calendar.getInstance();
            pmEnd.setTimeInMillis(System.currentTimeMillis());
            pmEnd.set(Calendar.HOUR_OF_DAY, 22);
            pmEndPendingIntent = PendingIntent.getBroadcast(context, PM_END, pmEndIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    pmEnd.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pmEndPendingIntent);
        }
    }
}
