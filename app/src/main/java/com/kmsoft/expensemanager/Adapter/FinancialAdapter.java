package com.kmsoft.expensemanager.Adapter;

import static com.kmsoft.expensemanager.Activity.SplashActivity.currencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class FinancialAdapter extends RecyclerView.Adapter<FinancialAdapter.ViewHolder> {

    Context context;
    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
    String spinner;
    String selected;
    double amount;

    public FinancialAdapter(Context context, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList, String spinner, String selected) {
        this.context = context;
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
        this.spinner = spinner;
        this.selected = selected;
    }

    @NonNull
    @Override
    public FinancialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_financial_transaction_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancialAdapter.ViewHolder holder, int position) {
        IncomeAndExpense incomeAndExpense = incomeAndExpenseArrayList.get(position);
        if (spinner.equals("Transaction")) {
            holder.itemName.setText(incomeAndExpense.getCategoryName());
            holder.itemDescription.setText(incomeAndExpense.getDescription());
            holder.itemDate.setText(incomeAndExpense.getDate());

            if (selected.equals("Income")) {
                holder.itemAmount.setText(String.format("+" + currencySymbol + incomeAndExpense.getAmount()));
                holder.itemAmount.setTextColor(context.getResources().getColor(R.color.green));
            } else if (selected.equals("Expense")) {
                holder.itemAmount.setText(String.format("-" + currencySymbol + incomeAndExpense.getAmount()));
                holder.itemAmount.setTextColor(context.getResources().getColor(R.color.red));
            }

            if (incomeAndExpense.getCategoryImage() == 0) {
                holder.itemImage.setImageResource(R.drawable.i);
            } else {
                holder.itemImage.setImageResource(incomeAndExpense.getCategoryImage());
            }
            holder.relative1.setVisibility(View.VISIBLE);
            holder.relative2.setVisibility(View.GONE);
        } else if (spinner.equals("Category")) {
            holder.itemCategoryName.setText(incomeAndExpense.getCategoryName());
            if (selected.equals("Income")) {
                holder.itemCategoryAmount.setText(String.format("+" + currencySymbol + incomeAndExpense.getAmount()));
                holder.itemCategoryAmount.setTextColor(context.getResources().getColor(R.color.green));
                double totalIncome = getTotalIncome();
                holder.setSlider.setValueFrom(0);
                holder.setSlider.setValueTo((float) totalIncome);
                String amount = extractNumericPart(incomeAndExpense.getAmount());
                holder.setSlider.setValue(Float.parseFloat(amount));
            } else if (selected.equals("Expense")) {
                holder.itemCategoryAmount.setText(String.format("-" + currencySymbol + incomeAndExpense.getAmount()));
                holder.itemCategoryAmount.setTextColor(context.getResources().getColor(R.color.red));
                double totalExpense = getTotalExpense();
                holder.setSlider.setValueFrom(0);
                holder.setSlider.setValueTo((float) totalExpense);
                String amount = extractNumericPart(incomeAndExpense.getAmount());
                holder.setSlider.setValue(Float.parseFloat(amount));
            }

            holder.setSlider.setEnabled(false);

            holder.relative1.setVisibility(View.GONE);
            holder.relative2.setVisibility(View.VISIBLE);
        }
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    public double getTotalIncome() {
        double totalIncome = 0.0;
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenseArrayList) {
            if (incomeAndExpense.getTag().equals("Income")) {
                amount = Double.parseDouble(extractNumericPart(incomeAndExpense.getAmount()));
                totalIncome += amount;
            }
        }
        return totalIncome;
    }

    public double getTotalExpense() {
        double totalExpense = 0.0;
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenseArrayList) {
            if (incomeAndExpense.getTag().equals("Expense")) {
                amount = Double.parseDouble(extractNumericPart(incomeAndExpense.getAmount()));
                totalExpense += amount;
            }
        }
        return totalExpense;
    }

    @Override
    public int getItemCount() {
        return incomeAndExpenseArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        RelativeLayout relative, relative1, relative2;
        Slider setSlider;
        TextView itemName, itemDescription, itemAmount, itemDate, itemCategoryName, itemCategoryAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemAmount = itemView.findViewById(R.id.item_amount);
            itemDate = itemView.findViewById(R.id.item_date);
            relative = itemView.findViewById(R.id.relative);
            relative1 = itemView.findViewById(R.id.relative1);
            relative2 = itemView.findViewById(R.id.relative2);
            itemCategoryName = itemView.findViewById(R.id.item_category_name);
            itemCategoryAmount = itemView.findViewById(R.id.item_category_amount);
            setSlider = itemView.findViewById(R.id.set_slider);
        }
    }
}
