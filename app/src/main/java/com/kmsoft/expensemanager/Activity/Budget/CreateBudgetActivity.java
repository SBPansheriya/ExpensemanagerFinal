package com.kmsoft.expensemanager.Activity.Budget;


import static com.kmsoft.expensemanager.Activity.MainActivity.currencySymbol;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;

import com.kmsoft.expensemanager.Activity.NotificationScheduler;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class CreateBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Slider createSlider;
    LinearLayout budgetCategory;
    EditText createBudgetAmount;
    TextView createBudgetContinue, budgetCategoryName;
    ActivityResultLauncher<Intent> launchSomeActivityResult;
    ImageView back;
    Budget budget;
    int imageResId;
    String categoryName;
    ArrayList<Budget> budgetArrayList;
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        init();

        dbHelper = new DBHelper(CreateBudgetActivity.this);

        String listGet = getIntent().getStringExtra("budgetArrayList");
        gson = new Gson();
        budgetArrayList = gson.fromJson(listGet, new TypeToken<ArrayList<Budget>>() {
        }.getType());

        back.setOnClickListener(v -> onBackPressed());

        launchSomeActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    imageResId = data.getIntExtra("categoryImage", 0);
                    categoryName = data.getStringExtra("categoryName");
                    if (!TextUtils.isEmpty(categoryName)) {
                        budgetCategoryName.setText(String.format("%s", categoryName));
                    }
                }
            }
        });

        createBudgetAmount.setText(String.format("%s0", currencySymbol));

        createBudgetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.startsWith(currencySymbol)) {
                    createBudgetAmount.setText(String.format(currencySymbol + input));
                    createBudgetAmount.setSelection(createBudgetAmount.getText().length());
                }
            }
        });

        createBudgetContinue.setOnClickListener(v -> {
            String budgetAmount =createBudgetAmount.getText().toString();
            int percentageBudget = (int) createSlider.getValue();
            if (budgetAmount.equals(currencySymbol + "0") || budgetAmount.equals(currencySymbol)) {
                Toast.makeText(CreateBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(budgetAmount)) {
                Toast.makeText(CreateBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(categoryName)) {
                Toast.makeText(CreateBudgetActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
            } else {
                boolean categoryFound = false;
                if (budgetArrayList.size() != 0) {
                    for (int i = 0; i < budgetArrayList.size(); i++) {
                        if (budgetArrayList.get(i).getCategoryNameBudget().equalsIgnoreCase(categoryName)) {
                            Toast.makeText(CreateBudgetActivity.this, "Category already exists", Toast.LENGTH_SHORT).show();
//                            budget = new Budget(budgetArrayList.get(i).getId(), budgetAmount, categoryName, imageResId, percentageBudget);
//                            dbHelper.updateBudgetData(budget);
                            categoryFound = true;
                            break;
                        }
                    }
                }

                if (!categoryFound) {
                    String amount = extractNumericPart(createBudgetAmount.getText().toString());
                    budget = new Budget(0, amount, categoryName, imageResId, percentageBudget);
                    budgetArrayList.add(budget);
                    dbHelper.insertBudgetData(budget);

                    expenseList = filterCategories(incomeAndExpenseArrayList);

                    double totalExpense = 0;
                    double budgetAmount1;
                    for (int i = 0; i < expenseList.size(); i++) {
                        if (expenseList.get(i).getCategoryName().equals(categoryName)) {
                            double amount1 = Double.parseDouble(extractNumericPart(expenseList.get(i).getAmount()));
                            totalExpense += amount1;
                        }
                    }

                    for (int i = 0; i < expenseList.size(); i++) {
                        if (categoryName.equals(expenseList.get(i).getCategoryName())) {
                            budgetAmount1 = Double.parseDouble(extractNumericPart(budgetAmount));
                            if (totalExpense >= budgetAmount1) {
                                NotificationScheduler.scheduleNotification(this, budget);
                                break;
                            }
                        }
                    }
                    onBackPressed();
                }
            }
        });

        budgetCategory.setOnClickListener(v -> {
            Intent intent = new Intent(CreateBudgetActivity.this, AddCategoryActivity.class);
            intent.putExtra("clicked", "Expense");
            intent.putExtra("image", imageResId);
            intent.putExtra("name", categoryName);
            launchSomeActivityResult.launch(intent);
        });
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    private ArrayList<IncomeAndExpense> filterCategories(ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            if (incomeAndExpense.getTag().equals("Expense")) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        Gson gson = new Gson();
        String list = gson.toJson(budgetArrayList);
        intent.putExtra("budgetArrayList", list);
        setResult(RESULT_OK, intent);
    }

    private void init() {
        createBudgetAmount = findViewById(R.id.create_budget_amount);
        createSlider = findViewById(R.id.create_budget_slider);
        budgetCategory = findViewById(R.id.budget_category);
        createBudgetContinue = findViewById(R.id.create_budget_continue);
        budgetCategoryName = findViewById(R.id.budget_category_name);
        back = findViewById(R.id.back);
    }
}