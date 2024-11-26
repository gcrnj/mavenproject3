package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.Employee;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.myforms.CreateEmployeeController;
import com.mycompany.mavenproject3.utils.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public interface EmployeesPage extends Refreshable {

    // Define the TableView and columns for employee data
    TableView<Employee> getEmployeesTableView();

    TableColumn<Employee, String> getEmployeeNumberColumn();

    TableColumn<Employee, String> getEmployeeNameColumn();

    TableColumn<Employee, String> getEmployeePositionColumn();

    TableColumn<Employee, String> getEmployeeMobileNumberColumn();

    TableColumn<Employee, String> getEmployeeEmailAddressColumn();


    // Method to load employee data
    default void loadEmployees() {
        // Retrieve employee data from the database
        List<Employee> employees = DbHelper.getEmployees(); // Assuming DbHelper provides a method to fetch employees
        ObservableList<Employee> observableEmployees = Util.getObservableList(employees);

        // Set the cell value factories for the columns
        getEmployeeNumberColumn().setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getEmployeeID())));
        getEmployeeNameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
        getEmployeePositionColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosition().getPositionName()));
        getEmployeeMobileNumberColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContactNumber()));
        getEmployeeEmailAddressColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        // Set the data in the table
        getEmployeesTableView().setItems(observableEmployees);

        // Create context menu and actions for the employee table rows
        createEmployeeOptions(observableEmployees);
    }

    // Method to create context menu for the employee table rows
    private void createEmployeeOptions(ObservableList<Employee> observableEmployees) {
        getEmployeesTableView().setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editOption = new MenuItem("Edit");
            MenuItem deleteOption = new MenuItem("Delete");

            // Show the context menu only for non-empty rows
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.getItems().clear();
                    contextMenu.getItems().addAll(editOption, deleteOption);
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            // Handle menu item actions
            editOption.setOnAction(e -> {
                Employee selectedEmployee = row.getItem();
                if (selectedEmployee != null) {
                    onEditEmployee(selectedEmployee);
                }
            });

            deleteOption.setOnAction(e -> {
                Employee selectedEmployee = row.getItem();
                if (selectedEmployee != null) {
                    onDeleteEmployee(selectedEmployee);
                }
            });

            // Attach the context menu to the row only when an item is set
            row.itemProperty().addListener((obs, previousItem, currentItem) -> {
                if (currentItem != null) {
                    row.setContextMenu(contextMenu);
                } else {
                    row.setContextMenu(null);
                }
            });

            return row;
        });
    }

    // Method to edit an employee (implement in your class)
    default void onEditEmployee(Employee employee) {
        CreateEmployeeController.startNewScene(this, employee);
    }

    // Method to delete an employee (implement in your class)
    private void onDeleteEmployee(Employee employee) {
        DbHelper.deleteEmployee(employee.getEmployeeID()); // Assuming DbHelper has a method to delete an employee
        loadEmployees(); // Reload the employee list after deletion
    }

    @Override
    default void refresh() {
        loadEmployees(); // Refresh the employee data
    }
    @FXML
    default void createEmployeeForm() {
        CreateEmployeeController.startNewScene(this, null);
    }

}
