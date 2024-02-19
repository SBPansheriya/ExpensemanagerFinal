package com.kmsoft.expensemanager.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.kmsoft.expensemanager.Model.Budget;

import java.util.Calendar;

public class NotificationScheduler {

    public static void scheduleNotification(Context context, Budget budget) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("budget", budget);

        int requestCode = budget.getCategoryNameBudget().hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long time = calendar.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}