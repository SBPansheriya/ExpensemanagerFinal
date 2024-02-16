package com.kmsoft.expensemanager;

import android.graphics.Color;

import com.kmsoft.expensemanager.Model.Budget;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.Model.IncomeAndExpense;

import java.util.ArrayList;

public class Constant {
    public static Integer[] iconList = {R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4, R.drawable.i5,
            R.drawable.i6, R.drawable.i7, R.drawable.i8, R.drawable.i9, R.drawable.i10,
            R.drawable.i11, R.drawable.i12, R.drawable.i13, R.drawable.i14, R.drawable.i15,
            R.drawable.i16, R.drawable.i17, R.drawable.i18, R.drawable.i19, R.drawable.i20,
            R.drawable.i21, R.drawable.i22, R.drawable.i23, R.drawable.i24, R.drawable.i25,
            R.drawable.i26, R.drawable.i27, R.drawable.i28, R.drawable.i29, R.drawable.i30,
            R.drawable.i31, R.drawable.i32, R.drawable.i33, R.drawable.i34, R.drawable.i35,
            R.drawable.i36, R.drawable.i37, R.drawable.i38, R.drawable.i39, R.drawable.i40,
            R.drawable.i41, R.drawable.i42, R.drawable.i43, R.drawable.i44, R.drawable.i45,
            R.drawable.i46, R.drawable.i47, R.drawable.i48, R.drawable.i49, R.drawable.i50};

    public static int[] MY_COLORS = {
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134),
            Color.rgb(179, 48, 80),
            Color.rgb(255, 102, 0),
            Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31),
            Color.rgb(179, 100, 53),
            Color.rgb(207, 248, 24),
            Color.rgb(148, 212, 212),
            Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175),
            Color.rgb(42, 109, 130),
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 120),
            Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157),
            Color.rgb(193, 37, 82)
    };
    public static String[] categories = {
            "Shopping",
            "Food",
            "Birthday",
            "Party",
            "Medicine",
            "Books",
            "Sports",
            "Traveling",
            "Education",
            "Transportation",
            "Entertainment",
            "Gifts",
            "Health & Fitness",
            "Investments",
            "Pets",
            "Games",
            "Car",
            "Donation",
            "Shipping",
            "Diamond & Jewellery"
    };

    public static int[] categoriesImage = {
            R.drawable.i23,
            R.drawable.i12,
            R.drawable.i11,
            R.drawable.i7,
            R.drawable.i18,
            R.drawable.i1,
            R.drawable.i43,
            R.drawable.i17,
            R.drawable.i22,
            R.drawable.i38,
            R.drawable.i15,
            R.drawable.i9,
            R.drawable.i41,
            R.drawable.i37,
            R.drawable.i39,
            R.drawable.i34,
            R.drawable.i25,
            R.drawable.i38,
            R.drawable.i30,
            R.drawable.i19
    };

    public static ArrayList<IncomeAndExpense> incomeAndExpenseArrayList = new ArrayList<>();
    public static ArrayList<Category> categoryArrayList = new ArrayList<>();
    public static ArrayList<Budget> budgetArrayList = new ArrayList<>();
}
