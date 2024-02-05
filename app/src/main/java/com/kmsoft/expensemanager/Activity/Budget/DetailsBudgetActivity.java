package com.kmsoft.expensemanager.Activity.Budget;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class DetailsBudgetActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView showCategoryNameBudget, amountBudget;
    ImageView showExceedAmount, back, delete, showCategoryImageBudget;
    Slider showPercentage;
    Button showEditBudget;
    Budget budget;
    ActivityResultLauncher<Intent> launchSomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(DetailsBudgetActivity.this);

        budget = (Budget) getIntent().getSerializableExtra("budget");
        setData();
        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    budget = (Budget) data.getSerializableExtra("budget");
                    if (budget != null){
                        setData();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showEditBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsBudgetActivity.this, EditBudgetActivity.class);
                intent.putExtra("budget", budget);
                launchSomeActivity.launch(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteBottomDialog();
            }
        });
    }

    private void setData(){
        showCategoryNameBudget.setText(budget.getCategoryNameBudget());
        if (budget.getCategoryImageBudget() == 0) {
            showCategoryImageBudget.setImageResource(R.drawable.i);
        } else {
            showCategoryImageBudget.setImageResource(budget.getCategoryImageBudget());
        }
        amountBudget.setText("" + budget.getAmountBudget());
        showPercentage.setValue(budget.getPercentageBudget());
        showPercentage.setEnabled(false);
    }

    private void showDeleteBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailsBudgetActivity.this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.delete_bottomsheet_layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        TextView no = bottomSheetDialog.findViewById(R.id.no);
        TextView yes = bottomSheetDialog.findViewById(R.id.yes);
        TextView txt = bottomSheetDialog.findViewById(R.id.txt);
        TextView txt1 = bottomSheetDialog.findViewById(R.id.txt1);

        txt.setText("Remove this budget ?");
        txt1.setText("Are you sure you want to remove this budget?");

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

                dbHelper.deleteBudgetData(budget.getId());

                Dialog dialog = new Dialog(DetailsBudgetActivity.this);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dailog_removed_layout);
                dialog.setCancelable(true);
                dialog.show();

                TextView txt = dialog.findViewById(R.id.txt);
                txt.setText("Budget has been successfully removed");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            onBackPressed();
                            dialog.dismiss();
                        }
                    }
                }, 1000);
            }
        });
    }

    private void init() {
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        showEditBudget = findViewById(R.id.show_edit_budget);
        showExceedAmount = findViewById(R.id.show_exceed_amount);
        showCategoryNameBudget = findViewById(R.id.show_category_name_budget);
        amountBudget = findViewById(R.id.amount_budget);
        showCategoryImageBudget = findViewById(R.id.show_category_image_budget);
        showPercentage = findViewById(R.id.set_slider_budget);
    }
}