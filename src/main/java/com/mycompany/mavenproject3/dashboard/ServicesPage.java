package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.models.Vehicle;
import com.mycompany.mavenproject3.utils.Util;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    TableColumn<Service, Boolean> getIsAvailableColumn();

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
        getIsAvailableColumn().setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isIsAvailable()));
        getServiceDescriptionColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        if (observableServices.isEmpty()) {
            getServicesCountText().setText("There are NO scheduled appointments");
        } else {
            getServicesCountText().setText("There are " + observableServices.size() + " services found");
        }
        getServiceTableView().setItems(observableServices);
    }

    // Override from Refreshable
    @Override
    default void refresh() {
        loadServices();
        System.out.println("2");
    }
}

