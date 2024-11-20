package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.AppointmentStatus;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.ServiceAppointment;
import com.mycompany.mavenproject3.utils.Util;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public interface AppointmentsPage extends Refreshable {


    TableView<ServiceAppointment> getAppointmentsTableView();

    TableColumn<ServiceAppointment, String> getAppointmentDateColumn();

    TableColumn<ServiceAppointment, String> getAppointmentTimeColumn();

    TableColumn<ServiceAppointment, String> getAppointmentCustomerColumn();

    TableColumn<ServiceAppointment, String> getAppointmentServicesColumn();

    TableColumn<ServiceAppointment, String> getAppointmentCreatedColumn();

    TableColumn<ServiceAppointment, String> getAppointmentEmployeePositionColumn();

    Text getAppointmentsCountText();

    CheckBox getScheduledCheckBox();

    CheckBox getCompletedCheckBox();

    CheckBox getCanceledCheckBox();

    default void loadAppointments() {

        List<ServiceAppointment> appointments = DbHelper.getServiceAppointments(
                getScheduledCheckBox().isSelected(),
                getCompletedCheckBox().isSelected(),
                getCanceledCheckBox().isSelected()
        ); // Retrieve appointments
        System.out.println("Appointments: " + appointments.size());
        //
        ObservableList<ServiceAppointment> observableAppointments = Util.getObservableList(appointments);
        //
        getAppointmentDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        getAppointmentTimeColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        getAppointmentCustomerColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getFullName()));
        getAppointmentServicesColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getService().getServiceName()));
        getAppointmentCreatedColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getFullName()));
        getAppointmentEmployeePositionColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getPosition().getPositionName()));
        if (appointments.isEmpty()) {
            getAppointmentsCountText().setText("There are NO scheduled appointments");
        } else {
            getAppointmentsCountText().setText("There are {" + appointments.size() + "} scheduled appointments");
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

    private void createOptions(ObservableList<ServiceAppointment> observableAppointments) {
        // Create the context menu

        getAppointmentsTableView().setRowFactory(tv -> {
            TableRow<ServiceAppointment> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editOption = new MenuItem("Edit");
            MenuItem deleteOption = new MenuItem("Delete");
            contextMenu.getItems().addAll(editOption, deleteOption);


            MenuItem markAsCompletedOption = new MenuItem("Mark as Completed");

            // Show the context menu only for non-empty rows
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    // Check if the current service appointment is active
                    ServiceAppointment selectedItem = row.getItem();
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
                ServiceAppointment selectedItem = row.getItem();
                if (selectedItem != null) {
                    System.out.println("Edit selected: " + selectedItem.getService().getServiceName());
                } else {
                    System.out.println("No item selected for Edit");
                }
            });

            deleteOption.setOnAction(e -> {
                ServiceAppointment selectedItem = row.getItem();
                if (selectedItem != null) {
                    observableAppointments.remove(selectedItem); // Remove from observableAppointments
                    System.out.println("Deleted: " + selectedItem);
                } else {
                    System.out.println("No item selected for Delete");
                }
            });

            markAsCompletedOption.setOnAction(e -> {
                ServiceAppointment selectedItem = row.getItem();
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
