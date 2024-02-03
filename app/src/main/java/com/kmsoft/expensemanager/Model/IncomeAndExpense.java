package com.kmsoft.expensemanager.Model;

import java.io.Serializable;

public class IncomeAndExpense implements Serializable {
    int id;
    String amount;
    String date;
    String categoryName;
    int categoryImage;
    String description;
    String addAttachment;
    String tag;

    public IncomeAndExpense() {
    }

    public IncomeAndExpense(int id, String amount, String date, String categoryName, int categoryImage, String description, String addAttachment, String tag) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
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

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddAttachment() {
        return addAttachment;
    }

    public void setAddAttachment(String addAttachment) {
        this.addAttachment = addAttachment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
