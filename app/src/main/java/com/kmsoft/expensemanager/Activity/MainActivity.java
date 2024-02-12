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

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    BottomNavigationView bottomNavigationView;
    String fragment = "";
    FloatingActionButton fab, fab1, fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    public static boolean isStep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        init();

        dbHelper = new DBHelper(this);

        openFragment(new HomeFragment());

        fabOpen = AnimationUtils.loadAnimation
                (this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation
                (this,R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation
                (this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation
                (this,R.anim.rotate_backward);
        fab.setOnClickListener(view -> animateFab());

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
                openFragment(new HomeFragment());
                return true;
            }
            if (item.getItemId() == R.id.transaction) {
                fragment = "transaction";
                isStep = true;
                openFragment(new TransactionFragment());
                return true;
            }
            if (item.getItemId() == R.id.budget) {
                fragment = "budget";
                openFragment(new BudgetFragment());
                return true;
            }
            if (item.getItemId() == R.id.profile) {
                fragment = "profile";
                openFragment(new ProfileFragment());
                return true;
            }
            return true;
        });
    }

    private void openFragment(Fragment fragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.framelayout, fragment)
                    .commit();

    }

    private void animateFab(){
        if (isOpen){
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen=false;
        } else {
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen=true;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (fragment instanceof TransactionFragment){
            if (isStep) {
                super.onBackPressed();
            }
            else {
                Fragment mFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void init(){
        bottomNavigationView = findViewById(R.id.bottomnavigationview);
        fab = findViewById(R.id.fabAdd);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
    }
}