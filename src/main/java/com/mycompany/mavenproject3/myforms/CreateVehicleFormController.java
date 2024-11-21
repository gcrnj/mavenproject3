package com.mycompany.mavenproject3.myforms;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateVehicleFormController {


    @FXML
    JFXTextField vehicleTextField;
    RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();

    Refreshable refreshable;


    Stage stage;

    public static void startNewScene(Refreshable refreshable) {
        // Load the new FXML for the new window
        try {
            FXMLLoader loader = new FXMLLoader(CreateVehicleFormController.class.getResource("create_vehicle_form.fxml"));

            Parent root = loader.load();

            CreateVehicleFormController controller = loader.getController();
            controller.refreshable = refreshable;

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

    @FXML
    private void onCancelClick() {

    }

    @FXML
    private void onSaveClick() {
        String error = DbHelper.createVehicle(vehicleTextField.getText());
        if(error != null) {
            refreshable.refresh();
            stage.close();
        } else {

        }
    }
}
