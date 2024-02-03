package com.kmsoft.expensemanager.Activity.FloatingButton;

import static android.Manifest.permission_group.CAMERA;
import static com.kmsoft.expensemanager.Activity.SplashActivity.CLICK_KEY;
import static com.kmsoft.expensemanager.Activity.SplashActivity.PREFS_NAME;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.R;

import java.io.IOException;

public class ExpenseActivity extends AppCompatActivity {

    ImageView back, calender, setExpenseImage, removeExpenseImage;
    TextView showExpenseDate;
    Button expenseAdded;
    RelativeLayout expenseSetImage;
    LinearLayout expenseCategory, expenseAddAttachment;
    String selectedDate;
    Bitmap bitmap;
    ActivityResultLauncher<Intent> launchSomeActivity;
    ActivityResultLauncher<CropImageContractOptions> cropImage;
    private static final int CAMERA_REQUEST = 100;
    int click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
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

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalenderBottomDialog();
            }
        });

        expenseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseActivity.this, AddCategoryActivity.class);
                intent.putExtra("clicked","Expense");
                startActivity(intent);
            }
        });

        expenseAddAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsForCamera();
            }
        });

        expenseAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Uri selectedImageUri = result.getData().getData();
                startCrop(selectedImageUri);
            }
        });

        removeExpenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseAddAttachment.setVisibility(View.VISIBLE);
                expenseSetImage.setVisibility(View.GONE);
            }
        });

        cropImage = registerForActivityResult(new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                Uri uriContent = result.getUriContent();
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    setExpenseImage.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
            expenseAddAttachment.setVisibility(View.GONE);
            expenseSetImage.setVisibility(View.VISIBLE);
        });
    }

    private void startCrop(Uri selectedImageUri) {
        CropImageOptions options = new CropImageOptions();
        options.guidelines = CropImageView.Guidelines.ON;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(selectedImageUri, options);
        cropImage.launch(cropImageContractOptions);
    }

    private void openImagePicker() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    private void cameraPermission() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, CAMERA_REQUEST);
    }

    private void showAttachmentBottomDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(ExpenseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_attachment_bottomsheet_layout);
        dialog.setCancelable(true);
        dialog.show();

        ImageView camera = dialog.findViewById(R.id.camera);
        ImageView gallery = dialog.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPermission();
                dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
                dialog.dismiss();
            }
        });
    }

    private void checkPermissionsForCamera() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ExpenseActivity.this, permissions, 100);
        } else {
            showAttachmentBottomDialog();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showAttachmentBottomDialog();
        } else {
            showPermissionDenyDialog(ExpenseActivity.this,CAMERA_REQUEST);
        }
    }

    private void showPermissionDenyDialog(Activity activity, int requestCode) {

        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        click = settings.getInt(CLICK_KEY, 0);

        if (click == 0) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA)) {

                Dialog dialog = new Dialog(activity);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.permission_denied_first_dialog);
                dialog.setCancelable(false);
                dialog.show();

                Button cancel = dialog.findViewById(R.id.canceldialog);
                Button ok = dialog.findViewById(R.id.okdialog);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPermissionsForCamera();
                        dialog.dismiss();
                    }
                });
            }
            click = 1;
        }
        else if (click == 1) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA)) {

                Dialog dialog = new Dialog(activity);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.permission_denied_first_dialog);
                dialog.setCancelable(false);
                dialog.show();

                Button cancel = dialog.findViewById(R.id.canceldialog);
                Button ok = dialog.findViewById(R.id.okdialog);
                TextView textView = dialog.findViewById(R.id.filename);

                textView.setText(R.string.camera_permission);
                ok.setText("Enable from settings");

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        ActivityCompat.startActivityForResult(ExpenseActivity.this,intent, requestCode,null);
                        dialog.dismiss();
                    }
                });
            }
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(CLICK_KEY, click);
        editor.apply();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            checkPermissionsForCamera();
        }
    }

    private void showCalenderBottomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(ExpenseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calender_bottomsheet_layout);
        dialog.setCancelable(false);
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);
        CalendarView calendarView = dialog.findViewById(R.id.trans_calenderView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(selectedDate)){
                    showExpenseDate.setText("");
                } else {
                    showExpenseDate.setText(""+selectedDate);
                }
                dialog.dismiss();
            }
        });
    }

    private void init() {
        back = findViewById(R.id.back);
        calender = findViewById(R.id.expense_calender);
        expenseCategory = findViewById(R.id.expense_category);
        expenseAddAttachment = findViewById(R.id.expense_add_attachment);
        showExpenseDate = findViewById(R.id.show_expense_date);
        setExpenseImage = findViewById(R.id.set_expense_image);
        expenseSetImage = findViewById(R.id.expense_set_image);
        removeExpenseImage = findViewById(R.id.remove_expense_image);
        expenseAdded = findViewById(R.id.expense_added);
    }
}