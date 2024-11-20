package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Service {

    public static final String TABLE_NAME = "Service";
    public static final String COL_SERVICE_ID = "ServiceID";
    public static final String COL_SERVICE_NAME = "ServiceName";
    public static final String COL_DESCRIPTION = "Description";
    public static final String COL_PRICE = "Price";
    public static final String COL_WHEELS = "Wheels";
    public static final String COL_IS_AVAILABLE = "IsAvailable";

    private int serviceID;
    private String serviceName;
    private String description;
    private double price;
    private int wheels;
    private boolean isAvailable;

    // Constructor to initialize Service from ResultSet
    public Service(ResultSet resultSet) throws SQLException {
        this.serviceID = resultSet.getInt(COL_SERVICE_ID);
        this.serviceName = resultSet.getString(COL_SERVICE_NAME);
        this.description = resultSet.getString(COL_DESCRIPTION);
        this.price = resultSet.getDouble(COL_PRICE);
        this.wheels = resultSet.getInt(COL_WHEELS);
        this.isAvailable = resultSet.getBoolean(COL_IS_AVAILABLE);
    }

    // Getters and Setters
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWheels() {
        return wheels;
    }

    public void setWheels(int wheels) {
        this.wheels = wheels;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    
    
}
