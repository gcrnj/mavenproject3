/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author GNew
 */
public class Appointment {

    public static final String TABLE_NAME = "Appointments";
    public static final String COL_SERVICE_APPOINTMENT_ID = "AppointmentID";

    public static final String COL_CUSTOMER_ID = "CustomerID";
    public static final String COL_EMPLOYEE_ID = "EmployeeID";
    public static final String COL_APPOINTMENT_DATETIME = "AppointmentDateTime";
    public static final String COL_STATUS = "Status";

    private final int serviceAppointmentID;
    private final Customer customer;
    private final Employee employee;
    private final Timestamp appointmentDateTime;
    private final AppointmentStatus status;
    private List<Service> services;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    public Appointment(ResultSet resultSet, boolean originalTableName) throws SQLException {
        this(
                resultSet.getInt(COL_SERVICE_APPOINTMENT_ID),
                new Customer(resultSet, originalTableName), // Assuming you have a constructor in Customer that takes an ID
                new Employee(resultSet, originalTableName), // Assuming you have a constructor in Employee that takes an ID
                resultSet.getTimestamp(COL_APPOINTMENT_DATETIME), // Use getTimestamp for DateTime values
                AppointmentStatus.valueOf(resultSet.getString(COL_STATUS))
        );
    }

    public Appointment(int serviceAppointmentID, Customer customer, Employee employee, Timestamp appointmentDateTime, AppointmentStatus status) {
        this.serviceAppointmentID = serviceAppointmentID;
        this.customer = customer;
        this.employee = employee;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    // Getters and Setters
    public int getAppointmentID() {
        return serviceAppointmentID;
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

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Service> getServices() {
        return services;
    }
}
