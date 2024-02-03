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
import com.kmsoft.expensemanager.R;

public class BudgetCreateAdapter extends RecyclerView.Adapter<BudgetCreateAdapter.ViewHolder> {

    BudgetFragment budgetFragment;

    public BudgetCreateAdapter(BudgetFragment budgetFragment) {
        this.budgetFragment = budgetFragment;
    }

    @NonNull
    @Override
    public BudgetCreateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetCreateAdapter.ViewHolder holder, int position) {
        holder.setCategory.setText("Shopping");
        holder.setRemainingAmount.setText("Remaining $1000");
        holder.setAmount.setText("₹1200 of ₹1000");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(budgetFragment.getContext(), DetailsBudgetActivity.class);
                budgetFragment.getContext().startActivity(intent);
            }
        });

        if (position == 4) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 350);
            holder.reletive.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
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
