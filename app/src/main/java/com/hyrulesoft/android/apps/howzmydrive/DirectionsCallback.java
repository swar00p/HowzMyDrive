package com.hyrulesoft.android.apps.howzmydrive;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

public class DirectionsCallback implements PendingResult.Callback<DirectionsResult> {
    private Context context;
    private String amOrPm;
    private NotificationManager mNotificationManager;

    public DirectionsCallback(Context context, String amOrPm) {
        this.context = context;
        this.amOrPm = amOrPm;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onResult(DirectionsResult result) {
        long commuteDuration=0;

        if ("AM".equals(amOrPm)) {
            DirectionsRoute[] routes = result.routes;
            for (DirectionsRoute route : routes) {
                DirectionsLeg[] legs = route.legs;
                for (DirectionsLeg leg : legs) {
                    commuteDuration += leg.duration.inSeconds;
                }
                showNotification(commuteDuration, "AM");
            }
        } else {
            DirectionsRoute[] routes = result.routes;
            for (DirectionsRoute route : routes) {
                DirectionsLeg[] legs = route.legs;
                for (DirectionsLeg leg : legs) {
                    commuteDuration += leg.duration.inSeconds;
                }
                showNotification(commuteDuration, "PM");
            }
        }
    }

    @Override
    public void onFailure(Throwable e) {

    }

    private void showNotification(long commuteDuration, String amOrPm) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("HowzMyDrive")
                .setContentText("Current " + amOrPm + " Commute: " + commuteDuration/60 + " min")
                .setAutoCancel(true);

        final Notification notification = builder.build();
        mNotificationManager.notify(1, notification);
    }

    /*
    private void showNotification(long commuteDuration, String amOrPm) {
        PendingIntent pi = PendingIntent.getActivity(this.context, 0, new Intent(this.context, MainActivity.class), 0);
        Resources r = context.getResources();
        NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
                context.getString(R.string.noti_channel_default), NotificationManager.IMPORTANCE_DEFAULT);
        chan1.setLightColor(Color.GREEN);
        chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan1);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                        .setContentTitle("HowzMyDrive")
                        .setContentText("Current " + amOrPm + " Commute: " + commuteDuration/60 + " min")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setAutoCancel(true);
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
    }*/
}
