package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.ServiceAppointment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class AppointmentItem {
    @FXML
    private Text appointmentCustomerName;

    @FXML
    private Text appointmentDate;

    @FXML
    private Button appointmentViewButton;

    private final HBox root; // This will be the container for the UI elements

    public AppointmentItem(ServiceAppointment appointment) throws IOException {
        // Load the FXML for this AppointmentItem
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment_item.fxml"));

        // Load the FXML, which initializes the UI components
        loader.setController(this); // Set the controller to this instance
        root = loader.load(); // Load and retrieve the root node

        // Initialize the appointment details
        appointmentCustomerName.setText(appointment.getCustomer().getFullName());
        appointmentDate.setText(appointment.getDateTime().toString());

        // Optionally add an action for the button
        appointmentViewButton.setOnAction(event -> viewAppointmentDetails());
    }

    public HBox getRoot() {
        return root; // Provide access to the root node
    }

    private void viewAppointmentDetails() {
        // Your logic to view appointment details
        System.out.println("Viewing appointment for: " + appointmentCustomerName.getText());
    }
}
