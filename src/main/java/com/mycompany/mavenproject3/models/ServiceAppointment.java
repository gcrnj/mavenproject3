/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author GNew
 */
public class ServiceAppointment {

    public static final String TABLE_NAME = "ServiceAppointment";
    public static final String COL_SERVICE_APPOINTMENT_ID = "ServiceAppointmentID";
    public static final String COL_SERVICE_ID = "ServiceID";

    public static final String COL_CUSTOMER_ID = "CustomerID";
    public static final String COL_EMPLOYEE_ID = "EmployeeID";
    public static final String COL_APPOINTMENT_DATETIME = "AppointmentDateTime";
    public static final String COL_STATUS = "Status";

    private int serviceAppointmentID;
    private int serviceID;
    private Customer customer;
    private Employee employee;
    private Timestamp appointmentDateTime;
    private String status;

    public ServiceAppointment(ResultSet resultSet) throws SQLException {
         this(
            resultSet.getInt(COL_SERVICE_APPOINTMENT_ID),
            resultSet.getInt(COL_SERVICE_ID),
            new Customer(resultSet), // Assuming you have a constructor in Customer that takes an ID
            new Employee(resultSet), // Assuming you have a constructor in Employee that takes an ID
            resultSet.getTimestamp(COL_APPOINTMENT_DATETIME), // Use getTimestamp for DateTime values
            resultSet.getString(COL_STATUS)
        );
    }
    public ServiceAppointment(int serviceAppointmentID, int serviceID, Customer customer, Employee employee, Timestamp appointmentDateTime, String status) {
        this.serviceAppointmentID = serviceAppointmentID;
        this.serviceID = serviceID;
        this.customer = customer;
        this.employee = employee;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    // Getters and Setters
    public int getServiceAppointmentID() {
        return serviceAppointmentID;
    }

    public void setServiceAppointmentID(int serviceAppointmentID) {
        this.serviceAppointmentID = serviceAppointmentID;
    }
    
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setAppointmentDateTime(Timestamp appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getAppointmentDateTime() {
        return appointmentDateTime.toLocalDateTime().toLocalDate().toString();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Customer getCustomer() {
        return customer;
    }
    

}
