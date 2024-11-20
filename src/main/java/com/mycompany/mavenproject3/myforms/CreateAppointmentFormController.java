/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.myforms;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXTextField;
import com.mycompany.mavenproject3.dashboard.CustomerItemController;
import com.mycompany.mavenproject3.dashboard.ServiceItemController;
import com.mycompany.mavenproject3.dashboard.ServicesPage;
import com.mycompany.mavenproject3.models.Customer;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.LocalCache;
import com.mycompany.mavenproject3.models.Service;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
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

    ServicesPage servicesPage;
    @FXML
    public VBox customersVbox;
    @FXML
    public JFXTextField customerSearchTextField, serviceSearchTextField;
    @FXML
    public VBox servicesVBox, selectedServicesVBox;
    @FXML
    private DatePicker serviceDatePicker;
    @FXML
    private Text customerNameText, dateText, timeText, servicesErrorText;
    @FXML
    private ComboBox<String> hourTimePicker, minutePicker, amOrPmPicker;

    ObservableList<Service> selectedServices = FXCollections.observableArrayList();
    SimpleObjectProperty<Customer> selectedCustomer = new SimpleObjectProperty<>();
    SimpleObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
    SimpleObjectProperty<String> selectedHour = new SimpleObjectProperty<>();
    SimpleObjectProperty<String> selectedMinute = new SimpleObjectProperty<>();
    SimpleObjectProperty<String> selectedAmPm = new SimpleObjectProperty<>();

    // Define the ChangeListeners
    private final ChangeListener<String> servicesTextChangeListener = (observable, oldValue, newValue) -> {
        reloadServicesList(newValue);
    };

    private final ChangeListener<String> customersTextChangeListener = (observable, oldValue, newValue) -> {
        reloadCustomersList(newValue);
    };

    private void reloadServicesList(String serviceNameSearch) {
        servicesVBox.getChildren().clear();
        servicesVBox.prefWidth(Double.MAX_VALUE);
        List<Service> services = serviceNameSearch.isBlank() ?
                DbHelper.getServices() : DbHelper.getServicesByName(serviceNameSearch);

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

                    selectedServices.add(service);
                    reloadServicesList(serviceNameSearch);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void reloadCustomersList(String customerSearch) {
        customersVbox.getChildren().remove(1, customersVbox.getChildren().size());
        customersVbox.prefWidth(Double.MAX_VALUE);
        List<Customer> customers = DbHelper.getCustomers(customerSearch);
        for (Customer customer : customers) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/mavenproject3/dashboard/customer_item.fxml"));
                Parent root = loader.load(); // Load and retrieve the root node
                CustomerItemController controller = loader.getController();
                controller.setCustomer(customer);
                // root.prefWidth(330); // Set your desired fixed width, e.g., 300px
                customersVbox.getChildren().add(root);

                root.setOnMouseClicked(mouseEvent -> {
                    selectedCustomer.set(customer);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startNewScene(ServicesPage servicesPage) {
        // Load the new FXML for the new window
        try {
            // Create an FXMLLoader instance
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(CreateAppointmentFormController.class.getResource("create_appointment_form.fxml")));

            // Load the FXML and retrieve the root element
            Parent root = loader.load();            // Create a new Stage (window)
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Create Appointment Form");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);  // Make stage2 modal
            stage.show();  // Show the new window
            CreateAppointmentFormController controller = loader.getController();
            controller.servicesPage = servicesPage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        addListeners();
        addObservers();
        reloadCustomersList(customerSearchTextField.getText());
        initDatePicker();
        reloadServicesList("");
    }

    @FXML
    private void onHourSelected() {
        this.selectedHour.setValue(hourTimePicker.getValue());
    }

    @FXML
    private void onMinuteSelected() {
        this.selectedMinute.setValue(minutePicker.getValue());
    }

    @FXML
    private void onAmPmSelected() {
        this.selectedAmPm.setValue(amOrPmPicker.getValue());
    }

    private void addObservers() {
        selectedCustomer.addListener((observable, oldValue, newValue) -> {
            customerNameText.setText(
                    newValue != null ? newValue.getFullName() : ""
            );
        });
        selectedServices.addListener((ListChangeListener.Change<? extends Service> change) -> {
            while (change.next()) {

                if (change.wasAdded()) {
                    for (Service addedService : change.getAddedSubList()) {
                        Text currentSelectedText = new Text(addedService.getServiceName());
                        selectedServicesVBox.getChildren().add(currentSelectedText);

                        currentSelectedText.setOnMouseClicked(event -> {
                            // Remove from the list
                            selectedServices.remove(addedService);
                            selectedServicesVBox.getChildren().remove(currentSelectedText);
                            reloadServicesList(serviceSearchTextField.getText());
                        });
                    }
                }
            }
        });
    }

    private void initDatePicker() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        serviceDatePicker.setConverter(new LocalDateStringConverter(formatter, DateTimeFormatter.ISO_LOCAL_DATE));
        serviceDatePicker.setDayCellFactory(datePicker1 -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Disable past dates
                setDisable(item.isBefore(LocalDate.now()));
            }
        });
        // Add a listener to detect when the date is changed
        serviceDatePicker.valueProperty().addListener((observable, oldValue, newValue)
                -> {
            selectedDate.setValue(newValue);
            System.out.println("Selected date: " + newValue.format(formatter));
        });
        // Select date now
        serviceDatePicker.setValue(LocalDate.now()); // Set the initial value (today's date)
    }

    private void addListeners() {
        customerSearchTextField.textProperty().removeListener(customersTextChangeListener);
        customerSearchTextField.textProperty().addListener(customersTextChangeListener);

        serviceSearchTextField.textProperty().removeListener(servicesTextChangeListener);
        serviceSearchTextField.textProperty().addListener(servicesTextChangeListener);
    }

    @FXML
    public void openCreateCustomerForm() {
        System.out.println("openCreateCustomerForm");
        CreateCustomerController.startNewScene();
    }

    @FXML
    public void saveButtonClick() {
        // Validate

        boolean isValid = true;

        // Set error messages to indicate missing fields
        if (selectedCustomer.getValue() == null) {
            customerNameText.setText("Customer is required");
            isValid = false;
        }

        if (selectedDate.getValue() == null) {
            dateText.setText("Date is required");
            isValid = false;
        } else {
            dateText.setText("");
        }

        if (selectedHour.getValue() == null || selectedMinute.getValue() == null || selectedAmPm.getValue() == null) {
            timeText.setText("Time is required");
            isValid = false;
        } else {
            timeText.setText("");
        }

        if (selectedServices.isEmpty()) {
            servicesErrorText.setText("At least one service is required");
            isValid = false;
        } else {
            servicesErrorText.setText("");
        }

        if (isValid) {
            int finalHour = Integer.parseInt(selectedHour.getValue());
            if (selectedAmPm.getValue().equals("PM")) {
                finalHour += 12;
            }
            // Add to db
            String error = DbHelper.createAppointment(
                    selectedServices.get(0).getServiceID(),
                    selectedCustomer.getValue().getCustomerID(),
                    LocalCache.getEmployee().getEmployeeID(),
                    selectedDate.get().atTime(finalHour, Integer.parseInt(selectedMinute.getValue()))
            );
            if (error == null) {
                // No error
                servicesPage.refresh();
            } else {
                // Show error
            }
        }
    }
}
