package com.example.pz_db.model;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final StringProperty categoryName = new SimpleStringProperty();
    private final StringProperty supplierName = new SimpleStringProperty();
    private int categoryId;
    private int supplierId;

    public Product() {}

    public Product(int productId, String name, double price, int quantity,
                   String categoryName, String supplierName) {
        setProductId(productId);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setCategoryName(categoryName);
        setSupplierName(supplierName);
    }

    // Property getters
    public IntegerProperty productIdProperty() { return productId; }
    public StringProperty nameProperty() { return name; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty quantityProperty() { return quantity; }
    public StringProperty categoryNameProperty() { return categoryName; }
    public StringProperty supplierNameProperty() { return supplierName; }

    // Regular getters and setters
    public int getProductId() { return productId.get(); }
    public void setProductId(int productId) { this.productId.set(productId); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }

    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    public String getCategoryName() { return categoryName.get(); }
    public void setCategoryName(String categoryName) { this.categoryName.set(categoryName); }

    public String getSupplierName() { return supplierName.get(); }
    public void setSupplierName(String supplierName) { this.supplierName.set(supplierName); }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
}