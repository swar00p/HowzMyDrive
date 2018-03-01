package com.hyrulesoft.android.apps.howzmydrive;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int CHECK_AM_COMMUTE = 0;
    public static final int CHECK_PM_COMMUTE = 1;
    private ArrayList<PendingIntent> allPendingIntents = new ArrayList<PendingIntent>();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent amIntent, pmIntent;
        PendingIntent amPendingIntent, pmPendingIntent;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String homeAddressSaved = sp.getString(MainActivity.HOME_ADDRESS, null);
        String workAddressSaved = sp.getString(MainActivity.WORK_ADDRESS, null);

        switch (intent.getIntExtra("ALARM_TYPE", 0)) {
            case BootReceiver.AM_START:
                amIntent = new Intent(context, AlarmReceiver.class);
                amIntent.putExtra("ALARM_TYPE", CHECK_AM_COMMUTE);
                Calendar amStart = Calendar.getInstance();
                amStart.setTimeInMillis(System.currentTimeMillis());
                amPendingIntent = PendingIntent.getBroadcast(context, CHECK_AM_COMMUTE, amIntent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC,
                        amStart.getTimeInMillis(),
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES/15, amPendingIntent);
                allPendingIntents.add(CHECK_AM_COMMUTE, amPendingIntent);
                break;
            case BootReceiver.AM_END:
                alarmManager.cancel(allPendingIntents.get(CHECK_AM_COMMUTE));
                break;
            case BootReceiver.PM_START:
                pmIntent = new Intent(context, AlarmReceiver.class);
                pmIntent.putExtra("ALARM_TYPE", CHECK_PM_COMMUTE);
                Calendar pmStart = Calendar.getInstance();
                pmStart.setTimeInMillis(System.currentTimeMillis());
                pmPendingIntent = PendingIntent.getBroadcast(context, CHECK_PM_COMMUTE, pmIntent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC,
                        pmStart.getTimeInMillis(),
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES/15, pmPendingIntent);
                allPendingIntents.add(CHECK_PM_COMMUTE, pmPendingIntent);
                break;
            case BootReceiver.PM_END:
                alarmManager.cancel(allPendingIntents.get(CHECK_PM_COMMUTE));
                break;
            case CHECK_AM_COMMUTE:
                //Calculate commute time from Home to Work
                //Display notification with commute time
                //https://maps.googleapis.com/maps/api/directions/json?origin=&destination=&key=
                GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyAD5wnOKi54M0ETlcpgbGGv6FoyzxTh1nU").build();
                DirectionsApiRequest apiRequest = DirectionsApi.
                        newRequest(geoApiContext).
                        origin(homeAddressSaved).
                        destination(workAddressSaved);
                apiRequest.setCallback(new DirectionsCallback(context));
                break;
            case CHECK_PM_COMMUTE:
                //Calculate commute time from Work to Home
                //Display notification with commute time
                break;
        }
    }

}