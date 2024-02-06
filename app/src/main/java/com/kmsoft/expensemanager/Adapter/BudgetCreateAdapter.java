package com.kmsoft.expensemanager.Adapter;

import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;
import com.google.gson.Gson;
import com.kmsoft.expensemanager.Activity.Budget.DetailsBudgetActivity;
import com.kmsoft.expensemanager.Fragment.BudgetFragment;
import com.kmsoft.expensemanager.Model.Budget;
<<<<<<< Updated upstream
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;
=======
<<<<<<< HEAD
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
=======
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes

public class BudgetCreateAdapter extends RecyclerView.Adapter<BudgetCreateAdapter.ViewHolder> {

    BudgetFragment budgetFragment;
    ArrayList<Budget> budgetArrayList;
<<<<<<< Updated upstream

    public BudgetCreateAdapter(BudgetFragment budgetFragment, ArrayList<Budget> budgetArrayList) {
        this.budgetFragment = budgetFragment;
        this.budgetArrayList = budgetArrayList;
=======
<<<<<<< HEAD
    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
    String remainingFinalAmount;
    String finalAmount;
    Map<Integer, String> remainingFinalAmountMap = new HashMap<>();
    Map<Integer, String> finalAmountMap = new HashMap<>();

    public BudgetCreateAdapter(BudgetFragment budgetFragment, ArrayList<Budget> budgetArrayList, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        this.budgetFragment = budgetFragment;
        this.budgetArrayList = budgetArrayList;
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
=======

    public BudgetCreateAdapter(BudgetFragment budgetFragment, ArrayList<Budget> budgetArrayList) {
        this.budgetFragment = budgetFragment;
        this.budgetArrayList = budgetArrayList;
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
    }

    @NonNull
    @Override
    public BudgetCreateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetCreateAdapter.ViewHolder holder, int position) {
        Budget budget = budgetArrayList.get(position);
        holder.setCategory.setText(budget.getCategoryNameBudget());
<<<<<<< Updated upstream
        holder.setRemainingAmount.setText("Remaining" + budget.getAmountBudget());
        holder.setAmount.setText("₹1200 of ₹1000");
        holder.setSlider.setValue(budget.getPercentageBudget());
=======
<<<<<<< HEAD
        double total = 0;
        for (int i = 0; i < incomeAndExpenseArrayList.size(); i++) {
            String categoryName = incomeAndExpenseArrayList.get(i).getCategoryName();
            if (budget.getCategoryNameBudget().equalsIgnoreCase(categoryName)) {
                String tag = incomeAndExpenseArrayList.get(i).getTag();
                String amountString = incomeAndExpenseArrayList.get(i).getAmount();
                String numericAmount = extractNumericPart(amountString);
                double amount = Double.parseDouble(numericAmount);

                if (tag.equals("Income")) {
                    total += amount;
                } else if (tag.equals("Expense")) {
                    total -= amount;
                }
                double budgetAmountValue = Double.parseDouble(extractNumericPart(budget.getAmountBudget()));
                String onlyExceedAmount = extractNumericPart(String.valueOf(total));
                double totalAmountValue = Double.parseDouble(onlyExceedAmount);

                DecimalFormat df = new DecimalFormat("#");
                String value = extractNumericPart(String.valueOf(total));
                double value1 = Double.parseDouble(value);
                finalAmount = df.format(value1);

                holder.setIncomeExpenseAmount.setText("₹" + finalAmount);

                double remainingAmount = budgetAmountValue - value1;
                remainingFinalAmount = df.format(remainingAmount);
                holder.setRemainingAmount.setText("Remaining " + "₹" + remainingFinalAmount);

                if (totalAmountValue >= budgetAmountValue) {
                    holder.setSlider.setValueFrom(0);
                    holder.setSlider.setValueTo((float) budgetAmountValue);
                    holder.setSlider.setValue((float) budgetAmountValue);
                    holder.setWarning.setVisibility(View.VISIBLE);
                    holder.setExceedAmount.setVisibility(View.VISIBLE);
                } else {
                    holder.setSlider.setValueFrom(0);
                    holder.setSlider.setValueTo((float) budgetAmountValue);
                    holder.setSlider.setValue((float) value1);
                    holder.setWarning.setVisibility(View.GONE);
                    holder.setExceedAmount.setVisibility(View.GONE);
                }

                remainingFinalAmountMap.put(position, remainingFinalAmount);
                finalAmountMap.put(position, finalAmount);
            }
        }

//        ArrayList<String> value = new ArrayList<>();
//
//        for (int i = 0; i < budgetArrayList.size(); i++) {
//
//        }

        holder.setAmount.setText(" of " + budget.getAmountBudget());
=======
        holder.setRemainingAmount.setText("Remaining" + budget.getAmountBudget());
        holder.setAmount.setText("₹1200 of ₹1000");
        holder.setSlider.setValue(budget.getPercentageBudget());
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
        holder.setSlider.setEnabled(false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clickedRemainingFinalAmount = remainingFinalAmountMap.get(position);
                String clickedFinalAmount = finalAmountMap.get(position);
                Intent intent = new Intent(budgetFragment.getContext(), DetailsBudgetActivity.class);
<<<<<<< Updated upstream
                intent.putExtra("budget",budgetArrayList.get(position));
=======
<<<<<<< HEAD
                intent.putExtra("budget", budgetArrayList.get(position));
                intent.putExtra("remainingFinalAmount", clickedRemainingFinalAmount);
                intent.putExtra("finalAmount", clickedFinalAmount);
                Gson gson = new Gson();
                String list = gson.toJson(budgetArrayList);
                intent.putExtra("budgetArrayList", list);
=======
                intent.putExtra("budget",budgetArrayList.get(position));
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
                budgetFragment.getContext().startActivity(intent);
            }
        });

<<<<<<< Updated upstream
        if (position == budgetArrayList.size()) {
=======
<<<<<<< HEAD
        if (position == budgetArrayList.size()-1) {
=======
        if (position == budgetArrayList.size()) {
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 330);
            holder.reletive.setLayoutParams(layoutParams);
        }
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    @Override
    public int getItemCount() {
        return budgetArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView setWarning;
        Slider setSlider;
        TextView setCategory, setRemainingAmount, setAmount, setExceedAmount, setIncomeExpenseAmount;
        RelativeLayout reletive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setWarning = itemView.findViewById(R.id.set_warning);
            setSlider = itemView.findViewById(R.id.set_slider);
            setCategory = itemView.findViewById(R.id.set_category);
            setRemainingAmount = itemView.findViewById(R.id.set_remaining_amount);
            setAmount = itemView.findViewById(R.id.set_amount);
            setExceedAmount = itemView.findViewById(R.id.set_exceed_amount);
            reletive = itemView.findViewById(R.id.reletive);
            setIncomeExpenseAmount = itemView.findViewById(R.id.set_income_expense_amount);
        }
    }
}