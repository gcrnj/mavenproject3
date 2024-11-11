/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

import java.util.Date;

/**
 *
 * @author GNew
 */
public class ProductSales {
    public static final String TABLE_NAME = "ProductSales";
    public static final String COL_PRODUCT_SALE_ID = "ProductSaleID";
    public static final String COL_PRODUCT_ID = "ProductID";
    public static final String COL_CUSTOMER_ID = "CustomerID";
    public static final String COL_EMPLOYEE_ID = "EmployeeID";
    public static final String COL_SALE_DATE = "SaleDate";
    public static final String COL_QUANTITY = "Quantity";
    public static final String COL_TOTAL_PRICE = "TotalPrice";

    private int productSaleID;
    private int productID;
    private int customerID;
    private int employeeID;
    private java.util.Date saleDate;
    private int quantity;
    private double totalPrice;

    public ProductSales(int productSaleID, int productID, int customerID, int employeeID, Date saleDate, int quantity, double totalPrice) {
        this.productSaleID = productSaleID;
        this.productID = productID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.saleDate = saleDate;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getProductSaleID() {
        return productSaleID;
    }

    public void setProductSaleID(int productSaleID) {
        this.productSaleID = productSaleID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public java.util.Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(java.util.Date saleDate) {
        this.saleDate = saleDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

