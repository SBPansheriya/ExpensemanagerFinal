package com.kmsoft.expensemanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Activity.Profile.NotificationActivity;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

public class ExceedBudgetAdapter extends RecyclerView.Adapter<ExceedBudgetAdapter.ViewHolder> {

    NotificationActivity notificationActivity;

    public ExceedBudgetAdapter(NotificationActivity notificationActivity) {
        this.notificationActivity = notificationActivity;
    }

    @NonNull
    @Override
    public ExceedBudgetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_transaction_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExceedBudgetAdapter.ViewHolder holder, int position) {
//        holder.itemName.setText(budget.getCategoryNameBudget());
//        holder.itemAmount.setText(budget.getAmountBudget());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName,itemDescription,itemAmount,itemDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemAmount = itemView.findViewById(R.id.item_amount);
            itemDate = itemView.findViewById(R.id.item_date);
        }
    }
}
