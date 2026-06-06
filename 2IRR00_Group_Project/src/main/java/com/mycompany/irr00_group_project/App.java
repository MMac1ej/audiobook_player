package com.mycompany.irr00_group_project;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Entry point for the Audiobook Player application. */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This sets up the screen.
     * Throws IOException if FXML file doesn't load.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/mycompany/irr00_group_project/homeview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 650);
            stage.setScene(scene);
            stage.setTitle("Audiobook Player");
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            stage.show();
        } catch (IOException e) {
            System.err.println(
                "Error: Could not load the FXML file. Check your file path and location!");
            e.printStackTrace();
        }
    }
}
