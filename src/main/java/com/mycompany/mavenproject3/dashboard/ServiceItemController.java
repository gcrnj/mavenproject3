/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.models.Vehicle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceItemController {

    private Service service;
    @FXML
    Text serviceNameText, wheelsText, descriptionText;

    @FXML
    HBox backgroundHBox;


    public ServiceItemController() {
    }

    public void setService(Service service) {
        this.service = service;
        reloadUI();
    }

    private void reloadUI() {

        serviceNameText.setText(service.getServiceName());
        wheelsText.setText(getVehicles(service.getVehicles()));
        descriptionText.setText(service.getDescription());

        String unavailable = "-fx-background-color: grey;";
        if (!service.isIsAvailable()) {
            backgroundHBox.setStyle(backgroundHBox.getStyle() + unavailable);
        }
    }

    private String getVehicles( List<Vehicle> vehicles) {
        if (vehicles != null && !vehicles.isEmpty()) {
            return vehicles.stream()
                    .map(Vehicle::getVehicleName) // Assuming 'getVehicleName' is the method to get the name
                    .collect(Collectors.joining(", ")); // Return the joined string as the cell value
        } else {
            return ""; // Return an empty string if no vehicles
        }
    }

    @FXML
    private void basta() {
        System.out.println("HEllo");
    }

}
