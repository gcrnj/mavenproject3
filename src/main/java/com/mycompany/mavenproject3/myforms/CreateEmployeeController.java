package com.mycompany.mavenproject3.myforms;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;
import com.jfoenix.validation.RequiredFieldValidator;

import com.mycompany.mavenproject3.models.*;
import com.mycompany.mavenproject3.utils.TextFormatterUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateEmployeeController {

    Stage stage;

    @FXML
    JFXTextField firstNameTextField, middleNameTextField, lastNameTextField, mobileNumberTextField, emailAddressTextField,
            houseTextField, streetTextField, buildingTextField, usernameTextField, passwordTextField;

    @FXML
    JFXComboBox<Barangay> barangayComboBox;
    @FXML
    JFXComboBox<Municipality> municipalityComboBox;
    @FXML
    JFXComboBox<Province> provinceComboBox;
    @FXML
    JFXComboBox<Position> positionComboBox;

    @FXML
    Text cityBarangayErrorText, positionErrorText;

    List<JFXTextField> requiredTextFields;

    Alert successAlert, cancelAlert, errorAlert;

    @FXML
    private void initialize() {
        requiredTextFields = Arrays.asList(
                firstNameTextField,
                lastNameTextField,
                mobileNumberTextField,
                emailAddressTextField,
                houseTextField,
                streetTextField,
                usernameTextField,
                passwordTextField
        );
        populateProvinceComboBox();
        populatePositionComboBox();
        addValidators();
        addFormatters();
    }

    // General function to populate any ComboBox
    private <T> void populateComboBox(ComboBox<T> comboBox, List<T> items, Function<T, String> function) {
        // Clear existing items in the ComboBox
        comboBox.getItems().clear();

        // Add all the items to the ComboBox
        comboBox.getItems().addAll(items);

        // Set the StringConverter for displaying only the necessary field (e.g., name)
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T type) {
                return type != null ? function.apply(type) : "";
            }

            @Override
            public T fromString(String string) {
                // You can return null or a default value, this is generally not used for selection.
                return null;
            }
        });
    }

    private void populatePositionComboBox() {
        List<Position> positions = DbHelper.getPositions();
        populateComboBox(positionComboBox, positions, Position::getPositionName);
    }

    private void populateProvinceComboBox() {
        // Get the list of provinces
        List<Province> provinces = DbHelper.getProvinces();
        populateComboBox(provinceComboBox, provinces, Province::getProvinceName);
    }

    private void populateMunicipalityComboBox(int provinceId) {
        // Get the list of provinces
        List<Municipality> municipalities = DbHelper.getMunicipalities(provinceId);
        populateComboBox(municipalityComboBox, municipalities, Municipality::getMunicipalityName);
    }

    private void populateBarangayComboBox(int municipalityID) {
        // Get the list of provinces
        List<Barangay> barangays = DbHelper.getBarangays(municipalityID);
        populateComboBox(barangayComboBox, barangays, Barangay::getBrgyName);
    }

    @FXML
    private void selectProvince() {
        Province selectedProvince = provinceComboBox.getSelectionModel().getSelectedItem();
        if (selectedProvince != null) {
            // You now have access to the full Province object
            System.out.println("Selected Province ID: " + selectedProvince.getProvinceID());
            System.out.println("Selected Province Name: " + selectedProvince.getProvinceName());
            populateMunicipalityComboBox(selectedProvince.getProvinceID());
        } else {
            municipalityComboBox.getItems().clear();
            barangayComboBox.getItems().clear();
        }
    }

    @FXML
    private void selectMunicipality() {
        Municipality selectedMunicipality = municipalityComboBox.getSelectionModel().getSelectedItem();
        if (selectedMunicipality != null) {
            // You now have access to the full Province object
            System.out.println("Selected Municipality ID: " + selectedMunicipality.getMunicipalityID());
            System.out.println("Selected Municipality Name: " + selectedMunicipality.getMunicipalityName());
            populateBarangayComboBox(selectedMunicipality.getMunicipalityID());
        } else {
            barangayComboBox.getItems().clear();
        }
    }

    public static void startNewScene() {
        // Load the new FXML for the new window
        try {
            FXMLLoader loader = new FXMLLoader(CreateEmployeeController.class.getResource("create_employee_form.fxml"));
            Parent root = loader.load();
            CreateEmployeeController controller = loader.getController();
            // Create a new Stage (window)
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Create Employee Form");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);  // Make stage2 modal

            System.out.println("Hey");
            stage.show();  // Show the new window
            controller.stage = stage;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveButtonClick() {
        // Employee Details and Contact
        String employeeFirstName = firstNameTextField.getText();
        String employeeLastName = lastNameTextField.getText();
        String employeeMiddleName = middleNameTextField.getText();
        String employeeMobileNumber = mobileNumberTextField.getText();
        String employeeEmailAddress = emailAddressTextField.getText();
        // Employee Address
        String employeeHouse = houseTextField.getText();
        String employeeStreet = streetTextField.getText();
        String employeeBuilding = buildingTextField.getText();

        Province province = provinceComboBox.getValue();
        Municipality municipality = municipalityComboBox.getValue();
        Barangay barangay = barangayComboBox.getValue();

        Position position = positionComboBox.getValue();

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        boolean isFormValid = true;
        // Validate all text fields
        for (JFXTextField textField : requiredTextFields) {
            if (!textField.validate()) {
                isFormValid = false;
            }
        }

        // Validate all combobox
        cityBarangayErrorText.setText("");
        if (province == null && municipality == null && barangay == null) {
            cityBarangayErrorText.setText("Province, Municipality and Barangay are required");
            isFormValid = false;
        } else if (province == null && municipality == null) {
            cityBarangayErrorText.setText("Province and Municipality are required");
        } else if (province == null && barangay == null) {
            cityBarangayErrorText.setText("Province and Barangay are required");
        } else if (municipality == null && barangay == null) {
            cityBarangayErrorText.setText("Municipality and Barangay are required");
            isFormValid = false;
        } else if (province == null) {
            cityBarangayErrorText.setText("Province is required");
            isFormValid = false;
        } else if (municipality == null) {
            cityBarangayErrorText.setText("Municipality is required");
            isFormValid = false;
        } else if (barangay == null) {
            cityBarangayErrorText.setText("Barangay is required");
            isFormValid = false;
        }

        if (position == null) {
            // No position
            isFormValid = false;
        }
        positionErrorText.setVisible(position == null);


        // Add to database if valid
        if (isFormValid) {

            String createEmployeeError = DbHelper.createEmployee(
                    employeeFirstName, employeeMiddleName, employeeLastName,
                    employeeMobileNumber, employeeEmailAddress,
                    barangay, employeeHouse, employeeStreet, employeeBuilding,
                    position.getPositionID(),
                    username, password
            );
            if (createEmployeeError == null) {
                // Success
                showSuccessDialog();

            } else {
                // Failed
                showErrorAlert(createEmployeeError);
            }
        }

    }

    private void showSuccessDialog() {
        if (successAlert == null) {
            successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("All fields are valid and the data has been saved.");

            // Optional: You can set an icon or other properties on the DialogPane if needed
            DialogPane dialogPane = successAlert.getDialogPane();
            dialogPane.setStyle("-fx-font-size: 14px;");

        }
        successAlert.showAndWait();
        stage.close();
    }

    private void showErrorAlert(String errorMessage) {
        if (errorAlert == null) {
            errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("An error occurred");
        }
        errorAlert.setContentText(errorMessage);

        // Optional: You can set an icon or other properties on the DialogPane if needed
        DialogPane dialogPane = errorAlert.getDialogPane();
        dialogPane.setStyle("-fx-font-size: 14px;");

        errorAlert.showAndWait();
    }

    private void addValidators() {
        String requiredErrorText = "This field is required";
        for (JFXTextField textField : requiredTextFields) {
            RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
            requiredFieldValidator.setMessage(requiredErrorText);
            textField.getValidators().add(requiredFieldValidator);
        }

        // Minimum length validator
        ValidatorBase min3LengthValidator = new ValidatorBase() {
            @Override
            protected void eval() {
                TextInputControl control = (TextInputControl) srcControl.get();
                hasErrors.set(control.getText() == null || control.getText().length() < 3);
            }
        };
        min3LengthValidator.setMessage("Minimum of 3 characters");
        usernameTextField.getValidators().add(min3LengthValidator);
        passwordTextField.getValidators().add(min3LengthValidator);

        // Email validator
        ValidatorBase emailValidator = new ValidatorBase() {
            Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            @Override
            protected void eval() {
                TextInputControl control = (TextInputControl) srcControl.get();
                String text = control.getText();

                Matcher matcher = pattern.matcher(text);

                hasErrors.set(!matcher.matches());
            }
        };
        emailValidator.setMessage("Invalid email format");
        emailAddressTextField.getValidators().add(emailValidator);

        // Mobile number validator
        ValidatorBase mobileNumberValidator = new ValidatorBase() {
            @Override
            protected void eval() {
                TextInputControl control = (TextInputControl) srcControl.get();
                String text = control.getText();
                boolean isError = false;
                if (text == null || !text.startsWith("09")) {
                    isError = true;
                    setMessage("Required prefix: 09");
                } else if (text.length() != 11) {
                    isError = true;
                    setMessage("Required length: 11");
                }
                hasErrors.set(isError);
            }
        };
        mobileNumberTextField.getValidators().add(mobileNumberValidator);
    }

    private void addFormatters() {
        mobileNumberTextField.setTextFormatter(TextFormatterUtil.intTextFormatter());
    }

    @FXML
    private void cancelButtonClick() {
        cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("Cancel");
        cancelAlert.setHeaderText("Are you sure you want to cancel?");
        cancelAlert.setContentText("Any unsaved data will be lost.");

        // Show confirmation dialog and wait for user response
        Optional<ButtonType> result = cancelAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Proceed with cancel operation (e.g., reset fields or close the form)
            stage.close();
            System.out.println("User confirmed cancellation");
        } else {
            cancelAlert.close();
            System.out.println("User canceled cancel operation");
        }
    }

//    public CreateEmployeeController() {
//
//    }
}
