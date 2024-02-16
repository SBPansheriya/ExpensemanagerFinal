package com.kmsoft.expensemanager.Activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class NotificationReceiver extends BroadcastReceiver {

    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
    Gson gson;

    @Override
    public void onReceive(Context context, Intent intent) {
        String listGet = intent.getStringExtra("incomeAndExpense");
        gson = new Gson();
        incomeAndExpenseArrayList = gson.fromJson(listGet, new TypeToken<ArrayList<Budget>>() {}.getType());
        Calendar cal = Calendar.getInstance();

//        Calendar calendar = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = sdf.format(calendar.getTime());

        incomeAndExpenseArrayList.sort(Comparator.comparing(IncomeAndExpense::getCurrantDateTimeStamp));

        if (incomeAndExpenseArrayList.isEmpty()) {
            if (cal.get(Calendar.HOUR_OF_DAY) == 20 && cal.get(Calendar.MINUTE) == 0) {
                showNotification(context);
            }
        }
    }

    private void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setContentTitle("Reminder")
                .setContentText("You haven't added any data today.")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }
}

