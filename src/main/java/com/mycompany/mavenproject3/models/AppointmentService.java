package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentService {

    public static final String TABLE_NAME = "AppointmentServices";
    public static final String COL_APPOINTMENT_SERVICE_ID = "AppointmentServiceId";
    public static final String COL_APPOINTMENT_ID = "AppointmentId";
    public static final String COL_SERVICE_ID = "ServiceId";

    private int appointmentServiceId;
    private int appointmentId;
    private int serviceId;

    // Constructor to initialize AppointmentService from ResultSet
    public AppointmentService(ResultSet resultSet) throws SQLException {
        this.appointmentServiceId = resultSet.getInt(COL_APPOINTMENT_SERVICE_ID);
        this.appointmentId = resultSet.getInt(COL_APPOINTMENT_ID);
        this.serviceId = resultSet.getInt(COL_SERVICE_ID);
    }

    // Getters and Setters
    public int getAppointmentServiceId() {
        return appointmentServiceId;
    }

    public void setAppointmentServiceId(int appointmentServiceId) {
        this.appointmentServiceId = appointmentServiceId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}

