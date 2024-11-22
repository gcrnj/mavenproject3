package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceVehicle {

    public static final String TABLE_NAME = "ServiceVehicle";
    public static final String COL_ID = "ID";
    public static final String COL_SERVICE_ID = "ServiceID";
    public static final String COL_VEHICLE_ID = "VehicleID";

    private int id;
    private int serviceID;
    private int vehicleID;

    // Constructor to initialize ServiceVehicle from ResultSet
    public ServiceVehicle(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt(COL_ID);
        this.serviceID = resultSet.getInt(COL_SERVICE_ID);
        this.vehicleID = resultSet.getInt(COL_VEHICLE_ID);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    // Optional: You can add a toString method for easy printing of the object
    @Override
    public String toString() {
        return "ServiceVehicle{" +
                "id=" + id +
                ", serviceID=" + serviceID +
                ", vehicleID=" + vehicleID +
                '}';
    }
}

