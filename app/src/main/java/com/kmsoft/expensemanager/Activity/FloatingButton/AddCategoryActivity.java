package com.kmsoft.expensemanager.Activity.FloatingButton;


import static com.kmsoft.expensemanager.Constant.categoryArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.Adapter.AddCategoryIncomeAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class AddCategoryActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView income, expense;
    ImageView back;
    AddCategoryIncomeAdapter addCategoryIncomeAdapter;
    RecyclerView incomeCategoryRecyclerview;
    RecyclerView expenseCategoryRecyclerview;
    Button addNewCategoryBtn;
    int addCategoryImageResId;
    BottomSheetDialog dialog1;
    public static boolean click;
    String clicked;
    String addCategoryName;
    ActivityResultLauncher<Intent> launchSomeActivity;
    Category category;
    String name;
    String getCategoryName;
    int image;
    String tagFind;
    String findClick;
    ArrayList<Category> incomeList = new ArrayList<>();
    ArrayList<Category> expenseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();

        dbHelper = new DBHelper(this);
//        categoryArrayList.add(new Category("Shopping", R.drawable.i47, "Income"));
//        categoryArrayList.add(new Category("Food", R.drawable.i12, "Income"));
//        categoryArrayList.add(new Category("Birthday", R.drawable.i11, "Income"));
//        categoryArrayList.add(new Category("Party", R.drawable.i7, "Income"));
//        categoryArrayList.add(new Category("Medicine", R.drawable.i18, "Income"));
//        categoryArrayList.add(new Category("Books", R.drawable.i1, "Income"));
//        categoryArrayList.add(new Category("Sports", R.drawable.i43, "Income"));
//        categoryArrayList.add(new Category("Traveling", R.drawable.i17, "Income"));
//        categoryArrayList.add(new Category("Education", R.drawable.i22, "Income"));
//        categoryArrayList.add(new Category("Transportation", R.drawable.i38, "Income"));
//        categoryArrayList.add(new Category("Entertainment", R.drawable.i15, "Income"));
//        categoryArrayList.add(new Category("Gifts", R.drawable.i9, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Shopping", R.drawable.i47, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Food", R.drawable.i12, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Birthday", R.drawable.i11, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Party", R.drawable.i7, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Medicine", R.drawable.i18, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Books", R.drawable.i1, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Sports", R.drawable.i43, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Traveling", R.drawable.i17, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Education", R.drawable.i22, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Transportation", R.drawable.i38, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Entertainment", R.drawable.i15, "Income"));
//        dbHelper.insertCategoryData(new Category(0,"Gifts", R.drawable.i9, "Income"));
        click = false;
        clicked = getIntent().getStringExtra("clicked");

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    addCategoryImageResId = data.getIntExtra("imageResId", 0);
                    getCategoryName = data.getStringExtra("name");
                }
            }
        });

        if (clicked.equals("Income")) {
            tagFind = "Income";

            Display();
            income.setBackgroundResource(R.drawable.selected_category);
            expense.setBackgroundResource(R.drawable.unselected_category);

            income.setTextColor(getResources().getColor(R.color.white));
            expense.setTextColor(getResources().getColor(R.color.black));
        } else if (clicked.equals("Expense")) {
            tagFind = "Expense";

            Display();
            income.setBackgroundResource(R.drawable.unselected_category);
            expense.setBackgroundResource(R.drawable.selected_category);

            income.setTextColor(getResources().getColor(R.color.black));
            expense.setTextColor(getResources().getColor(R.color.white));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagFind = "Income";

                Display();
                income.setBackgroundResource(R.drawable.selected_category);
                expense.setBackgroundResource(R.drawable.unselected_category);

                income.setTextColor(getResources().getColor(R.color.white));
                expense.setTextColor(getResources().getColor(R.color.black));
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagFind = "Expense";

                Display();
                expense.setBackgroundResource(R.drawable.selected_category);
                income.setBackgroundResource(R.drawable.unselected_category);

                expense.setTextColor(getResources().getColor(R.color.white));
                income.setTextColor(getResources().getColor(R.color.black));
            }
        });

        addNewCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewCategoryBottomDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (click) {
            if (findClick.equals("Add")) {
                showAddNewCategoryBottomDialog();
            } else if (findClick.equals("Edit")) {
                dialog1.show();
            }
        }
    }

