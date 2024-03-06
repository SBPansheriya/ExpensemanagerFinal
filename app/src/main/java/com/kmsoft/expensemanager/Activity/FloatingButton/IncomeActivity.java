package com.kmsoft.expensemanager.Activity.FloatingButton;

import static android.Manifest.permission_group.CAMERA;

import static com.kmsoft.expensemanager.Activity.MainActivity.currencySymbol;
import static com.kmsoft.expensemanager.Activity.SplashActivity.CLICK_KEY;
import static com.kmsoft.expensemanager.Activity.SplashActivity.PREFS_NAME;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IncomeActivity extends AppCompatActivity {

    DBHelper dbHelper;
    IncomeAndExpense incomeAndExpense = new IncomeAndExpense();
    ImageView back, calender, setIncomeImage, removeIncomeImage;
    TextView showIncomeDate, incomeCategoryName;
    EditText incomeAddAmount, incomeDescription;
    Button incomeAdded;
    RelativeLayout incomeSetImage;
    LinearLayout incomeCategory, incomeAddAttachment, dateSelected;
    String selectedDate;
    String dayName;
    String currantDate;
    Bitmap bitmap;
    ActivityResultLauncher<Intent> launchSomeActivity;
    ActivityResultLauncher<CropImageContractOptions> cropImage;
    ActivityResultLauncher<Intent> launchSomeActivityResult;
    private static final int CAMERA_REQUEST = 101;
    int click;
    int imageResId;
    double selectedDateTimeStamp;
    String categoryName;
    String incomeAddTime;
    String addAttachmentImage;
    int categoryColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(this);

        launchSomeActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    imageResId = data.getIntExtra("categoryImage", 0);
                    categoryName = data.getStringExtra("categoryName");
                    categoryColor = data.getIntExtra("categoryColor", 0);
                    if (!TextUtils.isEmpty(categoryName)) {
                        incomeCategoryName.setText(String.format("%s", categoryName));
                    }
                }
            }
        });

        back.setOnClickListener(v -> onBackPressed());
        dateSelected.setOnClickListener(v -> showCalenderBottomDialog());

        incomeCategory.setOnClickListener(v -> {
            Intent intent = new Intent(IncomeActivity.this, AddCategoryActivity.class);
            intent.putExtra("clicked", "Income");
            intent.putExtra("image", imageResId);
            intent.putExtra("name", categoryName);
            intent.putExtra("color", categoryColor);
            launchSomeActivityResult.launch(intent);
        });

        incomeAddAttachment.setOnClickListener(v -> checkPermissionsForCamera());

        incomeAddAmount.setText(String.format("%s0", currencySymbol));

        incomeAddAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.startsWith(currencySymbol)) {
                    incomeAddAmount.setText(String.format(currencySymbol + input));
                    incomeAddAmount.setSelection(incomeAddAmount.getText().length());
                }
            }
        });

        incomeAdded.setOnClickListener(v -> {
            String editTextValue = incomeAddAmount.getText().toString();
            if (editTextValue.equals(currencySymbol + "0") || editTextValue.equals(currencySymbol)) {
                Toast.makeText(IncomeActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(editTextValue)) {
                Toast.makeText(IncomeActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(showIncomeDate.getText().toString())) {
                Toast.makeText(IncomeActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(incomeCategoryName.getText().toString())) {
                Toast.makeText(IncomeActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
            } else {
                String amount = extractNumericPart(incomeAddAmount.getText().toString());
                String description = incomeDescription.getText().toString();
                double currantDateTimeStamp = Calendar.getInstance().getTimeInMillis() / 1000;
                incomeAndExpense = new IncomeAndExpense(0, amount, currantDateTimeStamp, selectedDateTimeStamp, currantDate, selectedDate, dayName, incomeAddTime, categoryName, imageResId, categoryColor, description, addAttachmentImage, "Income");
                incomeAndExpenseArrayList.add(incomeAndExpense);
                dbHelper.insertData(incomeAndExpense);
                Dialog dialog = new Dialog(IncomeActivity.this);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dailog_removed_layout);
                dialog.setCancelable(false);
                dialog.show();

                TextView txt = dialog.findViewById(R.id.txt);
                txt.setText(R.string.income_has_been_successfully_added);
                new Handler().postDelayed(() -> {
                    if (dialog.isShowing()) {
                        onBackPressed();
                        dialog.dismiss();
                    }
                }, 1000);
            }
        });

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Uri selectedImageUri = result.getData().getData();
                startCrop(selectedImageUri);
            }
        });

        removeIncomeImage.setOnClickListener(v -> {
            incomeAddAttachment.setVisibility(View.VISIBLE);
            incomeSetImage.setVisibility(View.GONE);
        });

        cropImage = registerForActivityResult(new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                Uri uriContent = result.getUriContent();
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriContent);
                    addAttachmentImage = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title2", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    setIncomeImage.setImageBitmap(bitmap);
                }
                incomeAddAttachment.setVisibility(View.GONE);
                incomeSetImage.setVisibility(View.VISIBLE);
            } else {
                incomeAddAttachment.setVisibility(View.VISIBLE);
                incomeSetImage.setVisibility(View.GONE);
            }
        });
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
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

    private void showAttachmentBottomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(IncomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_attachment_bottomsheet_layout);
        dialog.setCancelable(true);
        dialog.show();

        ImageView camera = dialog.findViewById(R.id.camera);
        ImageView gallery = dialog.findViewById(R.id.gallery);

        camera.setOnClickListener(v -> {
            cameraPermission();
            dialog.dismiss();
        });

        gallery.setOnClickListener(v -> {
            openImagePicker();
            dialog.dismiss();
        });
    }

    private void checkPermissionsForCamera() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(IncomeActivity.this, permissions, 100);
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
            showPermissionDenyDialog(IncomeActivity.this);
        }
    }

    private void showPermissionDenyDialog(Activity activity) {

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

                cancel.setOnClickListener(view -> dialog.dismiss());

                ok.setOnClickListener(view -> {
                    checkPermissionsForCamera();
                    dialog.dismiss();
                });
            }
            click = 1;
        } else if (click == 1) {
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
                ok.setText(R.string.enable_from_settings);

                cancel.setOnClickListener(view -> dialog.dismiss());

                ok.setOnClickListener(view -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    ActivityCompat.startActivityForResult(IncomeActivity.this, intent, 100, null);
                    dialog.dismiss();
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
        } else if (requestCode == CAMERA_REQUEST) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey("data")) {
                bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title2", null);
                    startCrop(Uri.parse(path));
                }
            } else {
                Toast.makeText(this, "No image data found in the intent.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showCalenderBottomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(IncomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calender_bottomsheet_layout);
        dialog.setCancelable(false);
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);
        CalendarView calendarView = dialog.findViewById(R.id.trans_calenderView);

        long currentDateMillis = System.currentTimeMillis();
        calendarView.setMaxDate(currentDateMillis);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar currentTime = Calendar.getInstance();
            currentTime.set(year, month, dayOfMonth);

            Date currentDate = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            currantDate = sdf.format(currentDate);

            int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);
            String[] daysOfWeek = new DateFormatSymbols().getWeekdays();
            dayName = daysOfWeek[dayOfWeek];
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            incomeAddTime = dateFormat.format(currentTime.getTime());

            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDate = dateFormat1.format(selectedDateCalendar.getTime());
            selectedDateTimeStamp = selectedDateCalendar.getTimeInMillis() / 1000;
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        ok.setOnClickListener(v -> {
            if (TextUtils.isEmpty(selectedDate)) {
                Calendar currentTime = Calendar.getInstance();
                Date currentDate = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                currantDate = sdf.format(currentDate);

                int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);
                String[] daysOfWeek = new DateFormatSymbols().getWeekdays();
                dayName = daysOfWeek[dayOfWeek];
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                incomeAddTime = dateFormat.format(currentTime.getTime());

                Calendar selectedDateCalendar = Calendar.getInstance();
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDate = dateFormat1.format(selectedDateCalendar.getTime());
                selectedDateTimeStamp = selectedDateCalendar.getTimeInMillis() / 1000;
                showIncomeDate.setText(selectedDate);
            } else {
                showIncomeDate.setText(String.format("%s", selectedDate));
            }
            dialog.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("incomeAndExpense", incomeAndExpense);
        setResult(RESULT_OK, intent);
    }

    private void init() {
        back = findViewById(R.id.back);
        calender = findViewById(R.id.income_calender);
        incomeCategory = findViewById(R.id.income_category);
        incomeAddAttachment = findViewById(R.id.income_add_attachment);
        showIncomeDate = findViewById(R.id.show_income_date);
        setIncomeImage = findViewById(R.id.set_income_image);
        incomeSetImage = findViewById(R.id.income_set_image);
        removeIncomeImage = findViewById(R.id.remove_income_image);
        incomeAdded = findViewById(R.id.income_added);
        incomeAddAmount = findViewById(R.id.income_add_amount);
        incomeDescription = findViewById(R.id.income_description);
        incomeCategoryName = findViewById(R.id.income_category_name);
        dateSelected = findViewById(R.id.date_selected);
    }
}