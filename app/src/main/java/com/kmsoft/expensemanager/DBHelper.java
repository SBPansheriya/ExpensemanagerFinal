package com.kmsoft.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "IncomeExpenseData";

    private static final String TABLE = "data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CURRENTDATETIMESTAMP = "currentdatetimestamp";
    private static final String COLUMN_SELECTEDDATETIMESTAMP = "selecteddatetimestamp";
    private static final String COLUMN_CURRENTDATE = "currentdate";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CATEGORY_NAME = "category_name";
    private static final String COLUMN_CATEGORY_IMAGE = "category_image";
    private static final String COLUMN_CATEGORY_COLOR = "category_color";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ADD_ATTACHMENT = "add_attachment";
    private static final String COLUMN_TAG = "tag";
    private static final String COLUMN_CATEGORY_ID = "category_id";

    private static final String TABLE1 = "Categorydata";
    private static final String COLUMN_ID_SHOW = "id";
    public static final String COLUMN_CATEGORY_NAME_SHOW = "category_name_show";
    public static final String COLUMN_CATEGORY_IMAGE_SHOW = "category_image_show";
    public static final String COLUMN_TAG_SHOW = "category_tag_show";
    public static final String COLUMN_TAG_COLOR = "category_tag_color";

    private static final String TABLE2 = "Budgetdata";
    private static final String COLUMN_ID_BUDGET = "id";
    private static final String COLUMN_AMOUNT_BUDGET = "amount_budget";
    public static final String COLUMN_CATEGORY_NAME_BUDGET = "category_name_budget";
    public static final String COLUMN_CATEGORY_IMAGE_BUDGET = "category_image_budget";
    public static final String COLUMN_PERCENTAGE_BUDGET = "percentage_budget";

    private static final String TABLE3 = "Notificationdata";
    private static final String COLUMN_ID_NOTIFICATION = "id";
    private static final String COLUMN_AMOUNT_NOTIFICATION = "notification_amount_budget";
    public static final String COLUMN_CATEGORY_NAME_NOTIFICATION = "notification_category_name_budget";
    public static final String COLUMN_CATEGORY_IMAGE_NOTIFICATION = "notification_category_image_budget";
    public static final String COLUMN_CATEGORY_CURRENTTIME_NOTIFICATION = "notification_category_currentTime_budget";
    public static final String COLUMN_CATEGORY_ISREMOVE_NOTIFICATION = "notification_category_isRemove_budget";
    public static final String COLUMN_CATEGORY_TAG_NOTIFICATION = "notification_category_tag_budget";

    private static final String TABLE4 = "Profiledata";
    private static final String COLUMN_ID_PROFILE = "id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_IMAGE = "user_image";

    private static final int VER = 1;

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_CURRENTDATETIMESTAMP + " TEXT,"
            + COLUMN_SELECTEDDATETIMESTAMP + " TEXT,"
            + COLUMN_CURRENTDATE + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_DAY + " TEXT,"
            + COLUMN_TIME + " TEXT,"
            + COLUMN_CATEGORY_NAME + " TEXT,"
            + COLUMN_CATEGORY_IMAGE + " TEXT,"
            + COLUMN_CATEGORY_COLOR + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_ADD_ATTACHMENT + " TEXT,"
            + COLUMN_TAG + " TEXT,"
            + COLUMN_CATEGORY_ID + " TEXT"
            + ")";

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE1 + "("
            + COLUMN_ID_SHOW + " INTEGER PRIMARY KEY,"
            + COLUMN_CATEGORY_NAME_SHOW + " TEXT,"
            + COLUMN_CATEGORY_IMAGE_SHOW + " TEXT,"
            + COLUMN_TAG_SHOW + " TEXT,"
            + COLUMN_TAG_COLOR + " TEXT"
            + ")";

    private static final String CREATE_TABLE_BUDGET = "CREATE TABLE " + TABLE2 + "("
            + COLUMN_ID_BUDGET + " INTEGER PRIMARY KEY,"
            + COLUMN_AMOUNT_BUDGET + " TEXT,"
            + COLUMN_CATEGORY_NAME_BUDGET + " TEXT,"
            + COLUMN_CATEGORY_IMAGE_BUDGET + " TEXT,"
            + COLUMN_PERCENTAGE_BUDGET + " TEXT"
            + ")";

    private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE " + TABLE3 + "("
            + COLUMN_ID_NOTIFICATION + " INTEGER PRIMARY KEY,"
            + COLUMN_AMOUNT_NOTIFICATION + " TEXT,"
            + COLUMN_CATEGORY_NAME_NOTIFICATION + " TEXT,"
            + COLUMN_CATEGORY_IMAGE_NOTIFICATION + " TEXT,"
            + COLUMN_CATEGORY_CURRENTTIME_NOTIFICATION + " TEXT,"
            + COLUMN_CATEGORY_ISREMOVE_NOTIFICATION + " TEXT,"
            + COLUMN_CATEGORY_TAG_NOTIFICATION + " TEXT"
            + ")";

    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE " + TABLE4 + "("
            + COLUMN_ID_PROFILE + " INTEGER PRIMARY KEY,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_IMAGE + " TEXT"
            + ")";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VER);
        Log.d("TTT", "DataBase: create database");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
        sqLiteDatabase.execSQL(CREATE_TABLE_BUDGET);
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTIFICATION);
        sqLiteDatabase.execSQL(CREATE_TABLE_PROFILE);
        Log.d("TTT", "onCreate: create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE4);
        onCreate(sqLiteDatabase);
    }

    // Insert Data
    public void insertData(IncomeAndExpense incomeAndExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, incomeAndExpense.getAmount());
        contentValues.put(COLUMN_CURRENTDATETIMESTAMP, incomeAndExpense.getCurrantDateTimeStamp());
        contentValues.put(COLUMN_SELECTEDDATETIMESTAMP, incomeAndExpense.getSelectedDateTimeStamp());
        contentValues.put(COLUMN_CURRENTDATE, incomeAndExpense.getCurrantDate());
        contentValues.put(COLUMN_DATE, incomeAndExpense.getDate());
        contentValues.put(COLUMN_DAY, incomeAndExpense.getDayName());
        contentValues.put(COLUMN_TIME, incomeAndExpense.getTime());
        contentValues.put(COLUMN_CATEGORY_NAME, incomeAndExpense.getCategoryName());
        contentValues.put(COLUMN_CATEGORY_IMAGE, incomeAndExpense.getCategoryImage());
        contentValues.put(COLUMN_CATEGORY_COLOR, incomeAndExpense.getCategoryColor());
        contentValues.put(COLUMN_DESCRIPTION, incomeAndExpense.getDescription());
        contentValues.put(COLUMN_ADD_ATTACHMENT, incomeAndExpense.getAddAttachment());
        contentValues.put(COLUMN_TAG, incomeAndExpense.getTag());
        contentValues.put(COLUMN_CATEGORY_ID, incomeAndExpense.getCategoryId());
        db.insert(TABLE, null, contentValues);
        db.close();
    }

    // Update Data
    public void updateData(IncomeAndExpense incomeAndExpense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, incomeAndExpense.getAmount());
        contentValues.put(COLUMN_CURRENTDATETIMESTAMP, incomeAndExpense.getCurrantDateTimeStamp());
        contentValues.put(COLUMN_SELECTEDDATETIMESTAMP, incomeAndExpense.getSelectedDateTimeStamp());
        contentValues.put(COLUMN_CURRENTDATE, incomeAndExpense.getCurrantDate());
        contentValues.put(COLUMN_DATE, incomeAndExpense.getDate());
        contentValues.put(COLUMN_DAY, incomeAndExpense.getDayName());
        contentValues.put(COLUMN_TIME, incomeAndExpense.getTime());
        contentValues.put(COLUMN_CATEGORY_NAME, incomeAndExpense.getCategoryName());
        contentValues.put(COLUMN_CATEGORY_IMAGE, incomeAndExpense.getCategoryImage());
        contentValues.put(COLUMN_CATEGORY_COLOR, incomeAndExpense.getCategoryColor());
        contentValues.put(COLUMN_DESCRIPTION, incomeAndExpense.getDescription());
        contentValues.put(COLUMN_ADD_ATTACHMENT, incomeAndExpense.getAddAttachment());
        contentValues.put(COLUMN_TAG, incomeAndExpense.getTag());
        contentValues.put(COLUMN_CATEGORY_ID, incomeAndExpense.getCategoryId());
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
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE, null);
    }


    // Category Crud

    //insert Data
    public void insertCategoryData(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY_NAME_SHOW, category.getCategoryName());
        contentValues.put(COLUMN_CATEGORY_IMAGE_SHOW, category.getCategoryImage());
        contentValues.put(COLUMN_TAG_SHOW, category.getCategoryTag());
        contentValues.put(COLUMN_TAG_COLOR, category.getColor());

        db.insert(TABLE1, null, contentValues);
        db.close();
    }

    public Cursor getCategory(int id){
        SQLiteDatabase db =  this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE1 + " WHERE " + COLUMN_ID_SHOW + " = ?", new String[]{String.valueOf(id)}, null);
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
        contentValues.put(COLUMN_TAG_COLOR, category.getColor());

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


    //Budget Crud

    //insert Data
    public void insertBudgetData(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT_BUDGET, budget.getAmountBudget());
        contentValues.put(COLUMN_CATEGORY_NAME_BUDGET, budget.getCategoryNameBudget());
        contentValues.put(COLUMN_CATEGORY_IMAGE_BUDGET, budget.getCategoryImageBudget());
        contentValues.put(COLUMN_PERCENTAGE_BUDGET, budget.getPercentageBudget());

        db.insert(TABLE2, null, contentValues);
        db.close();
    }

    // Retrieve All Data
    public Cursor getAllBudgetData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE2, null);
    }

    // Update Data
    public void updateBudgetData(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT_BUDGET, budget.getAmountBudget());
        contentValues.put(COLUMN_CATEGORY_NAME_BUDGET, budget.getCategoryNameBudget());
        contentValues.put(COLUMN_CATEGORY_IMAGE_BUDGET, budget.getCategoryImageBudget());
        contentValues.put(COLUMN_PERCENTAGE_BUDGET, budget.getPercentageBudget());
        db.update(TABLE2, contentValues, COLUMN_ID_BUDGET + " = ?",
                new String[]{String.valueOf(budget.getId())});
        db.close();
    }

    // Delete Data
    public void deleteBudgetData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE2, COLUMN_ID_BUDGET + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    // Notification Curd

    //insert Data
    public void insertBudgetNotificationData(String amount, String name, int image, String time, boolean isRemove, String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT_NOTIFICATION, amount);
        contentValues.put(COLUMN_CATEGORY_NAME_NOTIFICATION, name);
        contentValues.put(COLUMN_CATEGORY_IMAGE_NOTIFICATION, image);
        contentValues.put(COLUMN_CATEGORY_CURRENTTIME_NOTIFICATION, time);
        contentValues.put(COLUMN_CATEGORY_ISREMOVE_NOTIFICATION, isRemove);
        contentValues.put(COLUMN_CATEGORY_TAG_NOTIFICATION, tag);

        db.insert(TABLE3, null, contentValues);
        db.close();
    }

    // Retrieve All Data
    public Cursor getAllBudgetNotificationData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE3, null);
    }

    // Delete Data
    public void deleteAllBudgetNotificationData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE3, null, null);
        db.close();
    }

    //insert Data
    public void insertProfileData(String name,String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_USER_IMAGE, image);

        db.insert(TABLE4, null, contentValues);
        db.close();
    }

    // Retrieve All Data
    public Cursor getAllProfileData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE4, null);
    }

    // Delete Data
    public void deleteProfileData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE4, null, null);
        db.close();
    }

    public void restoreDatabase(Context context, Uri directoryUri) {
        try {
            File originalDBFile = context.getDatabasePath(DBNAME);
            try (InputStream inputStream = context.getContentResolver().openInputStream(directoryUri);
                 OutputStream outputStream = new FileOutputStream(originalDBFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
            Toast.makeText(context, "Restore Successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Restore error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backupDatabase(Context context, Uri directoryUri) {
        try {
            File originalDBFile = context.getDatabasePath(DBNAME);
            DocumentFile directory = DocumentFile.fromTreeUri(context, directoryUri);

            String backupFileName = "ListData.db";
            DocumentFile backupDBFile = directory.createFile("application/x-sqlite3", backupFileName);

            if (directory.exists()) {
                FileChannel src = new FileInputStream(originalDBFile).getChannel();
                    FileOutputStream outputStream = (FileOutputStream) context.getContentResolver().openOutputStream(backupDBFile.getUri());
                if (outputStream != null) {
                    FileChannel dst = outputStream.getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(context, "Backup Successfully!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(context, "Backup File Not Found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    public void backupDatabase(Context context) {
//        try {
//            File originalDBFile = context.getDatabasePath(DBNAME);
//            String backupFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/ExpenseManagerBackup";
//            File backupDir = new File(backupFilePath);
//            if (!backupDir.exists()) {
//                backupDir.mkdirs();
//            }
//
//            String timeStamp1 = new SimpleDateFormat("dd_MM_yyyy_HHmmss", Locale.getDefault()).format(new Date());
//            String backupFileName1 = "New" + timeStamp1;
//
//            String newFolderPath = backupFilePath + "/" + backupFileName1;
//            File newFolder = new File(newFolderPath);
//            if (!newFolder.exists()) {
//                newFolder.mkdirs();
//            }
//
//            String backupFileName = "ListData.db";
//            File backupDBFile = new File(newFolder, backupFileName);
//
//            if (originalDBFile.exists() && originalDBFile.length() > 0) {
//                FileChannel src = new FileInputStream(originalDBFile).getChannel();
//                FileChannel dst = new FileOutputStream(backupDBFile).getChannel();
//                dst.transferFrom(src, 0, src.size());
//                src.close();
//                dst.close();
//                Toast.makeText(context, "Backup Successfully!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "No data in the database to backup", Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Backup error!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void restoreDatabase1(Context context, Uri directoryUri) {
//        try {
//
//            InputStream inputStream = context.getContentResolver().openInputStream(directoryUri);
//            if (inputStream != null) {
//                OutputStream outputStream = new FileOutputStream(context.getDatabasePath(DBNAME));
//
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, length);
//                }
//                inputStream.close();
//                outputStream.close();
//                Toast.makeText(context, "Restore Successful!", Toast.LENGTH_SHORT).show();
//
//            } else {
//                // Handle backup file not found
//                Log.e("Restore", "Backup file not found");
//            }
//        } catch (IOException e) {
//            // Handle IOException
//            e.printStackTrace();
//            Log.e("Restore", "Error while restoring database: " + e.getMessage());
//        }
//
//    }
}
