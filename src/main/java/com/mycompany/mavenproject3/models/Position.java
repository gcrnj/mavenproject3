/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author GNew
 */
public class Position {

    public static final String TABLE_NAME = "Position";
    public static final String COL_POSITION_ID = "PositionID";
    public static final String COL_POSITION_NAME = "PositionName";
    public static final String COL_DESCRIPTION = "Description";

    private int positionID;
    private String positionName;
    private String description;

    public Position(ResultSet resultSet) throws SQLException {
        this(
                resultSet.getInt(COL_POSITION_ID),
                resultSet.getString(COL_POSITION_NAME),
                resultSet.getString(COL_DESCRIPTION)
        );
    }

    public Position(int positionID, String positionName, String description) {
        this.positionID = positionID;
        this.positionName = positionName;
        this.description = description;
    }

    // Getters and Setters
    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
