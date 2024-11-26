package com.mycompany.mavenproject3.models;

import com.mycompany.mavenproject3.utils.PasswordUtils;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
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

    public static List<Appointment> getAppointments(
            boolean scheduled,
            boolean completed,
            boolean canceled,
            LocalDate from,
            LocalDate to
    ) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT sa.*, "
                + "e.EmployeeID AS EmployeeID, e.FirstName AS EmployeeFirstName, e.LastName AS EmployeeLastName, e.ContactNumber AS EmployeeContactNumber, e.Email AS EmployeeEmail, "
                + "c.BarangayID, c.HouseNumber, c.Street, c.Building, "
                + "c.CustomerID AS CustomerID, c.FirstName AS CustomerFirstName, c.LastName AS CustomerLastName, c.ContactNumber AS CustomerContactNumber, c.Email AS CustomerEmail, "
                + "p.*, s.*, asv.Quantity AS ServiceQuantity "
                + "FROM " + Appointment.TABLE_NAME + " sa "
                + "JOIN " + Employee.TABLE_NAME + " e ON sa.EmployeeID = e.EmployeeID "
                + "JOIN " + Customer.TABLE_NAME + " c ON sa.CustomerID = c.CustomerID "
                + "JOIN " + Position.TABLE_NAME + " p ON e.PositionID = p.PositionID "
                + "JOIN " + AppointmentService.TABLE_NAME + " asv ON sa.AppointmentId = asv.AppointmentId "
                + "JOIN " + Service.TABLE_NAME + " s ON asv.ServiceId = s.ServiceID ";

        System.out.println(sql);
// Dynamically build the WHERE clause based on the flags
        List<String> conditions = new ArrayList<>();
        List<String> statusConditions = new ArrayList<>();  // Separate list for status conditions

// Add status filters to the statusConditions list
        if (scheduled) {
            statusConditions.add("sa.Status = 'SCHEDULED'");
        }
        if (completed) {
            statusConditions.add("sa.Status = 'COMPLETED'");
        }
        if (canceled) {
            statusConditions.add("sa.Status = 'CANCELED'");
        }

// If there are any status conditions, use OR to combine them
        if (!statusConditions.isEmpty()) {
            conditions.add("(" + String.join(" OR ", statusConditions) + ")");
        }

// Date range filter (AND logic)
        if (from != null && to != null) {
            conditions.add("CAST(sa.AppointmentDateTime AS DATE) BETWEEN ? AND ?");
        } else if (from != null) {
            conditions.add("CAST(sa.AppointmentDateTime AS DATE) >= ?");
        } else if (to != null) {
            conditions.add("CAST(sa.AppointmentDateTime AS DATE) <= ?");
        }

