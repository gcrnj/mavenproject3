/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.mavenproject3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author GNew
 */
public class DialogController {

    
    @FXML
    private TextArea causeText; // Use this to set the cause of the error
    @FXML
    private Button okayButton; // Reference to the Okay button

    // Method to set the cause of the error
    public void setCause(String cause) {
        causeText.setText(cause);
    }

    // Method to close the dialog
    @FXML
    private void handleOkayButton() {
        ((Stage) okayButton.getScene().getWindow()).close(); // Close the dialog
    }
    
}
