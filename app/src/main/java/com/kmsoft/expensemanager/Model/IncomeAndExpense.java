package com.kmsoft.expensemanager.Model;

import java.io.Serializable;

public class IncomeAndExpense implements Serializable {
    int id;
    String amount;
    double currantDateTimeStamp;
    double selectedDateTimeStamp;
    String currantDate;
    String date;
    String dayName;
    String time;
    String categoryName;
    int categoryImage;
    int categoryColor;
    String description;
    String addAttachment;
    String tag;

    public IncomeAndExpense() {
    }

    public IncomeAndExpense(int id, String amount, double currantDateTimeStamp, double selectedDateTimeStamp, String currantDate, String date, String dayName, String time, String categoryName, int categoryImage, int categoryColor, String description, String addAttachment, String tag) {
        this.id = id;
        this.amount = amount;
        this.currantDateTimeStamp = currantDateTimeStamp;
        this.selectedDateTimeStamp = selectedDateTimeStamp;
        this.currantDate = currantDate;
        this.date = date;
        this.dayName = dayName;
        this.time = time;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryColor = categoryColor;
        this.description = description;
        this.addAttachment = addAttachment;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public Double getCurrantDateTimeStamp() {
        return currantDateTimeStamp;
    }

    public Double getSelectedDateTimeStamp() {
        return selectedDateTimeStamp;
    }

    public String getCurrantDate() {
        return currantDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayName() {
        return dayName;
    }

    public String getTime() {
        return time;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddAttachment() {
        return addAttachment;
    }

    public String getTag() {
        return tag;
    }

    public double getAmountValue() {
        return Double.parseDouble(amount.replace("₹", ""));
    }
}
