package com.kmsoft.expensemanager.Activity;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MyAlarmReceiver extends BroadcastReceiver {

    DBHelper dbHelper;
    IncomeAndExpense incomeAndExpense;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("########################" + " receiverGet");
        dbHelper = new DBHelper(context);
        Display();

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
                checkForEntryAndNotify(context);
            }
        }
    }

    private void sendNotification(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "NotificationChannel";
//            String description = "Channel for notifications";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("default", name, importance);
//            channel.setDescription(description);
//
//            if (notificationManager == null) {
//                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            }
//            notificationManager.createNotificationChannel(channel);
//        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String time = dateFormat.format(calendar.getTime());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) != PackageManager.PERMISSION_DENIED) {
                dbHelper.insertBudgetNotificationData("", "", 0, time, false, "Reminder");
            }
        } else {
            dbHelper.insertBudgetNotificationData("", "", 0, time, false, "Reminder");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.only_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.only_logo))
                .setContentTitle("No Entry Added Today")
                .setContentText("You haven't added any income or expense entry for today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    private void checkForEntryAndNotify(Context context) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        boolean hasEntry = false;
        if (!incomeAndExpenseArrayList.isEmpty()) {
            for (IncomeAndExpense entry : incomeAndExpenseArrayList) {
                Calendar entryDate = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(entryDate.getTime());
                if (formattedDate.equals(entry.getCurrantDate())) {
                    hasEntry = true;
                    break;
                }
            }
        }

        if (!hasEntry) {
            sendNotification(context);
        }
    }

//    private void createNotificationChannel(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "NotificationChannel";
//            String description = "Channel for notifications";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("default", name, importance);
//            channel.setDescription(description);
//
//            if (notificationManager == null) {
//                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            }
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    private void Display() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            incomeAndExpenseArrayList = new ArrayList<>();
            do {
                int id = cursor.getInt(0);
                String incomeAmount = cursor.getString(1);
                double currantDateTimeStamp = cursor.getDouble(2);
                double selectedDateTimeStamp = cursor.getDouble(3);
                String currentdate = cursor.getString(4);
                String incomeDate = cursor.getString(5);
                String incomeDay = cursor.getString(6);
                String incomeAddTime = cursor.getString(7);
                String categoryName = cursor.getString(8);
                int categoryImage = cursor.getInt(9);
                int categoryColor = cursor.getInt(10);
                String incomeDescription = cursor.getString(11);
                String addAttachment = cursor.getString(12);
                String tag = cursor.getString(13);

                incomeAndExpense = new IncomeAndExpense(id, incomeAmount, currantDateTimeStamp, selectedDateTimeStamp, currentdate, incomeDate, incomeDay, incomeAddTime, categoryName, categoryImage, categoryColor, incomeDescription, addAttachment, tag);
                incomeAndExpenseArrayList.add(incomeAndExpense);
            } while (cursor.moveToNext());
        }
    }
}