package com.kmsoft.expensemanager.Activity.Budget;

import static com.kmsoft.expensemanager.Activity.MainActivity.currencySymbol;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView showCategoryNameBudget, amountBudget;
    ImageView showExceedAmount, back, delete, showCategoryImageBudget;
    Slider showPercentage;
    Button showEditBudget;
    Budget budget;
    ActivityResultLauncher<Intent> launchSomeActivity;
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();
    Gson gson;
    ArrayList<Budget> budgetArrayList;
    String remainingAmount;
    String finalAmount;
    String name;
    double remainingFinalAmount;
    int exceedAmount;
    double num1;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(DetailsBudgetActivity.this);

        budget = (Budget) getIntent().getSerializableExtra("budget");
        remainingAmount = getIntent().getStringExtra("remainingFinalAmount");
        finalAmount = getIntent().getStringExtra("finalAmount");
        String listGet = getIntent().getStringExtra("budgetArrayList");
        gson = new Gson();
        budgetArrayList = gson.fromJson(listGet, new TypeToken<ArrayList<Budget>>() {}.getType());

        setData();

        // exceedAmount set
        if (finalAmount != null && !finalAmount.isEmpty()) {
            exceedAmount = Integer.parseInt(finalAmount);
        } else {
            exceedAmount = 0;
        }
        String valueTo = extractNumericPart(budget.getAmountBudget());
        num1 = Double.parseDouble(valueTo);
        if (exceedAmount >= num1) {
            showPercentage.setValueTo((float) num1);
            showPercentage.setValueFrom(0);
            showPercentage.setValue((float) num1);
            showExceedAmount.setVisibility(View.VISIBLE);
        } else {
            if (exceedAmount < 0) {
                showPercentage.setValueTo(Float.parseFloat(valueTo));
                showPercentage.setValueFrom(exceedAmount);
                showPercentage.setValue(exceedAmount);
                showExceedAmount.setVisibility(View.GONE);
            } else {
                showPercentage.setValueTo(Float.parseFloat(valueTo));
                showPercentage.setValueFrom(0);
                showPercentage.setValue(exceedAmount);
                showExceedAmount.setVisibility(View.GONE);
            }
        }
        showPercentage.setEnabled(false);

        // remainingAmount set
        if (remainingAmount != null && !remainingAmount.isEmpty()) {
            remainingFinalAmount = Double.parseDouble(remainingAmount);
        } else {
            remainingFinalAmount = 0;
        }

        if (finalAmount == null){
            amountBudget.setText(String.format(currencySymbol + budget.getAmountBudget()));
        } else {
            DecimalFormat df1 = new DecimalFormat("#.##");
            df1.setRoundingMode(RoundingMode.HALF_UP);
            String reAmount = df1.format(remainingFinalAmount);
            amountBudget.setText(String.format(currencySymbol + reAmount));
        }

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    budget = (Budget) data.getSerializableExtra("budget");
                    String listGet1 = data.getStringExtra("budgetArrayList");
                    gson = new Gson();
                    budgetArrayList = gson.fromJson(listGet1, new TypeToken<ArrayList<Budget>>() {
                    }.getType());

                    if (budget != null) {
                        // exceedAmount set
                        String valueTo1 = extractNumericPart(budget.getAmountBudget());
                        num1 = Double.parseDouble(valueTo1);
                        if (budget.getCategoryNameBudget().equals(name)) {
                            if (exceedAmount >= num1) {
                                showPercentage.setValueTo((float) num1);
                                showPercentage.setValueFrom(0);
                                showPercentage.setValue((float) num1);
                                showExceedAmount.setVisibility(View.VISIBLE);
                            } else {
                                showPercentage.setValueTo(Float.parseFloat(valueTo1));
                                showPercentage.setValueFrom(0);
                                showPercentage.setValue(exceedAmount);
                                showExceedAmount.setVisibility(View.GONE);
                            }

                            // remainingAmount set
                            double result1 = num1 - exceedAmount;
                            DecimalFormat df1 = new DecimalFormat("#.##");
                            df1.setRoundingMode(RoundingMode.HALF_UP);
                            String reAmount = df1.format(result1);
                            amountBudget.setText(String.format(currencySymbol +  reAmount));
                        } else {
                            expenseList = filterCategories(incomeAndExpenseArrayList);
                            HashMap<String, Double> categoryTotalExpenses = new HashMap<>();
                            for (IncomeAndExpense expense : expenseList) {
                                double amount1 = Double.parseDouble(extractNumericPart(expense.getAmount()));
                                double categoryTotal = categoryTotalExpenses.getOrDefault(expense.getCategoryName(), 0.0);
                                categoryTotal += amount1;
                                categoryTotalExpenses.put(expense.getCategoryName(), categoryTotal);
                            }

                            double categoryTotalExpense = 0;
                            double budgetAmount1;
                            for (IncomeAndExpense expense : expenseList) {
                                if (expense.getCategoryName().equals(budget.getCategoryNameBudget())) {
                                    categoryTotalExpense = categoryTotalExpenses.getOrDefault(expense.getCategoryName(), 0.0);
                                }
                            }
                            budgetAmount1 = Double.parseDouble(extractNumericPart(budget.getAmountBudget()));

                            if (categoryTotalExpense >= budgetAmount1) {
                                showPercentage.setValueTo((float) budgetAmount1);
                                showPercentage.setValueFrom(0);
                                showPercentage.setValue((float) budgetAmount1);
                                showExceedAmount.setVisibility(View.VISIBLE);
                            } else {
                                showPercentage.setValueTo(Float.parseFloat(valueTo1));
                                showPercentage.setValueFrom(0);
                                showPercentage.setValue((float) categoryTotalExpense);
                                showExceedAmount.setVisibility(View.GONE);
                            }

                            // remainingAmount set
                            double result1 = budgetAmount1 - categoryTotalExpense;
                            int a = (int) result1;
                            amountBudget.setText(String.format(currencySymbol + a));
                        }
                        setData();
                    }
                }
            }
        });

        back.setOnClickListener(v -> onBackPressed());

        showEditBudget.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsBudgetActivity.this, EditBudgetActivity.class);
            intent.putExtra("budget", budget);
            gson = new Gson();
            String list = gson.toJson(budgetArrayList);
            intent.putExtra("budgetArrayList", list);
            launchSomeActivity.launch(intent);
        });

        delete.setOnClickListener(v -> showDeleteBottomDialog());
    }

    private ArrayList<IncomeAndExpense> filterCategories
            (ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            if (incomeAndExpense.getTag().equals("Expense")) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    private void setData() {

        name = budget.getCategoryNameBudget();
        showCategoryNameBudget.setText(budget.getCategoryNameBudget());
        if (budget.getCategoryImageBudget() == 0) {
            showCategoryImageBudget.setImageResource(R.drawable.i);
        } else {
            showCategoryImageBudget.setImageResource(budget.getCategoryImageBudget());
        }
        showPercentage.setEnabled(false);
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    private void showDeleteBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailsBudgetActivity.this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.delete_bottomsheet_layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        TextView no = bottomSheetDialog.findViewById(R.id.no);
        TextView yes = bottomSheetDialog.findViewById(R.id.yes);
        TextView txt = bottomSheetDialog.findViewById(R.id.txt);
        TextView txt1 = bottomSheetDialog.findViewById(R.id.txt1);

        txt.setText(R.string.remove_this_budget);
        txt1.setText(R.string.are_you_sure_you_want_to_remove_this_budget);

        no.setOnClickListener(v -> bottomSheetDialog.dismiss());

        yes.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();

            dbHelper.deleteBudgetData(budget.getId());

            Dialog dialog = new Dialog(DetailsBudgetActivity.this);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.dailog_removed_layout);
            dialog.setCancelable(false);
            dialog.show();

            TextView txt2 = dialog.findViewById(R.id.txt);
            txt2.setText(R.string.budget_has_been_successfully_removed);

            new Handler().postDelayed(() -> {
                if (dialog.isShowing()) {
                    onBackPressed();
                    dialog.dismiss();
                }
            }, 1000);
        });
    }

    private void init() {
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        showEditBudget = findViewById(R.id.show_edit_budget);
        showExceedAmount = findViewById(R.id.show_exceed_amount);
        showCategoryNameBudget = findViewById(R.id.show_category_name_budget);
        amountBudget = findViewById(R.id.amount_budget);
        showCategoryImageBudget = findViewById(R.id.show_category_image_budget);
        showPercentage = findViewById(R.id.set_slider_budget);
    }
}