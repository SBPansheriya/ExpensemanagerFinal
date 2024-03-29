package com.kmsoft.expensemanager.Fragment;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.kmsoft.expensemanager.Activity.SplashActivity.PREFS_NAME;
import static com.kmsoft.expensemanager.Activity.SplashActivity.USER_IMAGE;
import static com.kmsoft.expensemanager.Activity.SplashActivity.USER_NAME;
import static com.kmsoft.expensemanager.Constant.incomeAndExpenseArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmsoft.expensemanager.Activity.Profile.EditProfileActivity;
import com.kmsoft.expensemanager.Activity.Profile.ExportDataActivity;
import com.kmsoft.expensemanager.Activity.Profile.SettingsActivity;
import com.kmsoft.expensemanager.DBHelper;
import com.kmsoft.expensemanager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    DBHelper dbHelper;
    ActivityResultLauncher<Intent> launchPickActivity;
    ActivityResultLauncher<Intent> launchPickActivity1;
    ImageView editUserDetails, profileImage;
    RelativeLayout exportData, settings, backup, restore;
    TextView username;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ActivityResultLauncher<Intent> launchSomeActivity;
    String userName;
    String image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        dbHelper = new DBHelper(ProfileFragment.this.getActivity());
//        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();

//        userName = sharedPreferences.getString(USER_NAME, "");
//        image = sharedPreferences.getString(USER_IMAGE, "");
        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    userName = data.getStringExtra("userName");
                    image = data.getStringExtra("userImage");
                }
            }
        });

        editUserDetails.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("name",userName);
            intent.putExtra("image",image);
            launchSomeActivity.launch(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        exportData.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ExportDataActivity.class);
            startActivity(intent);
        });

        backup.setOnClickListener(v -> checkPermissionsForBackup());

        restore.setOnClickListener(v -> checkPermissionsForRestore());

        launchPickActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri directoryUri = data.getData();

                if (directoryUri != null) {
                    dbHelper.backupDatabase(ProfileFragment.this.getActivity(),directoryUri);
                }
            }
        });

        launchPickActivity1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri directoryUri = data.getData();

                if (directoryUri != null) {
                    dbHelper.restoreDatabase(ProfileFragment.this.getActivity(), directoryUri);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Display();
    }

    private void checkPermissionsForBackup() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            launchPickActivity.launch(intent);
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestBackupPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                launchPickActivity.launch(intent);
            }
        }
    }

    private void checkPermissionsForRestore() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            launchPickActivity1.launch(Intent.createChooser(intent, "Select file to import"));
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestRestorePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launchPickActivity1.launch(Intent.createChooser(intent, "Select file to import"));
            }
        }
    }

    private final ActivityResultLauncher<String> requestBackupPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    launchPickActivity.launch(intent);
                } else {
                    showPermissionDenyDialog1(getActivity());
                }
            });

    private final ActivityResultLauncher<String> requestRestorePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    launchPickActivity1.launch(Intent.createChooser(intent, "Select file to import"));
                } else {
                    showPermissionDenyDialog(getActivity());
                }
            });

    private void showPermissionDenyDialog(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE)) {
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

            filename.setText(R.string.write_permission);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                checkPermissionsForBackup();
                dialog.dismiss();
            });
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE)) {

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

            textView.setText(R.string.write_permission_setting);
            ok.setText(R.string.enable_from_settings);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                ActivityCompat.startActivityForResult(getActivity(), intent, 100, null);
                dialog.dismiss();
            });
        }
    }

    private void showPermissionDenyDialog1(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE)) {
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

            filename.setText(R.string.write_permission);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                checkPermissionsForBackup();
                dialog.dismiss();
            });
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE)) {

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

            textView.setText(R.string.write_permission_setting);
            ok.setText(R.string.enable_from_settings);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                ActivityCompat.startActivityForResult(getActivity(), intent, 101, null);
                dialog.dismiss();
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            checkPermissionsForBackup();
        } else if (requestCode == 101) {
            checkPermissionsForRestore();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData(String name,String image1) {
        if (TextUtils.isEmpty(name)) {
            username.setText("Your name");
            username.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
        } else {
            username.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            username.setText(name);
        }

        if (TextUtils.isEmpty(image1)) {
            profileImage.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(image1).into(profileImage);
        }
    }

    private void Display(){
        try {
            Cursor cursor = dbHelper.getAllProfileData();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    userName = cursor.getString(1);
                    image = cursor.getString(2);

                } while (cursor.moveToNext());
                setData(userName,image);
            }
        } catch (Exception e){

           }
    }


    private void init(View view) {
        editUserDetails = view.findViewById(R.id.edit_user_details);
        exportData = view.findViewById(R.id.export_data);
        settings = view.findViewById(R.id.settings);
        username = view.findViewById(R.id.username);
        profileImage = view.findViewById(R.id.profile_image);
        backup = view.findViewById(R.id.backup_data);
        restore = view.findViewById(R.id.restore_data);
    }
}