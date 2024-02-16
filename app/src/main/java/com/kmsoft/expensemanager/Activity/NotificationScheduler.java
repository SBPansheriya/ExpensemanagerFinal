package com.kmsoft.expensemanager.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationScheduler {

    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleNotification(Context context, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {

        Intent intent = new Intent(context, NotificationReceiver.class);
        Gson gson = new Gson();
        String list = gson.toJson(incomeAndExpenseArrayList);
        intent.putExtra("incomeAndExpense", list);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}