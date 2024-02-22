package com.kmsoft.expensemanager.Adapter;

import static com.kmsoft.expensemanager.Activity.SplashActivity.currencySymbol;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Activity.Profile.NotificationActivity;
import com.kmsoft.expensemanager.Model.BudgetNotification;
import com.kmsoft.expensemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExceedBudgetAdapter extends RecyclerView.Adapter<ExceedBudgetAdapter.ViewHolder> {

    NotificationActivity notificationActivity;
    ArrayList<BudgetNotification> notificationsList;

    public ExceedBudgetAdapter(NotificationActivity notificationActivity, ArrayList<BudgetNotification> notificationsList) {
        this.notificationActivity = notificationActivity;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public ExceedBudgetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExceedBudgetAdapter.ViewHolder holder, int position) {
        BudgetNotification budgetNotification = notificationsList.get(position);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currantDate = dateFormat.format(calendar.getTime());
        String dateOnly = null;
        String timeOnly = null;
        String currentTime = budgetNotification.getCurrentTime();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        try {
            Date parsedDate = dateFormat1.parse(currentTime);
            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            dateOnly = dateOnlyFormat.format(parsedDate);
            timeOnly = timeOnlyFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (budgetNotification.getTag().equals("Exceed")) {
            holder.itemName.setText(budgetNotification.getName());
            holder.itemAmount.setText(String.format("%s%s", currencySymbol, budgetNotification.getAmount()));
            if (budgetNotification.getImage() == 0) {
                holder.itemImage.setImageResource(R.drawable.i);
            } else {
                holder.itemImage.setImageResource(budgetNotification.getImage());
            }
            if (TextUtils.equals(currantDate, dateOnly)) {
                holder.itemDate.setText(timeOnly);
            } else {
                holder.itemDate.setText(budgetNotification.getCurrentTime());
            }
            holder.itemDescription.setText(R.string.you_ve_exceed_the_limit);
            holder.itemDescription.setTextColor(ContextCompat.getColor(notificationActivity, R.color.red));
            holder.relativeLayout1.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.VISIBLE);
        } else if (budgetNotification.getTag().equals("Reminder")) {
            holder.relativeLayout1.setVisibility(View.VISIBLE);
            holder.relativeLayout.setVisibility(View.GONE);
            if (TextUtils.equals(currantDate, dateOnly)) {
                holder.time.setText(timeOnly);
            } else {
                holder.time.setText(budgetNotification.getCurrentTime());
            }
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        RelativeLayout relativeLayout, relativeLayout1;
        TextView itemName, itemDescription, itemAmount, itemDate, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemAmount = itemView.findViewById(R.id.item_amount);
            itemDate = itemView.findViewById(R.id.item_date);
            relativeLayout = itemView.findViewById(R.id.relative1);
            relativeLayout1 = itemView.findViewById(R.id.relative2);
            time = itemView.findViewById(R.id.time);
        }
    }
}
