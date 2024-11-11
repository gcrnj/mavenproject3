/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author GNew
 */
public class Customer {

    public static final String TABLE_NAME = "Customer";
    public static final String COL_CUSTOMER_ID = "CustomerID";
    public static final String COL_FIRST_NAME = "FirstName";
    public static final String COL_LAST_NAME = "LastName";
    public static final String COL_CONTACT_NUMBER = "ContactNumber";
    public static final String COL_EMAIL = "Email";
    public static final String COL_ADDRESS = "Address";

    private int customerID;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private String address;

    public Customer(ResultSet resultSet) throws SQLException {
        this(
                resultSet.getInt(Customer.COL_CUSTOMER_ID),
                resultSet.getString(Customer.COL_FIRST_NAME),
                resultSet.getString(Customer.COL_LAST_NAME),
                resultSet.getString(Customer.COL_CONTACT_NUMBER),
                resultSet.getString(Customer.COL_EMAIL),
                resultSet.getString(Customer.COL_ADDRESS)
        );
    }

    public Customer(int customerID, String firstName, String lastName, String contactNumber, String email, String address) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
