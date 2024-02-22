package com.kmsoft.expensemanager.Fragment;

import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.Activity.Transaction.DetailsTransactionActivity;
import com.kmsoft.expensemanager.Activity.Transaction.FinancialReportActivity;
import com.kmsoft.expensemanager.Adapter.DateAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.Model.ListDateModel;
import com.kmsoft.expensemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class TransactionFragment extends Fragment {

    ImageView calender, filter, img;
    TextView see_financial_txt, emptyTransaction;
    IncomeAndExpense incomeAndExpense;
    RecyclerView dateRecyclerview;
    DateAdapter dateAdapter;
    ArrayList<IncomeAndExpense> incomeList;
    ArrayList<IncomeAndExpense> expenseList;
    DBHelper dbHelper;
    String filterBy;
    String sortBy;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREFS_NAME = "MyPrefsFile";
    String FILTER_KEY = "filter_by";
    String SORT_KEY = "sort_by";
    String date;
    int resetData = 0;
    int click = 0;
    String newDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        init(view);

        dbHelper = new DBHelper(getContext());

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(SORT_KEY, "");
        editor.putString(FILTER_KEY, "");
        editor.commit();

        filterBy = "All";
        sortBy = "Newest";

        calender.setOnClickListener(v -> showCalenderBottomDialog());

        see_financial_txt.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FinancialReportActivity.class);
            startActivity(intent);
        });

        filter.setOnClickListener(v -> showFilterBottomDialog());

        img.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailsTransactionActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Display();
    }

    private void showCalenderBottomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calender_bottomsheet_layout);
        dialog.setCancelable(false);
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);
        TextView reset = dialog.findViewById(R.id.reset);
        CalendarView calendarView = dialog.findViewById(R.id.trans_calenderView);

        reset.setVisibility(View.VISIBLE);

        if (date != null && resetData == 0) {
            Date date1 = parseDate(date);
            if (date1 != null) {
                calendarView.setDate(date1.getTime());
            }
        }

        if (resetData == -1){
            Date date1 = parseDate(newDate);
            if (date1 != null) {
                calendarView.setDate(date1.getTime());
            }
        }

        Calendar selectedDateCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDateCalendar.set(year, month, dayOfMonth);
            date = dateFormat1.format(selectedDateCalendar.getTime());
        });

        reset.setOnClickListener(v -> {
            click = 0;
            resetData = 1;
            date = null;
            Display();
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> {
            resetData = -1;
            dialog.dismiss();
        });

        ok.setOnClickListener(v -> {
            if (TextUtils.isEmpty(date)) {
                Calendar selectedDateCalendar1 = Calendar.getInstance();
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                date = dateFormat2.format(selectedDateCalendar1.getTime());
            }

            newDate = dateFormat1.format(selectedDateCalendar.getTime());
            click = 1;
            resetData = 0;
            Display();
            dialog.dismiss();
        });
    }

    private Date parseDate(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showFilterBottomDialog() {

        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        filterBy = sharedPreferences.getString(FILTER_KEY, "");
        sortBy = sharedPreferences.getString(SORT_KEY, "");

        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_bottomsheet_layout);
        dialog.show();

        TextView all = dialog.findViewById(R.id.trans_all);
        TextView income = dialog.findViewById(R.id.trans_income);
        TextView expense = dialog.findViewById(R.id.trans_expense);
        TextView highest = dialog.findViewById(R.id.trans_highest);
        TextView lowest = dialog.findViewById(R.id.trans_lowest);
        TextView newest = dialog.findViewById(R.id.trans_newest);
        TextView oldest = dialog.findViewById(R.id.trans_oldest);
        TextView reset = dialog.findViewById(R.id.trans_reset);
        Button apply = dialog.findViewById(R.id.trans_apply);

        switch (filterBy) {
            case "All":
                all.setBackgroundResource(R.drawable.selected_border_background);
                all.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                income.setBackgroundResource(R.drawable.unselected_border_background);
                income.setTextColor(Color.BLACK);
                expense.setBackgroundResource(R.drawable.unselected_border_background);
                expense.setTextColor(Color.BLACK);
                break;
            case "Income":
                income.setBackgroundResource(R.drawable.selected_border_background);
                income.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                all.setBackgroundResource(R.drawable.unselected_border_background);
                all.setTextColor(Color.BLACK);
                expense.setBackgroundResource(R.drawable.unselected_border_background);
                expense.setTextColor(Color.BLACK);
                break;
            case "Expense":
                expense.setBackgroundResource(R.drawable.selected_border_background);
                expense.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                all.setBackgroundResource(R.drawable.unselected_border_background);
                all.setTextColor(Color.BLACK);
                income.setBackgroundResource(R.drawable.unselected_border_background);
                income.setTextColor(Color.BLACK);
                break;
            default:
                all.setBackgroundResource(R.drawable.selected_border_background);
                all.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                income.setBackgroundResource(R.drawable.unselected_border_background);
                income.setTextColor(Color.BLACK);
                expense.setBackgroundResource(R.drawable.unselected_border_background);
                expense.setTextColor(Color.BLACK);
                break;
        }

        switch (sortBy) {
            case "Highest":
                highest.setBackgroundResource(R.drawable.selected_border_background);
                highest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                newest.setBackgroundResource(R.drawable.unselected_border_background);
                newest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
                break;
            case "Lowest":
                lowest.setBackgroundResource(R.drawable.selected_border_background);
                lowest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                highest.setBackgroundResource(R.drawable.unselected_border_background);
                highest.setTextColor(Color.BLACK);

                newest.setBackgroundResource(R.drawable.unselected_border_background);
                newest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
                break;
            case "Newest":
                newest.setBackgroundResource(R.drawable.selected_border_background);
                newest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                highest.setBackgroundResource(R.drawable.unselected_border_background);
                highest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
                break;
            case "Oldest":
                oldest.setBackgroundResource(R.drawable.selected_border_background);
                oldest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                newest.setBackgroundResource(R.drawable.unselected_border_background);
                newest.setTextColor(Color.BLACK);

                highest.setBackgroundResource(R.drawable.unselected_border_background);
                highest.setTextColor(Color.BLACK);
                break;
            default:
                newest.setBackgroundResource(R.drawable.selected_border_background);
                newest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                lowest.setBackgroundResource(R.drawable.unselected_border_background);
                lowest.setTextColor(Color.BLACK);

                highest.setBackgroundResource(R.drawable.unselected_border_background);
                highest.setTextColor(Color.BLACK);

                oldest.setBackgroundResource(R.drawable.unselected_border_background);
                oldest.setTextColor(Color.BLACK);
                break;
        }

        reset.setOnClickListener(v -> {
            filterBy = "All";
            sortBy = "Newest";
//            resetData = 1;
//            click = 0;
            all.setBackgroundResource(R.drawable.selected_border_background);
            all.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            income.setBackgroundResource(R.drawable.unselected_border_background);
            income.setTextColor(Color.BLACK);

            expense.setBackgroundResource(R.drawable.unselected_border_background);
            expense.setTextColor(Color.BLACK);

            newest.setBackgroundResource(R.drawable.selected_border_background);
            newest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

            lowest.setBackgroundResource(R.drawable.unselected_border_background);
            lowest.setTextColor(Color.BLACK);

            highest.setBackgroundResource(R.drawable.unselected_border_background);
            highest.setTextColor(Color.BLACK);

            oldest.setBackgroundResource(R.drawable.unselected_border_background);
            oldest.setTextColor(Color.BLACK);
        });

        all.setOnClickListener(v -> {
            filterBy = "All";

            all.setBackgroundResource(R.drawable.selected_border_background);
            income.setBackgroundResource(R.drawable.unselected_border_background);
            expense.setBackgroundResource(R.drawable.unselected_border_background);

            all.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            income.setTextColor(Color.BLACK);
            expense.setTextColor(Color.BLACK);

        });

        income.setOnClickListener(v -> {
            filterBy = "Income";

            income.setBackgroundResource(R.drawable.selected_border_background);
            all.setBackgroundResource(R.drawable.unselected_border_background);
            expense.setBackgroundResource(R.drawable.unselected_border_background);

            income.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            all.setTextColor(Color.BLACK);
            expense.setTextColor(Color.BLACK);
        });

        expense.setOnClickListener(v -> {
            filterBy = "Expense";

            expense.setBackgroundResource(R.drawable.selected_border_background);
            all.setBackgroundResource(R.drawable.unselected_border_background);
            income.setBackgroundResource(R.drawable.unselected_border_background);

            expense.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            all.setTextColor(Color.BLACK);
            income.setTextColor(Color.BLACK);
        });

        newest.setOnClickListener(v -> {
            sortBy = "Newest";

            newest.setBackgroundResource(R.drawable.selected_border_background);
            newest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

            lowest.setBackgroundResource(R.drawable.unselected_border_background);
            lowest.setTextColor(Color.BLACK);

            highest.setBackgroundResource(R.drawable.unselected_border_background);
            highest.setTextColor(Color.BLACK);

            oldest.setBackgroundResource(R.drawable.unselected_border_background);
            oldest.setTextColor(Color.BLACK);
        });

        oldest.setOnClickListener(v -> {
            sortBy = "Oldest";

            oldest.setBackgroundResource(R.drawable.selected_border_background);
            oldest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

            lowest.setBackgroundResource(R.drawable.unselected_border_background);
            lowest.setTextColor(Color.BLACK);

            newest.setBackgroundResource(R.drawable.unselected_border_background);
            newest.setTextColor(Color.BLACK);

            highest.setBackgroundResource(R.drawable.unselected_border_background);
            highest.setTextColor(Color.BLACK);
        });

        highest.setOnClickListener(v -> {
            sortBy = "Highest";

            highest.setBackgroundResource(R.drawable.selected_border_background);
            highest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

            lowest.setBackgroundResource(R.drawable.unselected_border_background);
            lowest.setTextColor(Color.BLACK);

            newest.setBackgroundResource(R.drawable.unselected_border_background);
            newest.setTextColor(Color.BLACK);

            oldest.setBackgroundResource(R.drawable.unselected_border_background);
            oldest.setTextColor(Color.BLACK);
        });

        lowest.setOnClickListener(v -> {
            sortBy = "Lowest";

            lowest.setBackgroundResource(R.drawable.selected_border_background);
            lowest.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

            highest.setBackgroundResource(R.drawable.unselected_border_background);
            highest.setTextColor(Color.BLACK);

            newest.setBackgroundResource(R.drawable.unselected_border_background);
            newest.setTextColor(Color.BLACK);

            oldest.setBackgroundResource(R.drawable.unselected_border_background);
            oldest.setTextColor(Color.BLACK);
        });

        apply.setOnClickListener(v -> {
            if (filterBy.isEmpty()) {
                filterBy = "All";
            }
            if (sortBy.isEmpty()) {
                sortBy = "Newest";
            }
            editor.putString(FILTER_KEY, filterBy).apply();
            editor.putString(SORT_KEY, sortBy).apply();
            Display();
            dialog.dismiss();
        });
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

            }
            while (cursor.moveToNext());
            incomeList = filterCategories(incomeAndExpenseArrayList, "Income");
            expenseList = filterCategories(incomeAndExpenseArrayList, "Expense");

            LinearLayoutManager manager = new LinearLayoutManager(getContext());

            switch (filterBy) {
                case "All": {
                    if (sortBy.equals("Oldest")) {
                        incomeAndExpenseArrayList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));
                    } else if (sortBy.equals("Newest")) {
                        incomeAndExpenseArrayList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));
                        Collections.reverse(incomeAndExpenseArrayList);
                    } else {
                        incomeAndExpenseArrayList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));
                        Collections.reverse(incomeAndExpenseArrayList);
                    }

                    if (click == 1) {
                        incomeAndExpenseArrayList = filterDataByDate(incomeAndExpenseArrayList, date);
                    }

                    ArrayList<ListDateModel> listDateModels = new ArrayList<>();
                    String date = "";
                    for (int i = 0; i < incomeAndExpenseArrayList.size(); i++) {
                        String convertDate = convertTimestampToDateString(incomeAndExpenseArrayList.get(i).getSelectedDateTimeStamp(), "dd/MM/yyyy");
                        if (!convertDate.equalsIgnoreCase(date)) {
                            date = convertDate;
                            ArrayList<IncomeAndExpense> newList = (ArrayList<IncomeAndExpense>) incomeAndExpenseArrayList.stream().filter(model -> model.getDate().equalsIgnoreCase(convertDate)).collect(Collectors.toList());

                            ListDateModel listDateModel = new ListDateModel();
                            listDateModel.incomeAndExpenseArrayList = newList;

                            listDateModels.add(listDateModel);
                        }
                    }
                    if (incomeAndExpenseArrayList.isEmpty()) {
                        emptyTransaction.setVisibility(View.VISIBLE);
                        dateRecyclerview.setVisibility(View.GONE);
                    } else {
                        dateRecyclerview.setVisibility(View.VISIBLE);
                        emptyTransaction.setVisibility(View.GONE);
                        dateAdapter = new DateAdapter(TransactionFragment.this.getContext(), listDateModels, sortBy);
                        dateRecyclerview.setLayoutManager(manager);
                        dateRecyclerview.setAdapter(dateAdapter);
                    }
                    break;
                }
                case "Income": {
                    if (sortBy.equals("Oldest")) {
                        incomeList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));

                    } else if (sortBy.equals("Newest")) {
                        incomeList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));

                        Collections.reverse(incomeList);
                    } else {
                        incomeList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));
                        Collections.reverse(incomeList);
                    }

                    if (click == 1) {
                        incomeList = filterDataByDate(incomeList, date);
                    }

                    ArrayList<ListDateModel> listDateModels = new ArrayList<>();
                    String date = "";
                    for (int i = 0; i < incomeList.size(); i++) {
                        String convertDate = convertTimestampToDateString(incomeList.get(i).getSelectedDateTimeStamp(), "dd/MM/yyyy");
                        if (!convertDate.equalsIgnoreCase(date)) {
                            date = convertDate;
                            ArrayList<IncomeAndExpense> newList = (ArrayList<IncomeAndExpense>) incomeList.stream().filter(model -> model.getDate().equalsIgnoreCase(convertDate)).collect(Collectors.toList());

                            ListDateModel listDateModel = new ListDateModel();
                            listDateModel.incomeAndExpenseArrayList = newList;

                            listDateModels.add(listDateModel);
                        }
                    }
                    if (incomeList.isEmpty()) {
                        emptyTransaction.setVisibility(View.VISIBLE);
                        dateRecyclerview.setVisibility(View.GONE);
                    } else {
                        dateRecyclerview.setVisibility(View.VISIBLE);
                        emptyTransaction.setVisibility(View.GONE);
                        dateAdapter = new DateAdapter(TransactionFragment.this.getContext(), listDateModels, sortBy);
                        dateRecyclerview.setLayoutManager(manager);
                        dateRecyclerview.setAdapter(dateAdapter);
                    }
                    break;
                }
                case "Expense": {
                    if (sortBy.equals("Oldest")) {
                        expenseList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));
                    } else if (sortBy.equals("Newest")) {
                        expenseList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));

                        Collections.reverse(expenseList);
                    } else {
                        expenseList.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));

                        Collections.reverse(expenseList);
                    }

                    if (click == 1) {
                        expenseList = filterDataByDate(expenseList, date);
                    }

                    ArrayList<ListDateModel> listDateModels = new ArrayList<>();
                    String date = "";
                    for (int i = 0; i < expenseList.size(); i++) {
                        String convertDate = convertTimestampToDateString(expenseList.get(i).getSelectedDateTimeStamp(), "dd/MM/yyyy");
                        if (!convertDate.equalsIgnoreCase(date)) {
                            date = convertDate;
                            ArrayList<IncomeAndExpense> newList = (ArrayList<IncomeAndExpense>) expenseList.stream().filter(model -> model.getDate().equalsIgnoreCase(convertDate)).collect(Collectors.toList());

                            ListDateModel listDateModel = new ListDateModel();
                            listDateModel.incomeAndExpenseArrayList = newList;

                            listDateModels.add(listDateModel);
                        }
                    }
                    if (expenseList.isEmpty()) {
                        emptyTransaction.setVisibility(View.VISIBLE);
                        dateRecyclerview.setVisibility(View.GONE);
                    } else {
                        dateRecyclerview.setVisibility(View.VISIBLE);
                        emptyTransaction.setVisibility(View.GONE);
                        dateAdapter = new DateAdapter(TransactionFragment.this.getContext(), listDateModels, sortBy);
                        dateRecyclerview.setLayoutManager(manager);
                        dateRecyclerview.setAdapter(dateAdapter);
                    }
                    break;
                }
            }
