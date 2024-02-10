package com.kmsoft.expensemanager.Adapter;

import android.content.Context;
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
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
    String selected;

    public HomeAdapter(Context context, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList,String selected) {
        this.context = context;
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
        this.selected = selected;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_transaction_layout,parent,false);
        return new HomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        IncomeAndExpense incomeAndExpense = incomeAndExpenseArrayList.get(position);
        holder.itemName.setText(incomeAndExpense.getCategoryName());
        holder.itemDescription.setText(incomeAndExpense.getDescription());
        holder.itemDate.setText(incomeAndExpense.getTime());

        if (incomeAndExpense.getCategoryImage() == 0){
            holder.itemImage.setImageResource(R.drawable.i);
        }
        else {
            holder.itemImage.setImageResource(incomeAndExpense.getCategoryImage());
        }

        if (selected.equals("Income")){
            holder.itemAmount.setText("+" + incomeAndExpense.getAmount());
            holder.itemAmount.setTextColor(context.getResources().getColor(R.color.green));
        } else if (selected.equals("Expense")) {
            holder.itemAmount.setText("-" + incomeAndExpense.getAmount());
            holder.itemAmount.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsTransactionActivity.class);
                intent.putExtra("incomeAndExpense",incomeAndExpenseArrayList.get(position));
                context.startActivity(intent);
            }
        });

//        if (position == 9) {
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0, 20, 0, 170);
//            holder.relative.setLayoutParams(layoutParams);
//        }
    }

    @Override
    public int getItemCount() {
        return Math.min(incomeAndExpenseArrayList.size(), 10);
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
