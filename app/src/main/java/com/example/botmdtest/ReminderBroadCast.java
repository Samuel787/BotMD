package com.example.botmdtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyTemperature")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Log Your Temperature")
                .setContentText("Take a moment to log your temperature.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
