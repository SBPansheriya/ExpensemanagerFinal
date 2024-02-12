package com.kmsoft.expensemanager.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kmsoft.expensemanager.R;

public class ExportDataActivity extends AppCompatActivity {

    ImageView back;
    Spinner whatDataExport, whenDateRange, whatFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        ArrayAdapter<String> whatDataExportAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, this.getResources().getStringArray(R.array.whatDataExport));
        whatDataExportAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        whatDataExport.setAdapter(whatDataExportAdapter);

        ArrayAdapter<String> whenDateAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, this.getResources().getStringArray(R.array.whenDate));
        whenDateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        whenDateRange.setAdapter(whenDateAdapter);

        ArrayAdapter<String> whatFormatAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, this.getResources().getStringArray(R.array.whatFormat));
        whatFormatAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        whatFormat.setAdapter(whatFormatAdapter);

        back.setOnClickListener(v -> onBackPressed());
    }

    private void init(){
        back = findViewById(R.id.back);
        whatDataExport = findViewById(R.id.what_data_export);
        whenDateRange = findViewById(R.id.when_date_range);
        whatFormat = findViewById(R.id.what_format);
    }
}