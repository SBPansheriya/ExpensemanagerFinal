package com.kmsoft.expensemanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Fragment.TransactionFragment;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int CHILD_TYPE = 1;

    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
    TransactionFragment transactionFragment;

    public ShowTransactionAdapter(TransactionFragment transactionFragment, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
        this.transactionFragment = transactionFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == HEADER_TYPE) {
            view = inflater.inflate(R.layout.item_header_layout, parent, false);
            return new HeaderViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_recent_transaction_layout, parent, false);
            return new ChildViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3 == 0 ? HEADER_TYPE : CHILD_TYPE;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == HEADER_TYPE) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            IncomeAndExpense incomeAndExpense = incomeAndExpenseArrayList.get(position);
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(currentDate);

            if (incomeAndExpense.getCurrantDate().equals(formattedDate)){
                headerViewHolder.headerTextView.setText("Today");
            } else {
                headerViewHolder.headerTextView.setText(incomeAndExpense.getDate());
            }
        } else {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            IncomeAndExpense incomeAndExpense = incomeAndExpenseArrayList.get(position);
            childViewHolder.itemName.setText(incomeAndExpense.getCategoryName());
            childViewHolder.itemDescription.setText(incomeAndExpense.getDescription());
            childViewHolder.itemDate.setText(incomeAndExpense.getTime());

            if (incomeAndExpense.getCategoryImage() == 0){
                childViewHolder.itemImage.setImageResource(R.drawable.i);
            }
            else {
                childViewHolder.itemImage.setImageResource(incomeAndExpense.getCategoryImage());
            }

            if (incomeAndExpense.getTag().equals("Income")){
                childViewHolder.itemAmount.setText("+" + incomeAndExpense.getAmount());
                childViewHolder.itemAmount.setTextColor(transactionFragment.getResources().getColor(R.color.green));
            } else if (incomeAndExpense.getTag().equals("Expense")) {
                childViewHolder.itemAmount.setText("-" + incomeAndExpense.getAmount());
                childViewHolder.itemAmount.setTextColor(transactionFragment.getResources().getColor(R.color.red));
            }
        }
    }

    @Override
    public int getItemCount() {
        return incomeAndExpenseArrayList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.header_textView);
        }
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        RelativeLayout relative;
        TextView itemName,itemDescription,itemAmount,itemDate;

        ChildViewHolder(View itemView) {
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