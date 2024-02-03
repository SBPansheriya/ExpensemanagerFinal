package com.kmsoft.expensemanager.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kmsoft.expensemanager.Activity.Budget.CreateBudgetActivity;
import com.kmsoft.expensemanager.Adapter.BudgetCreateAdapter;
import com.kmsoft.expensemanager.R;

public class BudgetFragment extends Fragment {

    Button createBudget;
    RecyclerView budgetRecyclerview;
    BudgetCreateAdapter budgetCreateAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget,container,false);

        init(view);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        budgetCreateAdapter = new BudgetCreateAdapter(BudgetFragment.this);
        budgetRecyclerview.setLayoutManager(manager);
        budgetRecyclerview.setAdapter(budgetCreateAdapter);


        createBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateBudgetActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void init(View view){
        createBudget = view.findViewById(R.id.create_budget);
        budgetRecyclerview = view.findViewById(R.id.budget_recyclerview);
    }
}