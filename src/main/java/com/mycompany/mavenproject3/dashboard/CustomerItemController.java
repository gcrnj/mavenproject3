package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.Customer;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CustomerItemController {


    private Customer customer;
    Runnable onClickListener;

    @FXML
    VBox parentVbox;

    @FXML
    Text customerNameText, customerEmailText, customerNumberText;
    final String hoverStyle  = "-fx-background-color: lightblue;";

    @FXML
    private void initialize() {


    }

    public void setOnClickListener(Runnable onClickListener) {
        this.onClickListener = onClickListener;
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


//    @FXML
//    // Change the color when mouse enters the VBox
//    private void onCustomerMouseEnter(MouseEvent event) {
//        parentVbox.setStyle(parentVbox.getStyle().replace(hoverStyle, ""));
//        parentVbox.setStyle(parentVbox.getStyle() + hoverStyle);
//    }
//
//    @FXML
//    // Revert the color when mouse exits the VBox
//    private void onCustomerMouseExit(MouseEvent event) {
//        parentVbox.setStyle(parentVbox.getStyle().replace(hoverStyle, ""));
//    }

    @FXML
    private void onMouseClicked() {
        onClickListener.run();
    }
}
