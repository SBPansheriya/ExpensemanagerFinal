package com.kmsoft.expensemanager.Fragment;

import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

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
import com.kmsoft.expensemanager.Activity.Profile.EditProfileActivity;
import com.kmsoft.expensemanager.Activity.Profile.NotificationActivity;
import com.kmsoft.expensemanager.Adapter.RecentTransactionAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    TextView today, week, month, year, see_all, showBalance, totalIncome, totalExpense, emptyTransaction;
    LineChart chart;
    ImageView homeProfileImage, homeNotification;
    RecyclerView recentTransactionRecyclerView;
    RecentTransactionAdapter recentTransactionAdapter;
    Spinner spinner;
    String selected;
    String totalIncomeAmount, totalExpenseAmount;
    DBHelper dbHelper;
    IncomeAndExpense incomeAndExpense;
    ArrayList<IncomeAndExpense> incomeList = new ArrayList<>();
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();
    NestedScrollView nestedScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        dbHelper = new DBHelper(getContext());

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    Calendar calendar = Calendar.getInstance();
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    int currentMonth = calendar.get(Calendar.MONTH) + 1;
                    int currentYear = calendar.get(Calendar.YEAR);

                    double[] totalAmountPerHour = new double[24];

                    for (int i = 0; i < incomeList.size(); i++) {
                        String entryDate = incomeList.get(i).getDate();
                        int entryDay = Integer.parseInt(entryDate.split("/")[0]);
                        int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
                        int entryYear = Integer.parseInt(entryDate.split("/")[2]);

                        int hourOfDay = getHourOfDay(incomeList.get(i).getTime());
                        String amountString = extractNumericPart(incomeList.get(i).getAmount());
                        double amount = Double.parseDouble(amountString);
                        if (entryYear == currentYear && entryMonth == currentMonth && entryDay == currentDay) {
                            if (hourOfDay >= 0 && hourOfDay < 24) {
                                totalAmountPerHour[hourOfDay] += amount;
                            }
                        }
                    }

                    for (int i = 0; i < totalAmountPerHour.length; i++) {
                        entries.add(new Entry(i, (float) totalAmountPerHour[i]));
                    }
                    final String[] days = {"1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12AM","1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM", "12PM"};

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

                            Calendar calendar = Calendar.getInstance();
                            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                            int currentMonth = calendar.get(Calendar.MONTH) + 1;
                            int currentYear = calendar.get(Calendar.YEAR);

                            double[] totalAmountPerHour = new double[24];

                            for (int i = 0; i < incomeList.size(); i++) {
                                String entryDate = incomeList.get(i).getDate();
                                int entryDay = Integer.parseInt(entryDate.split("/")[0]);
                                int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
                                int entryYear = Integer.parseInt(entryDate.split("/")[2]);

                                int hourOfDay = getHourOfDay(incomeList.get(i).getTime());
                                String amountString = extractNumericPart(incomeList.get(i).getAmount());
                                double amount = Double.parseDouble(amountString);
                                if (entryYear == currentYear && entryMonth == currentMonth && entryDay == currentDay) {
                                    if (hourOfDay >= 0 && hourOfDay < 24) {
                                        totalAmountPerHour[hourOfDay] += amount;
                                    }
                                }
                            }

                            for (int i = 0; i < totalAmountPerHour.length; i++) {
                                entries.add(new Entry(i, (float) totalAmountPerHour[i]));
                            }
                            final String[] days = {"1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12AM","1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM", "12PM"};

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);

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

                            double[] totalAmountPerDay = new double[7];
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
                                    calendar.add(Calendar.DAY_OF_MONTH,1);
                                }
                            }

                            for (int i = 0; i < totalAmountPerDay.length; i++) {
                                entries.add(new Entry(i, (float) totalAmountPerDay[i]));
                            }
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

                            double[] totalAmountPerMonth = new double[12];

                            for (int i = 0; i < incomeList.size(); i++) {
                                int month = getMonthFromDateString(incomeList.get(i).getDate()); // Assuming getDate() returns a date string

                                String amountString = extractNumericPart(incomeList.get(i).getAmount());
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
                            int numYearsToShow = 5;
                            int latestYear = getLatestYearFromData(incomeList);
                            int year = 0;
                            double[] totalAmountPerYear = new double[numYearsToShow];

                            for (int i = 0; i < incomeList.size(); i++) {
                                year = getYearFromDateString(incomeList.get(i).getDate()); // Assuming getDate() returns a date string

                                String amountString = extractNumericPart(incomeList.get(i).getAmount());
                                double amount = Double.parseDouble(amountString);

                                if (year >= latestYear - numYearsToShow + 1 && year <= latestYear) {
                                    int index = latestYear - year;

                                    totalAmountPerYear[index] += amount;
                                }
                            }

                            ArrayList<String> xAxisLabels = new ArrayList<>();
                            for (int i = 0; i < numYearsToShow; i++) {
                                xAxisLabels.add(String.valueOf(latestYear - numYearsToShow + 1 + i));
                            }

                            for (int i = 0; i < totalAmountPerYear.length; i++) {
                                for (int j = 0; j < xAxisLabels.size(); j++) {
                                    if (year == latestYear - numYearsToShow + 1 + j) {
                                        entries.add(new Entry(j, (float) totalAmountPerYear[i]));
                                    }
                                }
                            }

                            XAxis xAxis = chart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

                            chart.invalidate();

                            chart.getAxisLeft().setEnabled(false);
                            chart.getAxisRight().setEnabled(false);

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
                        }
                    });
                } else if (selected.equals("Expense")) {
                    today.setBackgroundResource(R.drawable.selected_home_background);
                    week.setBackgroundResource(R.drawable.unselected_home_background);
                    month.setBackgroundResource(R.drawable.unselected_home_background);
                    year.setBackgroundResource(R.drawable.unselected_home_background);

                    today.setTextColor(getResources().getColor(R.color.white));
                    week.setTextColor(getResources().getColor(R.color.gray));
                    month.setTextColor(getResources().getColor(R.color.gray));
                    year.setTextColor(getResources().getColor(R.color.gray));

                    ArrayList<Entry> entries = new ArrayList<>();

                    Calendar calendar = Calendar.getInstance();
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    int currentMonth = calendar.get(Calendar.MONTH) + 1;
                    int currentYear = calendar.get(Calendar.YEAR);

                    double[] totalAmountPerHour = new double[24];

                    for (int i = 0; i < expenseList.size(); i++) {
                        String entryDate = expenseList.get(i).getDate();
                        int entryDay = Integer.parseInt(entryDate.split("/")[0]);
                        int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
                        int entryYear = Integer.parseInt(entryDate.split("/")[2]);

                        int hourOfDay = getHourOfDay(expenseList.get(i).getTime());
                        String amountString = extractNumericPart(expenseList.get(i).getAmount());
                        double amount = Double.parseDouble(amountString);
                        if (entryYear == currentYear && entryMonth == currentMonth && entryDay == currentDay) {
                            if (hourOfDay >= 0 && hourOfDay < 24) {
                                totalAmountPerHour[hourOfDay] += amount;
                            }
                        }
                    }

                    for (int i = 0; i < totalAmountPerHour.length; i++) {
                        entries.add(new Entry(i, (float) totalAmountPerHour[i]));
                    }
                    final String[] days = {"1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12AM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM", "12PM"};

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

                            double[] totalAmountPerHour = new double[24];

                            Calendar calendar = Calendar.getInstance();
                            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                            int currentMonth = calendar.get(Calendar.MONTH) + 1;
                            int currentYear = calendar.get(Calendar.YEAR);

                            for (int i = 0; i < expenseList.size(); i++) {
                                String entryDate = expenseList.get(i).getDate();
                                int entryDay = Integer.parseInt(entryDate.split("/")[0]);
                                int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
                                int entryYear = Integer.parseInt(entryDate.split("/")[2]);

                                int hourOfDay = getHourOfDay(expenseList.get(i).getTime());
                                String amountString = extractNumericPart(expenseList.get(i).getAmount());
                                double amount = Double.parseDouble(amountString);
                                if (entryYear == currentYear && entryMonth == currentMonth && entryDay == currentDay) {
                                    if (hourOfDay >= 0 && hourOfDay < 24) {
                                        totalAmountPerHour[hourOfDay] += amount;
                                    }
                                }
                            }

                            for (int i = 0; i < totalAmountPerHour.length; i++) {
                                entries.add(new Entry(i, (float) totalAmountPerHour[i]));
                            }
                            final String[] days = {"1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12AM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM", "12PM"};
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
                            double[] totalAmountPerDay = new double[7];

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
                                    calendar.add(Calendar.DAY_OF_MONTH,1);
                                }
                            }

                            for (int i = 0; i < totalAmountPerDay.length; i++) {
                                entries.add(new Entry(i, (float) totalAmountPerDay[i]));
                            }

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
                Display();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private int getHourOfDay(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            try {
                int hour = Integer.parseInt(parts[0]);
                if (hour >= 0 && hour <= 12) {
                    int minute = Integer.parseInt(parts[1].split("\\s+")[0]); // Extracting minutes
                    String period = parts[1].split("\\s+")[1]; // Extracting AM/PM

                    if (period.equalsIgnoreCase("AM")) {
                        // If hour is 12 in AM, convert it to 0
                        if (hour == 12) {
                            return 0;
                        }
                    } else if (period.equalsIgnoreCase("PM")) {
                        // If hour is less than 12 in PM, add 12 to it
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


    public int getLatestYearFromData(ArrayList<IncomeAndExpense> incomeList) {
        int latestYear = Integer.MIN_VALUE; // Initialize with the smallest integer value

        for (IncomeAndExpense income : incomeList) {
            String date = income.getDate();
            int year = getYearFromDateString(date);
            if (year > latestYear) {
                latestYear = year;
            }
        }
        return latestYear;
    }

    public int getYearFromDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Adjust the format as per your date string
        try {
            Date date = dateFormat.parse(dateString);

            int year = date.getYear() + 1900; // Add 1900 to get the actual year
            return year;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getDayIndex(String dayName) {
        switch (dayName) {
            case "Mon":
                return 0;
            case "Tue":
                return 1;
            case "Wed":
                return 2;
            case "Thu":
                return 3;
            case "Fri":
                return 4;
            case "Sat":
                return 5;
            case "Sun":
                return 6;
            default:
                return -1; // Invalid day name
        }
    }

    public int getMonthFromDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = dateFormat.parse(dateString);

            // Get the month from the Date object
            // Note: Month starts from 0 (January) to 11 (December)
            // You may want to add 1 to make it start from 1 to 12
            int month = date.getMonth() + 1;
            return month;
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return -1; // Return
        }
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    @Override
    public void onResume() {
        super.onResume();
        Display();
        calculateTotalExpense();
        calculateTotalIncome();
        calculateTotalIncomeAndExpense();
    }

    private void setupLineChart(LineChart chart, String[] days, ArrayList<Entry> entries) {

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

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
    }

    private void setupLineChart1(LineChart chart, String[] days, ArrayList<Entry> entries) {

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

    public BigDecimal calculateTotalIncome() {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (IncomeAndExpense amount : incomeList) {
            String amountString = amount.getAmount();
            BigDecimal numericAmount = extractNumericAmount(amountString);
            totalSum = totalSum.add(numericAmount);
        }
        totalIncomeAmount = totalSum.toString();
        totalIncome.setText("₹" + totalSum);
        return totalSum;
    }

    public BigDecimal calculateTotalExpense() {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (IncomeAndExpense amount : expenseList) {
            String amountString = amount.getAmount();
            BigDecimal numericAmount = extractNumericAmount(amountString);
            totalSum = totalSum.add(numericAmount);
        }
        totalExpenseAmount = totalSum.toString();
        totalExpense.setText("₹" + totalSum);
        return totalSum;
    }

    public String calculateTotalIncomeAndExpense() {
        BigDecimal totalIncome = calculateTotalIncome();
        BigDecimal totalExpense = calculateTotalExpense();
        BigDecimal total = totalIncome.add(totalExpense);
        showBalance.setText("₹" + total.toString());
        return total.toString();
    }

    private BigDecimal extractNumericAmount(String amount) {
        String numericString = amount.substring(1);
        return new BigDecimal(numericString);
    }

    private void Display() {

        Cursor cursor = dbHelper.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            incomeAndExpenseArrayList = new ArrayList<>();
            do {
                int id = cursor.getInt(0);
                String incomeAmount = cursor.getString(1);
                String incomeDate = cursor.getString(2);
                String incomeDay = cursor.getString(3);
                String incomeAddTime = cursor.getString(4);
                String categoryName = cursor.getString(5);
                int categoryImage = cursor.getInt(6);
                String incomeDescription = cursor.getString(7);
                String addAttachment = cursor.getString(8);
                String tag = cursor.getString(9);

                incomeAndExpense = new IncomeAndExpense(id, incomeAmount, incomeDate, incomeDay, incomeAddTime, categoryName, categoryImage, incomeDescription, addAttachment, tag);
                incomeAndExpenseArrayList.add(incomeAndExpense);

                incomeList = filterCategories(incomeAndExpenseArrayList, "Income");
                expenseList = filterCategories(incomeAndExpenseArrayList, "Expense");

                recentTransactionRecyclerView.setVisibility(View.VISIBLE);
                emptyTransaction.setVisibility(View.GONE);

                if (TextUtils.equals(selected, "Income")) {
                    if (incomeList.isEmpty()) {
                        emptyTransaction.setVisibility(View.VISIBLE);
                        recentTransactionRecyclerView.setVisibility(View.GONE);
                    } else {
                        recentTransactionRecyclerView.setVisibility(View.VISIBLE);
                        emptyTransaction.setVisibility(View.GONE);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recentTransactionAdapter = new RecentTransactionAdapter(HomeFragment.this, incomeList);
                        recentTransactionRecyclerView.setLayoutManager(manager);
                        recentTransactionRecyclerView.setAdapter(recentTransactionAdapter);
                    }
                } else if (TextUtils.equals(selected, "Expense")) {
                    if (expenseList.isEmpty()) {
                        emptyTransaction.setVisibility(View.VISIBLE);
                        recentTransactionRecyclerView.setVisibility(View.GONE);
                    } else {
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recentTransactionAdapter = new RecentTransactionAdapter(HomeFragment.this, expenseList);
                        recentTransactionRecyclerView.setLayoutManager(manager);
                        recentTransactionRecyclerView.setAdapter(recentTransactionAdapter);
                    }
                }
            } while (cursor.moveToNext());
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
    }
}