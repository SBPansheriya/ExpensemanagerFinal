package com.kmsoft.expensemanager.Adapter;

import static com.kmsoft.expensemanager.Constant.categoryArrayList;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.PieEntry;
import com.kmsoft.expensemanager.Activity.Transaction.FinancialReportActivity;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class LegendAdapter extends RecyclerView.Adapter<LegendAdapter.ViewHolder> {
    FinancialReportActivity financialReportActivity;
    ArrayList<PieEntry> chartData;
    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;

    public LegendAdapter(FinancialReportActivity financialReportActivity, ArrayList<PieEntry> chartData, ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        this.financialReportActivity = financialReportActivity;
        this.chartData = chartData;
        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
    }

    @NonNull
    @Override
    public LegendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.legend_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LegendAdapter.ViewHolder holder, int position) {
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < incomeAndExpenseArrayList.size(); i++) {
            for (int j = 0; j < chartData.size(); j++) {
                if (incomeAndExpenseArrayList.get(i).getCategoryName().equals(chartData.get(j).getLabel())) {
                    colors.add(incomeAndExpenseArrayList.get(i).getCategoryColor());
                    break;
                }
            }
        }
        holder.legendBox.setBackgroundColor(colors.get(position));

        holder.legendTitle.setText(chartData.get(position).getLabel());

    }

    @Override
    public int getItemCount() {
        return chartData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View legendBox;
        TextView legendTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            legendBox = itemView.findViewById(R.id.legendBox);
            legendTitle = itemView.findViewById(R.id.legendTitle);
        }
    }
}
