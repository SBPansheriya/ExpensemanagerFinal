package com.kmsoft.expensemanager.Activity.FloatingButton;

import static com.kmsoft.expensemanager.Constant.categoryArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.Adapter.AddCategoryAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class AddCategoryActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView income, expense;
    ImageView back;
    AddCategoryAdapter addCategoryIncomeAdapter;
    RecyclerView incomeCategoryRecyclerview;
    Button addNewCategoryBtn;
    int addCategoryImage;
    int editCategoryImage;
    BottomSheetDialog dialog;
    BottomSheetDialog dialog1;
    public static boolean click;
    String clicked;
    String addCategoryName;
    ActivityResultLauncher<Intent> launchSomeActivity;
    Category category;
    String name;
    int image;
    String tagFind;
    String findClick;
    ArrayList<Category> incomeCategoryList = new ArrayList<>();
    ArrayList<Category> expenseCategoryList = new ArrayList<>();
    ImageView editNewCategoryImage;
    ImageView addNewCategoryImage;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();

        dbHelper = new DBHelper(this);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        if (isDatabaseEmpty()) {
            insertInitialCategories();
        }

        click = false;
        clicked = getIntent().getStringExtra("clicked");

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    if (findClick.equals("Edit")) {
                        editCategoryImage = data.getIntExtra("imageResId", 0);
                        editNewCategoryImage.setImageResource(editCategoryImage);
                    } else if (findClick.equals("Add")) {
                        addCategoryImage = data.getIntExtra("imageResId", 0);
                        addNewCategoryImage.setImageResource(addCategoryImage);
                    }
                }
            }
        });

        if (clicked.equals("Income")) {
            tagFind = "Income";

            Display();
            income.setBackgroundResource(R.drawable.selected_category);
            expense.setBackgroundResource(R.drawable.unselected_category);

            income.setTextColor(ContextCompat.getColor(this,R.color.white));
            expense.setTextColor(ContextCompat.getColor(this,R.color.black));
        } else if (clicked.equals("Expense")) {
            tagFind = "Expense";

            Display();
            income.setBackgroundResource(R.drawable.unselected_category);
            expense.setBackgroundResource(R.drawable.selected_category);

            income.setTextColor(ContextCompat.getColor(this,R.color.black));
            expense.setTextColor(ContextCompat.getColor(this,R.color.white));
        }

        back.setOnClickListener(v -> onBackPressed());

        income.setOnClickListener(v -> {
            tagFind = "Income";

            Display();
            income.setBackgroundResource(R.drawable.selected_category);
            expense.setBackgroundResource(R.drawable.unselected_category);

            income.setTextColor(ContextCompat.getColor(this,R.color.white));
            expense.setTextColor(ContextCompat.getColor(this,R.color.black));
        });

        expense.setOnClickListener(v -> {
            tagFind = "Expense";

            Display();
            expense.setBackgroundResource(R.drawable.selected_category);
            income.setBackgroundResource(R.drawable.unselected_category);

            expense.setTextColor(ContextCompat.getColor(this,R.color.white));
            income.setTextColor(ContextCompat.getColor(this,R.color.black));
        });

        addNewCategoryBtn.setOnClickListener(v -> showAddNewCategoryBottomDialog());

        incomeCategoryRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int last = layoutManager.findLastVisibleItemPosition();
                if (TextUtils.equals(tagFind, "Income")) {
                    if (last == incomeCategoryList.size() - 1) {
                        RelativeLayout.LayoutParams marginLayoutParams = new RelativeLayout.LayoutParams(
                                RecyclerView.LayoutParams.MATCH_PARENT,
                                RecyclerView.LayoutParams.MATCH_PARENT
                        );
                        marginLayoutParams.setMargins(80, 10, 80, 300);
                        incomeCategoryRecyclerview.setLayoutParams(marginLayoutParams);
                    } else {
                        RelativeLayout.LayoutParams marginLayoutParams = new RelativeLayout.LayoutParams(
                                RecyclerView.LayoutParams.MATCH_PARENT,
                                RecyclerView.LayoutParams.MATCH_PARENT
                        );
                        marginLayoutParams.setMargins(80, 40, 80, 40);
                        incomeCategoryRecyclerview.setLayoutParams(marginLayoutParams);
                    }
                } else {
                    if (last == expenseCategoryList.size() - 1) {
                        RelativeLayout.LayoutParams marginLayoutParams = new RelativeLayout.LayoutParams(
                                RecyclerView.LayoutParams.MATCH_PARENT,
                                RecyclerView.LayoutParams.MATCH_PARENT
                        );
                        marginLayoutParams.setMargins(80, 10, 80, 300);
                        incomeCategoryRecyclerview.setLayoutParams(marginLayoutParams);
                    } else {
                        RelativeLayout.LayoutParams marginLayoutParams = new RelativeLayout.LayoutParams(
                                RecyclerView.LayoutParams.MATCH_PARENT,
                                RecyclerView.LayoutParams.MATCH_PARENT
                        );
                        marginLayoutParams.setMargins(80, 40, 80, 40);
                        incomeCategoryRecyclerview.setLayoutParams(marginLayoutParams);
                    }
                }
