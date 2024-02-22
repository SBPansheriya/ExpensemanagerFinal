package com.kmsoft.expensemanager.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.kmsoft.expensemanager.Adapter.ExceedBudgetAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.BudgetNotification;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView exceedBudgetRecyclerview;
    ExceedBudgetAdapter exceedBudgetAdapter;
    DBHelper dbHelper;
    TextView noNotification;
    ImageView back, more;
    ArrayList<BudgetNotification> notificationsList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        dbHelper = new DBHelper(this);

        back.setOnClickListener(v -> onBackPressed());

        more.setOnClickListener(v -> {
            Context wrapper = new ContextThemeWrapper(NotificationActivity.this, R.style.YOURSTYLE);
            PopupMenu popupMenu = new PopupMenu(wrapper, v);
            popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getTitle().equals("Remove all")) {
                    if (!notificationsList.isEmpty()) {
                        dbHelper.deleteAllBudgetNotificationData();
                        notificationsList.clear();
                        exceedBudgetAdapter.notifyDataSetChanged();
                        noNotification.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            });
            popupMenu.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Display();
    }

    private void Display() {
        Cursor cursor = dbHelper.getAllBudgetNotificationData();
        if (cursor != null && cursor.moveToFirst()) {
            notificationsList = new ArrayList<>();
            do {
                int id = cursor.getInt(0);
                String amount = cursor.getString(1);
                String name = cursor.getString(2);
                int image = cursor.getInt(3);
                String currentTime = cursor.getString(4);
                String isRemove = cursor.getString(5);
                String tag = cursor.getString(6);

                BudgetNotification notification = new BudgetNotification(id, amount, name, image, currentTime, isRemove,tag);
                notificationsList.add(notification);

                notificationsList.sort(Comparator.comparing(BudgetNotification::getCurrentTime));

                Collections.reverse(notificationsList);

            } while (cursor.moveToNext());

            if (notificationsList.isEmpty()) {
                noNotification.setVisibility(View.VISIBLE);
                exceedBudgetRecyclerview.setVisibility(View.GONE);
            } else {
                noNotification.setVisibility(View.GONE);
                exceedBudgetRecyclerview.setVisibility(View.VISIBLE);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                exceedBudgetAdapter = new ExceedBudgetAdapter(NotificationActivity.this, notificationsList);
                exceedBudgetRecyclerview.setLayoutManager(manager);
                exceedBudgetRecyclerview.setAdapter(exceedBudgetAdapter);
            }
        } else {
            noNotification.setVisibility(View.VISIBLE);
            exceedBudgetRecyclerview.setVisibility(View.GONE);
        }
    }

    private void init() {
        exceedBudgetRecyclerview = findViewById(R.id.exceed_budget_recyclerview);
        noNotification = findViewById(R.id.no_notification);
        back = findViewById(R.id.back);
        more = findViewById(R.id.more);
    }
}