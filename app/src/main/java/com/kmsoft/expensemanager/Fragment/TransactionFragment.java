package com.kmsoft.expensemanager.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.Activity.Trancation.DetailsTrancationActivity;
import com.kmsoft.expensemanager.Activity.Trancation.FinancialReportActivity;
import com.kmsoft.expensemanager.R;

public class TransactionFragment extends Fragment {

    ImageView calender,filter,img;
    TextView see_financial_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction,container,false);

        init(view);

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalenderBottomDialog();
            }
        });

        see_financial_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), FinancialReportActivity.class);
                startActivity(intent);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterBottomDialog();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), DetailsTrancationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void showCalenderBottomDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calender_bottomsheet_layout);
        dialog.setCancelable(false);
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);
        CalendarView calendarView = dialog.findViewById(R.id.trans_calenderView);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long date = calendarView.getDate();
                dialog.dismiss();
            }
        });
    }

    private void showFilterBottomDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_bottomsheet_layout);
        dialog.show();

        TextView income = dialog.findViewById(R.id.trans_income);
        TextView expense = dialog.findViewById(R.id.trans_expense);
        TextView highest = dialog.findViewById(R.id.trans_highest);
        TextView lowest = dialog.findViewById(R.id.trans_lowest);
        TextView newest = dialog.findViewById(R.id.trans_newest);
        TextView oldest = dialog.findViewById(R.id.trans_oldest);
        TextView reset = dialog.findViewById(R.id.trans_reset);
        Button apply = dialog.findViewById(R.id.trans_apply);

        income.setBackgroundResource(R.drawable.selected_border_background);
        income.setTextColor(getResources().getColor(R.color.green));

        expense.setBackgroundResource(R.drawable.unselected_border_background);
        expense.setTextColor(Color.BLACK);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.setBackgroundResource(R.drawable.selected_border_background);
                income.setTextColor(getResources().getColor(R.color.green));

                expense.setBackgroundResource(R.drawable.unselected_border_background);
                expense.setTextColor(Color.BLACK);

                highest.setBackgroundResource(R.drawable.selected_border_background);
                highest.setTextColor(getResources().getColor(R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                newest.setBackgroundResource(R.drawable.unselected_border_background);
                newest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.setBackgroundResource(R.drawable.selected_border_background);
                income.setTextColor(getResources().getColor(R.color.green));

                expense.setBackgroundResource(R.drawable.unselected_border_background);
                expense.setTextColor(Color.BLACK);
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setBackgroundResource(R.drawable.selected_border_background);
                expense.setTextColor(getResources().getColor(R.color.green));

                income.setBackgroundResource(R.drawable.unselected_border_background);
                income.setTextColor(Color.BLACK);
            }
        });

        highest.setBackgroundResource(R.drawable.selected_border_background);
        highest.setTextColor(getResources().getColor(R.color.green));

        lowest.setBackgroundResource(R.drawable.unselected_border_background);
        lowest.setTextColor(Color.BLACK);

        newest.setBackgroundResource(R.drawable.unselected_border_background);
        newest.setTextColor(Color.BLACK);

        oldest.setBackgroundResource(R.drawable.unselected_border_background);
        oldest.setTextColor(Color.BLACK);

        highest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highest.setBackgroundResource(R.drawable.selected_border_background);
                highest.setTextColor(getResources().getColor(R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                newest.setBackgroundResource(R.drawable.unselected_border_background);
                newest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
            }
        });

        lowest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowest.setBackgroundResource(R.drawable.selected_border_background);
                lowest.setTextColor(getResources().getColor(R.color.green));

                highest.setBackgroundResource(R.drawable.unselected_border_background);
                highest.setTextColor(Color.BLACK);

                newest.setBackgroundResource(R.drawable.unselected_border_background);
                newest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
            }
        });

        newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newest.setBackgroundResource(R.drawable.selected_border_background);
                newest.setTextColor(getResources().getColor(R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                highest.setBackgroundResource(R.drawable.unselected_border_background);
                highest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
            }
        });

        oldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldest.setBackgroundResource(R.drawable.selected_border_background);
                oldest.setTextColor(getResources().getColor(R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                newest.setBackgroundResource(R.drawable.unselected_border_background);
                newest.setTextColor(Color.BLACK);

                highest.setBackgroundResource(R.drawable.unselected_border_background);
                highest.setTextColor(Color.BLACK);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void init(View view){
        calender = view.findViewById(R.id.trans_calender);
        filter = view.findViewById(R.id.trans_filter);
        img = view.findViewById(R.id.img);
        see_financial_txt = view.findViewById(R.id.see_financial_txt);
    }
}