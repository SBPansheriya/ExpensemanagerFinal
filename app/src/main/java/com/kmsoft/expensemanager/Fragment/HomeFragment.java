package com.kmsoft.expensemanager.Fragment;

import static com.kmsoft.expensemanager.Constant.categoryArrayList;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
import com.kmsoft.expensemanager.Activity.Profile.EditProfileActivity;
import com.kmsoft.expensemanager.Activity.Profile.NotificationActivity;
import com.kmsoft.expensemanager.Adapter.AddCategoryIncomeAdapter;
import com.kmsoft.expensemanager.Adapter.RecentTransactionAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    TextView today,week,month,year,see_all;
    LineChart chart;
    ImageView homeProfileImage, homeNotification;
    RecyclerView recentTransactionRecyclerView;
    RecentTransactionAdapter recentTransactionAdapter;
    Spinner spinner;
    String selected;
    DBHelper dbHelper;
    IncomeAndExpense incomeAndExpense;
    ActivityResultLauncher<Intent> launchSomeActivityResult;
    NestedScrollView nestedScrollView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        init(view);
        dbHelper = new DBHelper(HomeFragment.this.getContext());
        Display();


        launchSomeActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {

                }
            }
        });

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                nestedScrollView.setEnabled(true);
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        ArrayAdapter<String> spendFrequencyAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item1, getContext().getResources().getStringArray(R.array.spendFrequency));
        spendFrequencyAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spendFrequencyAdapter);

        spinner.setSelection(0);

        selected = spinner.getSelectedItem().toString();

        homeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        homeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = spinner.getSelectedItem().toString();
                if (selected.equals("Income")) {
                    today.setBackgroundResource(R.drawable.selected_home_background);
                    week.setBackgroundResource(R.drawable.unselected_home_background);
                    month.setBackgroundResource(R.drawable.unselected_home_background);
                    year.setBackgroundResource(R.drawable.unselected_home_background);

                    today.setTextColor(getResources().getColor(R.color.white));
                    week.setTextColor(getResources().getColor(R.color.gray));
                    month.setTextColor(getResources().getColor(R.color.gray));
                    year.setTextColor(getResources().getColor(R.color.gray));

                    ArrayList<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(0, 50));
                    entries.add(new Entry(1, 100));

                    chart.invalidate();

                    chart.getAxisLeft().setEnabled(false);
                    chart.getAxisRight().setEnabled(false);

                    final String[] days = {"Friday","Sunday"};

                    setupLineChart(chart, days, entries);

                    today.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            today.setBackgroundResource(R.drawable.selected_home_background);
                            week.setBackgroundResource(R.drawable.unselected_home_background);
                            month.setBackgroundResource(R.drawable.unselected_home_background);
                            year.setBackgroundResource(R.drawable.unselected_home_background);

                            today.setTextColor(getResources().getColor(R.color.white));
                            week.setTextColor(getResources().getColor(R.color.gray));
                            month.setTextColor(getResources().getColor(R.color.gray));
                            year.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 100));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"Friday","Sunday"};

                            setupLineChart(chart, days, entries);
                        }
                    });

                    week.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            week.setBackgroundResource(R.drawable.selected_home_background);
                            today.setBackgroundResource(R.drawable.unselected_home_background);
                            month.setBackgroundResource(R.drawable.unselected_home_background);
                            year.setBackgroundResource(R.drawable.unselected_home_background);

                            week.setTextColor(getResources().getColor(R.color.white));
                            today.setTextColor(getResources().getColor(R.color.gray));
                            month.setTextColor(getResources().getColor(R.color.gray));
                            year.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 50));
                            entries.add(new Entry(2, 80));
                            entries.add(new Entry(3, 60));
                            entries.add(new Entry(4, 40));
                            entries.add(new Entry(5, 70));
                            entries.add(new Entry(6, 100));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

                            setupLineChart(chart, days, entries);
                        }
                    });

                    month.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            month.setBackgroundResource(R.drawable.selected_home_background);
                            week.setBackgroundResource(R.drawable.unselected_home_background);
                            today.setBackgroundResource(R.drawable.unselected_home_background);
                            year.setBackgroundResource(R.drawable.unselected_home_background);

                            month.setTextColor(getResources().getColor(R.color.white));
                            week.setTextColor(getResources().getColor(R.color.gray));
                            today.setTextColor(getResources().getColor(R.color.gray));
                            year.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 50));
                            entries.add(new Entry(2, 80));
                            entries.add(new Entry(3, 60));
                            entries.add(new Entry(4, 40));
                            entries.add(new Entry(5, 70));
                            entries.add(new Entry(6, 100));
                            entries.add(new Entry(7, 50));
                            entries.add(new Entry(8, 110));
                            entries.add(new Entry(9, 150));
                            entries.add(new Entry(10, 10));
                            entries.add(new Entry(11, 90));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aus", "Sep", "Oct", "Nov", "Dec"};

                            setupLineChart(chart, days, entries);
                        }
                    });

                    year.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            year.setBackgroundResource(R.drawable.selected_home_background);
                            week.setBackgroundResource(R.drawable.unselected_home_background);
                            month.setBackgroundResource(R.drawable.unselected_home_background);
                            today.setBackgroundResource(R.drawable.unselected_home_background);

                            year.setTextColor(getResources().getColor(R.color.white));
                            week.setTextColor(getResources().getColor(R.color.gray));
                            month.setTextColor(getResources().getColor(R.color.gray));
                            today.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 20));
                            entries.add(new Entry(2, 100));
                            entries.add(new Entry(3, 60));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"2021", "2022", "2023", "2024"};

                            setupLineChart(chart, days, entries);
                        }
                    });
                }
                else if (selected.equals("Expense")) {
                    today.setBackgroundResource(R.drawable.selected_home_background);
                    week.setBackgroundResource(R.drawable.unselected_home_background);
                    month.setBackgroundResource(R.drawable.unselected_home_background);
                    year.setBackgroundResource(R.drawable.unselected_home_background);

                    today.setTextColor(getResources().getColor(R.color.white));
                    week.setTextColor(getResources().getColor(R.color.gray));
                    month.setTextColor(getResources().getColor(R.color.gray));
                    year.setTextColor(getResources().getColor(R.color.gray));

                    ArrayList<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(0, 50));
                    entries.add(new Entry(1, 100));

                    chart.invalidate();

                    chart.getAxisLeft().setEnabled(false);
                    chart.getAxisRight().setEnabled(false);
                    final String[] days = {"Friday","Sunday"};

                    setupLineChart1(chart, days, entries);

                    today.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            today.setBackgroundResource(R.drawable.selected_home_background);
                            week.setBackgroundResource(R.drawable.unselected_home_background);
                            month.setBackgroundResource(R.drawable.unselected_home_background);
                            year.setBackgroundResource(R.drawable.unselected_home_background);

                            today.setTextColor(getResources().getColor(R.color.white));
                            week.setTextColor(getResources().getColor(R.color.gray));
                            month.setTextColor(getResources().getColor(R.color.gray));
                            year.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 100));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"Friday","Sunday"};

                            setupLineChart1(chart, days, entries);
                        }
                    });

                    week.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            week.setBackgroundResource(R.drawable.selected_home_background);
                            today.setBackgroundResource(R.drawable.unselected_home_background);
                            month.setBackgroundResource(R.drawable.unselected_home_background);
                            year.setBackgroundResource(R.drawable.unselected_home_background);

                            week.setTextColor(getResources().getColor(R.color.white));
                            today.setTextColor(getResources().getColor(R.color.gray));
                            month.setTextColor(getResources().getColor(R.color.gray));
                            year.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 50));
                            entries.add(new Entry(2, 80));
                            entries.add(new Entry(3, 60));
                            entries.add(new Entry(4, 40));
                            entries.add(new Entry(5, 70));
                            entries.add(new Entry(6, 100));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

                            setupLineChart1(chart, days, entries);
                        }
                    });

                    month.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            month.setBackgroundResource(R.drawable.selected_home_background);
                            week.setBackgroundResource(R.drawable.unselected_home_background);
                            today.setBackgroundResource(R.drawable.unselected_home_background);
                            year.setBackgroundResource(R.drawable.unselected_home_background);

                            month.setTextColor(getResources().getColor(R.color.white));
                            week.setTextColor(getResources().getColor(R.color.gray));
                            today.setTextColor(getResources().getColor(R.color.gray));
                            year.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 50));
                            entries.add(new Entry(2, 80));
                            entries.add(new Entry(3, 60));
                            entries.add(new Entry(4, 40));
                            entries.add(new Entry(5, 70));
                            entries.add(new Entry(6, 100));
                            entries.add(new Entry(7, 50));
                            entries.add(new Entry(8, 110));
                            entries.add(new Entry(9, 150));
                            entries.add(new Entry(10, 10));
                            entries.add(new Entry(11, 90));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aus", "Sep", "Oct", "Nov", "Dec"};

                            setupLineChart1(chart, days, entries);
                        }
                    });

                    year.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            year.setBackgroundResource(R.drawable.selected_home_background);
                            week.setBackgroundResource(R.drawable.unselected_home_background);
                            month.setBackgroundResource(R.drawable.unselected_home_background);
                            today.setBackgroundResource(R.drawable.unselected_home_background);

                            year.setTextColor(getResources().getColor(R.color.white));
                            week.setTextColor(getResources().getColor(R.color.gray));
                            month.setTextColor(getResources().getColor(R.color.gray));
                            today.setTextColor(getResources().getColor(R.color.gray));

                            ArrayList<Entry> entries = new ArrayList<>();
                            entries.add(new Entry(0, 50));
                            entries.add(new Entry(1, 20));
                            entries.add(new Entry(2, 100));
                            entries.add(new Entry(3, 60));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);
                            final String[] days = {"2021", "2022", "2023", "2024"};

                            setupLineChart1(chart, days, entries);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void setupLineChart(LineChart chart,String[] days,ArrayList<Entry> entries) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineDataSet dataSet = new LineDataSet(entries, "Income");
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(9);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(getResources().getColor(R.color.green));
        dataSet.setFillColor(getResources().getColor(R.color.green));
        dataSet.setFillAlpha(30);
        dataSet.setValueTextColor(getResources().getColor(R.color.green));
        dataSet.setColor(getResources().getColor(R.color.green));

        // Create LineData and set the dataset
        LineData lineData = new LineData(dataSet);

        // Set LineData to the chart
        chart.setData(lineData);
    }
    private void setupLineChart1(LineChart chart,String[] days,ArrayList<Entry> entries) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineDataSet dataSet = new LineDataSet(entries, "Expense");
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(9);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(getResources().getColor(R.color.red));
        dataSet.setFillColor(getResources().getColor(R.color.red));
        dataSet.setFillAlpha(30);
        dataSet.setValueTextColor(getResources().getColor(R.color.red));
        dataSet.setColor(getResources().getColor(R.color.red));

        // Create LineData and set the dataset
        LineData lineData = new LineData(dataSet);

        // Set LineData to the chart
        chart.setData(lineData);
    }

    private void Display() {

        Cursor cursor = dbHelper.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String incomeAmount = cursor.getString(1);
                String incomeDate = cursor.getString(2);
                String categoryName = cursor.getString(3);
                int categoryImage = cursor.getInt(4);
                String incomeDescription = cursor.getString(5);
                String addAttachment = cursor.getString(6);
                String tag = cursor.getString(7);

                incomeAndExpense = new IncomeAndExpense(id,incomeAmount,incomeDate,categoryName, categoryImage, incomeDescription,addAttachment,tag);
                incomeAndExpenseArrayList.add(incomeAndExpense);

                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recentTransactionAdapter = new RecentTransactionAdapter(HomeFragment.this,incomeAndExpenseArrayList);
                recentTransactionRecyclerView.setLayoutManager(manager);
                recentTransactionRecyclerView.setAdapter(recentTransactionAdapter);

            } while (cursor.moveToNext());
        }
    }

    private void init(View view){
        spinner = view.findViewById(R.id.spinner);
        today = view.findViewById(R.id.home_today);
        month = view.findViewById(R.id.home_month);
        week = view.findViewById(R.id.home_week);
        year = view.findViewById(R.id.home_year);
        chart = view.findViewById(R.id.line_chart);
        homeNotification = view.findViewById(R.id.home_notification);
        homeProfileImage = view.findViewById(R.id.home_profile_image);
        recentTransactionRecyclerView = view.findViewById(R.id.recentTransactionRecyclerView);
        nestedScrollView = view.findViewById(R.id.nested);
        see_all = view.findViewById(R.id.see_all);
    }
}