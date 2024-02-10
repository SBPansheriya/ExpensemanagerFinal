package com.kmsoft.expensemanager.Activity.Trancation;

import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.kmsoft.expensemanager.Adapter.FinancialAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FinancialReportActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ImageView financialLineChart, financialPieChart, back;
    Spinner spinner, spinner1;
    TextView financialIncome, financialExpense, emptyTransaction;
    LineChart chart;
    PieChart pieChart;
    int click = 1;
    int imgClick = 1;
    RecyclerView financialRecyclerView;
    FinancialAdapter financialAdapter;
    IncomeAndExpense incomeAndExpense;
    ArrayList<IncomeAndExpense> incomeList = new ArrayList<>();
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();
    String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_report);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(FinancialReportActivity.this);
        selected = "Income";
        spinner.setSelection(0);
        spinner1.setSelection(0);
        ArrayAdapter<String> selectTypeAdapter = new ArrayAdapter<>(FinancialReportActivity.this, R.layout.simple_spinner_item1, FinancialReportActivity.this.getResources().getStringArray(R.array.financial_report_type));
        selectTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(selectTypeAdapter);

        ArrayAdapter<String> financialCategoryAdapter = new ArrayAdapter<>(FinancialReportActivity.this, R.layout.simple_spinner_item1, FinancialReportActivity.this.getResources().getStringArray(R.array.financial_report_transaction_type));
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
                selected = "Income";
                click = 1;

                financialIncome.setBackgroundResource(R.drawable.selected_category);
                financialExpense.setBackgroundResource(R.drawable.unselected_category);

                financialIncome.setTextColor(getResources().getColor(R.color.white));
                financialExpense.setTextColor(getResources().getColor(R.color.black));
                if (spinner.getSelectedItem().toString().equals("Today")) {
                    populateChartWithTodayIncomeData();
                } else if (spinner.getSelectedItem().toString().equals("Week")) {
                    populateChartWithWeekIncomeData();
                } else if (spinner.getSelectedItem().toString().equals("Month")) {
                    populateChartWithMonthIncomeData();
                } else if (spinner.getSelectedItem().toString().equals("Year")) {
                    populateChartWithYearIncomeData();
                }
                Display();

            }
        });

        financialExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "Expense";
                click = 2;

                financialExpense.setBackgroundResource(R.drawable.selected_category);
                financialIncome.setBackgroundResource(R.drawable.unselected_category);

                financialExpense.setTextColor(getResources().getColor(R.color.white));
                financialIncome.setTextColor(getResources().getColor(R.color.black));

                if (spinner.getSelectedItem().toString().equals("Today")) {
                    populateChartWithTodayExpenseData();
                } else if (spinner.getSelectedItem().toString().equals("Week")) {
                    populateChartWithWeekExpenseData();
                } else if (spinner.getSelectedItem().toString().equals("Month")) {
                    populateChartWithMonthExpenseData();
                } else if (spinner.getSelectedItem().toString().equals("Year")) {
                    populateChartWithYearExpenseData();
                }
                Display();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Today")) {
                    if (click == 1) {
                        populateChartWithTodayIncomeData();
                    } else if (click == 2) {
                        populateChartWithTodayExpenseData();
                    }
                } else if (spinner.getSelectedItem().toString().equals("Week")) {
                    if (click == 1) {
                        populateChartWithWeekIncomeData();
                    } else if (click == 2) {
                        populateChartWithWeekExpenseData();
                    }
                } else if (spinner.getSelectedItem().toString().equals("Month")) {
                    if (click == 1) {
                        populateChartWithMonthIncomeData();
                    } else if (click == 2) {
                        populateChartWithMonthExpenseData();
                    }
                } else if (spinner.getSelectedItem().toString().equals("Year")) {
                    if (click == 1) {
                        populateChartWithYearIncomeData();
                    } else if (click == 2) {
                        populateChartWithYearExpenseData();
                    }
                }
                Display();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Display();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Display();
    }

    private void populateChartWithTodayIncomeData() {

        ArrayList<Entry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        double[] totalAmountPerHour = new double[12];

        for (int i = 0; i < incomeList.size(); i++) {
            String entryDate = incomeList.get(i).getDate();
            int entryDay = Integer.parseInt(entryDate.split("/")[0]);
            int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);

            int hourOfDay = getHourOfDay(incomeList.get(i).getTime());
            String amountString = extractNumericPart(incomeList.get(i).getAmount());
            double amount = Double.parseDouble(amountString);
            if (entryYear == currentYear && entryMonth == currentMonth && entryDay == currentDay) {
                int index = hourOfDay / 2;
                if (index >= 0 && index < totalAmountPerHour.length) {
                    totalAmountPerHour[index] += amount;
                }
            }
        }

        for (int i = 0; i < totalAmountPerHour.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerHour[i]));
        }
        final String[] days = {"1AM", "3AM", "5AM", "7AM", "9AM", "11AM", "1PM", "3PM", "5PM", "7PM", "9PM", "11PM"};

        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30f, "Label 1"));
        pieEntries.add(new PieEntry(20f, "Label 2"));
        pieEntries.add(new PieEntry(50f, "Label 3"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart(chart, days, entries, pieEntries);
    }

    private void populateChartWithTodayExpenseData() {

        ArrayList<Entry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        double[] totalAmountPerHour = new double[12];

        for (int i = 0; i < expenseList.size(); i++) {
            String entryDate = expenseList.get(i).getDate();
            int entryDay = Integer.parseInt(entryDate.split("/")[0]);
            int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);

            int hourOfDay = getHourOfDay(expenseList.get(i).getTime());
            String amountString = extractNumericPart(expenseList.get(i).getAmount());
            double amount = Double.parseDouble(amountString);
            if (entryYear == currentYear && entryMonth == currentMonth && entryDay == currentDay) {
                int index = hourOfDay / 2;
                if (index >= 0 && index < totalAmountPerHour.length) {
                    totalAmountPerHour[index] += amount;
                }
            }
        }

        for (int i = 0; i < totalAmountPerHour.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerHour[i]));
        }
        final String[] days = {"1AM", "3AM", "5AM", "7AM", "9AM", "11AM", "1PM", "3PM", "5PM", "7PM", "9PM", "11PM"};

        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(500f, "Label 4"));
        pieEntries.add(new PieEntry(200f, "Label 5"));
        pieEntries.add(new PieEntry(50f, "Label 6"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart1(chart, days, entries, pieEntries);
    }

    private void populateChartWithWeekIncomeData() {

        ArrayList<Entry> entries = new ArrayList<>();

        double[] totalAmountPerDay = new double[7];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (IncomeAndExpense entry : incomeList) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            String entryDate = entry.getDate();
            for (int i = 0; i < 7; i++) {
                String day = dateFormat.format(calendar.getTime());
                if (day.equals(entryDate)) {
                    String amountString = extractNumericPart(entry.getAmount());
                    double amount = Double.parseDouble(amountString);
                    totalAmountPerDay[i] += amount;
                    break;
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        for (int i = 0; i < totalAmountPerDay.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerDay[i]));
        }
        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        final String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30f, "Label 1"));
        pieEntries.add(new PieEntry(20f, "Label 2"));
        pieEntries.add(new PieEntry(50f, "Label 3"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart(chart, days, entries, pieEntries);
    }

    private void populateChartWithWeekExpenseData() {

        ArrayList<Entry> entries = new ArrayList<>();

        double[] totalAmountPerDay = new double[7];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (IncomeAndExpense entry : expenseList) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            String entryDate = entry.getDate();
            for (int i = 0; i < 7; i++) {
                String day = dateFormat.format(calendar.getTime());
                if (day.equals(entryDate)) {
                    String amountString = extractNumericPart(entry.getAmount());
                    double amount = Double.parseDouble(amountString);
                    totalAmountPerDay[i] += amount;
                    break;
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        for (int i = 0; i < totalAmountPerDay.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerDay[i]));
        }
        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        final String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30f, "Label 1"));
        pieEntries.add(new PieEntry(20f, "Label 2"));
        pieEntries.add(new PieEntry(50f, "Label 3"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart1(chart, days, entries, pieEntries);
    }

    private void populateChartWithMonthIncomeData() {

        ArrayList<Entry> entries = new ArrayList<>();
        double[] totalAmountPerMonth = new double[12];

        for (IncomeAndExpense entry : incomeList) {
            String entryDate = entry.getDate();
            int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);

            if (currentYear == entryYear) {
                String amountString = extractNumericPart(entry.getAmount());
                double amount = Double.parseDouble(amountString);
                totalAmountPerMonth[entryMonth - 1] += amount;
            }
        }

        for (int i = 0; i < totalAmountPerMonth.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerMonth[i]));
        }

        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30f, "Label 1"));
        pieEntries.add(new PieEntry(20f, "Label 2"));
        pieEntries.add(new PieEntry(50f, "Label 3"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart(chart, months, entries, pieEntries);
    }

    private void populateChartWithMonthExpenseData() {

        ArrayList<Entry> entries = new ArrayList<>();
        double[] totalAmountPerMonth = new double[12];

        for (IncomeAndExpense entry : expenseList) {
            String entryDate = entry.getDate();
            int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);

            if (currentYear == entryYear) {
                String amountString = extractNumericPart(entry.getAmount());
                double amount = Double.parseDouble(amountString);
                totalAmountPerMonth[entryMonth - 1] += amount;
            }
        }

        for (int i = 0; i < totalAmountPerMonth.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerMonth[i]));
        }

        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30f, "Label 1"));
        pieEntries.add(new PieEntry(20f, "Label 2"));
        pieEntries.add(new PieEntry(50f, "Label 3"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart1(chart, months, entries, pieEntries);
    }

    private void populateChartWithYearIncomeData() {

        ArrayList<Entry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int startYear = currentYear - 1;
        int endYear = currentYear + 1;
        double[] totalAmountPerYear1 = new double[endYear - startYear + 1];

        for (IncomeAndExpense entry : incomeList) {
            String entryDate = entry.getDate();
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);

            if (entryYear >= startYear && entryYear <= endYear) {
                String amountString = extractNumericPart(entry.getAmount());
                double amount = Double.parseDouble(amountString);
                int index = entryYear - startYear;

                totalAmountPerYear1[index] += amount;
            }
        }

        for (int i = 0; i < totalAmountPerYear1.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerYear1[i]));
        }

        final String currentYearStr = String.valueOf(currentYear);

        // Calculate previous year
        calendar.add(Calendar.YEAR, -1);
        int previousYear = calendar.get(Calendar.YEAR);
        final String previousYearStr = String.valueOf(previousYear);

        // Calculate next year
        calendar.add(Calendar.YEAR, 2);
        int nextYear = calendar.get(Calendar.YEAR);
        final String nextYearStr = String.valueOf(nextYear);
        final String[] years = {previousYearStr, currentYearStr, nextYearStr};

        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30f, "Label 1"));
        pieEntries.add(new PieEntry(20f, "Label 2"));
        pieEntries.add(new PieEntry(50f, "Label 3"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart(chart, years, entries, pieEntries);
    }

    private void populateChartWithYearExpenseData() {

        ArrayList<Entry> entries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int startYear = currentYear - 1;
        int endYear = currentYear + 1;
        double[] totalAmountPerYear1 = new double[endYear - startYear + 1];

        for (IncomeAndExpense entry : incomeList) {
            String entryDate = entry.getDate();
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);

            if (entryYear >= startYear && entryYear <= endYear) {
                String amountString = extractNumericPart(entry.getAmount());
                double amount = Double.parseDouble(amountString);
                int index = entryYear - startYear;

                totalAmountPerYear1[index] += amount;
            }
        }

        for (int i = 0; i < totalAmountPerYear1.length; i++) {
            entries.add(new Entry(i, (float) totalAmountPerYear1[i]));
        }

        final String currentYearStr = String.valueOf(currentYear);

        // Calculate previous year
        calendar.add(Calendar.YEAR, -1);
        int previousYear = calendar.get(Calendar.YEAR);
        final String previousYearStr = String.valueOf(previousYear);

        // Calculate next year
        calendar.add(Calendar.YEAR, 2);
        int nextYear = calendar.get(Calendar.YEAR);
        final String nextYearStr = String.valueOf(nextYear);
        final String[] years = {previousYearStr, currentYearStr, nextYearStr};

        chart.invalidate();

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30f, "Label 1"));
        pieEntries.add(new PieEntry(20f, "Label 2"));
        pieEntries.add(new PieEntry(50f, "Label 3"));

        // Refresh chart
        pieChart.invalidate();

        setupLineChart1(chart, years, entries, pieEntries);
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    private int getHourOfDay(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            try {
                int hour = Integer.parseInt(parts[0]);
                if (hour >= 0 && hour <= 12) {
                    int minute = Integer.parseInt(parts[1].split("\\s+")[0]);
                    String period = parts[1].split("\\s+")[1];

                    if (period.equalsIgnoreCase("AM")) {
                        if (hour == 12) {
                            return 0;
                        }
                    } else if (period.equalsIgnoreCase("PM")) {
                        if (hour < 12) {
                            hour += 12;
                        }
                    }

                    if (minute >= 0 && minute < 60) {
                        return hour;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void setupLineChart(LineChart chart, String[] days, ArrayList<Entry> entries, ArrayList<PieEntry> pieEntries) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        chart.setDescription(null);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

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

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(getColor(R.color.green), getColor(R.color.lightDarkGreen), getColor(R.color.lightGreen));
        Description description = pieChart.getDescription();
        description.setText("");
        legend = pieChart.getLegend();
        legend.setEnabled(false);

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
        chart.setDescription(null);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

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

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(getColor(R.color.green), getColor(R.color.lightDarkGreen), getColor(R.color.lightGreen));
        Description description = pieChart.getDescription();
        description.setText("");
        legend = pieChart.getLegend();
        legend.setEnabled(false);
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

    private void Display() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            incomeAndExpenseArrayList = new ArrayList<>();
            do {
                int id = cursor.getInt(0);
                String incomeAmount = cursor.getString(1);
                double currantDateTimeStamp = cursor.getDouble(2);
                double selectedDateTimeStamp = cursor.getDouble(3);
                String currentdate = cursor.getString(4);
                String incomeDate = cursor.getString(5);
                String incomeDay = cursor.getString(6);
                String incomeAddTime = cursor.getString(7);
                String categoryName = cursor.getString(8);
                int categoryImage = cursor.getInt(9);
                String incomeDescription = cursor.getString(10);
                String addAttachment = cursor.getString(11);
                String tag = cursor.getString(12);

                incomeAndExpense = new IncomeAndExpense(id, incomeAmount, currantDateTimeStamp,selectedDateTimeStamp,currentdate, incomeDate, incomeDay, incomeAddTime, categoryName, categoryImage, incomeDescription, addAttachment, tag);
                incomeAndExpenseArrayList.add(incomeAndExpense);

                Collections.sort(incomeAndExpenseArrayList, new Comparator<IncomeAndExpense>() {
                    @Override
                    public int compare(IncomeAndExpense o1, IncomeAndExpense o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });

                incomeList = filterCategories(incomeAndExpenseArrayList, "Income");
                expenseList = filterCategories(incomeAndExpenseArrayList, "Expense");

                ArrayList<IncomeAndExpense> filteredTodayIncomeList = filterByTodayDate(incomeList);
                ArrayList<IncomeAndExpense> filteredTodayExpenseList = filterByTodayDate(expenseList);
                ArrayList<IncomeAndExpense> filteredWeekIncomeList = filterByCurrentWeek(incomeList);
                ArrayList<IncomeAndExpense> filteredWeekExpenseList = filterByCurrentWeek(expenseList);
                ArrayList<IncomeAndExpense> filteredMonthIncomeList = filterByCurrentMonth(incomeList);
                ArrayList<IncomeAndExpense> filteredMonthExpenseList = filterByCurrentMonth(expenseList);
                ArrayList<IncomeAndExpense> filteredYearIncomeList = filterByCurrentYear(incomeList);
                ArrayList<IncomeAndExpense> filteredYearExpenseList = filterByCurrentYear(expenseList);

                if (TextUtils.equals(spinner.getSelectedItem().toString(), "Today")) {
                    if (TextUtils.equals(selected, "Income")) {
                        if (filteredTodayIncomeList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredTodayIncomeList, spinner1.getSelectedItem().toString(), selected);

                        }
                    } else if (TextUtils.equals(selected, "Expense")) {
                        if (filteredTodayExpenseList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredTodayExpenseList, spinner1.getSelectedItem().toString(), selected);
                        }
                    }
                } else if (spinner.getSelectedItem().toString().equals("Week")) {
                    if (TextUtils.equals(selected, "Income")) {
                        if (filteredWeekIncomeList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredWeekIncomeList, spinner1.getSelectedItem().toString(), selected);
                        }
                    } else if (TextUtils.equals(selected, "Expense")) {
                        if (filteredWeekExpenseList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredWeekExpenseList, spinner1.getSelectedItem().toString(), selected);
                        }
                    }
                } else if (spinner.getSelectedItem().toString().equals("Month")) {
                    if (TextUtils.equals(selected, "Income")) {
                        if (filteredWeekIncomeList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredMonthIncomeList, spinner1.getSelectedItem().toString(), selected);
                        }
                    } else if (TextUtils.equals(selected, "Expense")) {
                        if (filteredWeekExpenseList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredMonthExpenseList, spinner1.getSelectedItem().toString(), selected);
                        }
                    }
                } else if (spinner.getSelectedItem().toString().equals("Year")) {
                    if (TextUtils.equals(selected, "Income")) {
                        if (filteredWeekIncomeList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredYearIncomeList, spinner1.getSelectedItem().toString(), selected);
                        }
                    } else if (TextUtils.equals(selected, "Expense")) {
                        if (filteredWeekExpenseList.isEmpty()) {
                            emptyTransaction.setVisibility(View.VISIBLE);
                            financialRecyclerView.setVisibility(View.GONE);
                        } else {
                            financialRecyclerView.setVisibility(View.VISIBLE);
                            emptyTransaction.setVisibility(View.GONE);
                            financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredYearExpenseList, spinner1.getSelectedItem().toString(), selected);
                        }
                    }
                }
                LinearLayoutManager manager = new LinearLayoutManager(FinancialReportActivity.this);
                financialRecyclerView.setLayoutManager(manager);
                financialRecyclerView.setAdapter(financialAdapter);
            } while (cursor.moveToNext());
        } else {
            incomeAndExpenseArrayList = new ArrayList<>();
            financialRecyclerView.setVisibility(View.GONE);
            emptyTransaction.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<IncomeAndExpense> filterCategories(ArrayList<IncomeAndExpense> incomeAndExpenses, String type) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            if (incomeAndExpense.getTag().equals(type)) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    private static ArrayList<IncomeAndExpense> filterByTodayDate(ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(currentDate);
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            if (incomeAndExpense.getDate().equals(formattedDate)) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    private static ArrayList<IncomeAndExpense> filterByCurrentWeek(ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfWeek = calendar.getTime();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endOfWeek = calendar.getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            String entryDateString = incomeAndExpense.getDate();
            try {
                Date entryDate = sdf.parse(entryDateString);
                if (entryDate.after(startOfWeek) && entryDate.before(endOfWeek)) {
                    filteredList.add(incomeAndExpense);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return filteredList;
    }

    private static ArrayList<IncomeAndExpense> filterByCurrentMonth(ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            String entryDate = incomeAndExpense.getDate();
            int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
            if (entryMonth == currentMonth) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    private static ArrayList<IncomeAndExpense> filterByCurrentYear(ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            String entryDate = incomeAndExpense.getDate();
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);
            if (entryYear == currentYear) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    private void init() {
        financialPieChart = findViewById(R.id.financial_pie_img);
        financialLineChart = findViewById(R.id.financial_line_img);
        spinner = findViewById(R.id.financial_spinner);
        spinner1 = findViewById(R.id.financial_spinner1);
        chart = findViewById(R.id.financial_line_chart);
        pieChart = findViewById(R.id.financial_pie_chart);
        financialRecyclerView = findViewById(R.id.financial_recyclerview);
        financialIncome = findViewById(R.id.financial_income);
        financialExpense = findViewById(R.id.financial_expense);
        emptyTransaction = findViewById(R.id.empty_transaction);
        back = findViewById(R.id.back);
    }
}