package com.kmsoft.expensemanager.Fragment;

import static com.kmsoft.expensemanager.Constant.budgetArrayList;
import static com.kmsoft.expensemanager.Constant.categoryArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kmsoft.expensemanager.Activity.Budget.CreateBudgetActivity;
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
import com.kmsoft.expensemanager.Adapter.AddCategoryIncomeAdapter;
import com.kmsoft.expensemanager.Adapter.BudgetCreateAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class BudgetFragment extends Fragment {

    DBHelper dbHelper;
    Button createBudget;
    TextView emptyBudget;
    RecyclerView budgetRecyclerview;
    BudgetCreateAdapter budgetCreateAdapter;
    Budget budget;
    ArrayList<Budget> budgetArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        init(view);

        dbHelper = new DBHelper(BudgetFragment.this.getContext());

        createBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateBudgetActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Display();
    }

    private void Display() {
        Cursor cursor = dbHelper.getAllBudgetData();
        if (cursor != null && cursor.moveToFirst()) {
            budgetArrayList = new ArrayList<>();
            do {
                int id = cursor.getInt(0);
                String amountBudget = cursor.getString(1);
                String categoryNameBudget = cursor.getString(2);
                int categoryImageBudget = cursor.getInt(3);
                int percentageBudget = cursor.getInt(4);

                budget = new Budget(id, amountBudget, categoryNameBudget, categoryImageBudget, percentageBudget);
                budgetArrayList.add(budget);

                if (budgetArrayList.isEmpty()) {
                    budgetRecyclerview.setVisibility(View.GONE);
                    emptyBudget.setVisibility(View.VISIBLE);
                } else {
                    budgetRecyclerview.setVisibility(View.VISIBLE);
                    emptyBudget.setVisibility(View.GONE);

                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    budgetCreateAdapter = new BudgetCreateAdapter(BudgetFragment.this, budgetArrayList);
                    budgetRecyclerview.setLayoutManager(manager);
                    budgetRecyclerview.setAdapter(budgetCreateAdapter);
                }
            } while (cursor.moveToNext());
        } else {
            budgetArrayList = new ArrayList<>();
            budgetRecyclerview.setVisibility(View.GONE);
            emptyBudget.setVisibility(View.VISIBLE);
        }
    }

    private void init(View view) {
        createBudget = view.findViewById(R.id.create_budget);
        budgetRecyclerview = view.findViewById(R.id.budget_recyclerview);
        emptyBudget = view.findViewById(R.id.empty_budget);
    }
}