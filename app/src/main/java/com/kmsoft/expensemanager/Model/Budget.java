package com.kmsoft.expensemanager.Model;

import java.io.Serializable;

public class Budget implements Serializable {
    int id;
    String amountBudget;
    String categoryNameBudget;
    int categoryImageBudget;
    int percentageBudget;

    public Budget(int id, String amountBudget, String categoryNameBudget, int categoryImageBudget, int percentageBudget) {
        this.id = id;
        this.amountBudget = amountBudget;
        this.categoryNameBudget = categoryNameBudget;
        this.categoryImageBudget = categoryImageBudget;
        this.percentageBudget = percentageBudget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmountBudget() {
        return amountBudget;
    }

    public String getCategoryNameBudget() {
        return categoryNameBudget;
    }

    public int getCategoryImageBudget() {
        return categoryImageBudget;
    }

    public int getPercentageBudget() {
        return percentageBudget;
    }

}
