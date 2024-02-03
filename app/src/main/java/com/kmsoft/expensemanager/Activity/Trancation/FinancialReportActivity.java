package com.kmsoft.expensemanager.Activity.Trancation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.kmsoft.expensemanager.Adapter.FinancialAdapter;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class FinancialReportActivity extends AppCompatActivity {

    ImageView financialLineChart, financialPieChart,back;
    Spinner spinner, spinner1;
    TextView financialIncome, financialExpense;
    RecyclerView financialRecyclerview;
    FinancialAdapter financialAdapter;
    LineChart chart;
    PieChart pieChart;
    int click = 1;
    int imgClick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_report);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        LinearLayoutManager manager = new LinearLayoutManager(FinancialReportActivity.this);
        financialAdapter = new FinancialAdapter(FinancialReportActivity.this);
        financialRecyclerview.setLayoutManager(manager);
        financialRecyclerview.setAdapter(financialAdapter);

        ArrayAdapter<String> selectTypeAdapter = new ArrayAdapter<>(FinancialReportActivity.this, R.layout.simple_spinner_item1, FinancialReportActivity.this.getResources().getStringArray(R.array.financial_report_type));
        selectTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(selectTypeAdapter);

        ArrayAdapter<String> financialCategoryAdapter = new ArrayAdapter<>(FinancialReportActivity.this, R.layout.simple_spinner_item1, FinancialReportActivity.this.getResources().getStringArray(R.array.financial_report_type));
        financialCategoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(financialCategoryAdapter);

        financialLineChart.setBackgroundResource(R.drawable.selected_financial_pie_background);
        financialPieChart.setBackgroundResource(R.drawable.unselected_financial_pie_background);
        financialLineChart.setImageDrawable(getResources().getDrawable(R.drawable.line_chart));
        financialPieChart.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart_green));

        financialIncome.setBackgroundResource(R.drawable.selected_category);
        financialExpense.setBackgroundResource(R.drawable.unselected_category);
        financialIncome.setTextColor(getResources().getColor(R.color.white));
        financialExpense.setTextColor(getResources().getColor(R.color.black));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        financialLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgClick = 1;
                financialLineChart.setBackgroundResource(R.drawable.selected_financial_pie_background);
                financialPieChart.setBackgroundResource(R.drawable.unselected_financial_pie_background);

                financialLineChart.setImageDrawable(getResources().getDrawable(R.drawable.line_chart));
                financialPieChart.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart_green));

                pieChart.setVisibility(View.GONE);
                chart.setVisibility(View.VISIBLE);
            }
        });

        financialPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgClick = 2;
                financialPieChart.setBackgroundResource(R.drawable.selected_financial_line_background);
                financialLineChart.setBackgroundResource(R.drawable.unselected_financial_line_backgound);

                financialLineChart.setImageDrawable(getResources().getDrawable(R.drawable.line_chart_green));
                financialPieChart.setImageDrawable(getResources().getDrawable(R.drawable.pie_chart));

                pieChart.setVisibility(View.VISIBLE);
                chart.setVisibility(View.GONE);
            }
        });

        financialIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = 1;
                financialIncome.setBackgroundResource(R.drawable.selected_category);
                financialExpense.setBackgroundResource(R.drawable.unselected_category);

                financialIncome.setTextColor(getResources().getColor(R.color.white));
                financialExpense.setTextColor(getResources().getColor(R.color.black));
                if (spinner.getSelectedItem().toString().equals("Today")) {

                    ArrayList<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(0, 50));
                    entries.add(new Entry(1, 100));
                    entries.add(new Entry(2, 10));
                    entries.add(new Entry(3, 130));
                    entries.add(new Entry(4, 90));

                    chart.invalidate();

                    chart.getAxisLeft().setEnabled(false);
                    chart.getAxisRight().setEnabled(false);

                    final String[] days = {"Friday", "Sunday"};

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart(chart, days, entries, pieEntries);

                } else if (spinner.getSelectedItem().toString().equals("Month")) {

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

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart(chart, days, entries, pieEntries);
                } else if (spinner.getSelectedItem().toString().equals("Week")) {

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

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart(chart, days, entries, pieEntries);
                } else if (spinner.getSelectedItem().toString().equals("Year")) {

                    ArrayList<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(0, 50));
                    entries.add(new Entry(1, 20));
                    entries.add(new Entry(2, 100));
                    entries.add(new Entry(3, 60));

                    chart.invalidate();

                    chart.getAxisLeft().setEnabled(false);
                    chart.getAxisRight().setEnabled(false);
                    final String[] days = {"2021", "2022", "2023", "2024"};

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart(chart, days, entries, pieEntries);
                }
            }
        });

        financialExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = 2;
                financialExpense.setBackgroundResource(R.drawable.selected_category);
                financialIncome.setBackgroundResource(R.drawable.unselected_category);

                financialExpense.setTextColor(getResources().getColor(R.color.white));
                financialIncome.setTextColor(getResources().getColor(R.color.black));

                if (spinner.getSelectedItem().toString().equals("Today")) {

                    ArrayList<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(0, 50));
                    entries.add(new Entry(1, 100));
                    entries.add(new Entry(2, 10));
                    entries.add(new Entry(3, 130));
                    entries.add(new Entry(4, 90));

                    chart.invalidate();

                    chart.getAxisLeft().setEnabled(false);
                    chart.getAxisRight().setEnabled(false);

                    final String[] days = {"Friday", "Sunday"};

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart1(chart, days, entries, pieEntries);

                } else if (spinner.getSelectedItem().toString().equals("Month")) {

                    ArrayList<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(0, 10));
                    entries.add(new Entry(1, 20));
                    entries.add(new Entry(2, 30));
                    entries.add(new Entry(3, 10));
                    entries.add(new Entry(4, 90));
                    entries.add(new Entry(5, 60));
                    entries.add(new Entry(6, 10));
                    entries.add(new Entry(7, 80));
                    entries.add(new Entry(8, 80));
                    entries.add(new Entry(9, 70));
                    entries.add(new Entry(10, 90));
                    entries.add(new Entry(11, 45));

                    chart.invalidate();

                    chart.getAxisLeft().setEnabled(false);
                    chart.getAxisRight().setEnabled(false);
                    final String[] days = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aus", "Sep", "Oct", "Nov", "Dec"};

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart1(chart, days, entries, pieEntries);
                } else if (spinner.getSelectedItem().toString().equals("Week")) {

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

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart1(chart, days, entries, pieEntries);
                } else if (spinner.getSelectedItem().toString().equals("Year")) {

                    ArrayList<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(0, 50));
                    entries.add(new Entry(1, 20));
                    entries.add(new Entry(2, 100));
                    entries.add(new Entry(3, 60));

                    chart.invalidate();

                    chart.getAxisLeft().setEnabled(false);
                    chart.getAxisRight().setEnabled(false);
                    final String[] days = {"2021", "2022", "2023", "2024"};

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(30f, "Label 1"));
                    pieEntries.add(new PieEntry(20f, "Label 2"));
                    pieEntries.add(new PieEntry(50f, "Label 3"));

                    // Refresh chart
                    pieChart.invalidate();

                    setupLineChart1(chart, days, entries, pieEntries);
                }
            }
        });

        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Today")) {
                    if (click == 1) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        entries.add(new Entry(0, 50));
                        entries.add(new Entry(1, 100));
                        entries.add(new Entry(2, 10));
                        entries.add(new Entry(3, 130));
                        entries.add(new Entry(4, 90));

                        chart.invalidate();

                        chart.getAxisLeft().setEnabled(false);
                        chart.getAxisRight().setEnabled(false);

                        final String[] days = {"Friday", "Sunday"};

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart(chart, days, entries, pieEntries);
                    } else if (click == 2) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        entries.add(new Entry(0, 50));
                        entries.add(new Entry(1, 100));
                        entries.add(new Entry(2, 10));
                        entries.add(new Entry(3, 130));
                        entries.add(new Entry(4, 90));

                        chart.invalidate();

                        chart.getAxisLeft().setEnabled(false);
                        chart.getAxisRight().setEnabled(false);

                        final String[] days = {"Friday", "Sunday"};

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart1(chart, days, entries, pieEntries);
                    }
                } else if (spinner.getSelectedItem().toString().equals("Week")) {
                    if (click == 1) {
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

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart(chart, days, entries, pieEntries);
                    } else if (click == 2) {
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

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart1(chart, days, entries, pieEntries);
                    }
                } else if (spinner.getSelectedItem().toString().equals("Month")) {
                    if (click == 1) {
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

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));
                        pieEntries.add(new PieEntry(58f, "Label 4"));
                        pieEntries.add(new PieEntry(100f, "Label 5"));
                        pieEntries.add(new PieEntry(10f, "Label 6"));
                        pieEntries.add(new PieEntry(80f, "Label 7"));
                        pieEntries.add(new PieEntry(20f, "Label 8"));
                        pieEntries.add(new PieEntry(20f, "Label 9"));
                        pieEntries.add(new PieEntry(10f, "Label 10"));
                        pieEntries.add(new PieEntry(60f, "Label 11"));
                        pieEntries.add(new PieEntry(80f, "Label 12"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart(chart, days, entries, pieEntries);
                    } else if (click == 2) {
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

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart1(chart, days, entries, pieEntries);
                    }
                } else if (spinner.getSelectedItem().toString().equals("Year")) {
                    if (click == 1) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        entries.add(new Entry(0, 50));
                        entries.add(new Entry(1, 20));
                        entries.add(new Entry(2, 100));
                        entries.add(new Entry(3, 60));

                        chart.invalidate();

                        chart.getAxisLeft().setEnabled(false);
                        chart.getAxisRight().setEnabled(false);
                        final String[] days = {"2021", "2022", "2023", "2024"};

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart(chart, days, entries, pieEntries);
                    } else if (click == 2) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        entries.add(new Entry(0, 50));
                        entries.add(new Entry(1, 20));
                        entries.add(new Entry(2, 100));
                        entries.add(new Entry(3, 60));

                        chart.invalidate();

                        chart.getAxisLeft().setEnabled(false);
                        chart.getAxisRight().setEnabled(false);
                        final String[] days = {"2021", "2022", "2023", "2024"};

                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(30f, "Label 1"));
                        pieEntries.add(new PieEntry(20f, "Label 2"));
                        pieEntries.add(new PieEntry(50f, "Label 3"));

                        // Refresh chart
                        pieChart.invalidate();

                        setupLineChart1(chart, days, entries, pieEntries);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupLineChart(LineChart chart, String[] days, ArrayList<Entry> entries, ArrayList<PieEntry> pieEntries) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);

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

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(getColor(R.color.green), getColor(R.color.lightDarkGreen), getColor(R.color.lightGreen));

        // Set up the data for the PieChart
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(9);

        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("₹1,230");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(20f);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        Typeface typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_extrabold);
        pieChart.setCenterTextTypeface(typeface);
    }

    private void setupLineChart1(LineChart chart, String[] days, ArrayList<Entry> entries, ArrayList<PieEntry> pieEntries) {

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

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(getColor(R.color.green), getColor(R.color.lightDarkGreen), getColor(R.color.lightGreen));

        // Set up the data for the PieChart
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(9);

        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("₹1,230");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(20f);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        Typeface typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_extrabold);
        pieChart.setCenterTextTypeface(typeface);
    }

    private void init() {
        financialPieChart = findViewById(R.id.financial_pie_img);
        financialLineChart = findViewById(R.id.financial_line_img);
        spinner = findViewById(R.id.financial_spinner);
        spinner1 = findViewById(R.id.financial_spinner1);
        chart = findViewById(R.id.financial_line_chart);
        pieChart = findViewById(R.id.financial_pie_chart);
        financialRecyclerview = findViewById(R.id.financial_recyclerview);
        financialIncome = findViewById(R.id.financial_income);
        financialExpense = findViewById(R.id.financial_expense);
        back = findViewById(R.id.back);
    }
}