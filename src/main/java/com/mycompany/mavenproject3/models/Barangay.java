package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Barangay {

    public static final String TABLE_NAME = "Barangays";
    public static final String COL_BRGY_ID = "BrgyID";
    public static final String COL_BRGY_NAME = "BrgyName";

    private int brgyId;
    private String brgyName;
    private Municipality municipality;

    public Barangay(ResultSet resultSet) throws SQLException {
        this(
                resultSet.getInt(Barangay.COL_BRGY_ID),
                resultSet.getString(Barangay.COL_BRGY_NAME),
                new Municipality(resultSet) // Assuming the result set contains Municipality data
        );
    }

    public Barangay(int brgyId, String brgyName, Municipality municipality) {
        this.brgyId = brgyId;
        this.brgyName = brgyName;
        this.municipality = municipality;
    }

    // Getters and Setters
    public int getBrgyId() {
        return brgyId;
    }

    public void setBrgyId(int brgyId) {
        this.brgyId = brgyId;
    }

    public String getBrgyName() {
        return brgyName;
    }

    public void setBrgyName(String brgyName) {
        this.brgyName = brgyName;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }
}
