package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.ServiceAppointment;
import com.mycompany.mavenproject3.utils.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.List;

public interface AppointmentsPage extends Refreshable {



    TableView<ServiceAppointment> getAppointmentsTableView();

    TableColumn<ServiceAppointment, String> getAppointmentDateColumn();

    TableColumn<ServiceAppointment, String> getAppointmentTimeColumn();

    TableColumn<ServiceAppointment, String> getAppointmentCustomerColumn();

    TableColumn<ServiceAppointment, String> getAppointmentServicesColumn();

    TableColumn<ServiceAppointment, String> getAppointmentCreatedColumn();

    TableColumn<ServiceAppointment, String> getAppointmentEmployeePositionColumn();

    Text getAppointmentsCountText();


    default void loadAppointments() {

        List<ServiceAppointment> appointments = DbHelper.getServiceAppointments(); // Retrieve appointments
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
        createOptions();

    }

    private void createOptions() {

        // Create the context menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editOption = new MenuItem("Edit");
        MenuItem deleteOption = new MenuItem("Delete");
        contextMenu.getItems().addAll(editOption, deleteOption);
        getAppointmentsTableView().setRowFactory(tv -> {
            TableRow<ServiceAppointment> row = new TableRow<>();

            // Show the context menu only for non-empty rows
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    row.getContextMenu().show(row, event.getScreenX(), event.getScreenY());
                }
            });

            // Attach the context menu to the row
            row.setContextMenu(contextMenu);

            // Handle menu item actions
            editOption.setOnAction(e -> {
                String selectedItem = row.getItem().getService().getServiceName();
                System.out.println("Edit selected: " + selectedItem);
            });

            deleteOption.setOnAction(e -> {
                ServiceAppointment selectedItem = row.getItem();
                getAppointmentsTableView().getItems().remove(selectedItem);
                System.out.println("Deleted: " + selectedItem);
            });

            return row;
        });
    }

    @Override
    default void refresh() {

    }
}
