/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

/**
 *
 * @author GNew
 */
public class Inventory {
    public static final String TABLE_NAME = "Inventory";
    public static final String COL_INVENTORY_ID = "InventoryID";
    public static final String COL_PRODUCT_ID = "ProductID";
    public static final String COL_SUPPLIER_ID = "SupplierID";
    public static final String COL_QUANTITY = "Quantity";
    public static final String COL_REORDER_LEVEL = "ReorderLevel";
    public static final String COL_LOCATION = "Location";

    private int inventoryID;
    private int productID;
    private int supplierID;
    private int quantity;
    private int reorderLevel;
    private String location;

    public Inventory(int inventoryID, int productID, int supplierID, int quantity, int reorderLevel, String location) {
        this.inventoryID = inventoryID;
        this.productID = productID;
        this.supplierID = supplierID;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.location = location;
    }

    // Getters and Setters
    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
}

