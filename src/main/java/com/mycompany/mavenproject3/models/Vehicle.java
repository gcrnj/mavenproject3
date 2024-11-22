package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * @author GNew
 */
public class Vehicle {

    public static final String TABLE_NAME = "Vehicles";
    public static final String COL_VEHICLE_ID = "VehicleId";
    public static final String COL_VEHICLE_NAME = "VehicleName";
    public static final String COL_VEHICLE_DELETED = "IsDeleted";
    public static final String COL_CREATED_BY = "CreatedBy";
    public static final String COL_CREATED_AT = "CreatedAt";

    private final int vehicleId;
    private final String vehicleName;
    private final Employee createdBy; // Assuming Employee class exists
    private final Timestamp createdAt;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    // Constructor for populating the Vehicle object from a ResultSet
    public Vehicle(ResultSet resultSet, boolean originalTableName) throws SQLException {
        this(
                resultSet.getInt(COL_VEHICLE_ID),
                resultSet.getString(COL_VEHICLE_NAME),
                new Employee(resultSet, originalTableName), // Assuming Employee constructor takes ResultSet
                resultSet.getTimestamp(COL_CREATED_AT) // Use getTimestamp for DateTime
        );
    }

    // Full constructor
    public Vehicle(int vehicleId, String vehicleName, Employee createdBy, Timestamp createdAt) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Getters
    public int getVehicleId() {
        return vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getFormattedCreatedAt() {
        return createdAt.toLocalDateTime().format(dateFormatter);
    }

    // Additional methods if needed
    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vehicleName='" + vehicleName + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + getFormattedCreatedAt() +
                '}';
    }
}