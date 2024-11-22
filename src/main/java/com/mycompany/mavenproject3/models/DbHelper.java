package com.mycompany.mavenproject3.models;

import com.mycompany.mavenproject3.utils.PasswordUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static List<ServiceAppointment> getServiceAppointments(
            boolean scheduled,
            boolean completed,
            boolean canceled
    ) {
        List<ServiceAppointment> appointments = new ArrayList<>();
        String sql = "SELECT sa.*, "
                + "e.EmployeeID AS EmployeeID, e.FirstName AS EmployeeFirstName, e.LastName AS EmployeeLastName, e.ContactNumber AS EmployeeContactNumber, e.Email AS EmployeeEmail, "
                + "c.BarangayID, c.HouseNumber, c.Street, c.Building, "
                + "c.CustomerID AS CustomerID, c.FirstName AS CustomerFirstName, c.LastName AS CustomerLastName, c.ContactNumber AS CustomerContactNumber, c.Email AS CustomerEmail, "
                + "p.*, s.* "
                + "FROM " + ServiceAppointment.TABLE_NAME + " sa "
                + "JOIN " + Employee.TABLE_NAME + " e ON sa.EmployeeID = e.EmployeeID "
                + "JOIN " + Customer.TABLE_NAME + " c ON sa.CustomerID = c.CustomerID "
                + "JOIN " + Position.TABLE_NAME + " p ON e.PositionID = p.PositionID "
                + "JOIN " + Service.TABLE_NAME + " s ON sa.ServiceID = s.ServiceID ";

        // Dynamically build the WHERE clause based on the flags
        List<String> conditions = new ArrayList<>();
        if (scheduled) {
            conditions.add("sa.Status = 'SCHEDULED' ");
        }
        if (completed) {
            conditions.add("sa.Status = 'COMPLETED' ");
        }
        if (canceled) {
            conditions.add("sa.Status = 'CANCELED' ");
        }
        if (!conditions.isEmpty()) {
            sql += "WHERE " + String.join(" OR ", conditions);
        }
        sql += ";";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Appointment objects
            while (resultSet.next()) {
                ServiceAppointment appointment = new ServiceAppointment(resultSet, false);
                appointments.add(appointment); // Add the Appointment object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            System.out.println("SQL = " + sql);
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

    public static List<Customer> getCustomers(String customerDetail) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * from " + Customer.TABLE_NAME +
                " WHERE " + Customer.COL_FIRST_NAME + " LIKE ? "
                + " OR " + Customer.COL_LAST_NAME + " LIKE ?"
                + " OR " + Customer.COL_EMAIL + " LIKE ?"
                + " OR " + Customer.COL_CONTACT_NUMBER + " LIKE ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, '%' + customerDetail + '%');
            preparedStatement.setString(2, '%' + customerDetail + '%');
            preparedStatement.setString(3, '%' + customerDetail + '%');
            preparedStatement.setString(4, '%' + customerDetail + '%');
            ResultSet resultSet = preparedStatement.executeQuery();

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

    public static String createCustomer(
            String firstName,
            String lastName,

            String contactNumber,
            String emailAddress,

            Barangay barangay,
            String houseNumber,
            String street,
            String building
    ) {

        String error = null;

        String sql = "INSERT INTO " + Customer.TABLE_NAME + " values (?,?,  ?,?,  ?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            preparedStatement.setString(3, contactNumber);
            preparedStatement.setString(4, emailAddress);

            preparedStatement.setInt(5, barangay.getBrgyId());
            preparedStatement.setString(6, houseNumber);
            preparedStatement.setString(7, street);
            preparedStatement.setString(8, building);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
            error = e.getMessage();
        }
        return error; // Return the list of appointments
    }

    public static List<Service> getServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.*, v.*, e.*, p.* " +  // Select services along with vehicle and employee info
                "FROM " + Service.TABLE_NAME + " AS s " +
                "LEFT JOIN ServiceVehicle AS sv ON s.ServiceID = sv.ServiceID " + // Join with ServiceVehicle table
                "LEFT JOIN Vehicles AS v ON sv.VehicleID = v.VehicleID " + // Join with Vehicle table
                "LEFT JOIN Employee AS e ON v.CreatedBy = e.EmployeeID " + // Join with Employee table
                "LEFT JOIN Position AS p ON e.PositionID = p.PositionID"; // Join with Position table

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            Service currentService = null;
            List<Vehicle> vehiclesForCurrentService = new ArrayList<>();

            // Loop through the result set
            while (resultSet.next()) {
                int serviceId = resultSet.getInt(Service.COL_SERVICE_ID);

                // If it's a new service, add the previous service to the list and reset the vehicles list
                if (currentService == null || currentService.getServiceID() != serviceId) {
                    if (currentService != null) {
                        currentService.setVehicles(vehiclesForCurrentService); // Set vehicles for the previous service
                        services.add(currentService); // Add the current service to the list
                    }

                    // Create a new service object and reset the vehicles list
                    currentService = new Service(resultSet);
                    vehiclesForCurrentService = new ArrayList<>();
                }

                // Add vehicle to the list if it's part of the current service
                Vehicle vehicle = new Vehicle(resultSet, true);
                vehiclesForCurrentService.add(vehicle);
            }

            // Add the last service to the list after exiting the loop
            if (currentService != null) {
                currentService.setVehicles(vehiclesForCurrentService); // Set vehicles for the last service
                services.add(currentService); // Add the final service to the list
            }

        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }

        return services; // Return the list of services with their associated vehicles
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

    public static String createAppointment(int serviceId, int customerId, int employeeId, LocalDateTime dateTime) {
        String scheduled = AppointmentStatus.SCHEDULED.name();
        String sql = "INSERT INTO " + ServiceAppointment.TABLE_NAME +
                " (ServiceID, CustomerID, EmployeeID, AppointmentDateTime, Status)" +
                " VALUES (?, ?, ?, ?, ?)";
        String error = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, serviceId);
            preparedStatement.setInt(2, customerId);
            preparedStatement.setInt(3, employeeId);
            preparedStatement.setString(4, dateTime.format(formatter));
            preparedStatement.setString(5, scheduled);
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

    public static String createService(String serviceName, double price, String description, List<Vehicle> vehicles, boolean isAvailable) {
        String serviceInsertSql = "INSERT INTO " + Service.TABLE_NAME + " (ServiceName, Price, Description, IsAvailable) VALUES (?, ?, ?, ?)";
        String serviceVehicleInsertSql = "INSERT INTO ServiceVehicle (ServiceID, VehicleID) VALUES (?, ?)";

        String error = null;
        int serviceId = -1; // Will store the generated ServiceID

        try {
            // Insert the service and retrieve the generated ServiceID
            PreparedStatement serviceInsertStatement = connection.prepareStatement(serviceInsertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            serviceInsertStatement.setString(1, serviceName);
            serviceInsertStatement.setDouble(2, price);
            serviceInsertStatement.setString(3, description);
            serviceInsertStatement.setBoolean(4, isAvailable);

            int rowsAffected = serviceInsertStatement.executeUpdate(); // Execute the insert statement

            if (rowsAffected == 0) {
                return "Error: No rows affected while inserting service.";
            }

            // Retrieve the generated ServiceID
            ResultSet generatedKeys = serviceInsertStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                serviceId = generatedKeys.getInt(1);  // Get the generated ServiceID
            } else {
                return "Error: Service ID not generated.";
            }

            // Insert the vehicles into the ServiceVehicle table
            for (Vehicle vehicle : vehicles) {
                PreparedStatement serviceVehicleInsertStatement = connection.prepareStatement(serviceVehicleInsertSql);
                serviceVehicleInsertStatement.setInt(1, serviceId);  // Set the generated ServiceID
                serviceVehicleInsertStatement.setInt(2, vehicle.getVehicleId());  // Set the VehicleID
                serviceVehicleInsertStatement.executeUpdate();
            }

        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
    }


    public static List<Province> getProvinces() {
        List<Province> provinces = new ArrayList<>();
        String sql = "SELECT * FROM " + Province.TABLE_NAME; // SQL query to select all provinces

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Province objects
            while (resultSet.next()) {
                Province province = new Province(resultSet); // Assuming Province constructor takes ResultSet
                provinces.add(province); // Add the Province object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }

        return provinces; // Return the list of provinces
    }

    public static List<Municipality> getMunicipalities(int provinceId) {
        List<Municipality> municipalities = new ArrayList<>();
        String sql = "SELECT m.*, p.* FROM " + Municipality.TABLE_NAME + " m " +
                "JOIN " + Province.TABLE_NAME + " p ON m.ProvinceID = p.ProvinceID " +
                "WHERE m.ProvinceID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, provinceId); // Set the provinceId parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Loop through the result set and create Municipality objects
                while (resultSet.next()) {
                    Municipality municipality = new Municipality(resultSet); // Assuming Municipality constructor takes ResultSet
                    municipalities.add(municipality); // Add the Municipality object to the list
                }
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }
        return municipalities; // Return the list of municipalities
    }

    public static List<Barangay> getBarangays(int municipalityId) {
        List<Barangay> barangays = new ArrayList<>();
        String sql = "SELECT b.*, m.MunicipalityName, p.ProvinceName, p.ProvinceID " +
                "FROM Barangays b " +
                "JOIN Municipalities m ON b.MunicipalityID = m.MunicipalityID " +
                "JOIN Provinces p ON m.ProvinceID = p.ProvinceID " +
                "WHERE b.MunicipalityID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, municipalityId); // Set the municipalityId parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Loop through the result set and create Barangay objects
                while (resultSet.next()) {
                    Barangay barangay = new Barangay(resultSet); // Assuming Barangay constructor takes ResultSet
                    barangays.add(barangay); // Add the Barangay object to the list
                }
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }

        return barangays; // Return the list of barangays
    }

    public static String createVehicle(String vehicleName) {
        int createdBy = LocalCache.getEmployee().getEmployeeID();
        String sqlInsert = "INSERT INTO " + Vehicle.TABLE_NAME +
                " (" + Vehicle.COL_VEHICLE_NAME + ", " + Vehicle.COL_CREATED_BY + ")" +
                " VALUES (?, ?)";  // Set IsDeleted to 0 when inserting new records
        String error = null;

        try {
            // Insert the vehicle directly without checking for duplicates
            PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);
            insertStatement.setString(1, vehicleName);
            insertStatement.setInt(2, createdBy);
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
    }


    public static String editVehicleName(int vehicleId, String newVehicleName) {
        String sql = "UPDATE " + Vehicle.TABLE_NAME +
                " SET " + Vehicle.COL_VEHICLE_NAME + " = ? " +
                "WHERE " + Vehicle.COL_VEHICLE_ID + " = ?";
        String error = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newVehicleName); // Set the new vehicle name
            preparedStatement.setInt(2, vehicleId); // Set the vehicle ID to update
            preparedStatement.executeUpdate(); // Execute the update query
        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
    }

    public static List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT v.*, e.*, p.* " +
                "FROM " + Vehicle.TABLE_NAME + " AS v " +
                "JOIN Employee AS e ON v.CreatedBy = e.EmployeeId " +  // Joining Vehicles with Employee based on CreatedBy (EmployeeId)
                "JOIN Position AS p ON e.PositionId = p.PositionId " +
                "WHERE " + Vehicle.COL_VEHICLE_DELETED + " = 0;"; // Joining Employee with Position based on PositionId

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Vehicle objects
            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle(resultSet, true); // Pass ResultSet to the Vehicle constructor
                vehicles.add(vehicle); // Add the Vehicle object to the list
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace(); // For debugging
        }

        return vehicles; // Return the list of vehicles
    }

    public static String deleteVehicleById(int vehicleId) {
        // SQL to check if the vehicle is used in the ServiceVehicle table
        String checkSql = "SELECT COUNT(*) FROM " + "ServiceVehicle" +
                " WHERE " + "VehicleID" + " = ?"; // Check if vehicle is referenced in ServiceVehicle table

        // SQL to mark the vehicle as deleted (soft delete)
        String updateSql = "UPDATE " + Vehicle.TABLE_NAME +
                " SET " + Vehicle.COL_VEHICLE_DELETED + " = 1 " + // Mark as deleted
                " WHERE " + Vehicle.COL_VEHICLE_ID + " = ?";

        // SQL to completely delete the vehicle (hard delete)
        String deleteSql = "DELETE FROM " + Vehicle.TABLE_NAME +
                " WHERE " + Vehicle.COL_VEHICLE_ID + " = ?";

        String error = null;

        try {
            // Check if the vehicle is used in the ServiceVehicle table
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setInt(1, vehicleId); // Set the vehicle ID to check
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // If the vehicle is referenced in the ServiceVehicle table, perform soft delete (set IsDeleted = 1)
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setInt(1, vehicleId); // Set the vehicle ID to mark as deleted
                updateStatement.executeUpdate();
            } else {
                // If the vehicle is not referenced in the ServiceVehicle table, perform hard delete
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, vehicleId); // Set the vehicle ID to delete completely
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
    }



}
