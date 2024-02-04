package com.kmsoft.expensemanager.Activity.Budget;

import static com.kmsoft.expensemanager.Constant.budgetArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

public class CreateBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Spinner createSpinner;
    Switch createSwitch;
    Slider createSlider;
    EditText createBudgetAmount;
    TextView createBudgetContinue;
    ImageView back;
    Budget budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        init();

        dbHelper = new DBHelper(CreateBudgetActivity.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        createBudgetContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetAmount = createBudgetAmount.getText().toString();
                int percentageBudget = (int) createSlider.getValue();
                budget = new Budget(0,budgetAmount,"Shopping",R.drawable.i1,percentageBudget);
                budgetArrayList.add(budget);
                dbHelper.insertBudgetData(budget);
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