// If there are conditions, build the WHERE clause
        if (!conditions.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", conditions);
        }

        sql += ";";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (from != null && to != null) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(from));
                preparedStatement.setDate(2, java.sql.Date.valueOf(to));
            } else if (from != null) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(from));
            } else if (to != null) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(to));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            // Loop through the result set and create Appointment objects
            while (resultSet.next()) {
                Appointment appointment = new Appointment(resultSet, false);

                // Get services for the appointment
                int appointmentId = appointment.getAppointmentID();
                List<Service> services = new ArrayList<>();
                do {
                    // Add the service to the list if it belongs to the same appointment
                    Service service = new Service(resultSet); // Assume Service constructor handles the ResultSet
                    service.setQuantity(resultSet.getInt("ServiceQuantity"));
                    services.add(service);

                    // If there's no more data for this appointment, break the loop
                } while (resultSet.next() && resultSet.getInt("AppointmentID") == appointmentId);

                // Set services in the appointment
                appointment.setServices(services);
                appointments.add(appointment);
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

    public static String createEmployee(
            String firstName,
            String middleName,
            String lastName,

            String contactNumber,
            String emailAddress,

            Barangay barangay,
            String houseNumber,
            String street,
            String building,

            int positionId,

            String username,
            String plainPassword
    ) {
        String error = null;

        // SQL to check if the username already exists
        String checkUsernameSql = "SELECT COUNT(*) FROM " + Employee.TABLE_NAME + " WHERE Username = ?";

        // SQL to insert the new employee
        String insertEmployeeSql = "INSERT INTO " + Employee.TABLE_NAME + " " +
                "(FirstName, MiddleName, LastName, " +
                "ContactNumber, Email, " +
                "BrgyID, HouseNumber, Street, Building, " +
                "PositionID, " +
                "Username, Password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Check if the username already exists
            try (PreparedStatement checkUsernameStmt = connection.prepareStatement(checkUsernameSql)) {
                checkUsernameStmt.setString(1, username);
                ResultSet resultSet = checkUsernameStmt.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return "Username already exists. Please choose a different username.";
                }
            }

            // Insert the employee if the username is unique
            try (PreparedStatement insertEmployeeStmt = connection.prepareStatement(insertEmployeeSql)) {
                insertEmployeeStmt.setString(1, firstName);
                insertEmployeeStmt.setString(2, middleName);
                insertEmployeeStmt.setString(3, lastName);

                insertEmployeeStmt.setString(4, contactNumber);
                insertEmployeeStmt.setString(5, emailAddress);

                insertEmployeeStmt.setInt(6, barangay.getBrgyId());
                insertEmployeeStmt.setString(7, houseNumber);
                insertEmployeeStmt.setString(8, street);
                insertEmployeeStmt.setString(9, building);

                insertEmployeeStmt.setInt(10, positionId);

                insertEmployeeStmt.setString(11, username);
                insertEmployeeStmt.setString(12, PasswordUtils.hashPassword(plainPassword));

                insertEmployeeStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // For debugging
            error = e.getMessage();
        }

        return error;
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
                currentService.setQuantity(1);
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

    public static String createAppointment(ObservableList<Service> services, int customerId, int employeeId, LocalDateTime dateTime) {
        String scheduled = AppointmentStatus.SCHEDULED.name();
        String sql = "INSERT INTO " + Appointment.TABLE_NAME +
                " (CustomerID, EmployeeID, AppointmentDateTime, Status)" +
                " VALUES (?, ?, ?, ?)";
        String error = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, employeeId);
            preparedStatement.setString(3, dateTime.format(formatter));
            preparedStatement.setString(4, scheduled);

            // Execute the insert and get the generated AppointmentId
            preparedStatement.executeUpdate();

            // Retrieve the generated AppointmentId
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int appointmentId = 0;
            if (resultSet.next()) {
                appointmentId = resultSet.getInt(1); // Retrieve the generated AppointmentId
            }

            // Now create records in AppointmentServices table
            error = createAppointmentServices(appointmentId, services); // Insert the services for the appointment

        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
    }

    public static String createAppointmentServices(int appointmentId, ObservableList<Service> services) {
        String error = null;
        String sql = "INSERT INTO " + AppointmentService.TABLE_NAME +
                " (AppointmentId, ServiceId, Quantity) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement;

            // Iterate over the list of services and add each to the AppointmentServices table
            for (Service service : services) {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, appointmentId);  // Set the AppointmentId
                preparedStatement.setInt(2, service.getServiceID()); // Set the ServiceId
                preparedStatement.setInt(3, service.getQuantity()); // Set the ServiceId
                preparedStatement.executeUpdate(); // Execute the insert
            }

        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
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

    public static String editService(int serviceId, String serviceName, double price, String description, List<Vehicle> vehicles, boolean isAvailable) {
        String updateServiceSql = "UPDATE " + Service.TABLE_NAME +
                " SET ServiceName = ?, Price = ?, Description = ?, IsAvailable = ? " +
                " WHERE " + Service.COL_SERVICE_ID + " = ?";
        String deleteServiceVehiclesSql = "DELETE FROM ServiceVehicle WHERE ServiceID = ?";
        String insertServiceVehicleSql = "INSERT INTO ServiceVehicle (ServiceID, VehicleID) VALUES (?, ?)";

        String error = null;

        try {
            // Update the service details
            PreparedStatement updateServiceStatement = connection.prepareStatement(updateServiceSql);
            updateServiceStatement.setString(1, serviceName);
            updateServiceStatement.setDouble(2, price);
            updateServiceStatement.setString(3, description);
            updateServiceStatement.setBoolean(4, isAvailable);
            updateServiceStatement.setInt(5, serviceId);

            int rowsAffected = updateServiceStatement.executeUpdate();
            if (rowsAffected == 0) {
                return "Error: No rows affected while updating service.";
            }

            // Delete existing vehicle associations for the service
            PreparedStatement deleteServiceVehiclesStatement = connection.prepareStatement(deleteServiceVehiclesSql);
            deleteServiceVehiclesStatement.setInt(1, serviceId);
            deleteServiceVehiclesStatement.executeUpdate();

            // Re-insert the updated vehicle associations
            for (Vehicle vehicle : vehicles) {
                PreparedStatement insertServiceVehicleStatement = connection.prepareStatement(insertServiceVehicleSql);
                insertServiceVehicleStatement.setInt(1, serviceId);  // Set the service ID
                insertServiceVehicleStatement.setInt(2, vehicle.getVehicleId());  // Set the Vehicle ID
                insertServiceVehicleStatement.executeUpdate();
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


    public static String editAppointment(
            int appointmentId,
            ObservableList<Service> services,
            int customerId,
            int employeeId,
            LocalDateTime dateTime) {
        String updated = AppointmentStatus.SCHEDULED.name();
        String sql = "UPDATE " + Appointment.TABLE_NAME +
                " SET CustomerID = ?, EmployeeID = ?, AppointmentDateTime = ?, Status = ?" +
                " WHERE AppointmentID = ?";
        String error = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, employeeId);
            preparedStatement.setString(3, dateTime.format(formatter));
            preparedStatement.setString(4, updated);
            preparedStatement.setInt(5, appointmentId);

            // Execute the update
            preparedStatement.executeUpdate();

            // Clear and update AppointmentServices for this appointment
            error = clearAppointmentServices(appointmentId);
            if (error == null) {
                error = createAppointmentServices(appointmentId, services);
            }

        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
    }

    private static String clearAppointmentServices(int appointmentId) {
        String sql = "DELETE FROM AppointmentServices WHERE AppointmentID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, appointmentId);
            preparedStatement.executeUpdate();
            return null; // Return null if successful
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage(); // Return error message if an exception occurs
        }
    }


    public static void updateAppointmentStatus(AppointmentStatus appointmentStatus, int appointmentId) {
        // SQL to update the status of an appointment
        String updateSql = "UPDATE " + Appointment.TABLE_NAME + " SET Status = ? WHERE AppointmentID = ?";
        System.out.println("updateAppointmentStatus = " + updateSql);
        System.out.println("updateAppointmentStatus = " + appointmentStatus);
        System.out.println("updateAppointmentStatus = " + appointmentId);
        try {
            // Prepare the SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);

            // Set the parameters: new status and appointment ID
            preparedStatement.setString(1, appointmentStatus.name()); // Assuming getStatus() returns a string
            preparedStatement.setInt(2, appointmentId); // Assuming getAppointmentId() returns an int

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment status updated successfully.");
            } else {
                System.out.println("No appointment found with the specified ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle exceptions appropriately
            System.out.println("Error updating appointment status: " + e.getMessage());
        }
    }

    public static String changeServiceAvailability(int serviceId, boolean isAvailableNewValue) {
        // SQL to mark the service as deleted (soft delete)
        int newAvailability = isAvailableNewValue ? 1 : 0;
        String updateSql = "UPDATE " + Service.TABLE_NAME +
                " SET " + Service.COL_IS_AVAILABLE + " = ? " +
                " WHERE " + Service.COL_SERVICE_ID + " = ? ";

        String error = null;

        try {
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setInt(1, newAvailability);
            updateStatement.setInt(2, serviceId);
            updateStatement.executeUpdate();

        } catch (SQLException e) {
            error = e.getMessage();
            e.printStackTrace(); // Handle exceptions properly in production
        }

        return error; // Return null if successful, or the error message if not
    }

    public static List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();
        String sql = "SELECT * FROM " + Position.TABLE_NAME;
        String managerWhere = " WHERE PositionName = 'Teller'";
        if (LocalCache.isManager()) {
            sql += managerWhere;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                positions.add(new Position(resultSet));
            }

        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., log error)
            e.printStackTrace();
        }

        return positions;
    }


    // Method to retrieve all employees from the database
    public static List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT e.*, p.* FROM " + Employee.TABLE_NAME + " e "
                + "JOIN " + Position.TABLE_NAME + " p ON e.positionId = p.PositionID "; // Your query to fetch employees

        String managerWhere = " WHERE PositionName = 'Teller'";
        if (LocalCache.isManager()) {
            query += managerWhere;
        }

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Loop through the result set and create Employee objects
            while (resultSet.next()) {
                Employee employee = new Employee(resultSet);
                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (e.g., log the error)
        }

        return employees;
    }

    // Method to delete an employee by ID
    public static void deleteEmployee(int employeeID) {
        String query = "DELETE FROM Employee WHERE EmployeeID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, employeeID); // Set the employee ID in the query
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee with ID " + employeeID + " has been deleted.");
            } else {
                System.out.println("No employee found with ID " + employeeID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (e.g., log the error)
        }
    }

}
