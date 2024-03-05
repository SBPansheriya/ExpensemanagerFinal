package com.kmsoft.expensemanager.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kmsoft.expensemanager.R;

public class SettingsActivity extends AppCompatActivity {

    RelativeLayout notification,share,rate,help,about;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        notification.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing this from my app!");
            startActivity(Intent.createChooser(shareIntent, "Share via..."));
        });

        help.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"testkmsoft@gmail.com"});
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        about.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this,AboutActivity.class);
            startActivity(intent);
        });

        back.setOnClickListener(v -> onBackPressed());
    }

    private void init(){
        notification = findViewById(R.id.notification);
        share = findViewById(R.id.share);
        rate = findViewById(R.id.rate);
        help = findViewById(R.id.help);
        about = findViewById(R.id.about);
        back = findViewById(R.id.back);
    }
}