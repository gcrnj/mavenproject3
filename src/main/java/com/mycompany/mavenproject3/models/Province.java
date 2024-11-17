package com.mycompany.mavenproject3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Province {

    public static final String TABLE_NAME = "Provinces";
    public static final String COL_PROVINCE_ID = "ProvinceID";
    public static final String COL_PROVINCE_NAME = "ProvinceName";

    private int provinceID;
    private String provinceName;

    public Province(ResultSet resultSet) throws SQLException {
        this(
                resultSet.getInt(Province.COL_PROVINCE_ID),
                resultSet.getString(Province.COL_PROVINCE_NAME)
        );
    }

    public Province(int provinceID, String provinceName) {
        this.provinceID = provinceID;
        this.provinceName = provinceName;
    }

    // Getters and Setters
    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
