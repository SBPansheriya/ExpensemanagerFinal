package com.kmsoft.expensemanager.Activity;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "ReminderChannel";

    DBHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("########################" + " receiverGet");

        dbHelper = new DBHelper(context);

        if (intent.getAction() != null && intent.getAction().equals("CHECK_FOR_ENTRY")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            long triggerTime = calendar.getTimeInMillis();

            calendar.setTimeInMillis(System.currentTimeMillis());
            long currentTime = calendar.getTimeInMillis();

            if (currentTime >= triggerTime && currentTime < triggerTime + 60000) {
                System.out.println("########################" + intent.getAction());
                sendNotification(context);
            }
        }
    }

    private void sendNotification(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NotificationChannel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        String time = dateFormat.format(calendar.getTime());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) != PackageManager.PERMISSION_DENIED) {
                dbHelper.insertBudgetNotificationData("", "", 0, time, false, "Reminder",0);
            }
        } else {
            dbHelper.insertBudgetNotificationData("", "", 0, time, false, "Reminder",0);
        }

        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.only_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.only_logo))
                .setContentTitle("Expense Manager")
                .setContentText("If you don't added any income or expense, please add your income and expense")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = builder.build();

        // Show notification
        notificationManager.notify(1, notification);
    }
}