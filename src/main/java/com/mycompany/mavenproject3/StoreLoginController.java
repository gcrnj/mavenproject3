package com.mycompany.mavenproject3;

import com.mycompany.mavenproject3.dashboard.TellerDashboardControllerPage;
import com.mycompany.mavenproject3.models.DbHelper;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class StoreLoginController {

    
    private static Stage stage;

    @FXML
    private TextField usernameField; // Add @FXML annotation to match FXML
    @FXML
    private TextField passwordField; // Add @FXML annotation to match FXML
    @FXML
    private ToggleGroup role; // Ensure this matches your FXML ToggleGroup

    // This method can be linked to a button's onAction in the store_login.fxml
    public static void startNewScene() throws IOException {
        // Load the new FXML for the new window
        Parent root = FXMLLoader.load(StoreLoginController.class.getResource("store_login.fxml"));

        // Create a new Stage (window)
        stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Teller Dashboard");
        stage.show();  // Show the new window

        // Optionally, close the current window (if you don't want both open)
        //currentStage.close();
    }
    
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        RadioButton selectedRole = (RadioButton) role.getSelectedToggle();
        String userRole = selectedRole != null ? selectedRole.getText() : "";

        // Validate input fields
        if (username.isEmpty() || password.isEmpty()) {
            // Display an error message (you could implement a dialog here)
            System.out.println("Username and password must not be empty.");
            return;
        }

        // Validate user credentials
        if (login(username, password, userRole)) {
            // Assuming login is successful, proceed to the dashboard
            TellerDashboardControllerPage.startNewScene();
            stage.close();
        } else {
            // Handle login failure (you could implement a dialog here)
            System.out.println("Invalid username or password.");
        }
    }

    // The login method checks the credentials against the database
    private boolean login(String username, String password, String role) {
        // Implement your login logic here, possibly using PasswordUtils for password verification
        return DbHelper.login(username, password, role); // Adjust this to your actual service method
    }

}
