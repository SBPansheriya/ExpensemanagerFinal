package com.kmsoft.expensemanager.Fragment;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static com.kmsoft.expensemanager.Activity.MainActivity.currencySymbol;
import static com.kmsoft.expensemanager.Activity.MainActivity.isStep;
import static com.kmsoft.expensemanager.Activity.SplashActivity.PREFS_NAME;
import static com.kmsoft.expensemanager.Activity.SplashActivity.USER_IMAGE;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.kmsoft.expensemanager.Activity.Profile.EditProfileActivity;
import com.kmsoft.expensemanager.Activity.Profile.NotificationActivity;
import com.kmsoft.expensemanager.Adapter.HomeAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class HomeFragment extends Fragment {

    TextView today, week, month, year, see_all, showBalance, totalIncome, totalExpense, emptyTransaction;
    LineChart chart;
    ImageView homeProfileImage, homeNotification, markRead;
    RecyclerView recentTransactionRecyclerView;
    HomeAdapter homeAdapter;
    Spinner spinner;
    String selected;
    String totalIncomeAmount, totalExpenseAmount;
    DBHelper dbHelper;
    IncomeAndExpense incomeAndExpense;
    ArrayList<IncomeAndExpense> incomeList = new ArrayList<>();
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();
    NestedScrollView nestedScrollView;
    ActivityResultLauncher<Intent> launchSomeActivity;
    String profileImage;
    String userName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        checkPermissionsForNotification();

        dbHelper = new DBHelper(getContext());

//        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == Activity.RESULT_OK) {
//                Intent data = result.getData();
//                if (data != null) {
//                    profileImage = data.getStringExtra("userImage");
//                }
//                if (TextUtils.isEmpty(profileImage)) {
//                    homeProfileImage.setImageResource(R.drawable.profile);
//                } else {
//                    Picasso.get().load(profileImage).into(homeProfileImage);
//                }
//            }
//        });

        see_all.setOnClickListener(v -> {
            isStep = false;
            TransactionFragment transactionFragment = new TransactionFragment();
            Bundle args = new Bundle();
            transactionFragment.setArguments(args);
            getFragmentManager().beginTransaction().add(R.id.framelayout, transactionFragment).commit();
        });

        ArrayAdapter<String> spendFrequencyAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item1, getContext().getResources().getStringArray(R.array.spendFrequency));
        spendFrequencyAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spendFrequencyAdapter);

        spinner.setSelection(0);

        selected = spinner.getSelectedItem().toString();

        homeProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("name", userName);
            intent.putExtra("image", profileImage);
            startActivity(intent);
        });

        homeNotification.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected = spinner.getSelectedItem().toString();
                if (selected.equals("Income")) {
                    populateChartWithTodayIncomeData();

                    today.setOnClickListener(v -> populateChartWithTodayIncomeData());

                    week.setOnClickListener(v -> {
                        week.setBackgroundResource(R.drawable.selected_home_background);
                        today.setBackgroundResource(R.drawable.unselected_home_background);
                        month.setBackgroundResource(R.drawable.unselected_home_background);
                        year.setBackgroundResource(R.drawable.unselected_home_background);

                        week.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        today.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        month.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        year.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        ArrayList<Entry> entries = new ArrayList<>();

                        double[] totalAmountPerDay = new double[7];
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        for (IncomeAndExpense entry : incomeList) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
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
                        final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

                        setupLineChart(chart, days, entries);
                    });

                    month.setOnClickListener(v -> {
                        month.setBackgroundResource(R.drawable.selected_home_background);
                        week.setBackgroundResource(R.drawable.unselected_home_background);
                        today.setBackgroundResource(R.drawable.unselected_home_background);
                        year.setBackgroundResource(R.drawable.unselected_home_background);

                        month.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        week.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        today.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        year.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));

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

                        setupLineChart(chart, months, entries);
                    });

                    year.setOnClickListener(v -> {
                        year.setBackgroundResource(R.drawable.selected_home_background);
                        week.setBackgroundResource(R.drawable.unselected_home_background);
                        month.setBackgroundResource(R.drawable.unselected_home_background);
                        today.setBackgroundResource(R.drawable.unselected_home_background);

                        year.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        week.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        month.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        today.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
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

                        calendar.add(Calendar.YEAR, -1);
                        int previousYear = calendar.get(Calendar.YEAR);
                        final String previousYearStr = String.valueOf(previousYear);

                        calendar.add(Calendar.YEAR, 2);
                        int nextYear = calendar.get(Calendar.YEAR);
                        final String nextYearStr = String.valueOf(nextYear);
                        final String[] years = {previousYearStr, currentYearStr, nextYearStr};

                        chart.invalidate();

                        chart.getAxisLeft().setEnabled(false);
                        chart.getAxisRight().setEnabled(false);

                        setupLineChart(chart, years, entries);
                    });
                } else if (selected.equals("Expense")) {

                    populateChartWithTodayExpenseData();

                    today.setOnClickListener(v -> populateChartWithTodayExpenseData());

                    week.setOnClickListener(v -> {
                        week.setBackgroundResource(R.drawable.selected_home_background);
                        today.setBackgroundResource(R.drawable.unselected_home_background);
                        month.setBackgroundResource(R.drawable.unselected_home_background);
                        year.setBackgroundResource(R.drawable.unselected_home_background);

                        week.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        today.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        month.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        year.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));

                        ArrayList<Entry> entries = new ArrayList<>();
                        double[] totalAmountPerDay = new double[7];

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        for (IncomeAndExpense entry : expenseList) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
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
                        final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

                        setupLineChart1(chart, days, entries);
                    });

                    month.setOnClickListener(v -> {
                        month.setBackgroundResource(R.drawable.selected_home_background);
                        week.setBackgroundResource(R.drawable.unselected_home_background);
                        today.setBackgroundResource(R.drawable.unselected_home_background);
                        year.setBackgroundResource(R.drawable.unselected_home_background);

                        month.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        week.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        today.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        year.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));

                        ArrayList<Entry> entries = new ArrayList<>();

                        double[] totalAmountPerMonth = new double[12];

                        for (int i = 0; i < expenseList.size(); i++) {
                            int month = getMonthFromDateString(expenseList.get(i).getDate());

                            String amountString = extractNumericPart(expenseList.get(i).getAmount());
                            double amount = Double.parseDouble(amountString);

                            if (month >= 1 && month <= 12) {
                                totalAmountPerMonth[month - 1] += amount;
                            }
                        }
                        for (int i = 0; i < totalAmountPerMonth.length; i++) {
                            entries.add(new Entry(i, (float) totalAmountPerMonth[i]));
                        }

                        chart.invalidate();

                        chart.getAxisLeft().setEnabled(false);
                        chart.getAxisRight().setEnabled(false);
                        final String[] days = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aus", "Sep", "Oct", "Nov", "Dec"};

                        setupLineChart1(chart, days, entries);
                    });

                    year.setOnClickListener(v -> {
                        year.setBackgroundResource(R.drawable.selected_home_background);
                        week.setBackgroundResource(R.drawable.unselected_home_background);
                        month.setBackgroundResource(R.drawable.unselected_home_background);
                        today.setBackgroundResource(R.drawable.unselected_home_background);

                        year.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        week.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        month.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                        today.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));

                        ArrayList<Entry> entries = new ArrayList<>();

                        Calendar calendar = Calendar.getInstance();
                        int currentYear = calendar.get(Calendar.YEAR);
                        int startYear = currentYear - 1;
                        int endYear = currentYear + 1;
                        double[] totalAmountPerYear1 = new double[endYear - startYear + 1];

                        for (IncomeAndExpense entry : expenseList) {
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

                        setupLineChart1(chart, years, entries);
                    });
                }
                Display();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Display();
        Display1();
        populateChartWithTodayIncomeData();
        calculateTotalExpense();
        calculateTotalIncome();
        calculateTotalIncomeAndExpense();
    }

    private void populateChartWithTodayIncomeData() {
        today.setBackgroundResource(R.drawable.selected_home_background);
        week.setBackgroundResource(R.drawable.unselected_home_background);
        month.setBackgroundResource(R.drawable.unselected_home_background);
        year.setBackgroundResource(R.drawable.unselected_home_background);

        today.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        week.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
        month.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
        year.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));

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

        setupLineChart(chart, days, entries);
    }

    private void populateChartWithTodayExpenseData() {
        today.setBackgroundResource(R.drawable.selected_home_background);
        week.setBackgroundResource(R.drawable.unselected_home_background);
        month.setBackgroundResource(R.drawable.unselected_home_background);
        year.setBackgroundResource(R.drawable.unselected_home_background);

        today.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        week.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
        month.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
        year.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));

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

        setupLineChart1(chart, days, entries);
    }

    private void setupLineChart(LineChart chart, String[] days, ArrayList<Entry> entries) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(11);
        xAxis.setYOffset((float) -0.5);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lexenddeca_semibold);
        xAxis.setTypeface(typeface);
        chart.setDescription(null);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries, "Income");

        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(9);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.green));
        dataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.green));
        dataSet.setFillAlpha(30);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.green));
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.green));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
    }

    private void setupLineChart1(LineChart chart, String[] days, ArrayList<Entry> entries) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(11);
        xAxis.setYOffset((float) -0.5);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lexenddeca_semibold);
        xAxis.setTypeface(typeface);
        chart.setDescription(null);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries, "Expense");
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(9);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.red));
        dataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.red));
        dataSet.setFillAlpha(30);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.red));
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.red));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
    }

    @SuppressLint("SetTextI18n")
    public BigDecimal calculateTotalIncome() {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (IncomeAndExpense amount : incomeList) {
            String amountString = amount.getAmount();
            BigDecimal numericAmount = extractNumericAmount(amountString);
            totalSum = totalSum.add(numericAmount);
        }
        totalIncomeAmount = totalSum.toString();
        totalIncome.setText(currencySymbol + totalSum);
        return totalSum;
    }

    @SuppressLint("SetTextI18n")
    public BigDecimal calculateTotalExpense() {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (IncomeAndExpense amount : expenseList) {
            String amountString = amount.getAmount();
            BigDecimal numericAmount = extractNumericAmount(amountString);
            totalSum = totalSum.add(numericAmount);
        }
        totalExpenseAmount = totalSum.toString();
        totalExpense.setText(currencySymbol + totalSum);
        return totalSum;
    }

    @SuppressLint("SetTextI18n")
    public void calculateTotalIncomeAndExpense() {
        BigDecimal totalIncome = calculateTotalIncome();
        BigDecimal totalExpense = calculateTotalExpense();
        BigDecimal total = totalIncome.subtract(totalExpense);
        DecimalFormat df1 = new DecimalFormat("#.##");
        df1.setRoundingMode(RoundingMode.HALF_UP);
        String reAmount = df1.format(total);
        showBalance.setText(currencySymbol + reAmount);
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    private BigDecimal extractNumericAmount(String amount) {
        String numericString = amount.replaceAll("[^\\d.]", "");
        return new BigDecimal(numericString);
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

    private void checkPermissionsForNotification() {
        String[] permissions = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{POST_NOTIFICATIONS};
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(getActivity(), permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", "onRequestPermissionsResult: Granted");
        } else {
            showPermissionDenyDialog(getActivity());
        }
    }

    private void showPermissionDenyDialog(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, POST_NOTIFICATIONS)) {
            Dialog dialog = new Dialog(activity);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.permission_denied_first_dialog);
            dialog.setCancelable(false);
            dialog.show();

            Button cancel = dialog.findViewById(R.id.canceldialog);
            Button ok = dialog.findViewById(R.id.okdialog);
            TextView filename = dialog.findViewById(R.id.filename);

            filename.setText(R.string.post_notification_permission_is_needed_for_proper_functioning_of_app);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                checkPermissionsForNotification();
                dialog.dismiss();
            });
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, POST_NOTIFICATIONS)) {

            Dialog dialog = new Dialog(activity);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.permission_denied_first_dialog);
            dialog.setCancelable(false);
            dialog.show();

            Button cancel = dialog.findViewById(R.id.canceldialog);
            Button ok = dialog.findViewById(R.id.okdialog);
            TextView textView = dialog.findViewById(R.id.filename);

            textView.setText(R.string.postNotification);
            ok.setText(R.string.enable_from_settings);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                ActivityCompat.startActivityForResult(getActivity(), intent, 100, null);
                dialog.dismiss();
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            checkPermissionsForNotification();
        }
    }

    public int getMonthFromDateString(String dateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            return date.getMonth() + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void Display1() {
        try {
            Cursor cursor = dbHelper.getAllProfileData();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    userName = cursor.getString(1);
                    profileImage = cursor.getString(2);

                } while (cursor.moveToNext());

                if (TextUtils.isEmpty(profileImage)) {
                    homeProfileImage.setImageResource(R.drawable.profile);
                } else {
                    Picasso.get().load(profileImage).into(homeProfileImage);
                }
            }
        } catch (Exception e) {

        }
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
                int categoryColor = cursor.getInt(10);
                String incomeDescription = cursor.getString(11);
                String addAttachment = cursor.getString(12);
                String tag = cursor.getString(13);
                int categoryId = cursor.getInt(14);
                String name = null;
                int categoryImage = 0;
                Cursor cursor1 = dbHelper.getCategory(categoryId);
                if (cursor1 != null && cursor1.moveToFirst()) {
                    do {
                        categoryImage = cursor1.getInt(2);
                        name = cursor1.getString(1);
                    } while (cursor1.moveToNext());
                }

                incomeAndExpense = new IncomeAndExpense(id, incomeAmount, currantDateTimeStamp, selectedDateTimeStamp, currentdate, incomeDate, incomeDay, incomeAddTime, name, categoryImage, categoryColor, incomeDescription, addAttachment, tag, categoryId);
                incomeAndExpenseArrayList.add(incomeAndExpense);
            } while (cursor.moveToNext());
            incomeAndExpenseArrayList.sort(Comparator.comparing(IncomeAndExpense::getCurrantDateTimeStamp));
            Collections.reverse(incomeAndExpenseArrayList);
            incomeList = filterCategories(incomeAndExpenseArrayList, "Income");
            expenseList = filterCategories(incomeAndExpenseArrayList, "Expense");

            recentTransactionRecyclerView.setVisibility(View.VISIBLE);
            emptyTransaction.setVisibility(View.GONE);

            if (incomeAndExpenseArrayList.isEmpty()) {
                emptyTransaction.setVisibility(View.VISIBLE);
                recentTransactionRecyclerView.setVisibility(View.GONE);
            } else {
                recentTransactionRecyclerView.setVisibility(View.VISIBLE);
                emptyTransaction.setVisibility(View.GONE);
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                homeAdapter = new HomeAdapter(getContext(), incomeAndExpenseArrayList, selected);
                recentTransactionRecyclerView.setLayoutManager(manager);
                recentTransactionRecyclerView.setAdapter(homeAdapter);
            }
        } else {
            incomeAndExpenseArrayList = new ArrayList<>();
            recentTransactionRecyclerView.setVisibility(View.GONE);
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

//    private static ArrayList<IncomeAndExpense> filterIncomeListByTodayDate(ArrayList<IncomeAndExpense> incomeAndExpenses) {
//        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
//        Date currentDate = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = sdf.format(currentDate);
//        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
//            if (incomeAndExpense.getDate().equals(formattedDate)) {
//                filteredList.add(incomeAndExpense);
//            }
//        }
//        return filteredList;
//    }

    private void init(View view) {
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
        showBalance = view.findViewById(R.id.show_balance);
        totalExpense = view.findViewById(R.id.total_expense);
        totalIncome = view.findViewById(R.id.total_income);
        emptyTransaction = view.findViewById(R.id.empty_transaction);
        markRead = view.findViewById(R.id.mark_read);
    }
}