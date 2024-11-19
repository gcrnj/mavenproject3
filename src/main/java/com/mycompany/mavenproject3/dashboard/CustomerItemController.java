package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.Customer;
import javafx.fxml.FXML;

public class CustomerItemController {


    private Customer customer;

    @FXML
    private void initialize() {


    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private void reloadUI() {

    }
}
