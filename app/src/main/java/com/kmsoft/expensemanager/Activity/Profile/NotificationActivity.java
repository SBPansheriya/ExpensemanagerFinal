package com.kmsoft.expensemanager.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.kmsoft.expensemanager.Adapter.ExceedBudgetAdapter;
import com.kmsoft.expensemanager.R;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView exceedBudgetRecyclerview;
    ExceedBudgetAdapter exceedBudgetAdapter;
    ImageView back,more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        exceedBudgetAdapter = new ExceedBudgetAdapter(NotificationActivity.this);
        exceedBudgetRecyclerview.setLayoutManager(manager);
        exceedBudgetRecyclerview.setAdapter(exceedBudgetAdapter);

        back.setOnClickListener(v -> onBackPressed());

        more.setOnClickListener(v -> {
            Context wrapper = new ContextThemeWrapper(NotificationActivity.this, R.style.YOURSTYLE);
            PopupMenu popupMenu = new PopupMenu(wrapper, v);
            popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Toast.makeText(NotificationActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });
            popupMenu.show();

        });
    }

    private void init(){
        exceedBudgetRecyclerview = findViewById(R.id.exceed_budget_recyclerview);
        back = findViewById(R.id.back);
        more = findViewById(R.id.more);
    }
}