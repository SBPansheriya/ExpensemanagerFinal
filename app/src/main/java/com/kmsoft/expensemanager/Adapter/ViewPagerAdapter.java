//package com.kmsoft.expensemanager.Adapter;
//
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.viewpager.widget.PagerAdapter;
//
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
//import com.kmsoft.expensemanager.Fragment.HomeFragment;
//import com.kmsoft.expensemanager.R;
//
//import java.util.ArrayList;
//
//public class ViewPagerAdapter extends PagerAdapter {
//
//    HomeFragment homeFragment;
//    String[] showChart;
//
//    public ViewPagerAdapter(HomeFragment homeFragment, String[] string) {
//        this.homeFragment = homeFragment;
//        this.showChart = string;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup view, int position) {
//        View layout = LayoutInflater.from(homeFragment.getContext()).inflate(R.layout.item_viewpager_layout, view, false);
//        view.addView(layout);
//
//        LineChart chart = layout.findViewById(R.id.line_chart);
//        setupLineChart(chart);
//        return layout;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
//        collection.removeView((View) view);
//    }
//
//    @Override
//    public int getCount() {
//        return showChart.length;
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == object;
//    }
//
//    private void setupLineChart(LineChart chart) {
//        chart.setBackgroundColor(Color.WHITE);
//        chart.getDescription().setEnabled(false);
//        chart.setTouchEnabled(true);
//
//        ArrayList<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(0, 50));
//        entries.add(new Entry(1, 50));
//        entries.add(new Entry(2, 80));
//        entries.add(new Entry(3, 60));
//        entries.add(new Entry(4, 40));
//        entries.add(new Entry(5, 70));
//        entries.add(new Entry(6, 100));
//
//        chart.invalidate();
//
//        chart.getAxisLeft().setEnabled(false);
//        chart.getAxisRight().setEnabled(false);
//        final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
//
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        LineDataSet dataSet = new LineDataSet(entries, "");
//        dataSet.setCircleRadius(5f);
//        dataSet.setDrawFilled(true);
//        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        dataSet.setCircleColor(Color.RED);
//        dataSet.setFillColor(Color.RED);
//        dataSet.setFillAlpha(30);
//        dataSet.setColor(Color.RED);
//        dataSet.setValueTextColor(Color.RED);
//
//        // Create LineData and set the dataset
//        LineData lineData = new LineData(dataSet);
//        // Set LineData to the chart
//        chart.setData(lineData);
//    }
//}
