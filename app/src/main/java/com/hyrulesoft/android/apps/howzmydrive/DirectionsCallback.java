package com.hyrulesoft.android.apps.howzmydrive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;

import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by backi on 2/28/2018.
 */

public class DirectionsCallback implements PendingResult.Callback<DirectionsResult> {
    Context context;

    public DirectionsCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onResult(DirectionsResult result) {
        long amCommuteDuration=0, pmCommuteDuration=0;

        DirectionsRoute[] routes = result.routes;
        for (int i=0; i<routes.length; i++) {
            DirectionsLeg[] legs = routes[i].legs;
            for (int j=0; j<legs.length; j++) {
                amCommuteDuration += legs[j].duration.inSeconds;
            }
            showNotification(amCommuteDuration);
        }
    }

    @Override
    public void onFailure(Throwable e) {

    }

    private void showNotification(long amCommuteDuration) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this., MainActivity.class), 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder()

                /new NotificationCompat.Builder(context, "default");
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();*/

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
}
