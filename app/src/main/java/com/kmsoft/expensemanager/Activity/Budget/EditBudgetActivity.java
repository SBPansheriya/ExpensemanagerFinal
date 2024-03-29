package com.kmsoft.expensemanager.Activity.Budget;

import static com.kmsoft.expensemanager.Activity.MainActivity.currencySymbol;
import static com.kmsoft.expensemanager.Constant.budgetArrayList;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;

import com.kmsoft.expensemanager.Activity.NotificationScheduler;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class EditBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText editBudgetAmount;
    ImageView back;
    Slider editBudgetSlider;
    Button editBudget;
    Budget budget;
    LinearLayout editBudgetCategory;
    TextView editBudgetCategoryName;
    ActivityResultLauncher<Intent> launchSomeActivityResult;
    int imageResId;
    String categoryName;
    Gson gson;
    int categoryId;
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();

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

        String listGet = getIntent().getStringExtra("budgetArrayList");
        gson = new Gson();
        budgetArrayList = gson.fromJson(listGet, new TypeToken<ArrayList<Budget>>() {
        }.getType());

        editBudgetAmount.setText(String.format("%s%s", currencySymbol, budget.getAmountBudget()));
        editBudgetCategoryName.setText(String.format("%s", budget.getCategoryNameBudget()));

        launchSomeActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    imageResId = data.getIntExtra("categoryImage", 0);
                    categoryName = data.getStringExtra("categoryName");
                    categoryId = data.getIntExtra("id", 0);
                    if (!TextUtils.isEmpty(categoryName)) {
                        editBudgetCategoryName.setText(String.format("%s", categoryName));
                    }
                }
            }
        });

        back.setOnClickListener(v -> onBackPressed());

        editBudgetCategory.setOnClickListener(v -> {
            Intent intent = new Intent(EditBudgetActivity.this, AddCategoryActivity.class);
            if (imageResId == 0) {
                intent.putExtra("image", budget.getCategoryImageBudget());
            } else {
                intent.putExtra("image", imageResId);
            }
            if (TextUtils.isEmpty(categoryName)) {
                intent.putExtra("name", budget.getCategoryNameBudget());
            } else {
                intent.putExtra("name", categoryName);
            }
            if (categoryId == 0) {
                intent.putExtra("id", budget.getCategoryId());
            } else {
                intent.putExtra("id", categoryId);
            }
            intent.putExtra("clicked", "Income");

            launchSomeActivityResult.launch(intent);
        });

        editBudgetAmount.addTextChangedListener(new TextWatcher() {
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
                    editBudgetAmount.setText(String.format("₹%s", input));
                    editBudgetAmount.setSelection(editBudgetAmount.getText().length());
                }
            }
        });

        editBudget.setOnClickListener(view -> {
            String amount = extractNumericPart(editBudgetAmount.getText().toString());
            int percentage = (int) editBudgetSlider.getValue();

            if (TextUtils.isEmpty(categoryName)) {
                categoryName = budget.getCategoryNameBudget();
            }
            if (amount.equals(currencySymbol + "0") || amount.equals(currencySymbol)) {
                Toast.makeText(EditBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(amount)) {
                Toast.makeText(EditBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(categoryName)) {
                Toast.makeText(EditBudgetActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
            } else {
                boolean categoryFound = false;
                int existingId = budget.getId();
                String existingCategoryName = budget.getCategoryNameBudget();
                if (imageResId == 0) {
                    imageResId = budget.getCategoryImageBudget();
                }
                if (categoryId == 0) {
                    categoryId = budget.getCategoryId();
                }

                for (int i = 0; i < budgetArrayList.size(); i++) {
                    if (budgetArrayList.get(i).getCategoryNameBudget().equalsIgnoreCase(categoryName)) {
                        if (!existingCategoryName.equalsIgnoreCase(budgetArrayList.get(i).getCategoryNameBudget())) {
                            budget = new Budget(budgetArrayList.get(i).getId(), amount, categoryName, imageResId, percentage, categoryId);
                            dbHelper.updateBudgetData(budget);
                            dbHelper.deleteBudgetData(existingId);
                        } else {
                            budget = new Budget(existingId, amount, categoryName, imageResId, percentage, categoryId);
                            dbHelper.updateBudgetData(budget);
                        }
                        categoryFound = true;
                        break;
                    }
                }

                if (!categoryFound) {
                    budget = new Budget(budget.getId(), amount, categoryName, imageResId, percentage, categoryId);
                    dbHelper.updateBudgetData(budget);
                }
                budgetArrayList.add(budget);
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
                        budgetAmount1 = Double.parseDouble(extractNumericPart(amount));
                        if (totalExpense >= budgetAmount1) {
                            NotificationScheduler.scheduleNotification(this, budget);
                            break;
                        }
                    }
                }
                onBackPressed();
            }
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
        Intent intent = new Intent();
        intent.putExtra("budget", budget);
        Gson gson = new Gson();
        String list = gson.toJson(budgetArrayList);
        intent.putExtra("budgetArrayList", list);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init() {
        back = findViewById(R.id.back);
        editBudgetAmount = findViewById(R.id.edit_budget_amount);
        editBudgetSlider = findViewById(R.id.edit_budget_slider);
        editBudget = findViewById(R.id.edit_budget_continue);

        editBudgetCategoryName = findViewById(R.id.edit_budget_category_name);
        editBudgetCategory = findViewById(R.id.edit_budget_category);
    }
}