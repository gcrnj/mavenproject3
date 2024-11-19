package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {

    public static final String TABLE_NAME = "Customer";
    public static final String COL_CUSTOMER_ID = "CustomerID";
    public static final String COL_FIRST_NAME = "FirstName";
    public static final String COL_LAST_NAME = "LastName";
    public static final String COL_CONTACT_NUMBER = "ContactNumber";
    public static final String COL_EMAIL = "Email";
    public static final String COL_BRGY_ID = "BarangayID";
    public static final String COL_HOUSE_NUMBER = "HouseNumber";
    public static final String COL_STREET = "Street";
    public static final String COL_BUILDING = "Building";

    private int customerID;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private int barangayID;
    private String houseNumber;
    private String street;
    private String building;

    public Customer(ResultSet resultSet) throws SQLException {
        this(resultSet, true);
    }

    public Customer(ResultSet resultSet, boolean originalColumnName) throws SQLException {
        this(
                resultSet.getInt(Customer.COL_CUSTOMER_ID),
                originalColumnName ? resultSet.getString(Customer.COL_FIRST_NAME) : resultSet.getString("CustomerFirstName"),
                originalColumnName ? resultSet.getString(Customer.COL_LAST_NAME) : resultSet.getString("CustomerLastName"),
                originalColumnName ? resultSet.getString(Customer.COL_CONTACT_NUMBER) : resultSet.getString("CustomerContactNumber"),
                originalColumnName ? resultSet.getString(Customer.COL_EMAIL) : resultSet.getString("CustomerEmail"),
                resultSet.getInt(Customer.COL_BRGY_ID),
                resultSet.getString(Customer.COL_HOUSE_NUMBER),
                resultSet.getString(Customer.COL_STREET),
                resultSet.getString(Customer.COL_BUILDING)
        );
    }

    public Customer(int customerID, String firstName, String lastName, String contactNumber, String email,
                    int barangayID, String houseNumber, String street, String building) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.barangayID = barangayID;
        this.houseNumber = houseNumber;
        this.street = street;
        this.building = building;
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

    public int getBarangayID() {
        return barangayID;
    }

    public void setBarangayID(int barangayID) {
        this.barangayID = barangayID;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
