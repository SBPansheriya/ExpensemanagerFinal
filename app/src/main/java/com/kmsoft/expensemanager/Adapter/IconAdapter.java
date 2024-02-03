package com.kmsoft.expensemanager.Adapter;

import static com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity.click;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
import com.kmsoft.expensemanager.Activity.FloatingButton.IconActivity;
import com.kmsoft.expensemanager.R;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    IconActivity iconActivity;
    Integer[] iconList;

    public IconAdapter(IconActivity iconActivity, Integer[] iconList) {
        this.iconActivity = iconActivity;
        this.iconList = iconList;
    }

    @NonNull
    @Override
    public IconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconAdapter.ViewHolder holder, int position) {
        holder.icon_img.setImageResource(iconList[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int clickedImageResId = iconList[position];
                String imageResIdAsString = String.valueOf(clickedImageResId);

                click = true;

                ((IconActivity) iconActivity).onBackPressed(clickedImageResId);

            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_img = itemView.findViewById(R.id.icon_img);
        }
    }
}
