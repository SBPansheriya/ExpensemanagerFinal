package com.kmsoft.expensemanager.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmsoft.expensemanager.R;

import java.util.Currency;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String CLICK_KEY = "click";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static String USER_NAME = "userName";
    public static String USER_IMAGE = "userImage";
    ImageView getStarted,person;
    public static String currencySymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        currencySymbol = getCurrencySymbol();

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

    private String getCurrencySymbol() {
        // Get the default locale of the device
        Locale locale = Locale.getDefault();

        // Get the currency instance for the device's locale
        Currency currency = Currency.getInstance(locale);

        // Get the currency symbol
        return currency.getSymbol();
    }
}