package com.kmsoft.expensemanager.Activity.Transaction;


import static com.kmsoft.expensemanager.Activity.MainActivity.currencySymbol;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

public class DetailsTransactionActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ImageView delete, back, showAddAttachment, placeHolder;
    TextView showTotalBalance, showDescription, showType, showCategory, showTime, showDate;
    IncomeAndExpense incomeAndExpense;
    ActivityResultLauncher<Intent> launchSomeActivity;
    Button editDetailsTransaction;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trancation);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(DetailsTransactionActivity.this);
        incomeAndExpense = (IncomeAndExpense) getIntent().getSerializableExtra("incomeAndExpense");
        setData();

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    incomeAndExpense = (IncomeAndExpense) data.getSerializableExtra("incomeAndExpense");
                    if (incomeAndExpense != null) {
                        setData();
                    }
                }
            }
        });

        back.setOnClickListener(v -> onBackPressed());

        delete.setOnClickListener(v -> showDeleteBottomDialog());

        editDetailsTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsTransactionActivity.this, EditDetailsTransactionActivity.class);
            intent.putExtra("incomeAndExpense", incomeAndExpense);
            launchSomeActivity.launch(intent);
        });
    }

    private void setData() {
        showTotalBalance.setText(String.format("%s%s", currencySymbol, incomeAndExpense.getAmount()));
        showDate.setText(String.format("%s,%s", incomeAndExpense.getDayName(), incomeAndExpense.getDate()));
        showType.setText(incomeAndExpense.getTag());
        showCategory.setText(incomeAndExpense.getCategoryName());
        showTime.setText(incomeAndExpense.getTime());
        if (TextUtils.isEmpty(incomeAndExpense.getDescription())) {
            showDescription.setText(R.string.no_description);
        } else {
            showDescription.setText(incomeAndExpense.getDescription());
        }
        if (TextUtils.isEmpty(incomeAndExpense.getAddAttachment())) {
            showAddAttachment.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            placeHolder.setImageResource(R.drawable.placeholder);
        } else {
            showAddAttachment.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            Glide.with(this).load(incomeAndExpense.getAddAttachment())
                    .into(showAddAttachment);
        }
    }

    private void showDeleteBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailsTransactionActivity.this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.delete_bottomsheet_layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        TextView no = bottomSheetDialog.findViewById(R.id.no);
        TextView yes = bottomSheetDialog.findViewById(R.id.yes);

        no.setOnClickListener(v -> bottomSheetDialog.dismiss());

        yes.setOnClickListener(v -> {

            dbHelper.deleteData(incomeAndExpense.getId());

            bottomSheetDialog.dismiss();
            Dialog dialog = new Dialog(DetailsTransactionActivity.this);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.dailog_removed_layout);
            dialog.setCancelable(false);
            dialog.show();

            new Handler().postDelayed(() -> {
                if (dialog.isShowing()) {
                    onBackPressed();
                    dialog.dismiss();
                }
            }, 1000);
        });
    }

    private void init() {
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
        placeHolder = findViewById(R.id.placeholder);
        cardView = findViewById(R.id.cardView);
    }
}