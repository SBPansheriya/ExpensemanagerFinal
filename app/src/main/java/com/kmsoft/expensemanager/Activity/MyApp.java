package com.kmsoft.expensemanager.Activity;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class MyApp extends Application {

    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        scheduleAlarm();
    }

    private void scheduleAlarm() {

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        createNotificationChannel();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyAlarmReceiver.class);
        intent.setAction("CHECK_FOR_ENTRY");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            long triggerTime = calendar.getTimeInMillis();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
        private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "Channel for notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
    }
}
