package com.mycompany.mavenproject3.models;

public class LocalCache {

    public static Customer customer;
    private static Employee employee;

    public static void setCustomer(Customer customer) {
        LocalCache.customer = customer;
        LocalCache.employee = null;
    }

    public static Customer getCustomer() {
        return customer;
    }
    

    public static void setEmployee(Employee employee) {
        LocalCache.employee = employee;
        LocalCache.customer = null;
    }

    public static Employee getEmployee() {
        return employee;
    }

    public static boolean isCustomer() {
        return customer != null;
    }
}
