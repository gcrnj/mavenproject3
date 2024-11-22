package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Vehicle;
import com.mycompany.mavenproject3.utils.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.List;

public interface VehiclesPage {
    TableView<Vehicle> getVehicleTableView();

    TableColumn<Vehicle, String> getVehicleNameColumn();

    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    default void loadVehicles() {
        List<Vehicle> vehicles = DbHelper.getVehicles(); // Retrieve vehicles
        System.out.println("Vehicles: " + vehicles.size());

        ObservableList<Vehicle> observableVehicles = Util.getObservableList(vehicles);

        getVehicleNameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVehicleName()));

        getVehicleTableView().setItems(observableVehicles);
        createOptions();
    }

    private void createOptions() {
        // Create the context menu

        getVehicleTableView().setRowFactory(tv -> {
            TableRow<Vehicle> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editOption = new MenuItem("Edit");
            MenuItem deleteOption = new MenuItem("Delete");
            contextMenu.getItems().addAll(editOption, deleteOption);

            // Show the context menu only for non-empty rows
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    row.getContextMenu().show(row, event.getScreenX(), event.getScreenY());
                }
            });

            // Handle menu item actions
            editOption.setOnAction(e -> {
                Vehicle selectedItem = row.getItem();
                if (selectedItem != null) {
                    editVehicle(selectedItem);
                    System.out.println("Edit selected: " + selectedItem.getVehicleName());
                } else {
                    System.out.println("No item selected for Edit");
                }
            });

            deleteOption.setOnAction(e -> {
                Vehicle selectedItem = row.getItem();
                if (selectedItem != null) {
                    String error = DbHelper.deleteVehicleById(selectedItem.getVehicleId());
                    if (error == null) {
                        // Success
                        refreshVehicles();
                    } else {
                        showError(error);
                    }
                } else {
                    System.out.println("No item selected for Delete");
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

    void editVehicle(Vehicle vehicle);

    // Override from Refreshable
    default void refreshVehicles() {
        loadVehicles();
    }

    private void showError(String error) {
        errorAlert.setContentText(error);
        errorAlert.showAndWait();
    }
}
