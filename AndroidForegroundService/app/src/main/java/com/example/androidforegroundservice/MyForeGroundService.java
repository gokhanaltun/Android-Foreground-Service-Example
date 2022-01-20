package com.example.androidforegroundservice;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyForeGroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null && action.equals("start")){
            createNotification();
        }else if (action != null && action.equals("stop")){
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void createNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(this, 1, mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopServiceIntent = new Intent(this, MyForeGroundService.class);
        stopServiceIntent.setAction("stop");

        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent stopServicePendingIntent = PendingIntent.getService(this, 1, stopServiceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.design_foreground_service_notification);
        remoteViews.setOnClickPendingIntent(R.id.btn_notification_stop_service, stopServicePendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channel_id = "id1";
            String channel_name = "name1";
            String channel_description = "description1";
            int notification_priority = NotificationManager.IMPORTANCE_NONE;

            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channel_id);

            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(channel_id, channel_name, notification_priority);
                notificationChannel.setDescription(channel_description);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id);
            builder.setSmallIcon(R.drawable.ic_service);
            builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            builder.setAutoCancel(true);
            builder.setCustomContentView(remoteViews);
            builder.setContentIntent(mainActivityPendingIntent);
            builder.build();
            startForeground(1, builder.build());
        }
    }
}
