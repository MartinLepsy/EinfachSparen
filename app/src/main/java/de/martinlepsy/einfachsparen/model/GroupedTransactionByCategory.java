package de.martinlepsy.einfachsparen.model;

/**
 * Created by martin on 11.10.17.
 */
public class GroupedTransactionByCategory {

    private String categoryName;

    private double value;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
