package com.mycompany.mavenproject3;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.PrimerDark;
import com.mycompany.mavenproject3.models.DbHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.stage.Modality;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        StoreLoginController.startNewScene();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        connectToDatabase();
    }
    
    private void connectToDatabase() {
        try {
            DbHelper.connect();
            System.out.print("DB Connection Success!");
        } catch (SQLException e) {
            Platform.runLater(() -> showErrorDialog("Database Connection Error", e.getMessage()));
        }
    }
    
    private void showErrorDialog(String title, String cause) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dialog.fxml"));
            Parent root = loader.load();

            // Get the controller and set the cause
            DialogController controller = loader.getController();
            controller.setCause(cause);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(stage); // Set the owner of the dialog
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait(); // Show the dialog and wait for it to close
        } catch (IOException e) {
            e.printStackTrace(); // Handle loading error
        }
    }
}