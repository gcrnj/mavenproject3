package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    public static final String TABLE_NAME = "Product";
    public static final String COL_PRODUCT_ID = "ProductID";
    public static final String COL_PRODUCT_NAME = "ProductName";
    public static final String COL_DESCRIPTION = "Description";
    public static final String COL_PRICE = "Price";
    public static final String COL_CATEGORY = "Category";
    public static final String COL_STOCK_QTY = "StockQuantity";

    private int productID;
    private String productName;
    private String description;
    private double price;
    private String category;
    private int stockQuantity;
    
    public Product(ResultSet resultSet) throws SQLException {
        this(
            resultSet.getInt(COL_PRODUCT_ID),
            resultSet.getString(COL_PRODUCT_NAME),
            resultSet.getString(COL_DESCRIPTION),
            resultSet.getDouble(COL_PRICE),
            resultSet.getString(COL_CATEGORY),
            resultSet.getInt(COL_STOCK_QTY)
        );
    }
    
    public Product(int productID, String productName, String description, double price, String category, int stockQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
    
    
}
