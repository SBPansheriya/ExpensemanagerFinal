package com.kmsoft.expensemanager.Activity;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    LinearLayout fabVisibility;
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

        checkPermissionsForNotification();

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

    private void checkPermissionsForNotification() {
        String[] permissions = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{POST_NOTIFICATIONS};
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            showPermissionDenyDialog(MainActivity.this);
        }
    }

    private void showPermissionDenyDialog(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, POST_NOTIFICATIONS)) {
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
            TextView filename = dialog.findViewById(R.id.filename);

            filename.setText(R.string.post_notification_permission_is_needed_for_proper_functioning_of_app);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                checkPermissionsForNotification();
                dialog.dismiss();
            });
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, POST_NOTIFICATIONS)) {

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

            textView.setText(R.string.postNotification);
            ok.setText(R.string.enable_from_settings);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                ActivityCompat.startActivityForResult(MainActivity.this, intent, 100, null);
                dialog.dismiss();
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            checkPermissionsForNotification();
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