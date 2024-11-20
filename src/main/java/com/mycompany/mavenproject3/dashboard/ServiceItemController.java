/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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


    public ServiceItemController() {
    }

    public void setService(Service service) {
        this.service = service;
        reloadUI();
    }

    private void reloadUI() {
        serviceNameText.setText(service.getServiceName());
        wheelsText.setText("Wheels: " + service.getWheels());
        descriptionText.setText(service.getDescription());

        String unavailable = "-fx-background-color: grey;";
        if (!service.isIsAvailable()) {
            backgroundHBox.setStyle(backgroundHBox.getStyle() + unavailable);
        }
    }
    
    @FXML
    private void basta() {
        System.out.println("HEllo");
    }

    public void removeButton() {
        backgroundHBox.getChildren().remove(optionsButton);
    }

}
