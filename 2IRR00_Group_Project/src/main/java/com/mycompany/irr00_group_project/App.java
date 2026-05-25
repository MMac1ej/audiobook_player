package com.mycompany.irr00_group_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Entry point for the Audiobook Player application. */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane(new Label("Audiobook Player"));
        stage.setScene(new Scene(root, 900, 650));
        stage.setTitle("Audiobook Player");
        stage.show();
    }
}
