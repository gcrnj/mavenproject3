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

    public static boolean isManager() {
        return employee != null && employee.getPosition().getPositionName().equals("Manager");
    }

    public static boolean isEmployee() {
        return employee != null && employee.getPosition().getPositionName().equals("Employee");
    }

    public static boolean isOwner() {
        return employee != null && employee.getPosition().getPositionName().equals("Owner");
    }

    public static void logout() {
        customer = null;
        employee = null;
    }
}
