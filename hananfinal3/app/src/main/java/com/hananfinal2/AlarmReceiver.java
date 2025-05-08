package com.hananfinal2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.io.StringWriter;

public class AlarmReceiver extends BroadcastReceiver {
    private String title;
    private String message;
    private int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {
         title = intent.getStringExtra("title");
         message = intent.getStringExtra("message");
         notificationId = intent.getIntExtra("notificationId", 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("pet_alarm_channel", "Pet Alarms", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        switch (notificationId) {
            case 404:
                showNotification(context);
                break;
            default:
                showNotification(context);
                break;
        }
    }

    private void showNotification(Context context) {
        Intent openIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "pet_alarm_channel")
                .setSmallIcon(R.drawable.paw)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
//101- forgot sleep
//202 - hungry
//303 - sad
//404 - decrease happiness and hunger
