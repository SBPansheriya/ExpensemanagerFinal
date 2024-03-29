package com.kmsoft.expensemanager.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kmsoft.expensemanager.Activity.FloatingButton.ExpenseActivity;
import com.kmsoft.expensemanager.Activity.FloatingButton.IncomeActivity;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.Fragment.BudgetFragment;
import com.kmsoft.expensemanager.Fragment.HomeFragment;
import com.kmsoft.expensemanager.Fragment.ProfileFragment;
import com.kmsoft.expensemanager.R;
import com.kmsoft.expensemanager.Fragment.TransactionFragment;

import java.util.Currency;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    BottomNavigationView bottomNavigationView;
    LinearLayout fabVisibility;
    String fragment = "";
    FloatingActionButton fab, fab1, fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    public static boolean isStep = false;
    public static String currencySymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        init();

        currencySymbol = getCurrencySymbol();

        dbHelper = new DBHelper(this);

        openFragment(new HomeFragment());

        fabOpen = AnimationUtils.loadAnimation
                (this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation
                (this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation
                (this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation
                (this, R.anim.rotate_backward);
        fab.setOnClickListener(view -> {
            animateFab();
            fabVisibility.setVisibility(View.VISIBLE);
        });

        fab1.setOnClickListener(view -> {
            animateFab();
            Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
            startActivity(intent);
        });
        fab2.setOnClickListener(view -> {
            animateFab();
            Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                fragment = "home";
                isOpen = false;
                openFragment(new HomeFragment());
                fabVisibility.setVisibility(View.GONE);
                return true;
            }
            if (item.getItemId() == R.id.transaction) {
                fragment = "transaction";
                isOpen = false;
                isStep = true;
                openFragment(new TransactionFragment());
                fabVisibility.setVisibility(View.GONE);
                return true;
            }
            if (item.getItemId() == R.id.budget) {
                fragment = "budget";
                isOpen = false;
                openFragment(new BudgetFragment());
                fabVisibility.setVisibility(View.GONE);
                return true;
            }
            if (item.getItemId() == R.id.profile) {
                fragment = "profile";
                isOpen = false;
                openFragment(new ProfileFragment());
                fabVisibility.setVisibility(View.GONE);
                return true;
            }
            return true;
        });
    }

    private String getCurrencySymbol() {
        // Get the default locale of the device
        Locale locale = Locale.getDefault();

        // Get the currency instance for the device's locale
        Currency currency = Currency.getInstance(locale);

        // Get the currency symbol
        return currency.getSymbol();
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.framelayout, fragment)
                .commit();
    }

    private void animateFab() {
        if (isOpen) {
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen = false;
        } else {
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen = true;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (fragment instanceof TransactionFragment) {
            if (isStep) {
                super.onBackPressed();
            } else {
                Fragment mFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        fab = findViewById(R.id.fabAdd);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fabVisibility = findViewById(R.id.fab_visibility);
    }
}