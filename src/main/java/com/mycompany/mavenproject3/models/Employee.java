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
    public static final String COL_LAST_NAME = "LastName";
    public static final String COL_POSITION_ID = "PositionID";
    public static final String COL_CONTACT_NUMBER = "ContactNumber";
    public static final String COL_EMAIL = "Email";

    private int employeeID;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private Position position;

    public Employee(ResultSet resultSet) throws SQLException {
        this(resultSet, true);
    }

    public Employee(ResultSet resultSet, boolean originalColumnName) throws SQLException {
        this(
                resultSet.getInt(Employee.COL_EMPLOYEE_ID),
                originalColumnName ? resultSet.getString(Employee.COL_FIRST_NAME) : resultSet.getString("EmployeeFirstName"),
                originalColumnName ? resultSet.getString(Employee.COL_LAST_NAME)  : resultSet.getString("EmployeeLastName"),
                originalColumnName ? resultSet.getString(Employee.COL_CONTACT_NUMBER) : resultSet.getString("EmployeeContactNumber"),
                originalColumnName ? resultSet.getString(Employee.COL_EMAIL) : resultSet.getString("EmployeeEmail"),
                new Position(resultSet)
        );
    }

    public Employee(int employeeID, String firstName, String lastName, String contactNumber, String email, Position position) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.position = position;
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
        return firstName + " " + lastName;
    }
}
