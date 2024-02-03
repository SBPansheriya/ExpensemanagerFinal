//package com.kmsoft.expensemanager.Adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
//import com.kmsoft.expensemanager.Model.Category;
//import com.kmsoft.expensemanager.R;
//
//import java.util.ArrayList;
//
//public class AddCategoryExpenseAdapter extends RecyclerView.Adapter<AddCategoryExpenseAdapter.ViewHolder> {
//
//    AddCategoryActivity addCategoryActivity;
//
//    public AddCategoryExpenseAdapter(AddCategoryActivity addCategoryActivity, ArrayList<Category> categoryArrayList) {
//        this.addCategoryActivity = addCategoryActivity;
//    }
//
//    @NonNull
//    @Override
//    public AddCategoryExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_category_layout,parent,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AddCategoryExpenseAdapter.ViewHolder holder, int position) {
//        holder.categoryName.setText("Birthday");
//
//        if (position == 9) {
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0, 20, 0, 350);
//            holder.relative.setLayoutParams(layoutParams);
//        }
//    }
//
//    public void updateData() {
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return 10;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView categoryName;
//        RelativeLayout relative;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            categoryName = itemView.findViewById(R.id.show_category_name);
//            relative = itemView.findViewById(R.id.relative);
//        }
//    }
//}
