package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.models.ServiceAppointment;
import com.mycompany.mavenproject3.myforms.CreateAppointmentFormController;
import com.mycompany.mavenproject3.myforms.CreateServiceFormController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GNew
 */
public class TellerDashboardControllerPage implements ServicesPage, AppointmentsPage, Refreshable  {

//    @FXML
//    public Text userName, userPosition, date;

    private Stage stage;

    @FXML
    public TableView<ServiceAppointment> appointmentsTableView;
    @FXML
    public TableColumn<ServiceAppointment, String> appointmentDateColumn, appointmentTimeColumn, appointmentCustomerColumn, appointmentServicesColumn, appointmentCreatedColumn, appointmentEmployeePositionColumn;

    @FXML
    public TableView<Service> serviceTableView;
    @FXML
    public TableColumn<Service, String> serviceNameColumn, servicePriceColumn, serviceWheelsColumn, serviceDescriptionColumn;
    @FXML
    public TableColumn<Service, Boolean> isAvailableColumn;

    @FXML
    public Text appointmentsCountText, servicesCountText;

    public static void startNewScene() throws IOException {
        // Load the new FXML for the new window
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(TellerDashboardControllerPage.class.getResource("teller_dashboard_1.fxml")));
        Parent root = loader.load();
        // Create a new Stage (window)
        TellerDashboardControllerPage controller = loader.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Teller Dashboard");
        stage.setResizable(false);
        stage.show();  // Show the new window
        controller.stage = stage;
    }

    @FXML
    public void initialize() {
        initUpperDetails();
        loadAppointments();
        loadServices();
    }

    private void initUpperDetails() {
//        userName.setText(LocalCache.getEmployee().getFirstName());
//        userPosition.setText(LocalCache.getEmployee().getPosition().getPositionName());

//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
//        date.setText(dateFormat.format(new Date()));

    }

    @FXML
    private void registerCustomer() {

    }

    @FXML
    private void openAppointmentForm() {
        CreateAppointmentFormController.startNewScene(this);
    }

    @FXML
    private void openServicesForm() {
        CreateServiceFormController.startNewScene(this);
    }

    @Override
    public void refresh() {
        initialize();
    }

    @FXML
    public void logout() {

    }

    // Start for override in Services
    @Override
    public TableView<Service> getServiceTableView() {
        return serviceTableView;
    }

    @Override
    public TableColumn<Service, String> getServiceNameColumn() {
        return serviceNameColumn;
    }

    @Override
    public TableColumn<Service, String> getServicePriceColumn() {
        return servicePriceColumn;
    }

    @Override
    public TableColumn<Service, String> getServiceWheelsColumn() {
        return serviceWheelsColumn;
    }

    @Override
    public TableColumn<Service, String> getServiceDescriptionColumn() {
        return serviceDescriptionColumn;
    }

    @Override
    public TableColumn<Service, Boolean> getIsAvailableColumn() {
        return isAvailableColumn;
    }

    @Override
    public Text getServicesCountText() {
        return servicesCountText;
    }

    // End for override in Services


    // Start for override in Appointments

    @Override
    public TableView<ServiceAppointment> getAppointmentsTableView() {
        return appointmentsTableView;
    }

    @Override
    public TableColumn<ServiceAppointment, String> getAppointmentDateColumn() {
        return appointmentDateColumn;
    }

    @Override
    public TableColumn<ServiceAppointment, String> getAppointmentTimeColumn() {
        return appointmentTimeColumn;
    }

    @Override
    public TableColumn<ServiceAppointment, String> getAppointmentCustomerColumn() {
        return appointmentCustomerColumn;
    }

    @Override
    public TableColumn<ServiceAppointment, String> getAppointmentServicesColumn() {
        return appointmentServicesColumn;
    }

    @Override
    public TableColumn<ServiceAppointment, String> getAppointmentCreatedColumn() {
        return appointmentCreatedColumn;
    }

    @Override
    public TableColumn<ServiceAppointment, String> getAppointmentEmployeePositionColumn() {
        return appointmentEmployeePositionColumn;
    }

    @Override
    public Text getAppointmentsCountText() {
        return appointmentsCountText;
    }
    // End for overrides in Appointments
}