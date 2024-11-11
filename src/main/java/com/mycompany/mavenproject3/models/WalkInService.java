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
public class WalkInService {
    public static final String TABLE_NAME = "WalkInService";
    public static final String COL_WALK_IN_ID = "WalkInID";
    public static final String COL_CUSTOMER_ID = "CustomerID";
    public static final String COL_EMPLOYEE_ID = "EmployeeID";
    public static final String COL_SERVICE_ID = "ServiceID";
    public static final String COL_SERVICE_DATETIME = "ServiceDateTime";
    public static final String COL_TOTAL_CHARGE = "TotalCharge";

    private int walkInID;
    private int customerID;
    private int employeeID;
    private int serviceID;
    private java.util.Date serviceDateTime;
    private double totalCharge;

    public WalkInService(int walkInID, int customerID, int employeeID, int serviceID, Date serviceDateTime, double totalCharge) {
        this.walkInID = walkInID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.serviceID = serviceID;
        this.serviceDateTime = serviceDateTime;
        this.totalCharge = totalCharge;
    }

    // Getters and Setters
    public int getWalkInID() {
        return walkInID;
    }

    public void setWalkInID(int walkInID) {
        this.walkInID = walkInID;
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

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public java.util.Date getServiceDateTime() {
        return serviceDateTime;
    }

    public void setServiceDateTime(java.util.Date serviceDateTime) {
        this.serviceDateTime = serviceDateTime;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
    }
}

