package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.models.Vehicle;
import com.mycompany.mavenproject3.utils.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public interface ServicesPage extends Refreshable {

    // Abstract method signatures
    TableView<Service> getServiceTableView();

    TableColumn<Service, String> getServiceNameColumn();

    TableColumn<Service, String> getServicePriceColumn();

    TableColumn<Service, String> getServiceWheelsColumn();

    TableColumn<Service, String> getServiceDescriptionColumn();

    TableColumn<Service, String> getIsAvailableColumn();

    Text getServicesCountText();

    // Default method for concrete logic
    default void loadServices() {
        List<Service> services = DbHelper.getServices(); // Retrieve services
        System.out.println("Services: " + services.size());

        ObservableList<Service> observableServices = Util.getObservableList(services);
        getServiceNameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiceName()));
        getServicePriceColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrice() + ""));
        getServiceWheelsColumn().setCellValueFactory(cellData -> {
            List<Vehicle> vehicles = cellData.getValue().getVehicles(); // Get the list of vehicles for the service
            if (vehicles != null && !vehicles.isEmpty()) {
                // Join the vehicle names using a comma separator
                String vehicleNames = vehicles.stream()
                        .map(Vehicle::getVehicleName) // Assuming 'getVehicleName' is the method to get the name
                        .collect(Collectors.joining(", ")); // Join with ", "
                return new SimpleStringProperty(vehicleNames); // Return the joined string as the cell value
            } else {
                return new SimpleStringProperty(""); // Return an empty string if no vehicles
            }
        });
        getIsAvailableColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isIsAvailable() ? "Yes" : "No"));
        getServiceDescriptionColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        if (observableServices.isEmpty()) {
            getServicesCountText().setText("There are NO scheduled appointments");
        } else {
            getServicesCountText().setText("There are " + observableServices.size() + " services found");
        }
        getServiceTableView().setItems(observableServices);
        createOptions();
    }

    // Override from Refreshable
    @Override
    default void refresh() {
        loadServices();
        System.out.println("2");
    }


    private void createOptions() {
        // Create the context menu

        getServiceTableView().setRowFactory(tv -> {
            TableRow<Service> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editOption = new MenuItem("Edit");
            MenuItem changeAvailabilityOption = new MenuItem("Set Unavailable");
            contextMenu.getItems().addAll(editOption, changeAvailabilityOption);

            // Show the context menu only for non-empty rows
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    Service selectedItem = row.getItem();
                    if(selectedItem != null && selectedItem.isIsAvailable()) {
                        // Available
                        changeAvailabilityOption.setText("Set Unavailable");
                    } else {
                        // Unavailable
                        changeAvailabilityOption.setText("Set Available");
                    }
                    row.getContextMenu().show(row, event.getScreenX(), event.getScreenY());
                }
            });

            // Handle menu item actions
            editOption.setOnAction(e -> {
                Service selectedItem = row.getItem();
                if (selectedItem != null) {
                    editService(selectedItem);
                    System.out.println("Edit selected: " + selectedItem.getServiceName());
                } else {
                    System.out.println("No item selected for Edit");
                }
            });

            changeAvailabilityOption.setOnAction(e -> {
                Service selectedItem = row.getItem();
                if (selectedItem != null) {
                    String error = DbHelper.changeServiceAvailability(selectedItem.getServiceID(), !selectedItem.isIsAvailable());
                    if (error == null) {
                        // Success
                        refresh();
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

    private void showError(String error) {

    }

    void editService(Service selectedItem);

}

