/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.Service;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ServiceItemController {

    private Service service;
    @FXML
    Text serviceNameText, descriptionText;

    @FXML
    HBox backgroundHBox;

    public void setService(Service service) {
        this.service = service;
        reloadUI();
    }

    private void reloadUI() {
        serviceNameText.setText(service.getServiceName());
        descriptionText.setText(service.getDescription());
        if (service.isIsAvailable()) {
            backgroundHBox.setStyle(backgroundHBox.getStyle() + " -fx-background-color: lightblue;");
        } else {
            backgroundHBox.setStyle(backgroundHBox.getStyle() + " -fx-background-color: lightgrey;");
        }
    }
    
    @FXML
    private void basta() {
        System.out.println("HEllo");
    }

}
