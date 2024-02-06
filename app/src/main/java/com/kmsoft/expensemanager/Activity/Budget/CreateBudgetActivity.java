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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
import com.kmsoft.expensemanager.Activity.FloatingButton.IncomeActivity;
=======
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class CreateBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
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
    Gson gson;
=======
>>>>>>> Stashed changes
    Spinner createSpinner;
    Switch createSwitch;
    Slider createSlider;
    EditText createBudgetAmount;
    TextView createBudgetContinue;
    ImageView back;
    Budget budget;

<<<<<<< Updated upstream
=======
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        init();

        dbHelper = new DBHelper(CreateBudgetActivity.this);
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        String listGet = getIntent().getStringExtra("budgetArrayList");
        gson = new Gson();
        budgetArrayList = gson.fromJson(listGet, new TypeToken<ArrayList<Budget>>() {}.getType());

=======
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

<<<<<<< Updated upstream
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
=======
<<<<<<< HEAD
        launchSomeActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    imageResId = data.getIntExtra("categoryImage", 0);
                    categoryName = data.getStringExtra("categoryName");
                    if (!TextUtils.isEmpty(categoryName)) {
                        budgetCategoryName.setText("" + categoryName);
                    }
                }
            }
        });

        createBudgetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.startsWith("₹")) {
                    createBudgetAmount.setText("₹" + input);
                    createBudgetAmount.setSelection(createBudgetAmount.getText().length());
                }
            }
        });

        createBudgetContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetAmount = createBudgetAmount.getText().toString();
                int percentageBudget = (int) createSlider.getValue();
                if (budgetAmount.equals("₹0") || budgetAmount.equals("₹")) {
                    Toast.makeText(CreateBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(budgetAmount)){
                    Toast.makeText(CreateBudgetActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(categoryName)) {
                    Toast.makeText(CreateBudgetActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
                } else {
                    boolean categoryFound = false;

                    if (budgetArrayList.size() != 0) {
                        for (int i = 0; i < budgetArrayList.size(); i++) {
                            if (budgetArrayList.get(i).getCategoryNameBudget().equalsIgnoreCase(categoryName)) {
                                budget = new Budget(budgetArrayList.get(i).getId(), budgetAmount, categoryName, imageResId, percentageBudget);
                                dbHelper.updateBudgetData(budget);
                                categoryFound = true;
                                break;
                            }
                        }
                    }

                    if (!categoryFound) {
                        budget = new Budget(0, budgetAmount, categoryName, imageResId, percentageBudget);
                        dbHelper.insertBudgetData(budget);
                    }
                    budgetArrayList.add(budget);
                    onBackPressed();
                }
            }
        });

        budgetCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateBudgetActivity.this, AddCategoryActivity.class);
                intent.putExtra("clicked", "Income");
                launchSomeActivityResult.launch(intent);
            }
        });
=======
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
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
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