package com.kmsoft.expensemanager.Activity.FloatingButton;


import static com.kmsoft.expensemanager.Constant.iconList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kmsoft.expensemanager.Adapter.IconAdapter;
import com.kmsoft.expensemanager.R;

public class IconActivity extends AppCompatActivity {

    RecyclerView iconRecyclerview;
    IconAdapter iconAdapter;
    ImageView back;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        iconRecyclerview = findViewById(R.id.icon_recyclerview);
        back = findViewById(R.id.back);

        name = getIntent().getStringExtra("name");

        back.setOnClickListener(v -> onBackPressed());

        GridLayoutManager layoutManager=new GridLayoutManager(this,5);
        iconAdapter = new IconAdapter(IconActivity.this,iconList);
        iconRecyclerview.setLayoutManager(layoutManager);
        iconRecyclerview.setAdapter(iconAdapter);
    }

    public void onBackPressed(int clickedImageResId) {
        Intent intent = new Intent();
        intent.putExtra("imageResId", clickedImageResId);
        intent.putExtra("name",name);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}