//            }
//            else if (click == 1) {
//                ArrayList<IncomeAndExpense> incomeAndExpenseArrayList1 = filterDataByDate(incomeAndExpenseArrayList, date);
//
//                incomeAndExpenseArrayList1.sort(Comparator.comparing(IncomeAndExpense::getSelectedDateTimeStamp));
//
//                Collections.reverse(incomeAndExpenseArrayList1);
//
//                ArrayList<ListDateModel> listDateModels = new ArrayList<>();
//                String date = "";
//                for (int i = 0; i < incomeAndExpenseArrayList1.size(); i++) {
//                    String convertDate = convertTimestampToDateString(incomeAndExpenseArrayList1.get(i).getSelectedDateTimeStamp(), "dd/MM/yyyy");
//                    if (!convertDate.equalsIgnoreCase(date)) {
//                        date = convertDate;
//                        ArrayList<IncomeAndExpense> newList = (ArrayList<IncomeAndExpense>) incomeAndExpenseArrayList1.stream().filter(model -> model.getDate().equalsIgnoreCase(convertDate)).collect(Collectors.toList());
//
//                        ListDateModel listDateModel = new ListDateModel();
//                        listDateModel.incomeAndExpenseArrayList = newList;
//
//                        listDateModels.add(listDateModel);
//                    }
//                }
//                if (listDateModels.isEmpty()) {
//                    emptyTransaction.setVisibility(View.VISIBLE);
//                    dateRecyclerview.setVisibility(View.GONE);
//                } else {
//                    dateRecyclerview.setVisibility(View.VISIBLE);
//                    emptyTransaction.setVisibility(View.GONE);
//                    dateAdapter = new DateAdapter(TransactionFragment.this.getContext(), listDateModels, sortBy);
//                    dateRecyclerview.setLayoutManager(manager);
//                    dateRecyclerview.setAdapter(dateAdapter);
//                }
//            }
        } else {
            incomeAndExpenseArrayList = new ArrayList<>();
            dateRecyclerview.setVisibility(View.GONE);
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

    private ArrayList<IncomeAndExpense> filterDataByDate(ArrayList<IncomeAndExpense> incomeAndExpenses, String targetDate) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (TextUtils.isEmpty(targetDate)) {
                Date currentDate = new Date();
                targetDate = sdf.format(currentDate);
            }
            Date targetDateObj = sdf.parse(targetDate);
            for (IncomeAndExpense data : incomeAndExpenses) {
                Date currentDateObj = sdf.parse(data.getDate());
                if (currentDateObj.equals(targetDateObj)) {
                    filteredList.add(data);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return filteredList;
    }


    public String convertTimestampToDateString(double timestamp, String format) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
            long time = (long) (timestamp * 1000);
            Date date = new Date(time);

            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void init(View view) {
        calender = view.findViewById(R.id.trans_calender);
        filter = view.findViewById(R.id.trans_filter);
        img = view.findViewById(R.id.img);
        see_financial_txt = view.findViewById(R.id.see_financial_txt);
        dateRecyclerview = view.findViewById(R.id.date_recyclerview);
        emptyTransaction = view.findViewById(R.id.empty_transaction);
    }
}