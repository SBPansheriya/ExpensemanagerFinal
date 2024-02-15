package com.kmsoft.expensemanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kmsoft.expensemanager.R;

public class SplashActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String CLICK_KEY = "click";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static String USER_NAME = "userName";
    public static String USER_IMAGE = "userImage";
    ImageView getStarted,person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getStarted = findViewById(R.id.get_started);
        person = findViewById(R.id.person);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.dance_animation);
        person.startAnimation(animation);

        getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}