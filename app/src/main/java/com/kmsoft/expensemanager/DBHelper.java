package com.kmsoft.expensemanager;

import static com.kmsoft.expensemanager.Constant.categoryArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "IncomeExpenseData";

    private static final String TABLE = "data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY_NAME = "category_name";
    private static final String COLUMN_CATEGORY_IMAGE = "category_image";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ADD_ATTACHMENT = "add_attachment";
    private static final String COLUMN_TAG = "tag";

    private static final String TABLE1 = "Categorydata";
    private static final String COLUMN_ID_SHOW = "id";
    public static final String COLUMN_CATEGORY_NAME_SHOW = "category_name_show";
    public static final String COLUMN_CATEGORY_IMAGE_SHOW = "category_image_show";
    public static final String COLUMN_TAG_SHOW = "category_tag_show";

    private static final int VER = 1;

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_CATEGORY_NAME + " TEXT,"
            + COLUMN_CATEGORY_IMAGE + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_ADD_ATTACHMENT + " TEXT,"
            + COLUMN_TAG + " TEXT"
            + ")";

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE1 + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_CATEGORY_NAME_SHOW + " TEXT,"
            + COLUMN_CATEGORY_IMAGE_SHOW + " TEXT,"
            + COLUMN_TAG_SHOW + " TEXT"
            + ")";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VER);
        Log.d("TTT", "DataBase: create database");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
        Log.d("TTT", "onCreate: create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE1);
        onCreate(sqLiteDatabase);
    }

    // Insert Data
    public void insertData(IncomeAndExpense incomeAndExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, incomeAndExpense.getAmount());
        contentValues.put(COLUMN_DATE, incomeAndExpense.getDate());
        contentValues.put(COLUMN_CATEGORY_NAME, incomeAndExpense.getCategoryName());
        contentValues.put(COLUMN_CATEGORY_IMAGE, incomeAndExpense.getCategoryImage());
        contentValues.put(COLUMN_DESCRIPTION, incomeAndExpense.getDescription());
        contentValues.put(COLUMN_ADD_ATTACHMENT, incomeAndExpense.getAddAttachment());
        contentValues.put(COLUMN_TAG, incomeAndExpense.getTag());
        db.insert(TABLE, null, contentValues);
        db.close();
    }

    // Update Data
    public void updateData(IncomeAndExpense incomeAndExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, incomeAndExpense.getAmount());
        contentValues.put(COLUMN_DATE, incomeAndExpense.getDate());
        contentValues.put(COLUMN_CATEGORY_NAME, incomeAndExpense.getCategoryName());
        contentValues.put(COLUMN_CATEGORY_IMAGE, incomeAndExpense.getCategoryImage());
        contentValues.put(COLUMN_DESCRIPTION, incomeAndExpense.getDescription());
        contentValues.put(COLUMN_ADD_ATTACHMENT, incomeAndExpense.getAddAttachment());
        contentValues.put(COLUMN_TAG, incomeAndExpense.getTag());
        db.update(TABLE, contentValues, COLUMN_ID + " = ?",
                new String[]{String.valueOf(incomeAndExpense.getId())});
        db.close();
    }

    // Delete Data
    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Retrieve All Data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE, null);
    }



    // Category Crud

    //insert
    public void insertCategoryData(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY_NAME_SHOW, category.getCategoryName());
        contentValues.put(COLUMN_CATEGORY_IMAGE_SHOW, category.getCategoryImage());
        contentValues.put(COLUMN_TAG_SHOW, category.getCategoryTag());

        db.insert(TABLE1, null, contentValues);
        db.close();
    }

    // Retrieve All Data
    public Cursor getAllCategoryData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE1, null);
    }

    // Update Data
    public void updateCategoryData(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY_NAME_SHOW, category.getCategoryName());
        contentValues.put(COLUMN_CATEGORY_IMAGE_SHOW, category.getCategoryImage());
        contentValues.put(COLUMN_TAG_SHOW, category.getCategoryTag());
        db.update(TABLE1, contentValues, COLUMN_ID_SHOW + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }

    // Delete Data
    public void deleteCategoryData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE1, COLUMN_ID_SHOW + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
