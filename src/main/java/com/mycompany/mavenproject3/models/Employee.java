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
public class Employee {

    public static final String TABLE_NAME = "Employee";
    public static final String COL_EMPLOYEE_ID = "EmployeeID";
    public static final String COL_FIRST_NAME = "FirstName";
    public static final String COL_MIDDLE_NAME = "MiddleName";
    public static final String COL_LAST_NAME = "LastName";
    public static final String COL_POSITION_ID = "PositionID";
    public static final String COL_CONTACT_NUMBER = "ContactNumber";
    public static final String COL_EMAIL = "Email";
    public static final String COL_HOUSE = "HouseNumber";
    public static final String COL_STREET = "Street";
    public static final String COL_BLDG = "Building";
    public static final String COL_USERNAME = "Username";
    public static final String COL_BRGY_ID = "BrgyID";
    public static final String COL_IS_DELETED = "IsEmployeeDeleted";

    private int employeeID;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactNumber;
    private String email;
    private Position position;

    private String houseNumber;
    private String street;
    private String building;
    private String username;

    private Barangay barangay;

    public Employee(ResultSet resultSet) throws SQLException {
        this(resultSet, true);
    }

    public Employee(ResultSet resultSet, boolean originalColumnName) throws SQLException {
        this(
                resultSet.getInt(Employee.COL_EMPLOYEE_ID),
                originalColumnName ? resultSet.getString(Employee.COL_FIRST_NAME) : resultSet.getString("EmployeeFirstName"),
                resultSet.getString(Employee.COL_MIDDLE_NAME),
                originalColumnName ? resultSet.getString(Employee.COL_LAST_NAME)  : resultSet.getString("EmployeeLastName"),
                originalColumnName ? resultSet.getString(Employee.COL_CONTACT_NUMBER) : resultSet.getString("EmployeeContactNumber"),
                originalColumnName ? resultSet.getString(Employee.COL_EMAIL) : resultSet.getString("EmployeeEmail"),
                new Position(resultSet),
                resultSet.getString(Employee.COL_HOUSE),
                resultSet.getString(Employee.COL_STREET),
                resultSet.getString(Employee.COL_BLDG),
                resultSet.getString(Employee.COL_USERNAME)
        );
    }

    public Employee(int employeeID, String firstName, String middleName, String lastName, String contactNumber, String email, Position position,
                    String houseNumber, String street, String building, String username) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.position = position;
        this.houseNumber = houseNumber;
        this.street = street;
        this.building = building;
        this.username = username;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
    
    public String getFullName() {
        if(middleName != null && !middleName.isBlank()) {
            return firstName + " " + middleName + " " + lastName;
        } else {
            return firstName + " " + lastName;
        }
    }

    public String getMiddleName() {
        return middleName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Barangay getBarangay() {
        return barangay;
    }

    public void setBarangay(Barangay barangay) {
        this.barangay = barangay;
    }

    public Municipality getMunicipality() {
        return barangay.getMunicipality();
    }

    public Province getProvince() {
        return getMunicipality().getProvince();
    }
}
