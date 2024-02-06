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

    public void setAmountBudget(String amountBudget) {
        this.amountBudget = amountBudget;
    }

    public String getCategoryNameBudget() {
        return categoryNameBudget;
    }

    public void setCategoryNameBudget(String categoryNameBudget) {
        this.categoryNameBudget = categoryNameBudget;
    }

    public int getCategoryImageBudget() {
        return categoryImageBudget;
    }

    public void setCategoryImageBudget(int categoryImageBudget) {
        this.categoryImageBudget = categoryImageBudget;
    }

    public int getPercentageBudget() {
        return percentageBudget;
    }

    public void setPercentageBudget(int percentageBudget) {
        this.percentageBudget = percentageBudget;
    }
}
=======
<<<<<<< HEAD
}
=======
}
>>>>>>> d485a6ca209ec19aec2cd48c442a90780c3cf271
>>>>>>> Stashed changes
