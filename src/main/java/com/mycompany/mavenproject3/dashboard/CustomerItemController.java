package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.Customer;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CustomerItemController {


    private Customer customer;

    @FXML
    VBox parentVbox;

    @FXML
    Text customerNameText, customerEmailText, customerNumberText;
    final String hoverStyle  = "-fx-background-color: lightblue;";

    @FXML
    private void initialize() {


    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        boolean hasCustomer = customer != null;
        customerNameText.setText(hasCustomer ? customer.getFullName() : "");
        customerEmailText.setText(hasCustomer ? customer.getEmail() : "");
        customerNumberText.setText(hasCustomer ? customer.getContactNumber() : "");
    }

    private void reloadUI() {

    }
}
