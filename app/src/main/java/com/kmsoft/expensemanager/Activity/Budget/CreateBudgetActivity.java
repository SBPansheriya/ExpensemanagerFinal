package com.kmsoft.expensemanager.Activity.Budget;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.kmsoft.expensemanager.R;

public class CreateBudgetActivity extends AppCompatActivity {

    Spinner createSpinner;
    Switch createSwitch;
    Slider createSlider;
    TextView createBudgetAmount,createBudgetContinue;
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        createBudgetContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init(){
        createSpinner = findViewById(R.id.create_budget_spinner);
        createBudgetAmount = findViewById(R.id.create_budget_amount);
        createSlider = findViewById(R.id.create_budget_slider);
        createSwitch = findViewById(R.id.create_budget_switch);
        createBudgetContinue = findViewById(R.id.create_budget_continue);
        back = findViewById(R.id.back);
    }
}