package com.npktech.ravi.newsapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;



public class NotificationUtils {

    Context mCtx;
    String CHANNEL_ID = "jobservice";
    int ID_SMALL_NOTIFICATION = 252;

    NotificationUtils(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void showSmallNotification(String title, String message, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel();

        PendingIntent resultPendingIntent =
                PendingIntent.getService(
                        mCtx,
                        0,
                        intent,
                        0
                );


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, CHANNEL_ID);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(false)


                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_autorenew_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher_round))
                .setContentText(message)
                .addAction(0, "Cancel", resultPendingIntent)
                .build();


            notification.flags |= Notification.FLAG_ONGOING_EVENT;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "News Update";
            String description = "Used for show news updating notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = mCtx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void dismissNotification(){
        NotificationManagerCompat.from(mCtx).cancel(ID_SMALL_NOTIFICATION);
    }
}
