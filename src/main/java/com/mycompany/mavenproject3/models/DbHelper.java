package com.mycompany.mavenproject3.models;

import com.mycompany.mavenproject3.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static Connection connection;

    public static void connect() throws SQLException {
        String un = "CLAWS\\GNew";
        String connectionUrl = "jdbc:sqlserver://CLAWS:1433;"
                + "Database=superspeed_db;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "integratedSecurity=true;";
        // Make connection
        connection = DriverManager.getConnection(connectionUrl);
    }

    public static boolean login(String username, String password, String role) {
        boolean isValidUser = false;
        String sql;

        // Define the SQL query based on the role
        switch (role.toLowerCase()) {
            case "teller":
            case "manager":
            case "owner":
                sql = "SELECT e.*, p.* FROM " + Employee.TABLE_NAME + " e "
                        + "JOIN " + Position.TABLE_NAME + " p ON e.positionId = p.PositionID "
                        + "WHERE e.username = ? "
                        + "AND p.positionName = ?;";
                break;

            case "customer":
                sql = "SELECT * FROM " + Customer.TABLE_NAME
                        + " WHERE username = ? "
                        + "AND password = ?;";
                break;

            default:
                return false; // Invalid role
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            if (role.equalsIgnoreCase("teller") || role.equalsIgnoreCase("manager") || role.equalsIgnoreCase("owner")) {
                preparedStatement.setString(2, role); // Set the position for teller/manager
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Check which role we are dealing with
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    boolean isPasswordCorrect = PasswordUtils.verifyPassword(password, hashedPassword);
                    if (isPasswordCorrect) {
                        switch (role.toLowerCase()) {
                            case "teller":
                            case "manager":
                            case "owner":
                                Employee employee = new Employee(resultSet);
                                LocalCache.setEmployee(employee);
                                break;
                            case "customer":
                                Customer customer = new Customer(resultSet);
                                LocalCache.setCustomer(customer);
                                break;
                        }
                        isValidUser = true; // User exists with matching credentials
                    }
                }
            }
        } catch (SQLException e) {
            // Handle exceptions properly in production
            e.printStackTrace(); // For debugging
        }

        return isValidUser;
    }

    public static List<ServiceAppointment> getServiceAppointments() {
        List<ServiceAppointment> appointments = new ArrayList<>();
        String sql = "SELECT sa.*, e.*, c.* "
                + "FROM " + ServiceAppointment.TABLE_NAME + " sa "
                + "JOIN " + Employee.TABLE_NAME + " e ON sa.EmployeeID = e.EmployeeID "
                + "JOIN " + Customer.TABLE_NAME + " c ON sa.CustomerID = c.CustomerID";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Appointment objects
            while (resultSet.next()) {
                ServiceAppointment appointment = new ServiceAppointment(resultSet);
                appointments.add(appointment); // Add the Appointment object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }
        return appointments; // Return the list of appointments
    }

    public static List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * from " + Customer.TABLE_NAME;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Appointment objects
            while (resultSet.next()) {
                customers.add(new Customer(resultSet)); // Add the Appointment object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }
        return customers; // Return the list of appointments
    }

    public static List<Service> getServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM " + Service.TABLE_NAME; // SQL query to select all services

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Service objects
            while (resultSet.next()) {
                Service service = new Service(resultSet);
                services.add(service); // Add the Service object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }

        return services; // Return the list of services
    }

    public static List<Service> getServicesByName(String serviceName) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM " + Service.TABLE_NAME +
                " WHERE " + Service.COL_SERVICE_NAME + " LIKE ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + serviceName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            // Loop through the result set and create Service objects
            while (resultSet.next()) {
                Service service = new Service(resultSet);
                services.add(service); // Add the Service object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }

        return services; // Return the list of services
    }

    // Method to retrieve all products
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + Product.TABLE_NAME; // SQL query to select all products

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Product objects
            while (resultSet.next()) {
                Product product = new Product(resultSet);
                products.add(product); // Add the Product object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }

        return products; // Return the list of products
    }

    public static String createAppointment(String customerName, String contactNumber, int serviceId, String appointmentDate) {
        String sql = "INSERT INTO " + ServiceAppointment.TABLE_NAME + " (CustomerName, contactNumber, serviceId, appointmentDate) VALUES (?, ?, ?, ?)";
        String error = null;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, contactNumber);
            preparedStatement.setInt(3, serviceId);
            preparedStatement.setString(4, appointmentDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }
        return error; // Return whether the appointment was created successfully
    }

    public static String sellProduct(int productId, int quantitySold, int totalPrice) {
        String insertSaleSql = "INSERT INTO selling (ProductID, Quantity, Price) VALUES (?, ?, ?)";
        String error = null;
        try {

            // Insert the sale into the tb_SALES table
            PreparedStatement insertStatement = connection.prepareStatement(insertSaleSql);
            insertStatement.setInt(1, productId);
            insertStatement.setInt(2, quantitySold);
            insertStatement.setInt(3, totalPrice);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error;
    }

    public static String createService(String serviceName, double price, String description, String wheelsAvailable, boolean isAvailable) {
        String sql = "INSERT INTO " + Service.TABLE_NAME + " (ServiceName, Price, Description, Wheels, IsAvailable) VALUES (?, ?, ?, ?, ?)";
        String error = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, serviceName);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, wheelsAvailable);
            preparedStatement.setBoolean(5, isAvailable);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }
        return error; // Return whether the appointment was created successfully
    }

}
