package com.kmsoft.expensemanager.Adapter;

import android.graphics.Color;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_legend_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LegendAdapter.ViewHolder holder, int position) {
        ArrayList<Integer> colors = new ArrayList<>();
        Map<String, Integer> categoryColorMap = new HashMap<>();

        HashSet<String> uniqueCategories = new HashSet<>();

        for (IncomeAndExpense item : incomeAndExpenseArrayList) {
            categoryColorMap.put(item.getCategoryName(), item.getCategoryColor());
        }

        for (PieEntry entry : chartData) {
            String categoryName = entry.getLabel();
            Integer color = categoryColorMap.get(categoryName);
            if (color != null) {
                colors.add(color.intValue());
            } else {
                colors.add(Color.rgb(112, 244, 251));
            }
        }
//        for (int i = 0; i < incomeAndExpenseArrayList.size(); i++) {
//            String categoryName = incomeAndExpenseArrayList.get(i).getCategoryName();
//            int color = categoryColorMap.get(categoryName);
//
//            if (!uniqueCategories.contains(categoryName)) {
//                for (int j = 0; j < chartData.size(); j++) {
//                    if (categoryName.equals(chartData.get(j).getLabel())) {
//                        colors.add(color);
////                        colors.add(incomeAndExpenseArrayList.get(i).getCategoryColor());
//                        uniqueCategories.add(categoryName);
//                        break;
//                    }
//                }
//            }
//        }
        holder.legendBox.setBackgroundColor(colors.get(position));

        holder.legendTitle.setText(chartData.get(position).getLabel());

    }

    @Override
    public int getItemCount() {
        return chartData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View legendBox;
        TextView legendTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            legendBox = itemView.findViewById(R.id.legendBox);
            legendTitle = itemView.findViewById(R.id.legendTitle);
        }
    }
}
