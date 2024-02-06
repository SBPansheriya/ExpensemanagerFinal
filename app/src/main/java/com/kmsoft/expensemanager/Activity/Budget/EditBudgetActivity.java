package com.kmsoft.expensemanager.Activity.Budget;

import static com.kmsoft.expensemanager.Constant.budgetArrayList;

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
=======
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
import android.widget.TextView;

import com.google.android.material.slider.Slider;
=======
<<<<<<< HEAD
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
=======
import android.widget.TextView;

import com.google.android.material.slider.Slider;
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class EditBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText editBudgetAmount;
<<<<<<< Updated upstream
    ImageView back;
    Slider editBudgetSlider;
    Button editBudget;
    Budget budget;
=======
<<<<<<< HEAD
    LinearLayout editBudgetCategory;
    TextView editBudgetCategoryName;
    ImageView back;
    Slider editBudgetSlider;
    Button editBudget;
    ActivityResultLauncher<Intent> launchSomeActivityResult;
    Budget budget;
    int imageResId;
    String categoryName;
    Gson gson;
=======
    ImageView back;
    Slider editBudgetSlider;
    Button editBudget;
    Budget budget;
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream

        editBudgetAmount.setText(""+budget.getAmountBudget());
        editBudgetSlider.setValue(budget.getPercentageBudget());
=======
<<<<<<< HEAD
        String listGet = getIntent().getStringExtra("budgetArrayList");
        gson = new Gson();
        budgetArrayList = gson.fromJson(listGet, new TypeToken<ArrayList<Budget>>() {}.getType());

        editBudgetAmount.setText("" + budget.getAmountBudget());
        editBudgetCategoryName.setText(""+budget.getCategoryNameBudget());

        launchSomeActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    imageResId = data.getIntExtra("categoryImage", 0);
                    categoryName = data.getStringExtra("categoryName");
                    if (!TextUtils.isEmpty(categoryName)) {
                        editBudgetCategoryName.setText("" + categoryName);
                    }
                }
            }
        });
=======

        editBudgetAmount.setText(""+budget.getAmountBudget());
        editBudgetSlider.setValue(budget.getPercentageBudget());
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        editBudgetCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBudgetActivity.this, AddCategoryActivity.class);
                intent.putExtra("clicked", "Income");
                launchSomeActivityResult.launch(intent);
            }
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
                if (!input.startsWith("₹")) {
                    editBudgetAmount.setText("₹" + input);
                    editBudgetAmount.setSelection(editBudgetAmount.getText().length());
                }
            }
        });

=======
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
        editBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = editBudgetAmount.getText().toString();
                int percentage = (int) editBudgetSlider.getValue();
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
                if (TextUtils.isEmpty(categoryName)) {
                    categoryName = budget.getCategoryNameBudget();
                }
                if (amount.equals("₹0") || amount.equals("₹")) {
                    Toast.makeText(EditBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(EditBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(categoryName)) {
                    Toast.makeText(EditBudgetActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
                } else {

                    boolean categoryFound = false;
                    int existingId = budget.getId();
                    String existingCategoryName = budget.getCategoryNameBudget();

                    for (int i = 0; i < budgetArrayList.size(); i++) {
                        if (budgetArrayList.get(i).getCategoryNameBudget().equalsIgnoreCase(categoryName)){
                            if (!existingCategoryName.equalsIgnoreCase(budgetArrayList.get(i).getCategoryNameBudget())) {
                                budget = new Budget(budgetArrayList.get(i).getId(), amount, categoryName, imageResId, percentage);
                                dbHelper.updateBudgetData(budget);
                                dbHelper.deleteBudgetData(existingId);
                            } else {
                                budget = new Budget(existingId, amount, categoryName, imageResId, percentage);
                                dbHelper.updateBudgetData(budget);
                            }
                            categoryFound = true;
                            break;
                        }
                    }

                    if (!categoryFound) {
                        budget = new Budget(budget.getId(), amount, categoryName, imageResId, percentage);
                        dbHelper.updateBudgetData(budget);
                    }
                    budgetArrayList.add(budget);
                    onBackPressed();
                }
            }
        });
=======
>>>>>>> Stashed changes
                budget = new Budget(budget.getId(), amount,budget.getCategoryNameBudget(),budget.getCategoryImageBudget(),percentage);
                budgetArrayList.add(budget);
                dbHelper.updateBudgetData(budget);
                onBackPressed();
            }
        });
<<<<<<< Updated upstream
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("budget",budget);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
=======
>>>>>>> Stashed changes
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("budget",budget);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
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
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        editBudgetCategoryName = findViewById(R.id.edit_budget_category_name);
        editBudgetCategory = findViewById(R.id.edit_budget_category);
=======
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
    }
}