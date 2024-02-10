package com.kmsoft.expensemanager.Fragment;

import static com.kmsoft.expensemanager.Activity.SplashActivity.PREFS_NAME;
import static com.kmsoft.expensemanager.Activity.SplashActivity.USER_IMAGE;
import static com.kmsoft.expensemanager.Activity.SplashActivity.USER_NAME;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmsoft.expensemanager.Activity.Profile.EditProfileActivity;
import com.kmsoft.expensemanager.Activity.Profile.ExportDataActivity;
import com.kmsoft.expensemanager.Activity.Profile.SettingsActivity;
import com.kmsoft.expensemanager.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView editUserDetails,exportData,settings,logout, profileImage;
    TextView username;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ActivityResultLauncher<Intent> launchSomeActivity;
    String userName;
    String image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        init(view);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userName = sharedPreferences.getString(USER_NAME,"");
        image = sharedPreferences.getString(USER_IMAGE,"");

        setData();

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    userName = data.getStringExtra("userName");
                    image = data.getStringExtra("userImage");
                }
                setData();
            }
        });

        editUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                launchSomeActivity.launch(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        exportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExportDataActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutBottomDialog();
            }
        });

        return view;
    }

    private void setData(){
        if (TextUtils.isEmpty(username.getText())){
            username.setText("Your name");
        } else {
            username.setText(userName);
        }

        if (TextUtils.isEmpty(image)){
            profileImage.setImageResource(R.drawable.profile);
        } else {
            Picasso.get().load(image).into(profileImage);
        }
    }

    private void showLogoutBottomDialog(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.logout_bottomsheet_layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        TextView no = bottomSheetDialog.findViewById(R.id.no);
        TextView yes = bottomSheetDialog.findViewById(R.id.yes);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                Dialog dialog = new Dialog(getContext());
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(true);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dailog_removed_layout);
                dialog.setCancelable(true);
                dialog.show();

                TextView txt = dialog.findViewById(R.id.txt);
                txt.setText("Budget has been successfully removed");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }, 2000);
            }
        });
    }


    private void init(View view){
        editUserDetails = view.findViewById(R.id.edit_user_details);
        exportData = view.findViewById(R.id.export_data);
        settings = view.findViewById(R.id.settings);
        logout = view.findViewById(R.id.logout);
        username = view.findViewById(R.id.username);
        profileImage = view.findViewById(R.id.profile_image);
    }
}