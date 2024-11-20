/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3.myforms;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.utils.TextFormatterUtil;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GNew
 */
public class CreateServiceFormController {

    private static Stage stage;
    private Alert successAlert, cancelAlert;
    private Refreshable refreshable;
    @FXML
    public TextField serviceNameTextField, priceTextField, wheelsAvailableTextField;

    @FXML
    public TextArea descriptionTextArea;

    @FXML
    public CheckBox isAvailableCheckBox;

    @FXML
    public Text serviceNameErrorText, priceErrorText, dbErrorText, wheelsCountErrorText;

    public static void startNewScene(Refreshable refreshable) {
        // Load the new FXML for the new window
        try {
            FXMLLoader loader = new FXMLLoader(CreateServiceFormController.class.getResource("create_service_form.fxml"));

            Parent root = loader.load();

            CreateServiceFormController controller = loader.getController();
            controller.setRefreshable(refreshable);

            // Create a new Stage (window)
            stage = new Stage();
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
    public void initialize() {
        System.out.println("here");
        priceTextField.setTextFormatter(TextFormatterUtil.doubleTextFormatter());
        wheelsAvailableTextField.setTextFormatter(TextFormatterUtil.intTextFormatter());
    }

    public void setRefreshable(Refreshable refreshable) {
        this.refreshable = refreshable;
    }

    @FXML
    public void onOkayClick() {
        dbErrorText.setVisible(false);
        String regex = "^\\d+(,\\d+)*$";
        Pattern pattern = Pattern.compile(regex);

        String wheelsCountInput = wheelsAvailableTextField.getText();
        boolean isServiceNameError = serviceNameTextField.getText().isBlank();
        boolean isPriceError = priceTextField.getText().isBlank();
        boolean isWheelsCountError = !wheelsCountInput.isBlank() && Integer.parseInt(wheelsAvailableTextField.getText()) <= 0;

        // Reset all error texts to hidden initially
        serviceNameErrorText.setVisible(false);
        priceErrorText.setVisible(false);
        wheelsCountErrorText.setVisible(false);

        if (isServiceNameError && isPriceError && isWheelsCountError) {
            serviceNameErrorText.setVisible(true);
            priceErrorText.setVisible(true);
            wheelsCountErrorText.setVisible(true);
        } else if (isServiceNameError && isPriceError) {
            serviceNameErrorText.setVisible(true);
            priceErrorText.setVisible(true);
        } else if (isServiceNameError && isWheelsCountError) {
            serviceNameErrorText.setVisible(true);
            wheelsCountErrorText.setVisible(true);
        } else if (isPriceError && isWheelsCountError) {
            priceErrorText.setVisible(true);
            wheelsCountErrorText.setVisible(true);
        } else if (isServiceNameError) {
            serviceNameErrorText.setVisible(true);
        } else if (isPriceError) {
            priceErrorText.setVisible(true);
        } else if (isWheelsCountError) {
            wheelsCountErrorText.setVisible(true);
        } else {
            serviceNameErrorText.setVisible(false);
            priceErrorText.setVisible(false);
            wheelsCountErrorText.setVisible(false);
            // Everything is handled
            // Add to database
            String error = DbHelper.createService(
                    serviceNameTextField.getText(),
                    Double.parseDouble(priceTextField.getText()),
                    descriptionTextArea.getText(),
                    wheelsAvailableTextField.getText(),
                    isAvailableCheckBox.isSelected()
            );
            if (error != null) {
                dbErrorText.setVisible(true);
                dbErrorText.setText(error);
            } else {
                dbErrorText.setVisible(false);
                if (successAlert == null) {
                    successAlert = new Alert(AlertType.INFORMATION);
                }
                refreshable.refresh();
                successAlert.setTitle("Service added");
                successAlert.setHeaderText("Success!");
                successAlert.setContentText("Service " + serviceNameTextField.getText() + " has been added successfully!");
                // Show the dialog and wait for the user's response
                ButtonType result = successAlert.showAndWait().orElse(ButtonType.CLOSE);

                if (result == ButtonType.OK) {
                    // User clicked OK, so close the window
                    stage.close();// Close the stage (window)
                }
            }

        }

    }

    @FXML
    public void onCancelClick() {
        // Create a confirmation alert

        if (!serviceNameTextField.getText().isBlank()
                || !priceTextField.getText().isBlank()
                || !wheelsAvailableTextField.getText().isBlank()
                || !descriptionTextArea.getText().isBlank()
                || !isAvailableCheckBox.isSelected()) {
            if (cancelAlert == null) {
                cancelAlert = new Alert(AlertType.CONFIRMATION);
            }
            cancelAlert.setTitle("Confirm Cancellation");
            cancelAlert.setHeaderText("Are you sure you want to close?");
            cancelAlert.setContentText("Any unsaved changes will be lost.");

            // Show the dialog and wait for the user's response
            ButtonType result = cancelAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // User clicked OK, so close the window
                stage.close();// Close the stage (window)
            } else {
                // User clicked Cancel, so do nothing
                // Optionally, you can log or do something else here
            }
        } else {
            stage.close();
        }
    }

}
