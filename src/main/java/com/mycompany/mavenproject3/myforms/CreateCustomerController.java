package com.mycompany.mavenproject3.myforms;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.mycompany.mavenproject3.models.Barangay;
import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Municipality;
import com.mycompany.mavenproject3.models.Province;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class CreateCustomerController {

    @FXML
    JFXTextField firstNameTextField, lastNameTextField, mobileNumberTextField, emailAddressTextField,
            houseTextField, streetTextField, buildingTextField;

    @FXML
    JFXComboBox<Barangay> barangayComboBox;
    @FXML
    JFXComboBox<Municipality> municipalityComboBox;
    @FXML
    JFXComboBox<Province> provinceComboBox;

    @FXML
    Text cityBarangayErrorText;

    List<JFXTextField> textFields;

    Alert successAlert, cancelAlert, errorAlert;

    @FXML
    private void initialize() {
        textFields = Arrays.asList(
                firstNameTextField,
                lastNameTextField,
                mobileNumberTextField,
                emailAddressTextField,
                houseTextField,
                streetTextField,
                buildingTextField
        );
        populateProvinceComboBox();
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

    private void populateProvinceComboBox() {
        // Get the list of provinces
        List<Province> provinces = DbHelper.getProvinces();
        populateComboBox(provinceComboBox, provinces, province -> province.getProvinceName());
    }

    private void populateMunicipalityComboBox(int provinceId) {
        // Get the list of provinces
        List<Municipality> municipalities = DbHelper.getMunicipalities(provinceId);
        populateComboBox(municipalityComboBox, municipalities, municipality -> municipality.getMunicipalityName());
    }

    private void populateBarangayComboBox(int municipalityID) {
        // Get the list of provinces
        List<Barangay> barangays = DbHelper.getBarangays(municipalityID);
        populateComboBox(barangayComboBox, barangays, barangay -> barangay.getBrgyName());
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(CreateCustomerController.class.getResource("create_customer_form.fxml")));
            // Create a new Stage (window)
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Create Customer Form");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);  // Make stage2 modal

            System.out.println("Hey");
            stage.show();  // Show the new window
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveButtonClick() {
        // Customer Details and Contact
        String customerFirstName = firstNameTextField.getText();
        String customerLastName = lastNameTextField.getText();
        String customerMobileNumber = mobileNumberTextField.getText();
        String customerEmailAddress = emailAddressTextField.getText();
        // Customer Address
        String customerHouse = houseTextField.getText();
        String customerStreet = streetTextField.getText();
        String customerBuilding = buildingTextField.getText();

        Province province = provinceComboBox.getValue();
        Municipality municipality = municipalityComboBox.getValue();
        Barangay barangay = barangayComboBox.getValue();


        // Validators
        addValidators();

        boolean isFormValid = true;
        // Validate all text fields
        for (JFXTextField textField : textFields) {
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

        // Add to database if valid
        if (isFormValid) {
            String createCustomerError = DbHelper.createCustomer(
                    customerFirstName, customerLastName,
                    customerMobileNumber, customerEmailAddress,
                    barangay, customerHouse, customerStreet, customerBuilding
            );
            if (createCustomerError == null) {
                // Success
                showSuccessDialog();

            } else {
                // Failed
                showErrorAlert(createCustomerError);
            }
        }

    }

    private void showSuccessDialog() {
        if(successAlert == null) {
            successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("All fields are valid and the data has been saved.");

            // Optional: You can set an icon or other properties on the DialogPane if needed
            DialogPane dialogPane = successAlert.getDialogPane();
            dialogPane.setStyle("-fx-font-size: 14px;");

        }
        successAlert.showAndWait();
        // Todo handle ok by finishing screen
        // Todo refresh previous screen - add refreshable
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
        for (JFXTextField textField : textFields) {
            RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
            requiredFieldValidator.setMessage(requiredErrorText);
            textField.getValidators().add(requiredFieldValidator);
        }
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
            // Todo handle ok
            System.out.println("User confirmed cancellation");
        } else {
            // User canceled the cancellation
            // Todo handle cancel
            System.out.println("User canceled cancel operation");
        }
    }

//    public CreateCustomerController() {
//
//    }
}
