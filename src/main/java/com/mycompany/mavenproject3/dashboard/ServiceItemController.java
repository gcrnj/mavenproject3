/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.models.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class ServiceItemController {

    private Service service;
    @FXML
    Text serviceNameText, wheelsText, descriptionText;

    @FXML
    HBox backgroundHBox;

    @FXML
    Button optionsButton;

    @FXML
    ComboBox<Vehicle> vehiclesComboBox;

    public ServiceItemController() {
    }

    public void setService(Service service) {
        this.service = service;
        reloadUI();
    }

    private void reloadUI() {
        serviceNameText.setText(service.getServiceName());
        descriptionText.setText(service.getDescription());

        String unavailable = "-fx-background-color: grey;";
        if (!service.isIsAvailable()) {
            backgroundHBox.setStyle(backgroundHBox.getStyle() + unavailable);
        }

        // Vehicles
        vehiclesComboBox.getItems().clear();
        vehiclesComboBox.getItems().addAll(service.getVehicles());
    }
    
    @FXML
    private void basta() {
        System.out.println("HEllo");
    }

    public void removeButton() {
        backgroundHBox.getChildren().remove(optionsButton);
    }

}
