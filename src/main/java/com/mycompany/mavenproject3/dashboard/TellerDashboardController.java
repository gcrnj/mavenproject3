package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.LocalCache;
import com.mycompany.mavenproject3.models.Service;
import com.mycompany.mavenproject3.models.ServiceAppointment;
import com.mycompany.mavenproject3.myforms.CreateAppointmentFormController;
import com.mycompany.mavenproject3.myforms.CreateServiceFormController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GNew
 */
public class TellerDashboardController implements Refreshable {

//    @FXML
//    public Text userName, userPosition, date;

    private static Stage stage;

    @FXML
    public TableView<ServiceAppointment> appointmentsTableView;
    @FXML
    public TableColumn<ServiceAppointment, String> appointmentDateColumn, appointmentTimeColumn, appointmentCustomerColumn, appointmentServicesColumn, appointmentCreatedColumn, appointmentEmployeePositionColumn;
    @FXML
    public Text appointmentsCountText;

    public static void startNewScene() throws IOException {
        // Load the new FXML for the new window
        Parent root = FXMLLoader.load(Objects.requireNonNull(TellerDashboardController.class.getResource("teller_dashboard_1.fxml")));
        // Create a new Stage (window)
        stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Teller Dashboard");
        stage.setResizable(false);
        stage.show();  // Show the new window
    }

    @FXML
    public void initialize() {
        initUpperDetails();
        initAppointments();
        initServices();
    }

    private void initUpperDetails() {
//        userName.setText(LocalCache.getEmployee().getFirstName());
//        userPosition.setText(LocalCache.getEmployee().getPosition().getPositionName());

//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
//        date.setText(dateFormat.format(new Date()));

    }

    private void initAppointments() {
        ObservableList<ServiceAppointment> observableAppointments = FXCollections.observableArrayList();

        List<ServiceAppointment> appointments = DbHelper.getServiceAppointments(); // Retrieve appointments
        System.out.println("Appointments: " + appointments.size());
        //
        observableAppointments.clear();
        observableAppointments.addAll(appointments);
        appointmentsTableView.setItems(observableAppointments);
        //
        appointmentDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentDateTime()));
        appointmentTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentDateTime()));
        appointmentCustomerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getFullName()));
        appointmentServicesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiceAppointmentID() + ""));
        appointmentCreatedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getFullName()));
        appointmentEmployeePositionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getPosition().getPositionName()));
        if (appointments.isEmpty()) {
            appointmentsCountText.setText("There are NO scheduled appointments");
        } else {
            appointmentsCountText.setText("There are {" + appointments.size() + "} scheduled appointments");
        }

    }

    private void initServices() {
        List<Service> services = DbHelper.getServices(); // Retrieve appointments

    }

    @FXML
    private void registerCustomer() {

    }

    @FXML
    private void openAppointmentForm() {
        CreateAppointmentFormController.startNewScene();
    }

    @FXML
    private void openServicesForm() {
        CreateServiceFormController.startNewScene(this);
        stage.setOpacity(0.5);
        stage.setIconified(true);

    }

    @Override
    public void refresh() {
        initialize();
    }

    @FXML
    public void logout() {
        
    }

}
