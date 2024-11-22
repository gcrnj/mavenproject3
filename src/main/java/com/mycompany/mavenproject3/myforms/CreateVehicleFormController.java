package com.mycompany.mavenproject3.myforms;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.mycompany.mavenproject3.dashboard.VehiclesPage;
import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Vehicle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateVehicleFormController {


    @FXML
    JFXTextField vehicleTextField;
    RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();

    private VehiclesPage vehiclesPage;
    private Vehicle vehicle;

    Stage stage;
    Alert errorAlert;

    public static void startNewScene(VehiclesPage vehiclesPage, Vehicle vehicle) {
        // Load the new FXML for the new window
        try {
            FXMLLoader loader = new FXMLLoader(CreateVehicleFormController.class.getResource("create_vehicle_form.fxml"));

            Parent root = loader.load();

            CreateVehicleFormController controller = loader.getController();
            controller.setVehicle(vehicle, vehiclesPage);

            // Create a new Stage (window)
            Stage stage = new Stage();
            controller.stage = stage;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Create Service Form");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);  // Make stage2 modal
            stage.show();  // Show the new window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // Validator
        String requiredErrorText = "This field is required";
        requiredFieldValidator.setMessage(requiredErrorText);
        vehicleTextField.getValidators().add(requiredFieldValidator);
    }

    public void setVehicle(Vehicle vehicle, VehiclesPage vehiclesPage) {
        this.vehicle = vehicle;
        this.vehiclesPage = vehiclesPage;
        if (vehicle != null) {
            vehicleTextField.setText(vehicle.getVehicleName());
        }
    }

    @FXML
    private void onCancelClick() {
        stage.close();
    }

    @FXML
    private void onSaveClick() {
        String error = "";
        if (vehicle != null) {
            // Edit
            error = DbHelper.editVehicleName(vehicle.getVehicleId(), vehicleTextField.getText());
        } else {
            // Create
            error = DbHelper.createVehicle(vehicleTextField.getText());
        }
        if (error == null) {
            vehiclesPage.refreshVehicles();
            stage.close();
        } else {
            showError(error);
        }
    }

    private void showError(String error) {
        if (errorAlert == null) {
            errorAlert = new Alert(Alert.AlertType.ERROR);
        }
        errorAlert.setContentText(error);
        errorAlert.showAndWait();
    }
}
