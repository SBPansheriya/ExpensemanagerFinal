package com.kmsoft.expensemanager.Activity.Transaction;

import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
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
import com.kmsoft.expensemanager.Adapter.LegendAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FinancialReportActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ImageView financialLineChart, financialPieChart, back;
    Spinner spinner, spinner1;
    RecyclerView legendRecyclerview;
    View legendBox;
    TextView legendTitle;
    LegendAdapter legendAdapter;
    LinearLayout relativeLayout;
    LinearLayout pieLinear, pieLinear1, pieLinear2;
    TextView jan, feb, mar, apr, may, june, july, aug, sep, oct, nov, dec, previousYear, currantYear, nextYear, noData;
    TextView financialIncome, financialExpense, emptyTransaction, mon, tue, wed, thu, fri, sat, sun;
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
    String weekly;
    String yearly;
    int monthly;
    int totalAmount;
    String clickedDate;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_report);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();

        init();

        dbHelper = new DBHelper(FinancialReportActivity.this);

        setData();

        ArrayAdapter<String> selectTypeAdapter = new ArrayAdapter<>(FinancialReportActivity.this, R.layout.simple_spinner_item1, FinancialReportActivity.this.getResources().getStringArray(R.array.financial_report_type));
        selectTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(selectTypeAdapter);

        ArrayAdapter<String> financialCategoryAdapter = new ArrayAdapter<>(FinancialReportActivity.this, R.layout.simple_spinner_item1, FinancialReportActivity.this.getResources().getStringArray(R.array.financial_report_transaction_type));
        financialCategoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(financialCategoryAdapter);

        back.setOnClickListener(v -> onBackPressed());

        financialLineChart.setOnClickListener(v -> {
            imgClick = 1;
            financialLineChart.setBackgroundResource(R.drawable.selected_financial_pie_background);
            financialPieChart.setBackgroundResource(R.drawable.unselected_financial_pie_background);

            financialLineChart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.line_chart));
            financialPieChart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pie_chart_green));

            legendRecyclerview.setVisibility(View.GONE);
            pieLinear.setVisibility(View.GONE);
            pieLinear1.setVisibility(View.GONE);
            pieLinear2.setVisibility(View.GONE);
            pieChart.setVisibility(View.GONE);
            chart.setVisibility(View.VISIBLE);
        });

        financialPieChart.setOnClickListener(v -> {
            imgClick = 2;
            financialPieChart.setBackgroundResource(R.drawable.selected_financial_line_background);
            financialLineChart.setBackgroundResource(R.drawable.unselected_financial_line_backgound);

            financialLineChart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.line_chart_green));
            financialPieChart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pie_chart));

            relativeLayout.setVisibility(View.VISIBLE);
            legendRecyclerview.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.VISIBLE);
            chart.setVisibility(View.GONE);

            if (spinner.getSelectedItem().toString().equals("Week")) {
                pieLinear.setVisibility(View.VISIBLE);
            } else if (spinner.getSelectedItem().toString().equals("Month")) {
                pieLinear1.setVisibility(View.VISIBLE);
            } else if (spinner.getSelectedItem().toString().equals("Year")) {
                pieLinear2.setVisibility(View.VISIBLE);
            }
        });

        financialIncome.setOnClickListener(v -> {
            selected = "Income";
            click = 1;

            financialIncome.setBackgroundResource(R.drawable.selected_category);
            financialExpense.setBackgroundResource(R.drawable.unselected_category);

            financialIncome.setTextColor(ContextCompat.getColor(this, R.color.white));
            financialExpense.setTextColor(ContextCompat.getColor(this, R.color.black));
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
        });

        financialExpense.setOnClickListener(v -> {
            selected = "Expense";
            click = 2;

            financialExpense.setBackgroundResource(R.drawable.selected_category);
            financialIncome.setBackgroundResource(R.drawable.unselected_category);

            financialExpense.setTextColor(ContextCompat.getColor(this, R.color.white));
            financialIncome.setTextColor(ContextCompat.getColor(this, R.color.black));

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
        });

        previousYear.setOnClickListener(v -> {
            yearly = previousYear.getText().toString();
            previousYear.setTextColor(ContextCompat.getColor(this, R.color.green));
            currantYear.setTextColor(Color.BLACK);
            nextYear.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Year")) {
                populateChartWithYearIncomeData();
            } else {
                populateChartWithYearExpenseData();
            }
        });

        currantYear.setOnClickListener(v -> {
            yearly = currantYear.getText().toString();
            currantYear.setTextColor(ContextCompat.getColor(this, R.color.green));
            previousYear.setTextColor(Color.BLACK);
            nextYear.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Year")) {
                populateChartWithYearIncomeData();
            } else {
                populateChartWithYearExpenseData();
            }
        });

        nextYear.setOnClickListener(v -> {
            yearly = nextYear.getText().toString();
            nextYear.setTextColor(ContextCompat.getColor(this, R.color.green));
            previousYear.setTextColor(Color.BLACK);
            currantYear.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Year")) {
                populateChartWithYearIncomeData();
            } else {
                populateChartWithYearExpenseData();
            }
        });

        sun.setOnClickListener(v -> {
            sun.setTextColor(ContextCompat.getColor(this, R.color.green));
            mon.setTextColor(Color.BLACK);
            tue.setTextColor(Color.BLACK);
            wed.setTextColor(Color.BLACK);
            thu.setTextColor(Color.BLACK);
            fri.setTextColor(Color.BLACK);
            sat.setTextColor(Color.BLACK);
            weekly = "Sunday";
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            clickedDate = dateFormat1.format(calendar1.getTime());
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Week")) {
                populateChartWithWeekIncomeData();
            } else {
                populateChartWithWeekExpenseData();
            }
        });

        mon.setOnClickListener(v -> {
            weekly = "Monday";
            mon.setTextColor(ContextCompat.getColor(this, R.color.green));
            tue.setTextColor(Color.BLACK);
            wed.setTextColor(Color.BLACK);
            thu.setTextColor(Color.BLACK);
            fri.setTextColor(Color.BLACK);
            sat.setTextColor(Color.BLACK);
            sun.setTextColor(Color.BLACK);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar1.add(Calendar.DAY_OF_WEEK, 1);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            clickedDate = dateFormat1.format(calendar1.getTime());
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Week")) {
                populateChartWithWeekIncomeData();
            } else {
                populateChartWithWeekExpenseData();
            }
        });

        tue.setOnClickListener(v -> {
            tue.setTextColor(ContextCompat.getColor(this, R.color.green));
            mon.setTextColor(Color.BLACK);
            wed.setTextColor(Color.BLACK);
            thu.setTextColor(Color.BLACK);
            fri.setTextColor(Color.BLACK);
            sat.setTextColor(Color.BLACK);
            sun.setTextColor(Color.BLACK);
            weekly = "Tuesday";
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar1.add(Calendar.DAY_OF_WEEK, 2);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            clickedDate = dateFormat1.format(calendar1.getTime());
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Week")) {
                populateChartWithWeekIncomeData();
            } else {
                populateChartWithWeekExpenseData();
            }
        });

        wed.setOnClickListener(v -> {
            wed.setTextColor(ContextCompat.getColor(this, R.color.green));
            mon.setTextColor(Color.BLACK);
            tue.setTextColor(Color.BLACK);
            thu.setTextColor(Color.BLACK);
            fri.setTextColor(Color.BLACK);
            sat.setTextColor(Color.BLACK);
            sun.setTextColor(Color.BLACK);
            weekly = "Wednesday";
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar1.add(Calendar.DAY_OF_WEEK, 3);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            clickedDate = dateFormat1.format(calendar1.getTime());
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Week")) {
                populateChartWithWeekIncomeData();
            } else {
                populateChartWithWeekExpenseData();
            }
        });

        thu.setOnClickListener(v -> {
            thu.setTextColor(ContextCompat.getColor(this, R.color.green));
            mon.setTextColor(Color.BLACK);
            tue.setTextColor(Color.BLACK);
            wed.setTextColor(Color.BLACK);
            fri.setTextColor(Color.BLACK);
            sat.setTextColor(Color.BLACK);
            sun.setTextColor(Color.BLACK);
            weekly = "Thursday";
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar1.add(Calendar.DAY_OF_WEEK, 4);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            clickedDate = dateFormat1.format(calendar1.getTime());
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Week")) {
                populateChartWithWeekIncomeData();
            } else {
                populateChartWithWeekExpenseData();
            }
        });

        fri.setOnClickListener(v -> {
            fri.setTextColor(ContextCompat.getColor(this, R.color.green));
            mon.setTextColor(Color.BLACK);
            tue.setTextColor(Color.BLACK);
            wed.setTextColor(Color.BLACK);
            thu.setTextColor(Color.BLACK);
            sat.setTextColor(Color.BLACK);
            sun.setTextColor(Color.BLACK);
            weekly = "Friday";
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar1.add(Calendar.DAY_OF_WEEK, 5);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            clickedDate = dateFormat1.format(calendar1.getTime());
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Week")) {
                populateChartWithWeekIncomeData();
            } else {
                populateChartWithWeekExpenseData();
            }
        });

        sat.setOnClickListener(v -> {
            sat.setTextColor(ContextCompat.getColor(this, R.color.green));
            mon.setTextColor(Color.BLACK);
            tue.setTextColor(Color.BLACK);
            wed.setTextColor(Color.BLACK);
            thu.setTextColor(Color.BLACK);
            fri.setTextColor(Color.BLACK);
            sun.setTextColor(Color.BLACK);
            weekly = "Saturday";
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            calendar1.add(Calendar.DAY_OF_WEEK, 6);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            clickedDate = dateFormat1.format(calendar1.getTime());
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Week")) {
                populateChartWithWeekIncomeData();
            } else {
                populateChartWithWeekExpenseData();
            }
        });

        jan.setOnClickListener(v -> {
            monthly = 1;
            jan.setTextColor(ContextCompat.getColor(this, R.color.green));
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        feb.setOnClickListener(v -> {
            monthly = 2;
            feb.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        mar.setOnClickListener(v -> {
            monthly = 3;
            mar.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        apr.setOnClickListener(v -> {
            monthly = 3;
            apr.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        may.setOnClickListener(v -> {
            monthly = 3;
            may.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        june.setOnClickListener(v -> {
            monthly = 3;
            june.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        july.setOnClickListener(v -> {
            monthly = 3;
            july.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        aug.setOnClickListener(v -> {
            monthly = 3;
            aug.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        sep.setOnClickListener(v -> {
            monthly = 3;
            sep.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        oct.setOnClickListener(v -> {
            monthly = 3;
            oct.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        nov.setOnClickListener(v -> {
            monthly = 3;
            nov.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            dec.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        dec.setOnClickListener(v -> {
            monthly = 3;
            dec.setTextColor(ContextCompat.getColor(this, R.color.green));
            jan.setTextColor(Color.BLACK);
            feb.setTextColor(Color.BLACK);
            mar.setTextColor(Color.BLACK);
            apr.setTextColor(Color.BLACK);
            may.setTextColor(Color.BLACK);
            june.setTextColor(Color.BLACK);
            july.setTextColor(Color.BLACK);
            aug.setTextColor(Color.BLACK);
            sep.setTextColor(Color.BLACK);
            oct.setTextColor(Color.BLACK);
            nov.setTextColor(Color.BLACK);
            if (TextUtils.equals(selected, "Income") && TextUtils.equals(spinner.getSelectedItem().toString(), "Month")) {
                populateChartWithMonthIncomeData();
            } else {
                populateChartWithMonthExpenseData();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (pieChart.getVisibility() == View.VISIBLE && spinner.getSelectedItem().toString().equals("Week")) {
                    pieLinear.setVisibility(View.VISIBLE);
                    pieLinear1.setVisibility(View.GONE);
                    pieLinear2.setVisibility(View.GONE);
                } else if (pieChart.getVisibility() == View.VISIBLE && spinner.getSelectedItem().toString().equals("Month")) {
                    pieLinear1.setVisibility(View.VISIBLE);
                    pieLinear.setVisibility(View.GONE);
                    pieLinear2.setVisibility(View.GONE);
                } else if (pieChart.getVisibility() == View.VISIBLE && spinner.getSelectedItem().toString().equals("Year")) {
                    pieLinear2.setVisibility(View.VISIBLE);
                    pieLinear1.setVisibility(View.GONE);
                    pieLinear.setVisibility(View.GONE);
                } else {
                    pieLinear.setVisibility(View.GONE);
                    pieLinear1.setVisibility(View.GONE);
                    pieLinear2.setVisibility(View.GONE);
                }
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

        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeToday(incomeList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
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

        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeToday(expenseList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
        pieChart.invalidate();
        setupLineChart1(chart, days, entries, pieEntries);
    }

    private void populateChartWithWeekIncomeData() {

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

        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeWeek(incomeList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
        pieChart.invalidate();
        setupLineChart(chart, days, entries, pieEntries);
    }

    private void populateChartWithWeekExpenseData() {

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

        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeWeek(expenseList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
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

        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeMonth(incomeList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
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


        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeMonth(expenseList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
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

        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeYear(incomeList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
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

        ArrayList<PieEntry> pieEntries;
        pieEntries = categorizeYear(expenseList);
        if (pieEntries.isEmpty()) {
            pieChart.setCenterText("");
        } else {
            pieChart.setCenterText("₹" + totalAmount);
        }
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
        xAxis.setTextSize(11);
        Typeface typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_semibold);
        xAxis.setTypeface(typeface);
        chart.setDescription(null);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries, "Income");
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(9);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.green));
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.green));
        dataSet.setFillAlpha(30);
        dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.green));
        dataSet.setColor(ContextCompat.getColor(this, R.color.green));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        Description description = pieChart.getDescription();
        description.setText("");

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10);
        typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_extrabold);
        pieData.setValueTypeface(typeface);
        pieData.setValueTextColor(Color.WHITE);

        legend = pieChart.getLegend();
        legend.setEnabled(false);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < incomeAndExpenseArrayList.size(); i++) {
            for (int j = 0; j < pieEntries.size(); j++) {
                if (incomeAndExpenseArrayList.get(i).getCategoryName().equals(pieEntries.get(j).getLabel())) {
                    colors.add(incomeAndExpenseArrayList.get(i).getCategoryColor());
                    break;
                }
            }
        }
        pieDataSet.setColors(colors);

        if (pieEntries.isEmpty() && pieChart.getVisibility() == View.VISIBLE) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        legendRecyclerview.setLayoutManager(layoutManager);
        legendAdapter = new LegendAdapter(FinancialReportActivity.this, pieEntries, incomeAndExpenseArrayList);
        legendRecyclerview.setAdapter(legendAdapter);

        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(20f);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_extrabold);
        pieChart.setCenterTextTypeface(typeface);
    }

    private void setupLineChart1(LineChart chart, String[] days, ArrayList<Entry> entries, ArrayList<PieEntry> pieEntries) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        Typeface typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_extrabold);
        xAxis.setTypeface(typeface);
        chart.setDescription(null);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries, "Expense");
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(9);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.red));
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.red));
        dataSet.setFillAlpha(30);
        dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.red));
        dataSet.setColor(ContextCompat.getColor(this, R.color.red));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        Description description = pieChart.getDescription();
        description.setText("");

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10);
        typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_extrabold);
        pieData.setValueTypeface(typeface);
        pieData.setValueTextColor(Color.WHITE);

        legend = pieChart.getLegend();
        legend.setEnabled(false);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < incomeAndExpenseArrayList.size(); i++) {
            for (int j = 0; j < pieEntries.size(); j++) {
                if (incomeAndExpenseArrayList.get(i).getCategoryName().equals(pieEntries.get(j).getLabel())) {
                    colors.add(incomeAndExpenseArrayList.get(i).getCategoryColor());
                    break;
                }
            }
        }

        pieDataSet.setColors(colors);

        if (pieEntries.isEmpty() && pieChart.getVisibility() == View.VISIBLE) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);

        }

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        legendRecyclerview.setLayoutManager(layoutManager);
        legendAdapter = new LegendAdapter(FinancialReportActivity.this, pieEntries, incomeAndExpenseArrayList);
        legendRecyclerview.setAdapter(legendAdapter);

        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(20f);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        typeface = ResourcesCompat.getFont(FinancialReportActivity.this, R.font.lexenddeca_extrabold);
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
                int categoryColor = cursor.getInt(10);
                String incomeDescription = cursor.getString(11);
                String addAttachment = cursor.getString(12);
                String tag = cursor.getString(13);

                incomeAndExpense = new IncomeAndExpense(id, incomeAmount, currantDateTimeStamp, selectedDateTimeStamp, currentdate, incomeDate, incomeDay, incomeAddTime, categoryName, categoryImage, categoryColor, incomeDescription, addAttachment, tag);
                incomeAndExpenseArrayList.add(incomeAndExpense);

                incomeAndExpenseArrayList.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            } while (cursor.moveToNext());
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
                    if (filteredMonthIncomeList.isEmpty()) {
                        emptyTransaction.setVisibility(View.VISIBLE);
                        financialRecyclerView.setVisibility(View.GONE);
                    } else {
                        financialRecyclerView.setVisibility(View.VISIBLE);
                        emptyTransaction.setVisibility(View.GONE);
                        financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredMonthIncomeList, spinner1.getSelectedItem().toString(), selected);
                    }
                } else if (TextUtils.equals(selected, "Expense")) {
                    if (filteredMonthExpenseList.isEmpty()) {
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
                    if (filteredYearIncomeList.isEmpty()) {
                        emptyTransaction.setVisibility(View.VISIBLE);
                        financialRecyclerView.setVisibility(View.GONE);
                    } else {
                        financialRecyclerView.setVisibility(View.VISIBLE);
                        emptyTransaction.setVisibility(View.GONE);
                        financialAdapter = new FinancialAdapter(FinancialReportActivity.this, filteredYearIncomeList, spinner1.getSelectedItem().toString(), selected);
                    }
                } else if (TextUtils.equals(selected, "Expense")) {
                    if (filteredYearExpenseList.isEmpty()) {
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
        } else {
            incomeAndExpenseArrayList = new ArrayList<>();
            financialRecyclerView.setVisibility(View.GONE);
            emptyTransaction.setVisibility(View.VISIBLE);
        }
    }

    private static ArrayList<IncomeAndExpense> filterCategories(ArrayList<IncomeAndExpense> incomeAndExpenses, String type) {
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.WEEK_OF_YEAR, currentWeek);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int startMonth = calendar.get(Calendar.MONTH);
        int startYear = calendar.get(Calendar.YEAR);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            String entryDateString = incomeAndExpense.getDate();
            try {
                Date entryDate = sdf.parse(entryDateString);
                calendar.setTime(entryDate);

                if (calendar.get(Calendar.YEAR) == startYear && calendar.get(Calendar.MONTH) == startMonth &&
                        calendar.get(Calendar.DAY_OF_MONTH) >= startDay && calendar.get(Calendar.DAY_OF_MONTH) <= endDay) {
                    filteredList.add(incomeAndExpense);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return filteredList;
    }

    private static ArrayList<IncomeAndExpense> filterByCurrentMonth(ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentYearInt = calendar.get(Calendar.YEAR);
        String currentYearStr = String.valueOf(currentYearInt);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            String entryDate = incomeAndExpense.getDate();
            int entryMonth = Integer.parseInt(entryDate.split("/")[1]);
            int entryYear = Integer.parseInt(entryDate.split("/")[2]);
            String formattedYear = String.valueOf(entryYear);
            if (entryMonth == currentMonth && TextUtils.equals(currentYearStr, formattedYear)) {
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

    private ArrayList<PieEntry> categorizeToday(ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        Map<String, Float> categoryTotals = new HashMap<>();

        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(currentDate);
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenseArrayList) {
            String category = incomeAndExpense.getCategoryName();
            String entryDateString = incomeAndExpense.getDate();
            String amt = extractNumericPart(incomeAndExpense.getAmount());
            float amount = Float.parseFloat(amt);
            if (formattedDate.equals(entryDateString)) {
                if (categoryTotals.containsKey(category)) {
                    categoryTotals.put(category, categoryTotals.get(category) + amount);
                } else {
                    categoryTotals.put(category, amount);
                }
            }
        }
        totalAmount = getTotalAmount(categoryTotals);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        return entries;
    }

    private ArrayList<PieEntry> categorizeWeek(ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        Map<String, Float> categoryTotals = new HashMap<>();

        for (IncomeAndExpense incomeAndExpense : incomeAndExpenseArrayList) {
            String category = incomeAndExpense.getCategoryName();
            String entryDateString = incomeAndExpense.getDate();
            String amt = extractNumericPart(incomeAndExpense.getAmount());
            float amount = Float.parseFloat(amt);
            String entryDateName = incomeAndExpense.getDayName();
            if (entryDateName.equals(weekly) && clickedDate.equals(entryDateString)) {
                if (categoryTotals.containsKey(category)) {
                    categoryTotals.put(category, categoryTotals.get(category) + amount);
                } else {
                    categoryTotals.put(category, amount);
                }
            }
        }
        totalAmount = getTotalAmount(categoryTotals);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        return entries;
    }

    private ArrayList<PieEntry> categorizeMonth(ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        Map<String, Float> categoryTotals = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        int currentYearInt = calendar.get(Calendar.YEAR);
        String currentYearStr = String.valueOf(currentYearInt);
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenseArrayList) {
            String category = incomeAndExpense.getCategoryName();
            String amt = extractNumericPart(incomeAndExpense.getAmount());
            float amount = Float.parseFloat(amt);
            String entry = incomeAndExpense.getDate();
            int entryMonth = Integer.parseInt(entry.split("/")[1]);
            int entryYear = Integer.parseInt(entry.split("/")[2]);
            String formattedYear = String.valueOf(entryYear);

            if (entryMonth == monthly && TextUtils.equals(formattedYear, currentYearStr)) {
                if (categoryTotals.containsKey(category)) {
                    categoryTotals.put(category, categoryTotals.get(category) + amount);
                } else {
                    categoryTotals.put(category, amount);
                }
            }
        }
        totalAmount = getTotalAmount(categoryTotals);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        return entries;
    }

    private ArrayList<PieEntry> categorizeYear(ArrayList<IncomeAndExpense> incomeAndExpenseArrayList) {
        Map<String, Float> categoryTotals = new HashMap<>();

        for (IncomeAndExpense incomeAndExpense : incomeAndExpenseArrayList) {
            String category = incomeAndExpense.getCategoryName();
            String amt = extractNumericPart(incomeAndExpense.getAmount());
            float amount = Float.parseFloat(amt);
            String entry = incomeAndExpense.getDate();
            int entryYear = Integer.parseInt(entry.split("/")[2]);
            int formattedYear = Integer.parseInt(yearly);
            if (entryYear == formattedYear) {
                if (categoryTotals.containsKey(category)) {
                    categoryTotals.put(category, categoryTotals.get(category) + amount);
                } else {
                    categoryTotals.put(category, amount);
                }
            }
        }
        totalAmount = getTotalAmount(categoryTotals);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        return entries;
    }

    private int getTotalAmount(Map<String, Float> categoryTotals) {
        int totalAmount = 0;
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            totalAmount += entry.getValue();
        }
        return totalAmount;
    }

    private void setData() {
        Calendar calendarCurrent = Calendar.getInstance();
        int dayOfWeek = calendarCurrent.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = new DateFormatSymbols().getWeekdays();
        weekly = daysOfWeek[dayOfWeek];
        int currentMonth = calendarCurrent.get(Calendar.MONTH) + 1;
        monthly = currentMonth;
        switch (currentMonth) {
            case 1:
                jan.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 2:
                feb.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 3:
                mar.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 4:
                apr.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 5:
                may.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 6:
                june.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 7:
                july.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 8:
                aug.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 9:
                sep.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 10:
                oct.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 11:
                nov.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 12:
                dec.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
        }
        TextView[] daysButtons = {sun, mon, tue, wed, thu, fri, sat};
        if (dayOfWeek >= Calendar.SUNDAY && dayOfWeek <= Calendar.SATURDAY) {
            daysButtons[dayOfWeek - 1].setTextColor(ContextCompat.getColor(this, R.color.green));
        }

        int currentYearInt = calendarCurrent.get(Calendar.YEAR);
        final String currentYearStr = String.valueOf(currentYearInt);

        calendarCurrent.add(Calendar.YEAR, -1);
        int previousYearInt = calendarCurrent.get(Calendar.YEAR);
        final String previousYearStr = String.valueOf(previousYearInt);

        calendarCurrent.add(Calendar.YEAR, 2);
        int nextYearInt = calendarCurrent.get(Calendar.YEAR);
        final String nextYearStr = String.valueOf(nextYearInt);

        currantYear.setText(currentYearStr);
        previousYear.setText(previousYearStr);
        nextYear.setText(nextYearStr);
        currantYear.setTextColor(ContextCompat.getColor(this, R.color.green));

        yearly = currentYearStr;

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat11 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        clickedDate = dateFormat11.format(calendar1.getTime());

        selected = "Income";

        spinner.setSelection(0);
        spinner1.setSelection(0);

        financialLineChart.setBackgroundResource(R.drawable.selected_financial_pie_background);
        financialPieChart.setBackgroundResource(R.drawable.unselected_financial_pie_background);
        financialLineChart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.line_chart));
        financialPieChart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pie_chart_green));

        financialIncome.setBackgroundResource(R.drawable.selected_category);
        financialExpense.setBackgroundResource(R.drawable.unselected_category);
        financialIncome.setTextColor(ContextCompat.getColor(this, R.color.white));
        financialExpense.setTextColor(ContextCompat.getColor(this, R.color.black));

        if (imgClick == 1) {
            legendRecyclerview.setVisibility(View.GONE);
        }
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
        pieLinear = findViewById(R.id.pie_linear);
        pieLinear1 = findViewById(R.id.pie_linear1);
        pieLinear2 = findViewById(R.id.pie_linear2);
        relativeLayout = findViewById(R.id.relativeLayout);
        back = findViewById(R.id.back);
        noData = findViewById(R.id.no_data);
        previousYear = findViewById(R.id.previous_year);
        currantYear = findViewById(R.id.currant_year);
        nextYear = findViewById(R.id.next_year);
        legendBox = findViewById(R.id.legendBox);
        legendTitle = findViewById(R.id.legendTitle);
        legendRecyclerview = findViewById(R.id.legend_recyclerview);
        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);
        jan = findViewById(R.id.jan);
        feb = findViewById(R.id.feb);
        mar = findViewById(R.id.mar);
        apr = findViewById(R.id.apr);
        may = findViewById(R.id.may);
        june = findViewById(R.id.june);
        july = findViewById(R.id.july);
        aug = findViewById(R.id.aug);
        sep = findViewById(R.id.sep);
        oct = findViewById(R.id.oct);
        nov = findViewById(R.id.nov);
        dec = findViewById(R.id.dec);
    }
}