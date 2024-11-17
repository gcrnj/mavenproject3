package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Municipality {

    public static final String TABLE_NAME = "Municipalities";
    public static final String COL_MUNICIPALITY_ID = "MunicipalityID";
    public static final String COL_MUNICIPALITY_NAME = "MunicipalityName";

    private int municipalityID;
    private String municipalityName;
    private Province province;

    public Municipality(ResultSet resultSet) throws SQLException {
        this(
                resultSet.getInt(Municipality.COL_MUNICIPALITY_ID),
                resultSet.getString(Municipality.COL_MUNICIPALITY_NAME),
                new Province(resultSet) // Assuming the resultSet contains Province data
        );
    }

    public Municipality(int municipalityID, String municipalityName, Province province) {
        this.municipalityID = municipalityID;
        this.municipalityName = municipalityName;
        this.province = province;
    }

    // Getters and Setters
    public int getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(int municipalityID) {
        this.municipalityID = municipalityID;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
}
