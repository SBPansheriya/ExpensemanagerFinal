package com.kmsoft.expensemanager.Activity.Trancation;

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
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

public class DetailsTrancationActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ImageView delete,back, showAddAttachment;
    TextView showTotalBalance, showDescription, showType, showCategory, showTime, showDate;
    IncomeAndExpense incomeAndExpense;
    ActivityResultLauncher<Intent> launchSomeActivity;
    Button editDetailsTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trancation);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(DetailsTrancationActivity.this);
        incomeAndExpense = (IncomeAndExpense) getIntent().getSerializableExtra("incomeAndExpense");
        setData();

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    incomeAndExpense = (IncomeAndExpense) data.getSerializableExtra("incomeAndExpense");
                    if (incomeAndExpense != null){
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteBottomDialog();
            }
        });

        editDetailsTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsTrancationActivity.this, EditDetailsTrancationActivity.class);
                intent.putExtra("incomeAndExpense",incomeAndExpense);
                launchSomeActivity.launch(intent);
            }
        });
    }

    private void setData(){
        showTotalBalance.setText(incomeAndExpense.getAmount());
        showTime.setText(incomeAndExpense.getDayName() + "," + incomeAndExpense.getTime());
        showType.setText(incomeAndExpense.getTag());
        showCategory.setText(incomeAndExpense.getCategoryName());
        showTime.setText(incomeAndExpense.getTime());
        showDescription.setText(incomeAndExpense.getDescription());
        if (incomeAndExpense.getCategoryImage() == 0) {
            showAddAttachment.setImageResource(R.drawable.i);
        } else {
            showAddAttachment.setImageResource(incomeAndExpense.getCategoryImage());
        }
    }

    private void showDeleteBottomDialog(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailsTrancationActivity.this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.delete_bottomsheet_layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        TextView no = bottomSheetDialog.findViewById(R.id.no);
        TextView yes = bottomSheetDialog.findViewById(R.id.yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.deleteData(incomeAndExpense.getId());

                bottomSheetDialog.dismiss();
                Dialog dialog = new Dialog(DetailsTrancationActivity.this);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(true);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dailog_removed_layout);
                dialog.setCancelable(true);
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }, 1000);
            }
        });
    }

    private void init(){
        delete = findViewById(R.id.delete);
        back = findViewById(R.id.back);
        showTotalBalance = findViewById(R.id.show_total_balance);
        showDate = findViewById(R.id.show_date);
        showAddAttachment = findViewById(R.id.show_addAttachment);
        showCategory = findViewById(R.id.show_category);
        showTime = findViewById(R.id.show_time);
        showType = findViewById(R.id.show_type);
        showDescription = findViewById(R.id.show_description);
        editDetailsTransaction = findViewById(R.id.show_edit_details_transaction);
    }
}