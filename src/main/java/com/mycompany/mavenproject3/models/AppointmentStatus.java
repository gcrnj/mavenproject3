package com.mycompany.mavenproject3.models;

public enum AppointmentStatus {
    SCHEDULED("Scheduled"),    // Appointment is scheduled but not yet completed
    COMPLETED("Completed"),    // Appointment has been successfully completed
    CANCELED("Canceled");     // Appointment has been canceled
    // Declare a private field to hold the camelCaseName
    private final String camelCaseName;

    // Constructor for the enum
    AppointmentStatus(String camelCaseName) {
        this.camelCaseName = camelCaseName;
    }

    // Getter method to retrieve the camelCaseName
    public String getCamelCaseName() {
        return camelCaseName;
    }
}