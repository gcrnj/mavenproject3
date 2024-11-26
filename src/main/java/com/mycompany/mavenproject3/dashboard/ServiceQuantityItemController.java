package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.utils.TextFormatterUtil;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ServiceQuantityItemController {

    @FXML
    private TextField serviceQuantityTextField;

    @FXML
    private Text serviceNameText, priceText, totalText;

    private Service service;
    ChangeListener<Integer> changeListener;

    // Called when the FXML file is loaded
    @FXML
    public void initialize() {
        // Perform any necessary initialization here, if needed
        serviceQuantityTextField.setTextFormatter(TextFormatterUtil.intTextFormatter());
    }

    // Set the service and update the UI components
    public void setService(Service service, ChangeListener<Integer> changeListener) {
        this.service = service;
        this.changeListener = changeListener;
        if (service != null) {
            serviceNameText.setText(service.getServiceName());
            serviceQuantityTextField.setText(service.getQuantity() + ""); // Default quantity or fetched from Service
            priceText.setText(service.getPrice() + "");
            totalText.setText(service.getPrice() * service.getQuantity() + "");
            // Add a listener to track quantity changes
            serviceQuantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    int quantity = Integer.parseInt(newValue);
                    changeListener.changed(null, Integer.parseInt(oldValue), quantity);
                    priceText.setText(service.getPrice() + "");
                    totalText.setText(service.getPrice() * quantity + "");
                } catch (NumberFormatException e) {
                    // Reset to previous valid value if input is invalid
                    serviceQuantityTextField.setText(oldValue);
                }
            });
        }
    }
}