//                int firstVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
//
//                if (firstVisibleItemPosition > lastFirstVisiblePosition) {
//                    addNewCategoryBtn.setVisibility(View.GONE);
//                } else if (firstVisibleItemPosition < lastFirstVisiblePosition) {
//                    addNewCategoryBtn.setVisibility(View.VISIBLE);
//                }
//
//                lastFirstVisiblePosition = firstVisibleItemPosition;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (click) {
            if (findClick.equals("Add")) {
                dialog.show();
            } else if (findClick.equals("Edit")) {
                dialog1.show();
            }
        }
    }

    private void showAddNewCategoryBottomDialog() {

        dialog = new BottomSheetDialog(AddCategoryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_category_bottomsheet_layout);
        dialog.setCancelable(true);

        EditText addNewCategory = dialog.findViewById(R.id.adding);
        Button save = dialog.findViewById(R.id.save);
        addNewCategoryImage = dialog.findViewById(R.id.category_image);

        if (addCategoryImage == 0) {
            addNewCategoryImage.setImageResource(R.drawable.i);
        } else {
            addNewCategoryImage.setImageResource(addCategoryImage);
        }

        addNewCategoryImage.setOnClickListener(v -> {
            findClick = "Add";
            Intent intent = new Intent(AddCategoryActivity.this, IconActivity.class);
            intent.putExtra("name", addNewCategory.getText().toString());
            launchSomeActivity.launch(intent);
            dialog.dismiss();
        });

        dialog.setOnDismissListener(dialog -> addCategoryImage = 0);

        save.setOnClickListener(v -> {
            if (TextUtils.isEmpty(addNewCategory.getText().toString())) {
                Toast.makeText(AddCategoryActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
            } else {
                addCategoryName = addNewCategory.getText().toString();
                category = new Category(0, addCategoryName, addCategoryImage, tagFind);
                categoryArrayList.add(category);
                dbHelper.insertCategoryData(category);
                if (tagFind.equals("Income")) {
                    incomeCategoryList.add(category);
                    addCategoryIncomeAdapter.updateData(incomeCategoryList);
                } else if (tagFind.equals("Expense")) {
                    expenseCategoryList.add(category);
                    addCategoryIncomeAdapter.updateData(expenseCategoryList);
                } else {
                    Toast.makeText(AddCategoryActivity.this, "No Added Data", Toast.LENGTH_SHORT).show();
                }
                addCategoryImage = 0;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showEditNewCategoryBottomDialog(Category getcategory, int position) {
        dialog1 = new BottomSheetDialog(AddCategoryActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.edit_category_bottomsheet_layout);
        dialog1.setCancelable(true);
        dialog1.show();

        EditText editCategory = dialog1.findViewById(R.id.edit_category_name);
        TextView update = dialog1.findViewById(R.id.update);
        TextView delete = dialog1.findViewById(R.id.delete);
        editNewCategoryImage = dialog1.findViewById(R.id.edit_category_image);

        if (getcategory.getCategoryImage() == 0) {
            editNewCategoryImage.setImageResource(R.drawable.i);
        } else {
            if (editCategoryImage == 0) {
                editNewCategoryImage.setImageResource(getcategory.getCategoryImage());
            } else {
                editNewCategoryImage.setImageResource(editCategoryImage);
            }
        }

        dialog1.setOnDismissListener(dialog -> editCategoryImage = 0);

        editCategory.setText(getcategory.getCategoryName());

        editNewCategoryImage.setOnClickListener(v -> {
            findClick = "Edit";
            Intent intent = new Intent(AddCategoryActivity.this, IconActivity.class);
            launchSomeActivity.launch(intent);
            dialog1.dismiss();
        });

        delete.setOnClickListener(v -> {
            dialog1.dismiss();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddCategoryActivity.this);
            bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            bottomSheetDialog.setContentView(R.layout.delete_bottomsheet_layout);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            TextView no = bottomSheetDialog.findViewById(R.id.no);
            TextView yes = bottomSheetDialog.findViewById(R.id.yes);
            TextView txt = bottomSheetDialog.findViewById(R.id.txt);
            TextView txt1 = bottomSheetDialog.findViewById(R.id.txt1);

            txt.setText(R.string.remove_this_category);
            txt1.setText(R.string.are_you_sure_do_you_wanna_remove_this_category);

            no.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
                dialog1.show();
            });

            yes.setOnClickListener(v12 -> {
                if (tagFind.equals("Income")) {
                    dbHelper.deleteCategoryData(getcategory.getId());
                    incomeCategoryList.remove(position);
                    addCategoryIncomeAdapter.updateData(incomeCategoryList);
                } else if (tagFind.equals("Expense")) {
                    dbHelper.deleteCategoryData(getcategory.getId());
                    expenseCategoryList.remove(position);
                    addCategoryIncomeAdapter.updateData(expenseCategoryList);
                }
                categoryArrayList.remove(position);
                bottomSheetDialog.dismiss();

                Dialog dialog = new Dialog(AddCategoryActivity.this);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(true);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dailog_removed_layout);
                dialog.setCancelable(true);
                dialog.show();

                TextView text = dialog.findViewById(R.id.txt);
                text.setText(R.string.category_has_been_successfully_removed);

                new Handler().postDelayed(() -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }, 1000);
            });
        });

        update.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editCategory.getText().toString())) {
                Toast.makeText(AddCategoryActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
            } else {
                addCategoryName = editCategory.getText().toString();
                category = new Category(getcategory.getId(), addCategoryName, editCategoryImage, tagFind);
                categoryArrayList.add(category);
                if (tagFind.equals("Income")) {
                    for (int i = 0; i < incomeCategoryList.size(); i++) {
                        if (incomeCategoryList.get(i).getId() == getcategory.getId()) {
                            incomeCategoryList.set(i, category);
                            break;
                        }
                    }
                    addCategoryIncomeAdapter.updateData(incomeCategoryList);
                } else if (tagFind.equals("Expense")) {
                    for (int i = 0; i < expenseCategoryList.size(); i++) {
                        if (expenseCategoryList.get(i).getId() == getcategory.getId()) {
                            expenseCategoryList.set(i, category);
                            break;
                        }
                    }
                    addCategoryIncomeAdapter.updateData(expenseCategoryList);
                } else {
                    Toast.makeText(AddCategoryActivity.this, "No Added Data", Toast.LENGTH_SHORT).show();
                }
                dbHelper.updateCategoryData(AddCategoryActivity.this.category);
                editCategoryImage = 0;
                dialog1.dismiss();
            }
        });
    }

    private boolean isDatabaseEmpty() {
        Cursor cursor = dbHelper.getAllCategoryData();
        int count = cursor.getCount();
        cursor.close();
        return count == 0;
    }

    private void insertInitialCategories() {
        dbHelper.insertCategoryData(new Category(0, "Shopping", R.drawable.i47, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Food", R.drawable.i12, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Birthday", R.drawable.i11, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Party", R.drawable.i7, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Medicine", R.drawable.i18, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Books", R.drawable.i1, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Sports", R.drawable.i43, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Traveling", R.drawable.i17, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Education", R.drawable.i22, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Transportation", R.drawable.i38, "Income"));
        dbHelper.insertCategoryData(new Category(0, "Entertainment", R.drawable.i15, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Gifts", R.drawable.i9, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Health & Fitness", R.drawable.i41, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Investments", R.drawable.i37, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Pets", R.drawable.i39, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Games", R.drawable.i34, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Car", R.drawable.i25, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Donation", R.drawable.i38, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Shipping", R.drawable.i30, "Expense"));
        dbHelper.insertCategoryData(new Category(0, "Diamond & Jewellery", R.drawable.i19, "Expense"));
    }

    private void Display() {
        Cursor cursor = dbHelper.getAllCategoryData();
        if (cursor != null && cursor.moveToFirst()) {
            categoryArrayList = new ArrayList<>();
            do {
                int id = cursor.getInt(0);
                String categoryName = cursor.getString(1);
                int categoryImage = cursor.getInt(2);
                String tag = cursor.getString(3);

                category = new Category(id, categoryName, categoryImage, tag);
                categoryArrayList.add(category);

                incomeCategoryList = filterCategories(categoryArrayList, "Income");
                expenseCategoryList = filterCategories(categoryArrayList, "Expense");

                if (TextUtils.equals(tagFind, "Income")) {
                    incomeCategoryRecyclerview.setLayoutManager(layoutManager);
                    addCategoryIncomeAdapter = new AddCategoryAdapter(AddCategoryActivity.this, incomeCategoryList);
                    incomeCategoryRecyclerview.setAdapter(addCategoryIncomeAdapter);
                } else if (TextUtils.equals(tagFind, "Expense")) {
                    incomeCategoryRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                    addCategoryIncomeAdapter = new AddCategoryAdapter(AddCategoryActivity.this, expenseCategoryList);
                    incomeCategoryRecyclerview.setAdapter(addCategoryIncomeAdapter);
                }

            } while (cursor.moveToNext());
        }
    }

    private ArrayList<Category> filterCategories(ArrayList<Category> categories, String type) {
        ArrayList<Category> filteredList = new ArrayList<>();
        for (Category category : categories) {
            if (category.getCategoryTag().equals(type)) {
                filteredList.add(category);
            }
        }
        return filteredList;
    }

    public void getData(String name1, int image1) {
        name = name1;
        image = image1;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("categoryImage", image);
        intent.putExtra("categoryName", name);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init() {
        income = findViewById(R.id.category_income);
        expense = findViewById(R.id.category_expense);
        incomeCategoryRecyclerview = findViewById(R.id.incomeCategoryRecyclerview);
        addNewCategoryBtn = findViewById(R.id.add_new_category);
        back = findViewById(R.id.back);
    }
}