package com.kmsoft.expensemanager.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.Model.ListDateModel;
import com.kmsoft.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MainViewHolder> {

    public Context context;
    public ArrayList<ListDateModel> listDateModels;
    String sortBy;

    public DateAdapter(Context context, ArrayList<ListDateModel> listDateModels,String sortBy) {
        this.context = context;
        this.listDateModels = listDateModels;
        this.sortBy = sortBy;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show_transaction_layout, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        ListDateModel listDateModel = listDateModels.get(position);
        int numberOfDays = 1;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1 * numberOfDays);
        Date yesterdayDate = calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String yesterday = sdf.format(yesterdayDate);

        Date currentDate = new Date();
        String today = sdf.format(currentDate);

        if (listDateModel.incomeAndExpenseArrayList.size() > 0) {
            if (listDateModel.incomeAndExpenseArrayList.get(0).getDate().equals(today)) {
                holder.date_text.setText(R.string.today);
            } else if (listDateModel.incomeAndExpenseArrayList.get(0).getDate().equals(yesterday)) {
                holder.date_text.setText(R.string.yesterday);
            } else {
                holder.date_text.setText(listDateModel.incomeAndExpenseArrayList.get(0).getDate());
            }

            if (sortBy.equals("Highest")) {
                listDateModel.incomeAndExpenseArrayList.sort(Comparator.comparingDouble(IncomeAndExpense::getAmountValue).reversed());
            } else if (sortBy.equals("Lowest")) {
                listDateModel.incomeAndExpenseArrayList.sort(Comparator.comparingDouble(IncomeAndExpense::getAmountValue));
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            ShowTransactionAdapter transactionAdapter = new ShowTransactionAdapter(context, listDateModel.incomeAndExpenseArrayList);
            holder.data_recycler.setLayoutManager(layoutManager);
            holder.data_recycler.setAdapter(transactionAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return listDateModels.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView date_text;
        RecyclerView data_recycler;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            date_text = itemView.findViewById(R.id.header_textView);
            data_recycler = itemView.findViewById(R.id.show_data_recyclerview);
        }
    }
}
