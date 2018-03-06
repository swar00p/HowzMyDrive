package com.hyrulesoft.android.apps.howzmydrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DirectionsApiRequest apiRequest;
        String apiKey;
        long amStartTime, amEndTime, pmStartTime, pmEndTime;
        Calendar cal = Calendar.getInstance();
        long currentHour = cal.get(Calendar.HOUR_OF_DAY);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String homeAddressSaved = sp.getString(MainActivity.HOME_ADDRESS, null);
        String workAddressSaved = sp.getString(MainActivity.WORK_ADDRESS, null);

        ApplicationInfo ai = context.getApplicationInfo();
        Bundle bundle = ai.metaData;

        apiKey = bundle.getString("API_KEY");
        amStartTime = bundle.getInt("AM_START_TIME");
        amEndTime = bundle.getInt("AM_END_TIME");
        pmStartTime = bundle.getInt("PM_START_TIME");
        pmEndTime = bundle.getInt("PM_END_TIME");

        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(apiKey).build();

        if ((currentHour >= amStartTime && currentHour <= amEndTime) ||
            (currentHour >= pmStartTime && currentHour <= pmEndTime)) {
            //Calculate commute time from Home to Work
            //Display notification with commute time
            //https://maps.googleapis.com/maps/api/directions/json?origin=&destination=&key=
            apiRequest = DirectionsApi.
                    newRequest(geoApiContext).
                    origin(homeAddressSaved).
                    destination(workAddressSaved);
            apiRequest.setCallback(new DirectionsCallback(context, "AM"));
        }
    }
}
