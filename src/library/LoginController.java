package library;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginController {

    private boolean authenticated = false;

    public boolean showLogin(Stage stage) {

        Label title = new Label("LIBRARY MANAGEMENT");
        title.setFont(Font.font("Arial", 25));
        title.setTextFill(Color.web("#163B65"));

        Label subtitle = new Label("ADMINISTRATOR LOGIN");
        subtitle.setTextFill(Color.GRAY);

        TextField username = new TextField();
        username.setPromptText("Enter username");
        username.setPrefHeight(42);

        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");
        password.setPrefHeight(42);

        Label message = new Label();
        message.setTextFill(Color.RED);

        Button login = new Button("LOGIN");
        login.setMaxWidth(Double.MAX_VALUE);
        login.setPrefHeight(42);
        login.setStyle(
            "-fx-background-color: #1769AA;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;"
        );

        login.setOnAction(e -> {
            if (username.getText().equals("admin") &&
                password.getText().equals("admin123")) {

                authenticated = true;
                stage.close();

            } else {
                message.setText("Invalid username or password!");
            }
        });

        VBox card = new VBox(15,
            title,
            subtitle,
            new Label("Username"),
            username,
            new Label("Password"),
            password,
            message,
            login
        );

        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(35));
        card.setMaxWidth(380);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, #888888, 15, 0, 0, 5);"
        );

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #EAF2F8;");

        Scene scene = new Scene(root, 650, 500);

        stage.setTitle("Library Admin Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();

        return authenticated;
    }
}