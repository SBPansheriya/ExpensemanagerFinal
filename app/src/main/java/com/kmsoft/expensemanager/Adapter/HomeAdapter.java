package com.kmsoft.expensemanager.Adapter;

import static com.kmsoft.expensemanager.Activity.MainActivity.currencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
    String selected;

    public HomeAdapter(Context context, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList, String selected) {
        this.context = context;
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
        this.selected = selected;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_transaction_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        IncomeAndExpense incomeAndExpense = incomeAndExpenseArrayList.get(position);
        holder.itemName.setText(incomeAndExpense.getCategoryName());
        holder.itemDescription.setText(incomeAndExpense.getDescription());
        holder.itemDate.setText(incomeAndExpense.getTime());

        if (incomeAndExpense.getCategoryImage() == 0) {
            holder.itemImage.setImageResource(R.drawable.i);
        } else {
            holder.itemImage.setImageResource(incomeAndExpense.getCategoryImage());
        }

        if (incomeAndExpense.getTag().equals("Income")) {
            holder.itemAmount.setText(String.format("+" + currencySymbol + incomeAndExpense.getAmount()));
            holder.itemAmount.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (incomeAndExpense.getTag().equals("Expense")) {
            holder.itemAmount.setText(String.format("-" + currencySymbol + incomeAndExpense.getAmount()));
            holder.itemAmount.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(incomeAndExpenseArrayList.size(), 10);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        RelativeLayout relative;
        TextView itemName, itemDescription, itemAmount, itemDate;

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
