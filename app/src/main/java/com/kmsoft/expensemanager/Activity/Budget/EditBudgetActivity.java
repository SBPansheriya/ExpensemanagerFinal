package com.kmsoft.expensemanager.Activity.Budget;

import static com.kmsoft.expensemanager.Constant.budgetArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

public class EditBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText editBudgetAmount;
    ImageView back;
    Slider editBudgetSlider;
    Button editBudget;
    Budget budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(EditBudgetActivity.this);

        budget = (Budget) getIntent().getSerializableExtra("budget");

        editBudgetAmount.setText(""+budget.getAmountBudget());
        editBudgetSlider.setValue(budget.getPercentageBudget());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = editBudgetAmount.getText().toString();
                int percentage = (int) editBudgetSlider.getValue();
                budget = new Budget(budget.getId(), amount,budget.getCategoryNameBudget(),budget.getCategoryImageBudget(),percentage);
                budgetArrayList.add(budget);
                dbHelper.updateBudgetData(budget);
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("budget",budget);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init(){
        back = findViewById(R.id.back);
        editBudgetAmount = findViewById(R.id.edit_budget_amount);
        editBudgetSlider = findViewById(R.id.edit_budget_slider);
        editBudget = findViewById(R.id.edit_budget_continue);
    }
}