package com.kmsoft.expensemanager.Fragment;

<<<<<<< Updated upstream
import static com.kmsoft.expensemanager.Constant.budgetArrayList;
import static com.kmsoft.expensemanager.Constant.categoryArrayList;

=======
<<<<<<< HEAD

import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.app.Activity;
=======
import static com.kmsoft.expensemanager.Constant.budgetArrayList;
import static com.kmsoft.expensemanager.Constant.categoryArrayList;

>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmsoft.expensemanager.Activity.Budget.CreateBudgetActivity;
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
import com.kmsoft.expensemanager.Adapter.AddCategoryIncomeAdapter;
import com.kmsoft.expensemanager.Adapter.BudgetCreateAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
<<<<<<< Updated upstream
import com.kmsoft.expensemanager.Model.Category;
=======
<<<<<<< HEAD
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
=======
import com.kmsoft.expensemanager.Model.Category;
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    ActivityResultLauncher<Intent> launchSomeActivity;
    Gson gson;
=======
>>>>>>> Stashed changes

>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        init(view);

        dbHelper = new DBHelper(BudgetFragment.this.getContext());
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String listGet = data.getStringExtra("budgetArrayList");
                    gson = new Gson();
                    budgetArrayList = gson.fromJson(listGet, new TypeToken<ArrayList<Budget>>() {}.getType());
                }
            }
        });
=======
>>>>>>> Stashed changes

>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
        createBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateBudgetActivity.class);
                gson = new Gson();
                String list = gson.toJson(budgetArrayList);
                intent.putExtra("budgetArrayList", list);
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
<<<<<<< Updated upstream
                    budgetCreateAdapter = new BudgetCreateAdapter(BudgetFragment.this, budgetArrayList);
=======
<<<<<<< HEAD
                    budgetCreateAdapter = new BudgetCreateAdapter(BudgetFragment.this, budgetArrayList,incomeAndExpenseArrayList);
=======
                    budgetCreateAdapter = new BudgetCreateAdapter(BudgetFragment.this, budgetArrayList);
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
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