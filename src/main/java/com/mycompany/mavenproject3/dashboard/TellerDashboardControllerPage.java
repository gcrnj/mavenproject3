package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.StoreLoginController;
import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.*;
import com.mycompany.mavenproject3.myforms.CreateAppointmentFormController;
import com.mycompany.mavenproject3.myforms.CreateServiceFormController;

import java.io.IOException;
import java.util.Objects;

import com.mycompany.mavenproject3.myforms.CreateVehicleFormController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GNew
 */
public class TellerDashboardControllerPage implements ServicesPage, AppointmentsPage, VehiclesPage, Refreshable {

//    @FXML
//    public Text userName, userPosition, date;

    private Stage stage;

    @FXML
    public TableView<Appointment> appointmentsTableView;
    @FXML
    public TableColumn<Appointment, String> appointmentDateColumn, appointmentTimeColumn, appointmentCustomerColumn, appointmentServicesColumn, appointmentCreatedColumn, appointmentEmployeePositionColumn;

    @FXML
    DatePicker appointmentFromDatePicker, appointmentToDatePicker;

    @FXML
    public TableView<Service> serviceTableView;
    @FXML
    public TableColumn<Service, String> serviceNameColumn, servicePriceColumn, serviceWheelsColumn, serviceDescriptionColumn;
    @FXML
    public TableColumn<Service, String> isAvailableColumn;
    @FXML
    public Button serviceCreateButton;

    @FXML
    public TableView<Vehicle> vehiclesTableView;
    @FXML
    public TableColumn<Vehicle, String> vehicleNameColumn;
    @FXML
    public Button vehicleCreateButton;

    @FXML
    ImageView profileImageView;
    @FXML
    Text userNameText;

    @FXML
    public Text appointmentsCountText, servicesCountText;

    @FXML
    HBox appointmentStatusHBox;

    @FXML
    CheckBox scheduledCheckBox, completedCheckBox, canceledCheckBox;

    @Override
    public CheckBox getScheduledCheckBox() {
        return scheduledCheckBox;
    }

    @Override
    public CheckBox getCompletedCheckBox() {
        return completedCheckBox;
    }

    @Override
    public CheckBox getCanceledCheckBox() {
        return canceledCheckBox;
    }

    @Override
    public DatePicker getAppointmentsFromDatePicker() {
        return appointmentFromDatePicker;
    }

    @Override
    public DatePicker getAppointmentsToDatePicker() {
        return appointmentToDatePicker;
    }

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
    public void onAppointmentFrom() {
        loadAppointments();
    }

    @FXML
    public void onAppointmentTo() {
        loadAppointments();
    }

    @FXML
    public void initialize() {
        initUpperDetails();
        loadAppointments();
        loadServices();
        loadVehicles();
    }

    private void initUpperDetails() {
        userNameText.setText(LocalCache.getEmployee().getFirstName());

//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
//        date.setText(dateFormat.format(new Date()));
        Image image = new Image(getClass().getResource("/images/abc.png").toExternalForm());
        profileImageView.setImage(image);

        // Teller-only visibility
        boolean isTeller = LocalCache.isTeller();
        serviceCreateButton.setVisible(!isTeller);
        vehicleCreateButton.setVisible(!isTeller);
    }

    @FXML
    private void registerCustomer() {

    }

    @FXML
    private void openAppointmentForm() {
        CreateAppointmentFormController.startNewScene(this, null);
    }

    @FXML
    private void openServicesForm() {
        CreateServiceFormController.startNewScene(this, null);
    }

    @FXML
    private void openVehicleForm() {
        CreateVehicleFormController.startNewScene(this, null);
    }

    @Override
    public void editVehicle(Vehicle vehicle) {
        CreateVehicleFormController.startNewScene(this, vehicle);
    }

    @Override
    public TableView<Vehicle> getVehicleTableView() {
        return vehiclesTableView;
    }

    @Override
    public TableColumn<Vehicle, String> getVehicleNameColumn() {
        return vehicleNameColumn;

    }

    @Override
    public void refresh() {
        initialize();
    }

    @Override
    public void editService(Service service) {
        CreateServiceFormController.startNewScene(this, service);
    }

    @Override
    public void onEditAppointment(Appointment appointment) {
        CreateAppointmentFormController.startNewScene(this, appointment);
    }

    @FXML
    public void logout() {
        LocalCache.logout();
        try {
            StoreLoginController.startNewScene();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public TableColumn<Service, String> getIsAvailableColumn() {
        return isAvailableColumn;
    }

    @Override
    public Text getServicesCountText() {
        return servicesCountText;
    }

    // End for override in Services


    // Start for override in Appointments

    @Override
    public TableView<Appointment> getAppointmentsTableView() {
        return appointmentsTableView;
    }

    @Override
    public TableColumn<Appointment, String> getAppointmentDateColumn() {
        return appointmentDateColumn;
    }

    @Override
    public TableColumn<Appointment, String> getAppointmentTimeColumn() {
        return appointmentTimeColumn;
    }

    @Override
    public TableColumn<Appointment, String> getAppointmentCustomerColumn() {
        return appointmentCustomerColumn;
    }

    @Override
    public TableColumn<Appointment, String> getAppointmentServicesColumn() {
        return appointmentServicesColumn;
    }

    @Override
    public TableColumn<Appointment, String> getAppointmentCreatedColumn() {
        return appointmentCreatedColumn;
    }

    @Override
    public TableColumn<Appointment, String> getAppointmentEmployeePositionColumn() {
        return appointmentEmployeePositionColumn;
    }

    @Override
    public Text getAppointmentsCountText() {
        return appointmentsCountText;
    }
    // End for overrides in Appointments
}
