/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

/**
 *
 * @author GNew
 */
public class Supplier {
    public static final String TABLE_NAME = "Supplier";
    public static final String COL_SUPPLIER_ID = "SupplierID";
    public static final String COL_SUPPLIER_NAME = "SupplierName";
    public static final String COL_CONTACT_NUMBER = "ContactNumber";
    public static final String COL_EMAIL = "Email";
    public static final String COL_ADDRESS = "Address";

    private int supplierID;
    private String supplierName;
    private String contactNumber;
    private String email;
    private String address;

    public Supplier(int supplierID, String supplierName, String contactNumber, String email, String address) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

