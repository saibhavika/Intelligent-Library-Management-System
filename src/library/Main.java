
package library;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        LibraryService service = new LibraryService();

        // Load saved library data
        try {
            FileManager.load(service);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data Loading Error");
            alert.setHeaderText("Could not load library data");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        // Open administrator login
        LoginController login = new LoginController();
        Stage loginStage = new Stage();

        boolean authenticated = login.showLogin(loginStage);

        if (!authenticated) {
            Platform.exit();
            return;
        }

        // Open main dashboard after successful login
        DashboardController dashboard =
                new DashboardController(service);

        Scene scene = new Scene(
                dashboard.createView(),
                1200,
                750
        );

        primaryStage.setTitle("Intelligent Library Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}