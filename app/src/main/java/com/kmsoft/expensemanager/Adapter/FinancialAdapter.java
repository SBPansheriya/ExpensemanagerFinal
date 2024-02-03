package com.kmsoft.expensemanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Activity.Trancation.FinancialReportActivity;
import com.kmsoft.expensemanager.R;

public class FinancialAdapter extends RecyclerView.Adapter<FinancialAdapter.ViewHolder> {

    FinancialReportActivity financialReportActivity;

    public FinancialAdapter(FinancialReportActivity financialReportActivity) {
        this.financialReportActivity = financialReportActivity;
    }

    @NonNull
    @Override
    public FinancialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_transaction_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancialAdapter.ViewHolder holder, int position) {
        holder.itemName.setText("Subscription");
        holder.itemDescription.setText("Disney+ Annual..");
        holder.itemAmount.setText("- ₹80");
        holder.itemDate.setText("03:30 PM");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemDescription, itemAmount, itemDate;

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
