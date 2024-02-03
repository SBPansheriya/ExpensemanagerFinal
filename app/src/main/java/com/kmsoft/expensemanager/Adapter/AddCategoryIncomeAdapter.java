package com.kmsoft.expensemanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.expensemanager.Activity.FloatingButton.AddCategoryActivity;
import com.kmsoft.expensemanager.Model.Category;
import com.kmsoft.expensemanager.R;

import java.util.ArrayList;

public class AddCategoryIncomeAdapter extends RecyclerView.Adapter<AddCategoryIncomeAdapter.ViewHolder> {

    AddCategoryActivity addCategoryActivity;
    ArrayList<Category> categoryArrayList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public AddCategoryIncomeAdapter(AddCategoryActivity addCategoryActivity, ArrayList<Category> categoryArrayList) {
        this.addCategoryActivity = addCategoryActivity;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public AddCategoryIncomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddCategoryIncomeAdapter.ViewHolder holder, int position) {

        holder.categoryName.setText(categoryArrayList.get(position).getCategoryName());

        if (categoryArrayList.get(position).getCategoryImage() == 0) {
            holder.addNewCategoryImage.setImageResource(R.drawable.i);
        } else {
            holder.addNewCategoryImage.setImageResource(categoryArrayList.get(position).getCategoryImage());
        }

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddCategoryActivity) addCategoryActivity).showEditNewCategoryBottomDialog(categoryArrayList.get(position),position);
            }
        });
        holder.checkbox.setChecked(position == selectedPosition);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                String categoryName = categoryArrayList.get(position).getCategoryName();
                int categoryImage = categoryArrayList.get(position).getCategoryImage();
                ((AddCategoryActivity) addCategoryActivity).getData(categoryName, categoryImage);
                notifyDataSetChanged();
            }
        });

        if (position == categoryArrayList.size()) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 350);
            holder.relative.setLayoutParams(layoutParams);
        }
    }

//    private void showAddNewCategoryBottomDialog() {
//
//        BottomSheetDialog dialog = new BottomSheetDialog(addCategoryActivity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.add_new_category_bottomsheet_layout);
//        dialog.setCancelable(true);
//        dialog.show();
//
//        EditText addNewCategory = dialog.findViewById(R.id.adding);
//        TextView save = dialog.findViewById(R.id.save);
//        ImageView categoryImage = dialog.findViewById(R.id.category_image);
//
//        if (addCategoryImageResId != 0) {
//            categoryImage.setImageResource(addCategoryImageResId);
//        } else {
//            categoryImage.setImageResource(R.drawable.i);
//        }
//
//        categoryImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AddCategoryActivity.this, IconActivity.class);
//                launchSomeActivity.launch(intent);
//                dialog.dismiss();
//            }
//        });
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(addNewCategory.getText().toString())) {
//                    Toast.makeText(AddCategoryActivity.this, "Please enter a valid category", Toast.LENGTH_SHORT).show();
//                } else {
//                    addCategoryName = addNewCategory.getText().toString();
//                    category = new Category(0, addCategoryName, addCategoryImageResId, tagFind);
//                    categoryArrayList.add(category);
//                    if (tagFind.equals("Income")) {
//                        incomeList.add(category);
//                        addCategoryIncomeAdapter.updateData(incomeList);
//                    } else if (tagFind.equals("Expense")) {
//                        expenseList.add(category);
//                        addCategoryIncomeAdapter.updateData(expenseList);
//                    } else {
//                        Toast.makeText(AddCategoryActivity.this, "No Added Data", Toast.LENGTH_SHORT).show();
//                    }
//                    dbHelper.insertCategoryData(category);
//                    dialog.dismiss();
//                }
//            }
//        });
//    }


    public void updateData(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView addNewCategoryImage;
        CheckBox checkbox;
        RelativeLayout relative, editCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.show_category_name);
            relative = itemView.findViewById(R.id.relative);
            checkbox = itemView.findViewById(R.id.checkbox);
            addNewCategoryImage = itemView.findViewById(R.id.add_new_category_image);
            editCategory = itemView.findViewById(R.id.edit_category);
        }
    }
}
