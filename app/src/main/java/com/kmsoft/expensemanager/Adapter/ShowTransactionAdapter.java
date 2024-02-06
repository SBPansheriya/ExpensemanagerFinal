//package com.kmsoft.expensemanager.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.kmsoft.expensemanager.Fragment.TransactionFragment;
//import com.kmsoft.expensemanager.Model.IncomeAndExpense;
//import com.kmsoft.expensemanager.R;
//
//import java.util.ArrayList;
//
//
//public class ShowTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    public static final int SECTION_VIEW = 1;
//    public static final int CONTENT_VIEW = 2;
//
//    ArrayList<IncomeAndExpense> incomeAndExpenseArrayList;
//    TransactionFragment transactionFragment;
//
//    public MyAdapter(ArrayList<IncomeAndExpense> incomeAndExpenseArrayList, TransactionFragment transactionFragment) {
//        this.incomeAndExpenseArrayList = incomeAndExpenseArrayList;
//        this.transactionFragment = transactionFragment;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view;
//        if (viewType == SECTION_VIEW) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_group_title, parent, false);
//            return new HeaderViewHolder(view);
//        } else {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_transaction_layout, parent, false);
//            return new ChildViewHolder(view);
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Context context = incomeAndExpenseArrayList.get();
//        if (viewType == SECTION_VIEW) {
//            return new SectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_group_title, parent, false));
//        }
//        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_item, parent, false), context);
//    }
//
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//
//        if (getItemViewType(position) == SECTION_VIEW){
//            ((HeaderViewHolder) holder).setTitle(mUsersAndSectionList.get(position).title);
//        }
//        else {
//            ((ChildViewHolder) holder).setChild(mUsersAndSectionList.get(position).name,mUsersAndSectionList.get(position).phone);
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mUsersAndSectionList.size();
//    }
//
//    //holder
//    public class ChildViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView TvName, TvPhone;
//        public LinearLayout ll;
//
//        public ChildViewHolder(View itemView) {
//
//            super(itemView);
//            TvName = (TextView) itemView.findViewById(R.id.tv_name);
//            TvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
//            ll = (LinearLayout) itemView.findViewById(R.id.ll_layout);
//        }
//
//        private void setChild(String name, String phone){
//            TvName.setText(name);
//            TvPhone.setText(phone);
//        }
//    }
//
//    public class HeaderViewHolder extends RecyclerView.ViewHolder {
//        TextView title;
//
//        public HeaderViewHolder(View itemView) {
//            super(itemView);
//            title = (TextView) itemView.findViewById(R.id.tv_group_title);
//        }
//
//        private void setTitle(String title){
//            this.title.setText(title);
//        }
//    }
//}