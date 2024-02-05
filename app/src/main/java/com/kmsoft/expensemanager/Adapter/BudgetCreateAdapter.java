package com.kmsoft.expensemanager.Adapter;

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
import com.kmsoft.expensemanager.Activity.Budget.DetailsBudgetActivity;
import com.kmsoft.expensemanager.Fragment.BudgetFragment;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class BudgetCreateAdapter extends RecyclerView.Adapter<BudgetCreateAdapter.ViewHolder> {

    BudgetFragment budgetFragment;
    ArrayList<Budget> budgetArrayList;

    public BudgetCreateAdapter(BudgetFragment budgetFragment, ArrayList<Budget> budgetArrayList) {
        this.budgetFragment = budgetFragment;
        this.budgetArrayList = budgetArrayList;
    }

    @NonNull
    @Override
    public BudgetCreateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetCreateAdapter.ViewHolder holder, int position) {
        Budget budget = budgetArrayList.get(position);
        holder.setCategory.setText(budget.getCategoryNameBudget());
        holder.setRemainingAmount.setText("Remaining" + budget.getAmountBudget());
        holder.setAmount.setText("₹1200 of ₹1000");
        holder.setSlider.setValue(budget.getPercentageBudget());
        holder.setSlider.setEnabled(false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(budgetFragment.getContext(), DetailsBudgetActivity.class);
                intent.putExtra("budget",budgetArrayList.get(position));
                budgetFragment.getContext().startActivity(intent);
            }
        });

        if (position == budgetArrayList.size()) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 350);
            holder.reletive.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return budgetArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView setWarning;
        Slider setSlider;
        TextView setCategory,setRemainingAmount,setAmount,setExceedAmount;
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
        }
    }
}