//    private boolean isDataExists() {
//        Cursor cursor = dbHelper.getAllCategoryData();
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String categoryName = cursor.getString(1);
//            int categoryImage = cursor.getInt(2);
//            String tag = cursor.getString(3);
//            for (int i = 0; i < categoryArrayList.size(); i++) {
//                String name = categoryArrayList.get(i).getCategoryName();
//                int image = categoryArrayList.get(i).getCategoryImage();
//                String tag1 = categoryArrayList.get(i).getCategoryTag();
//                if (TextUtils.equals(name, categoryName) && image == categoryImage && TextUtils.equals(tag, tag1)) {
//                    cursor.close();
//                    dbHelper.close();
//                    return true;
//                }
//            }
//        }
//        cursor.close();
//        dbHelper.close();
//        return false;
//    }

    private void showAddNewCategoryBottomDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(AddCategoryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_category_bottomsheet_layout);
        dialog.setCancelable(true);
        dialog.show();

        EditText addNewCategory = dialog.findViewById(R.id.adding);
        TextView save = dialog.findViewById(R.id.save);
        ImageView categoryImage = dialog.findViewById(R.id.category_image);

        if (addCategoryImageResId != 0) {
            categoryImage.setImageResource(addCategoryImageResId);
        } else {
            categoryImage.setImageResource(R.drawable.i);
        }

        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findClick = "Add";
                Intent intent = new Intent(AddCategoryActivity.this, IconActivity.class);
                intent.putExtra("name",addNewCategory.getText().toString());
                launchSomeActivity.launch(intent);
                dialog.dismiss();
            }
        });

        if (!TextUtils.isEmpty(getCategoryName)){
            addNewCategory.setText(getCategoryName);
        } else {
            addNewCategory.setHint("Add category name");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addNewCategory.getText().toString())) {
                    Toast.makeText(AddCategoryActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
                } else {
                    addCategoryName = addNewCategory.getText().toString();
                    category = new Category(0, addCategoryName, addCategoryImageResId, tagFind);
                    categoryArrayList.add(category);
                    if (tagFind.equals("Income")) {
                        incomeList.add(category);
                        addCategoryIncomeAdapter.updateData(incomeList);
                    } else if (tagFind.equals("Expense")) {
                        expenseList.add(category);
                        addCategoryIncomeAdapter.updateData(expenseList);
                    } else {
                        Toast.makeText(AddCategoryActivity.this, "No Added Data", Toast.LENGTH_SHORT).show();
                    }
                    dbHelper.insertCategoryData(category);
                    dialog.dismiss();
                }
            }
        });
    }

    public void showEditNewCategoryBottomDialog(Category getcategory, int position) {

        dialog1 = new BottomSheetDialog(AddCategoryActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.add_new_category_bottomsheet_layout);
        dialog1.setCancelable(true);
        dialog1.show();

        EditText editCategory = dialog1.findViewById(R.id.adding);
        View view = dialog1.findViewById(R.id.view);
        TextView save = dialog1.findViewById(R.id.save);
        TextView delete = dialog1.findViewById(R.id.delete);
        ImageView categoryImage = dialog1.findViewById(R.id.category_image);

        save.setText("Update");

        delete.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);

        if (getcategory.getCategoryImage() == 0 && addCategoryImageResId == 0) {
            categoryImage.setImageResource(R.drawable.i);
        } else {
            categoryImage.setImageResource(getcategory.getCategoryImage());
        }

//        if (TextUtils.isEmpty(addCategoryName)){
            editCategory.setText(getcategory.getCategoryName());
