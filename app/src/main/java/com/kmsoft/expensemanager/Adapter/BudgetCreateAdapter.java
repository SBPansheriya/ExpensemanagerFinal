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
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class BudgetCreateAdapter extends RecyclerView.Adapter<BudgetCreateAdapter.ViewHolder> {

    BudgetFragment budgetFragment;
    ArrayList<Budget> budgetArrayList;
    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
    String remainingFinalAmount;
    String finalAmount;
    Map<Integer, String> remainingFinalAmountMap = new HashMap<>();
    Map<Integer, String> finalAmountMap = new HashMap<>();

    public BudgetCreateAdapter(BudgetFragment budgetFragment, ArrayList<Budget> budgetArrayList, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        this.budgetFragment = budgetFragment;
        this.budgetArrayList = budgetArrayList;
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
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

        double total = 0;
        for (int i = 0; i < incomeAndExpenseArrayList.size(); i++) {
            String categoryName = incomeAndExpenseArrayList.get(i).getCategoryName();
            if (budget.getCategoryNameBudget().equalsIgnoreCase(categoryName)) {
                String tag = incomeAndExpenseArrayList.get(i).getTag();
                String amountString = incomeAndExpenseArrayList.get(i).getAmount();
                String numericAmount = extractNumericPart(amountString);
                double amount = Double.parseDouble(numericAmount);

                if (tag.equals("Expense")) {
                    total += amount;
                }
                double budgetAmountValue = Double.parseDouble(extractNumericPart(budget.getAmountBudget()));

                DecimalFormat df = new DecimalFormat("#");
                finalAmount = df.format(total);

                double remainingAmount = budgetAmountValue - total;
                remainingFinalAmount = df.format(remainingAmount);
                holder.setRemainingAmount.setText("Remaining " + "₹" + remainingFinalAmount);

                remainingFinalAmountMap.put(position, remainingFinalAmount);
                finalAmountMap.put(position, finalAmount);

                int total1 = (int) total;
                holder.setIncomeExpenseAmount.setText("₹" + total1);

                if (total1 >= budgetAmountValue) {
                    holder.setSlider.setValueFrom(0);
                    holder.setSlider.setValueTo((float) budgetAmountValue);
                    holder.setSlider.setValue((float) budgetAmountValue);
                    holder.setWarning.setVisibility(View.VISIBLE);
                    holder.setExceedAmount.setVisibility(View.VISIBLE);
                } else {
                    if (total1 < 0) {
                        holder.setSlider.setValueFrom((float) total);
                        holder.setSlider.setValueTo((float) budgetAmountValue);
                        holder.setSlider.setValue((float) total);
                        break;
                    } else {
                        holder.setSlider.setValueFrom(0);
                        holder.setSlider.setValueTo((float) budgetAmountValue);
                        holder.setSlider.setValue((float) total);
                    }
                    holder.setWarning.setVisibility(View.GONE);
                    holder.setExceedAmount.setVisibility(View.GONE);
                }
            }
        }
        holder.setSlider.setEnabled(false);

        holder.setAmount.setText(" of " + budget.getAmountBudget());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clickedRemainingFinalAmount = remainingFinalAmountMap.get(position);
                String clickedFinalAmount = finalAmountMap.get(position);
                Intent intent = new Intent(budgetFragment.getContext(), DetailsBudgetActivity.class);
                intent.putExtra("budget", budgetArrayList.get(position));
                intent.putExtra("remainingFinalAmount", clickedRemainingFinalAmount);
                intent.putExtra("finalAmount", clickedFinalAmount);
                Gson gson = new Gson();
                String list = gson.toJson(budgetArrayList);
                intent.putExtra("budgetArrayList", list);
                budgetFragment.getContext().startActivity(intent);
            }
        });

        if (position == budgetArrayList.size() - 1) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 330);
            holder.reletive.setLayoutParams(layoutParams);
        }
    }

    public void updateData(ArrayList<Budget> budgetList, ArrayList<IncomeAndExpense> incomeAndExpenseList) {
        this.budgetArrayList = budgetList;
        this.incomeAndExpenseArrayList = incomeAndExpenseList;
        notifyDataSetChanged();
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