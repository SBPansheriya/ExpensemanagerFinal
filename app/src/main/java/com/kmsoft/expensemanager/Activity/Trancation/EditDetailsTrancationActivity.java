package com.kmsoft.expensemanager.Activity.Trancation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

public class EditDetailsTrancationActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ImageView back, editAddAttachment;
    TextView editTotalBalance, editDescription, editType, editCategory, editTime, editDate;
    IncomeAndExpense incomeAndExpense;
    ActivityResultLauncher<Intent> launchSomeActivity;
    Button editDetailsTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details_trancation);

        init();

        dbHelper = new DBHelper(EditDetailsTrancationActivity.this);
        incomeAndExpense = (IncomeAndExpense) getIntent().getSerializableExtra("incomeAndExpense");
        setData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editDetailsTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = editTotalBalance.getText().toString();
                String time = editTime.getText().toString();
                String date = editDate.getText().toString();

                onBackPressed();
            }
        });

    }

    private void setData(){
        editTotalBalance.setText(incomeAndExpense.getAmount());
        editTime.setText(incomeAndExpense.getDayName() + "," + incomeAndExpense.getTime());
        editType.setText(incomeAndExpense.getTag());
        editCategory.setText(incomeAndExpense.getCategoryName());
        editTime.setText(incomeAndExpense.getTime());
        editDescription.setText(incomeAndExpense.getDescription());
        if (incomeAndExpense.getCategoryImage() == 0) {
            editAddAttachment.setImageResource(R.drawable.i);
        } else {
            editAddAttachment.setImageResource(incomeAndExpense.getCategoryImage());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("incomeAndExpense",incomeAndExpense);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init(){
        back = findViewById(R.id.back);
        editTotalBalance = findViewById(R.id.edit_total_balance);
        editDate = findViewById(R.id.edit_date);
        editAddAttachment = findViewById(R.id.edit_addAttachment);
        editCategory = findViewById(R.id.edit_category);
        editTime = findViewById(R.id.edit_time);
        editType = findViewById(R.id.edit_type);
        editDescription = findViewById(R.id.edit_description);
        editDetailsTransaction = findViewById(R.id.edit_details_transaction);
    }
}