//        } else {
//            editCategory.setText(getCategoryName);
//        }

        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findClick = "Edit";
                Intent intent = new Intent(AddCategoryActivity.this, IconActivity.class);
                intent.putExtra("name",getcategory.getCategoryName());
                launchSomeActivity.launch(intent);
                dialog1.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagFind.equals("Income")) {
                    dbHelper.deleteCategoryData(getcategory.getId());
                    incomeList.remove(position);
                    addCategoryIncomeAdapter.updateData(incomeList);
                } else if (tagFind.equals("Expense")) {
                    dbHelper.deleteCategoryData(getcategory.getId());
                    expenseList.remove(position);
                    addCategoryIncomeAdapter.updateData(expenseList);
                }
                categoryArrayList.remove(position);
                dialog1.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editCategory.getText().toString())) {
                    Toast.makeText(AddCategoryActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
                } else {
                    addCategoryName = editCategory.getText().toString();

                    if (tagFind.equals("Income")) {
                        for (int i = 0; i < incomeList.size(); i++) {
                            if (incomeList.get(i).getId() == getcategory.getId()) {
                                incomeList.set(i, category);
                                break;
                            }
                        }
                        addCategoryIncomeAdapter.updateData(incomeList);
                    } else if (tagFind.equals("Expense")) {
                        for (int i = 0; i < expenseList.size(); i++) {
                            if (expenseList.get(i).getId() == getcategory.getId()) {
                                expenseList.set(i, category);
                                break;
                            }
                        }
                        addCategoryIncomeAdapter.updateData(expenseList);
                    } else {
                        Toast.makeText(AddCategoryActivity.this, "No Added Data", Toast.LENGTH_SHORT).show();
                    }
                    category = new Category(getcategory.getId(), addCategoryName, addCategoryImageResId, tagFind);
                    categoryArrayList.add(category);
                    dbHelper.updateCategoryData(AddCategoryActivity.this.category);
                    dialog1.dismiss();
                }
            }
        });
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

                incomeList = filterCategories(categoryArrayList, "Income");
                expenseList = filterCategories(categoryArrayList, "Expense");

                if (TextUtils.equals(tagFind, "Income")) {
                    incomeCategoryRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                    addCategoryIncomeAdapter = new AddCategoryIncomeAdapter(AddCategoryActivity.this, incomeList);
                    incomeCategoryRecyclerview.setAdapter(addCategoryIncomeAdapter);
                } else if (TextUtils.equals(tagFind, "Expense")) {
                    incomeCategoryRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                    addCategoryIncomeAdapter = new AddCategoryIncomeAdapter(AddCategoryActivity.this, expenseList);
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

//    private void showIncomeRecyclerView() {
//
////        Collections.sort(categoryArrayList, new Comparator<Category>() {
////            @Override
////            public int compare(Category category1, Category category2) {
////                return category1.getCategoryName().compareTo(category2.getCategoryName());
////            }
////        });
//
//        incomeCategoryRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        addCategoryIncomeAdapter = new AddCategoryIncomeAdapter(AddCategoryActivity.this, categoryArrayList);
//        incomeCategoryRecyclerview.setAdapter(addCategoryIncomeAdapter);
//
//        incomeCategoryRecyclerview.setVisibility(View.VISIBLE);
//        expenseCategoryRecyclerview.setVisibility(View.GONE);
//    }
//
//    private void showExpenseRecyclerView() {
//        expenseCategoryRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        addCategoryExpenseAdapter = new AddCategoryExpenseAdapter(AddCategoryActivity.this, categoryArrayList);
//        expenseCategoryRecyclerview.setAdapter(addCategoryExpenseAdapter);
//
//        incomeCategoryRecyclerview.setVisibility(View.GONE);
//        expenseCategoryRecyclerview.setVisibility(View.VISIBLE);
//    }

    public void getData(String name1, int image1) {
        name = name1;
        image = image1;
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
        expenseCategoryRecyclerview = findViewById(R.id.expenseCategoryRecyclerview);
        addNewCategoryBtn = findViewById(R.id.add_new_category);
        back = findViewById(R.id.back);
    }
}