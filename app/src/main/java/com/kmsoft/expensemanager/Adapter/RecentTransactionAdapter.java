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

import com.kmsoft.expensemanager.Activity.Trancation.DetailsTransactionActivity;
import com.kmsoft.expensemanager.Fragment.HomeFragment;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class RecentTransactionAdapter extends RecyclerView.Adapter<RecentTransactionAdapter.ViewHolder> {

    HomeFragment homeFragment;
    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;

    public RecentTransactionAdapter(HomeFragment homeFragment, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        this.homeFragment = homeFragment;
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
    }

    @NonNull
    @Override
    public RecentTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_transaction_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTransactionAdapter.ViewHolder holder, int position) {
        IncomeAndExpense incomeAndExpense = incomeAndExpenseArrayList.get(position);
        holder.itemName.setText(incomeAndExpense.getCategoryName());
        holder.itemDescription.setText(incomeAndExpense.getDescription());
        holder.itemAmount.setText(incomeAndExpense.getAmount());
        holder.itemDate.setText(incomeAndExpense.getDate());

        if (incomeAndExpense.getCategoryImage() == 0){
            holder.itemImage.setImageResource(R.drawable.i);
        }
        else {
            holder.itemImage.setImageResource(incomeAndExpense.getCategoryImage());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeFragment.getContext(), DetailsTransactionActivity.class);
                intent.putExtra("incomeAndExpense",incomeAndExpenseArrayList.get(position));
                homeFragment.getContext().startActivity(intent);
            }
        });

        if (position == incomeAndExpenseArrayList.size()-1) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 170);
            holder.relative.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return incomeAndExpenseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        RelativeLayout relative;
        TextView itemName,itemDescription,itemAmount,itemDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemAmount = itemView.findViewById(R.id.item_amount);
            itemDate = itemView.findViewById(R.id.item_date);
            relative = itemView.findViewById(R.id.relative);
        }
    }
}
