/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.myforms;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXTextArea;
import com.mycompany.mavenproject3.dashboard.ServiceItemController;
import com.mycompany.mavenproject3.models.Customer;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Service;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

/**
 * FXML Controller class
 *
 * @author GNew
 */
public class CreateAppointmentFormController {

    @FXML
    public VBox customersVbox;
    @FXML
    public JFXTextArea customerSearchTextArea, serviceSearchTextArea;
    @FXML
    public VBox servicesVBox, selectedServicesVBox;
    @FXML
    private DatePicker serviceDatePicker;
    @FXML
    private Text customerNameText, dateText, timeText;

    List<Service> selectedServices = new ArrayList<>();

    // Define the ChangeListener
    private final ChangeListener<String> textChangeListener = (observable, oldValue, newValue) -> {
        reloadServicesList(newValue);
    };

    private void reloadServicesList(String serviceNameSearch) {
        servicesVBox.getChildren().clear();
        servicesVBox.prefWidth(Double.MAX_VALUE);
        if (!serviceNameSearch.isBlank()) {
            List<Service> services = DbHelper.getServicesByName(serviceNameSearch);
            List<Integer> selectedServiceIds = selectedServices.stream()
                    .map(Service::getServiceID)
                    .collect(Collectors.toList()); // Collect the stream into a List

            services.removeIf(service -> selectedServiceIds.contains(service.getServiceID()));

            try {
                for (Service service : services) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/mavenproject3/dashboard/service_item.fxml"));
                    Parent root = loader.load(); // Load and retrieve the root node
                    ServiceItemController controller = loader.getController();
                    controller.setService(service);
                    controller.removeButton();
                    root.prefWidth(330); // Set your desired fixed width, e.g., 300px
                    servicesVBox.getChildren().add(root);

                    root.setOnMouseClicked(mouseEvent -> {
                        Text currentSelectedText = new Text(service.getServiceName());

                        selectedServices.add(service);
                        selectedServicesVBox.getChildren().add(currentSelectedText);
                        reloadServicesList(serviceNameSearch);

                        currentSelectedText.setOnMouseClicked(event -> {
                            selectedServices.remove(service);
                            selectedServicesVBox.getChildren().remove(currentSelectedText);
                            reloadServicesList(serviceSearchTextArea.getText());
                        });
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void startNewScene() {
        // Load the new FXML for the new window
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(CreateAppointmentFormController.class.getResource("create_appointment_form.fxml")));
            // Create a new Stage (window)
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Create Appointment Form");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);  // Make stage2 modal

            stage.show();  // Show the new window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        addListeners();
        loadCustomers();
        initDatePicker();
    }

    private void initDatePicker() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        serviceDatePicker.setConverter(new LocalDateStringConverter(formatter, DateTimeFormatter.ISO_LOCAL_DATE));
        serviceDatePicker.setValue(LocalDate.now()); // Set the initial value (today's date)
        serviceDatePicker.setDayCellFactory(datePicker1 -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Disable past dates
                setDisable(item.isBefore(LocalDate.now()));
            }
        });
    }

    private void addListeners() {
        serviceSearchTextArea.textProperty().removeListener(textChangeListener);
        serviceSearchTextArea.textProperty().addListener(textChangeListener);
    }

    @FXML
    public void loadCustomers() {
        List<Customer> customers = DbHelper.getCustomers();
        for (Customer customer : customers) {
            customersVbox.getChildren().add(new Text(customer.getFullName()));
        }
    }

    @FXML
    public void openCreateCustomerForm() {

    }
}
