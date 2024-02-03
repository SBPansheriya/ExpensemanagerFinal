package com.kmsoft.expensemanager.Activity.Budget;

import androidx.appcompat.app.AppCompatActivity;

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
import com.kmsoft.expensemanager.Fragment.BudgetFragment;
import com.kmsoft.expensemanager.R;

public class DetailsBudgetActivity extends AppCompatActivity {

    ImageView showExceedAmount,back,delete;
    Button showEditBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_budget);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

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
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteBottomDialog();
            }
        });
    }

    private void showDeleteBottomDialog(){
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
                Dialog dialog = new Dialog(DetailsBudgetActivity.this);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(true);
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
                }, 2000);
            }
        });
    }

    private void init(){
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        showEditBudget = findViewById(R.id.show_edit_budget);
        showExceedAmount = findViewById(R.id.show_exceed_amount);
    }
}