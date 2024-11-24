package com.mycompany.mavenproject3;

import com.mycompany.mavenproject3.models.DbHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        StoreLoginController.startNewScene(); // Starts the login process
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        connectToDatabase(); // Initialize the database connection
    }

    private void connectToDatabase() {
        try {
            DbHelper.connect();
            System.out.println("DB Connection Success!");
        } catch (SQLException e) {
            Platform.runLater(() -> showErrorDialog("Database Connection Error", e.getMessage()));
        }
    }

    private void showErrorDialog(String title, String cause) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dialog.fxml"));
            Parent root = loader.load();

            // Set the cause in the dialog
            DialogController controller = loader.getController();
            controller.setCause(cause);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
