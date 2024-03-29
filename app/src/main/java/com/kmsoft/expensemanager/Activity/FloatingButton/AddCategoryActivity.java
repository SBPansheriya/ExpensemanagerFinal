package com.kmsoft.expensemanager.Activity.FloatingButton;

import static com.kmsoft.expensemanager.Activity.FloatingButton.IncomeActivity.clickable;
import static com.kmsoft.expensemanager.Constant.MY_COLORS;
import static com.kmsoft.expensemanager.Constant.categories;
import static com.kmsoft.expensemanager.Constant.categoriesImage;
import static com.kmsoft.expensemanager.Constant.categoryArrayList;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.Adapter.AddCategoryAdapter;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;
import java.util.Random;

public class AddCategoryActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView income, expense;
    ImageView back;
    LinearLayout add_mini_layout;
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
    int id;
    int image;
    int color;
    String tagFind;
    String findClick;
    ArrayList<Category> incomeCategoryList = new ArrayList<>();
    ArrayList<Category> expenseCategoryList = new ArrayList<>();
    ImageView editNewCategoryImage;
    ImageView addNewCategoryImage;
    LinearLayoutManager layoutManager;
    int getImage;
    String getName;
    int getColor;
    int newImage;
    int getId;

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
        getImage = getIntent().getIntExtra("image", 0);
        getColor = getIntent().getIntExtra("color", 0);
        getId = getIntent().getIntExtra("id", 0);
        getName = getIntent().getStringExtra("name");

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    if (findClick.equals("Edit")) {
                        newImage = data.getIntExtra("imageResId", 0);
                        editNewCategoryImage.setImageResource(newImage);
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

            income.setTextColor(ContextCompat.getColor(this, R.color.white));
            expense.setTextColor(ContextCompat.getColor(this, R.color.black));
        } else if (clicked.equals("Expense")) {
            tagFind = "Expense";

            Display();
            income.setBackgroundResource(R.drawable.unselected_category);
            expense.setBackgroundResource(R.drawable.selected_category);

            income.setTextColor(ContextCompat.getColor(this, R.color.black));
            expense.setTextColor(ContextCompat.getColor(this, R.color.white));
        }

        back.setOnClickListener(v -> onBackPressed());

        income.setOnClickListener(v -> {
            tagFind = "Income";

            Display();
            income.setBackgroundResource(R.drawable.selected_category);
            expense.setBackgroundResource(R.drawable.unselected_category);

            income.setTextColor(ContextCompat.getColor(this, R.color.white));
            expense.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        expense.setOnClickListener(v -> {
            tagFind = "Expense";

            Display();
            expense.setBackgroundResource(R.drawable.selected_category);
            income.setBackgroundResource(R.drawable.unselected_category);

            expense.setTextColor(ContextCompat.getColor(this, R.color.white));
            income.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        addNewCategoryBtn.setOnClickListener(v -> showAddNewCategoryBottomDialog());
        add_mini_layout.setOnClickListener(v -> showAddNewCategoryBottomDialog());

        incomeCategoryRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (TextUtils.equals(tagFind, "Income")) {
                        if (layoutManager.findLastVisibleItemPosition() == incomeCategoryList.size() - 1) {
                            addNewCategoryBtn.animate().alpha(0.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.GONE));

                            add_mini_layout.animate().alpha(1.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.VISIBLE));

                        } else {
                            addNewCategoryBtn.animate().alpha(1.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.VISIBLE));

                            add_mini_layout.animate().alpha(0.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.GONE));
                        }
                    } else {
                        if (layoutManager.findLastVisibleItemPosition() == expenseCategoryList.size() - 1) {
                            addNewCategoryBtn.animate().alpha(0.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.GONE));

                            add_mini_layout.animate().alpha(1.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.VISIBLE));

                        } else {
                            addNewCategoryBtn.animate().alpha(1.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.VISIBLE));

                            add_mini_layout.animate().alpha(0.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.GONE));
                        }
                    }

                }
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
                int randomColor = getRandomColor();
                boolean categoryFound = false;
                for (int i = 0; i < categoryArrayList.size(); i++) {
                    if (addCategoryName.equals(categoryArrayList.get(i).getCategoryName())) {
                        Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show();
                        categoryFound = true;
                        break;
                    }
                }

                if (!categoryFound) {
                    category = new Category(0, addCategoryName, addCategoryImage, tagFind, randomColor);
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
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static int getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        int randomColor = Color.rgb(red, green, blue);

        for (int color : MY_COLORS) {
            if (color == randomColor) {
                return getRandomColor();
            }
        }
        return randomColor;
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

        dialog1.setOnDismissListener(dialog -> {
            editCategoryImage = 0;
            newImage = 0;
        });

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
                clickable = 1;
                if (tagFind.equals("Income")) {
                    dbHelper.deleteCategoryData(getcategory.getId());
                    incomeCategoryList.remove(position);
                    addCategoryIncomeAdapter.updateData(incomeCategoryList);
                } else if (tagFind.equals("Expense")) {
                    dbHelper.deleteCategoryData(getcategory.getId());
                    expenseCategoryList.remove(position);
                    addCategoryIncomeAdapter.updateData(expenseCategoryList);
                }
                dbHelper.deleteAllData(getcategory.getId());
                dbHelper.deleteNotificationData(getcategory.getId());
                dbHelper.deleteAllBudgetData(getcategory.getId());
                categoryArrayList.remove(position);
                bottomSheetDialog.dismiss();

                Dialog dialog = new Dialog(AddCategoryActivity.this);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dailog_removed_layout);
                dialog.setCancelable(false);
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
                if (editCategoryImage == 0) {
                    editCategoryImage = category.getCategoryImage();
                }

                boolean categoryFound = false;
                for (int i = 0; i < categoryArrayList.size(); i++) {
                    if (addCategoryName.equals(categoryArrayList.get(i).getCategoryName()) && categoryArrayList.get(i).getId() != getcategory.getId()) {
                        Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show();
                        categoryFound = true;
                        break;
                    }
                }

                if (!categoryFound) {
                    if (newImage == 0) {
                        newImage = getcategory.getCategoryImage();
                    }
                    category = new Category(getcategory.getId(), addCategoryName, newImage, tagFind, category.getColor());
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
                }
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
        for (int i = 0; i < categories.length; i++) {
            String type = (i < 10) ? "Income" : "Expense";
            dbHelper.insertCategoryData(new Category(0, categories[i], categoriesImage[i], type, MY_COLORS[i]));
        }
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
                int color = cursor.getInt(4);

                category = new Category(id, categoryName, categoryImage, tag, color);
                categoryArrayList.add(category);
            } while (cursor.moveToNext());
            incomeCategoryList = filterCategories(categoryArrayList, "Income");
            expenseCategoryList = filterCategories(categoryArrayList, "Expense");

            if (TextUtils.equals(tagFind, "Income")) {
                incomeCategoryRecyclerview.setLayoutManager(layoutManager);
                addCategoryIncomeAdapter = new AddCategoryAdapter(AddCategoryActivity.this, incomeCategoryList, getName);
                incomeCategoryRecyclerview.setAdapter(addCategoryIncomeAdapter);
                if (layoutManager.findLastVisibleItemPosition() == incomeCategoryList.size() - 1) {
                    addNewCategoryBtn.animate().alpha(0.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.GONE));

                    add_mini_layout.animate().alpha(1.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.VISIBLE));

                } else {
                    addNewCategoryBtn.animate().alpha(1.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.VISIBLE));

                    add_mini_layout.animate().alpha(0.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.GONE));
                }
            } else if (TextUtils.equals(tagFind, "Expense")) {
                incomeCategoryRecyclerview.setLayoutManager(layoutManager);
                addCategoryIncomeAdapter = new AddCategoryAdapter(AddCategoryActivity.this, expenseCategoryList, getName);
                incomeCategoryRecyclerview.setAdapter(addCategoryIncomeAdapter);
                if (layoutManager.findLastVisibleItemPosition() == expenseCategoryList.size() - 1) {
                    addNewCategoryBtn.animate().alpha(0.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.GONE));

                    add_mini_layout.animate().alpha(1.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.VISIBLE));

                } else {
                    addNewCategoryBtn.animate().alpha(1.0f).setDuration(500).withEndAction(() -> addNewCategoryBtn.setVisibility(View.VISIBLE));

                    add_mini_layout.animate().alpha(0.0f).setDuration(500).withEndAction(() -> add_mini_layout.setVisibility(View.GONE));
                }
            }
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

    public void getData(String name1, int image1, int categoryColor, int id1) {
        if (TextUtils.isEmpty(name1)) {
            name = getName;
        } else {
            name = name1;
        }

        if (id1 == 0) {
            id = getId;
        } else {
            id = id1;
        }

        if (image1 == 0) {
            image = getImage;
        } else {
            image = image1;
        }

        if (categoryColor == 0) {
            color = getColor;
        } else {
            color = categoryColor;
        }
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (TextUtils.isEmpty(name)) {
            name = getName;
        }
        if (id == 0) {
            id = getId;
        }
        if (image == 0) {
            image = getImage;
        }
        if (color == 0) {
            color = getColor;
        }
        intent.putExtra("id", id);
        intent.putExtra("categoryImage", image);
        intent.putExtra("categoryName", name);
        intent.putExtra("categoryColor", color);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init() {
        income = findViewById(R.id.category_income);
        expense = findViewById(R.id.category_expense);
        incomeCategoryRecyclerview = findViewById(R.id.incomeCategoryRecyclerview);
        addNewCategoryBtn = findViewById(R.id.add_new_category);
        back = findViewById(R.id.back);
        add_mini_layout = findViewById(R.id.add_mini_layout);
    }
}