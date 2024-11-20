/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
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

    private final int serviceAppointmentID;
    private final int serviceID;
    private final Customer customer;
    private final Employee employee;
    private final Service service;
    private final Timestamp appointmentDateTime;
    private final AppointmentStatus status;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    public ServiceAppointment(ResultSet resultSet, boolean originalTableName) throws SQLException {
        this(
                resultSet.getInt(COL_SERVICE_APPOINTMENT_ID),
                resultSet.getInt(COL_SERVICE_ID),
                new Customer(resultSet, originalTableName), // Assuming you have a constructor in Customer that takes an ID
                new Employee(resultSet, originalTableName), // Assuming you have a constructor in Employee that takes an ID
                new Service(resultSet),
                resultSet.getTimestamp(COL_APPOINTMENT_DATETIME), // Use getTimestamp for DateTime values
                AppointmentStatus.valueOf(resultSet.getString(COL_STATUS))
        );
    }

    public ServiceAppointment(int serviceAppointmentID, int serviceID, Customer customer, Employee employee, Service service, Timestamp appointmentDateTime, AppointmentStatus status) {
        this.serviceAppointmentID = serviceAppointmentID;
        this.serviceID = serviceID;
        this.customer = customer;
        this.employee = employee;
        this.service = service;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    // Getters and Setters
    public int getServiceAppointmentID() {
        return serviceAppointmentID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public String getDateTime() {
        return appointmentDateTime.toLocalDateTime().toString();
    }

    public String getDate() {
        return appointmentDateTime.toLocalDateTime().format(dateFormatter);
    }

    public String getTime() {
        return appointmentDateTime.toLocalDateTime().format(timeFormatter);
    }


    public Employee getEmployee() {
        return employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Service getService() {
        return service;
    }
}
