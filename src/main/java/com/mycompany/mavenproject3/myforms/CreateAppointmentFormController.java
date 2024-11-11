/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.myforms;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.mycompany.mavenproject3.models.Customer;
import com.mycompany.mavenproject3.models.DbHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GNew
 */
public class CreateAppointmentFormController {

    private static Stage stage;
    @FXML
    public VBox customersVbox;

    public static void startNewScene() {
        // Load the new FXML for the new window
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(CreateAppointmentFormController.class.getResource("create_appointment_form.fxml")));
            // Create a new Stage (window)
            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Create Appointment Form");
            stage.setResizable(false);
            stage.show();  // Show the new window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Heyyy yow!");
        loadCustomers();
    }

    @FXML
    public void loadCustomers() {
        List<Customer> customers = DbHelper.getCustomers();
        for (Customer customer : customers) {
            customersVbox.getChildren().add(new Text(customer.getFullName()));
        }
    }
}
