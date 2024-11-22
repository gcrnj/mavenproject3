package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.AppointmentStatus;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Appointment;
import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.utils.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public interface AppointmentsPage extends Refreshable {


    TableView<Appointment> getAppointmentsTableView();

    TableColumn<Appointment, String> getAppointmentDateColumn();

    TableColumn<Appointment, String> getAppointmentTimeColumn();

    TableColumn<Appointment, String> getAppointmentCustomerColumn();

    TableColumn<Appointment, String> getAppointmentServicesColumn();

    TableColumn<Appointment, String> getAppointmentCreatedColumn();

    TableColumn<Appointment, String> getAppointmentEmployeePositionColumn();

    Text getAppointmentsCountText();

    CheckBox getScheduledCheckBox();

    CheckBox getCompletedCheckBox();

    CheckBox getCanceledCheckBox();

    default void loadAppointments() {

        List<Appointment> appointments = DbHelper.getAppointments(
                getScheduledCheckBox().isSelected(),
                getCompletedCheckBox().isSelected(),
                getCanceledCheckBox().isSelected()
        ); // Retrieve appointments
        System.out.println("Appointments: " + appointments.size());
        //
        ObservableList<Appointment> observableAppointments = Util.getObservableList(appointments);
        //
        getAppointmentDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        getAppointmentTimeColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        getAppointmentCustomerColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getFullName()));
        getAppointmentServicesColumn().setCellValueFactory(cellData -> {
            // Get the list of services for the current appointment
            List<Service> services = cellData.getValue().getServices(); // Assuming getServices() returns the list of services

            // Join the service names with a newline separator
            String serviceNames = services.stream()
                    .map(Service::getServiceName) // Extract service names
                    .collect(Collectors.joining(", ")); // Join with "\n"

            return new SimpleStringProperty(serviceNames); // Return the joined string as a SimpleStringProperty
        });
        getAppointmentCreatedColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getFullName()));
        getAppointmentEmployeePositionColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getPosition().getPositionName()));
        if (appointments.isEmpty()) {
            getAppointmentsCountText().setText("There are NO scheduled appointments");
        } else {
            getAppointmentsCountText().setText("There are " + appointments.size() + " scheduled appointments");
        }
        getAppointmentsTableView().setItems(observableAppointments);
        createOptions(observableAppointments); // Pass observableAppointments to createOptions
        listeners();
    }

    private void listeners() {
        getScheduledCheckBox().setOnAction(event -> {
            loadAppointments();
        });
        getCompletedCheckBox().setOnAction(event -> {
            loadAppointments();
        });
        getCanceledCheckBox().setOnAction(event -> {
            loadAppointments();
        });
    }

    private void createOptions(ObservableList<Appointment> observableAppointments) {
        // Create the context menu

        getAppointmentsTableView().setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editOption = new MenuItem("Edit");
            MenuItem deleteOption = new MenuItem("Delete");
            contextMenu.getItems().addAll(editOption, deleteOption);


            MenuItem markAsCompletedOption = new MenuItem("Mark as Completed");

            // Show the context menu only for non-empty rows
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    // Check if the current service appointment is active
                    Appointment selectedItem = row.getItem();
                    if (selectedItem != null && selectedItem.getStatus() == AppointmentStatus.SCHEDULED) { // Assuming isActive() is a method in ServiceAppointment
                        contextMenu.getItems().add(markAsCompletedOption);
                    } else {
                        // Ensure the "Mark as Completed" option is not shown if the appointment is not active
                        contextMenu.getItems().remove(markAsCompletedOption);
                    }
                    row.getContextMenu().show(row, event.getScreenX(), event.getScreenY());
                }
            });

            // Handle menu item actions
            editOption.setOnAction(e -> {
                Appointment selectedItem = row.getItem();
                if (selectedItem != null) {
                    System.out.println("Edit selected: " + selectedItem.getServices().get(0).getServiceName());
                } else {
                    System.out.println("No item selected for Edit");
                }
            });

            deleteOption.setOnAction(e -> {
                Appointment selectedItem = row.getItem();
                if (selectedItem != null) {
                    observableAppointments.remove(selectedItem); // Remove from observableAppointments
                    System.out.println("Deleted: " + selectedItem);
                } else {
                    System.out.println("No item selected for Delete");
                }
            });

            markAsCompletedOption.setOnAction(e -> {
                Appointment selectedItem = row.getItem();
                if (selectedItem != null) {
                    // Update the status of the appointment to completed
                    System.out.println("Marked as completed: " + selectedItem);
                } else {
                    System.out.println("No item selected for Mark as Completed");
                }
            });

            // Attach the context menu to the row only when an item is set
            row.itemProperty().addListener((obs, previousItem, currentItem) -> {
                if (currentItem != null) {
                    row.setContextMenu(contextMenu);
                } else {
                    row.setContextMenu(null);
                }
            });

            return row;
        });
    }

    @Override
    default void refresh() {
        loadAppointments();
    }
}
