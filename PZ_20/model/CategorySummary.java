package com.example.pz_db.model;

import javafx.beans.property.*;

public class CategorySummary {
    private final StringProperty categoryName = new SimpleStringProperty();
    private final IntegerProperty totalQuantity = new SimpleIntegerProperty();

    public CategorySummary() {}

    public CategorySummary(String categoryName, int totalQuantity) {
        setCategoryName(categoryName);
        setTotalQuantity(totalQuantity);
    }

    // Property getters
    public StringProperty categoryNameProperty() { return categoryName; }
    public IntegerProperty totalQuantityProperty() { return totalQuantity; }

    // Regular getters and setters
    public String getCategoryName() { return categoryName.get(); }
    public void setCategoryName(String categoryName) { this.categoryName.set(categoryName); }

    public int getTotalQuantity() { return totalQuantity.get(); }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity.set(totalQuantity); }
}