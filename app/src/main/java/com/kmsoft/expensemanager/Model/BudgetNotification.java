package com.kmsoft.expensemanager.Model;

public class BudgetNotification {
    private int id;
    private String amount;
    private String name;
    private int image;
    private String currentTime;
    private String isRemove;
    private String tag;

    public BudgetNotification(int id, String amount, String name, int image, String currentTime, String isRemove,String tag) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.image = image;
        this.currentTime = currentTime;
        this.isRemove = isRemove;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(String isRemove) {
        this.isRemove = isRemove;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

