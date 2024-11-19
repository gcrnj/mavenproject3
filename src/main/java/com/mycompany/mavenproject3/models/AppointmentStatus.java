package com.mycompany.mavenproject3.models;

public enum AppointmentStatus {
    SCHEDULED,    // Appointment is scheduled but not yet completed
    COMPLETED,    // Appointment has been successfully completed
    CANCELED,     // Appointment has been canceled
    RESCHEDULED;  // Appointment has been rescheduled
}