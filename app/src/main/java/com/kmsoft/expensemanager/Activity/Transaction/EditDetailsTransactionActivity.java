package com.kmsoft.expensemanager.Activity.Transaction;

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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
import com.kmsoft.expensemanager.Activity.NotificationScheduler;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;
import com.kmsoft.expensemanager.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditDetailsTransactionActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ImageView back, setEditImage, removeImage;
    RelativeLayout editSetImage;
    LinearLayout editAddAttachment, editShowCategory, title1;
    EditText editTotalBalance;
    TextView editDescription, editCategory, editDate;
    IncomeAndExpense incomeAndExpense;
    Button editDetailsTransaction;
    ActivityResultLauncher<Intent> launchSomeActivity;
    ActivityResultLauncher<CropImageContractOptions> cropImage;
    ActivityResultLauncher<Intent> launchSomeActivityResult;
    private static final int CAMERA_REQUEST = 101;
    ImageView calendar;
    String currantDate;
    int click;
    String selectedDate;
    String dayName;
    String editAddTime;
    Bitmap bitmap;
    int imageResId;
    int categoryColor;
    double selectedDateTimeStamp;
    String categoryName;
    String addAttachmentImage;
    ArrayList<Budget> budgetArrayList = new ArrayList<>();
    ArrayList<IncomeAndExpense> expenseList = new ArrayList<>();
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details_trancation);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        init();

        dbHelper = new DBHelper(EditDetailsTransactionActivity.this);
        incomeAndExpense = (IncomeAndExpense) getIntent().getSerializableExtra("incomeAndExpense");
        setData();

        launchSomeActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    imageResId = data.getIntExtra("categoryImage", 0);
                    categoryName = data.getStringExtra("categoryName");
                    categoryColor = data.getIntExtra("categoryColor", 0);
                    categoryId = data.getIntExtra("id", 0);

                    if (!TextUtils.isEmpty(categoryName)) {
                        editCategory.setText(String.format("%s", categoryName));
                    }
                }
            }
        });

        back.setOnClickListener(view -> onBackPressed());

        editTotalBalance.addTextChangedListener(new TextWatcher() {
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
                    editTotalBalance.setText(String.format(currencySymbol + input));
                    editTotalBalance.setSelection(editTotalBalance.getText().length());
                }
            }
        });

        editDetailsTransaction.setOnClickListener(view -> {
            String amount = editTotalBalance.getText().toString();
            String description = editDescription.getText().toString();
            if (imageResId == 0) {
                imageResId = incomeAndExpense.getCategoryImage();
            }
            if (TextUtils.isEmpty(categoryName)) {
                categoryName = incomeAndExpense.getCategoryName();
                categoryColor = incomeAndExpense.getCategoryColor();
            }
            if (TextUtils.isEmpty(selectedDate)) {
                selectedDate = incomeAndExpense.getDate();
                dayName = incomeAndExpense.getDayName();
                editAddTime = incomeAndExpense.getTime();
                currantDate = incomeAndExpense.getCurrantDate();
                selectedDateTimeStamp = incomeAndExpense.getSelectedDateTimeStamp();
            }
            if (TextUtils.isEmpty(addAttachmentImage)) {
                addAttachmentImage = incomeAndExpense.getAddAttachment();
            }
            if (amount.equals(currencySymbol + "0") || amount.equals(currencySymbol)) {
                Toast.makeText(EditDetailsTransactionActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(amount)) {
                Toast.makeText(EditDetailsTransactionActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(selectedDate)) {
                Toast.makeText(EditDetailsTransactionActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(categoryName)) {
                Toast.makeText(EditDetailsTransactionActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
            } else {
                double currantDateTimeStamp = Calendar.getInstance().getTimeInMillis();
                String reAmount = extractNumericPart(amount);
                incomeAndExpense = new IncomeAndExpense(incomeAndExpense.getId(), reAmount, currantDateTimeStamp, selectedDateTimeStamp, currantDate, selectedDate, dayName, editAddTime, categoryName, imageResId, categoryColor, description, addAttachmentImage, incomeAndExpense.getTag(), categoryId);
                dbHelper.updateData(incomeAndExpense);

                Cursor cursor = dbHelper.getAllBudgetData();
                if (cursor != null && cursor.moveToFirst()) {
                    budgetArrayList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(0);
                        String amountBudget = cursor.getString(1);
                        String categoryNameBudget = cursor.getString(2);
                        int categoryImageBudget = cursor.getInt(3);
                        int percentageBudget = cursor.getInt(4);
                        int categoryId = cursor.getInt(5);

                        Budget budget = new Budget(id, amountBudget, categoryNameBudget, categoryImageBudget, percentageBudget, categoryId);
                        budgetArrayList.add(budget);
                    } while (cursor.moveToNext());
                }

                expenseList = filterCategories(incomeAndExpenseArrayList);
                HashMap<String, Double> categoryTotalExpenses = new HashMap<>();

                for (IncomeAndExpense expense : expenseList) {
                    String categoryName = expense.getCategoryName();
                    double amount1 = Double.parseDouble(extractNumericPart(expense.getAmount()));
                    double categoryTotal = categoryTotalExpenses.getOrDefault(categoryName, 0.0);
                    categoryTotal += amount1;
                    categoryTotalExpenses.put(categoryName, categoryTotal);
                }

                for (Budget budget : budgetArrayList) {
                    String categoryName1 = budget.getCategoryNameBudget();
                    double categoryTotalExpense = categoryTotalExpenses.getOrDefault(categoryName1, 0.0);
                    if (categoryName.equals(budget.getCategoryNameBudget())) {
                        double budgetAmount1 = Double.parseDouble(extractNumericPart(budget.getAmountBudget()));
                        if (categoryTotalExpense >= budgetAmount1) {
                            NotificationScheduler.scheduleNotification(this, budget);
                        }
                    }
                }
                onBackPressed();
            }
        });

        editShowCategory.setOnClickListener(v -> {
            Intent intent = new Intent(EditDetailsTransactionActivity.this, AddCategoryActivity.class);
            intent.putExtra("clicked", incomeAndExpense.getTag());
            if (imageResId == 0) {
                intent.putExtra("image", incomeAndExpense.getCategoryImage());
            } else {
                intent.putExtra("image", imageResId);
            }
            if (TextUtils.isEmpty(categoryName)) {
                intent.putExtra("name", incomeAndExpense.getCategoryName());
            } else {
                intent.putExtra("name", categoryName);
            }
            if (categoryColor == 0) {
                intent.putExtra("color", incomeAndExpense.getCategoryColor());
            } else {
                intent.putExtra("color", categoryColor);
            }
            if (categoryId == 0) {
                intent.putExtra("id", incomeAndExpense.getCategoryId());
            } else {
                intent.putExtra("id", categoryId);
            }
            launchSomeActivityResult.launch(intent);
        });

        title1.setOnClickListener(v -> showCalenderBottomDialog());

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Uri selectedImageUri = result.getData().getData();
                startCrop(selectedImageUri);
            }
        });

        editAddAttachment.setOnClickListener(v -> checkPermissionsForCamera());

        removeImage.setOnClickListener(v -> {
            editAddAttachment.setVisibility(View.VISIBLE);
            editSetImage.setVisibility(View.GONE);
        });

        cropImage = registerForActivityResult(new CropImageContract(), result -> {
            addAttachmentImage = null;
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
                    setEditImage.setImageBitmap(bitmap);
                }
                editAddAttachment.setVisibility(View.GONE);
                editSetImage.setVisibility(View.VISIBLE);
            } else {
                if (!TextUtils.isEmpty(incomeAndExpense.getAddAttachment())) {
                    Picasso.get().load(incomeAndExpense.getAddAttachment()).into(setEditImage);
                    editAddAttachment.setVisibility(View.GONE);
                    editSetImage.setVisibility(View.VISIBLE);
                } else {
                    editAddAttachment.setVisibility(View.VISIBLE);
                    editSetImage.setVisibility(View.GONE);
                }
            }
        });
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    private ArrayList<IncomeAndExpense> filterCategories
            (ArrayList<IncomeAndExpense> incomeAndExpenses) {
        ArrayList<IncomeAndExpense> filteredList = new ArrayList<>();
        for (IncomeAndExpense incomeAndExpense : incomeAndExpenses) {
            if (incomeAndExpense.getTag().equals("Expense")) {
                filteredList.add(incomeAndExpense);
            }
        }
        return filteredList;
    }

    private void setData() {
        editTotalBalance.setText(String.format("%s%s", currencySymbol, incomeAndExpense.getAmount()));
        editDate.setText(incomeAndExpense.getDate());
        editCategory.setText(incomeAndExpense.getCategoryName());
        editDescription.setText(incomeAndExpense.getDescription());
        if (TextUtils.isEmpty(incomeAndExpense.getAddAttachment())) {
            editAddAttachment.setVisibility(View.VISIBLE);
            editSetImage.setVisibility(View.GONE);
        } else {
            Picasso.get().load(incomeAndExpense.getAddAttachment()).into(setEditImage);
            editAddAttachment.setVisibility(View.GONE);
            editSetImage.setVisibility(View.VISIBLE);
        }
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
        BottomSheetDialog dialog = new BottomSheetDialog(EditDetailsTransactionActivity.this);
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
            ActivityCompat.requestPermissions(EditDetailsTransactionActivity.this, permissions, 100);
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
            showPermissionDenyDialog(EditDetailsTransactionActivity.this);
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
                    ActivityCompat.startActivityForResult(EditDetailsTransactionActivity.this, intent, 100, null);
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
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                    Uri imageUri = Uri.parse(path);
                    startCrop(imageUri);
                }
            } else {
                Toast.makeText(this, "No image data found in the intent.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showCalenderBottomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(EditDetailsTransactionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calender_bottomsheet_layout);
        dialog.setCancelable(false);
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);
        CalendarView calendarView = dialog.findViewById(R.id.trans_calenderView);

        Date date;
        if (selectedDate != null) {
            date = parseDate(selectedDate);
        } else {
            date = parseDate(incomeAndExpense.getDate());
        }
        if (date != null) {
            calendarView.setDate(date.getTime());
        }

        long currentDateMillis = System.currentTimeMillis();
        calendarView.setMaxDate(currentDateMillis);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar currentTime = Calendar.getInstance();
            currentTime.set(year, month, dayOfMonth);
            int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);
            Date currentDate = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            currantDate = sdf.format(currentDate);

            String[] daysOfWeek = new DateFormatSymbols().getShortWeekdays();
            dayName = daysOfWeek[dayOfWeek];
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            editAddTime = dateFormat.format(currentTime.getTime());
            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDate = dateFormat1.format(selectedDateCalendar.getTime());
            selectedDateTimeStamp = selectedDateCalendar.getTimeInMillis() / 1000;

        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        ok.setOnClickListener(v -> {
            if (TextUtils.isEmpty(selectedDate)) {
                editDate.setText("");
            } else {
                editDate.setText(String.format("%s", selectedDate));
            }
            dialog.dismiss();
        });
    }

    private Date parseDate(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("incomeAndExpense", incomeAndExpense);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init() {
        back = findViewById(R.id.back);
        editTotalBalance = findViewById(R.id.edit_amount);
        editDate = findViewById(R.id.edit_date);
        editAddAttachment = findViewById(R.id.edit_add_attachment);
        editCategory = findViewById(R.id.edit_category_name);
        editDescription = findViewById(R.id.edit_description);
        editDetailsTransaction = findViewById(R.id.edit_transaction);
        setEditImage = findViewById(R.id.set_edit_image);
        removeImage = findViewById(R.id.remove_image);
        calendar = findViewById(R.id.calendar);
        editShowCategory = findViewById(R.id.edit_category);
        editSetImage = findViewById(R.id.edit_set_image);
        title1 = findViewById(R.id.title1);
    }
}