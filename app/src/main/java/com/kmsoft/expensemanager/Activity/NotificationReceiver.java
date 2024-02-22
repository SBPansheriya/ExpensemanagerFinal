package com.kmsoft.expensemanager.Activity;

import static android.Manifest.permission.POST_NOTIFICATIONS;

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
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "BudgetExceedChannel";
    NotificationManager notificationManager;
    DBHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        Budget budget = (Budget) intent.getSerializableExtra("budget");

        dbHelper = new DBHelper(context);
        showNotification(context, budget);
    }

    private void showNotification(Context context, Budget budget) {

        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Budget Exceed Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        String time = dateFormat.format(calendar.getTime());

        int notificationId = budget.getCategoryNameBudget().hashCode();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) != PackageManager.PERMISSION_DENIED) {
                dbHelper.insertBudgetNotificationData(budget.getAmountBudget(), budget.getCategoryNameBudget(), budget.getCategoryImageBudget(), time, false, "Exceed");
            }
        } else {
            dbHelper.insertBudgetNotificationData(budget.getAmountBudget(), budget.getCategoryNameBudget(), budget.getCategoryImageBudget(), time, false, "Exceed");
        }

        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.only_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.only_logo))
                .setContentTitle("Budget Exceeded")
                .setContentText("Your budget for " + budget.getCategoryNameBudget() + " has been exceeded.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show notification
        notificationManager.notify(notificationId, builder.build());
    }
}

