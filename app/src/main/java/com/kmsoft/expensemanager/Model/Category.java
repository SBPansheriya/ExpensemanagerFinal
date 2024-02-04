package com.kmsoft.expensemanager.Model;

import java.io.Serializable;

public class Category implements Serializable {
    int id;
    String categoryName;
    int categoryImage;
    String categoryTag;

    public Category(int id,String categoryName, int categoryImage,String categoryTag) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryTag = categoryTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }
}
