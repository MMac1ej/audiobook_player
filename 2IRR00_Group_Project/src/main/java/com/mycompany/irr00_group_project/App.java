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

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane(new Label("Audiobook Player"));
        stage.setScene(new Scene(
            root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT));
        stage.setTitle("Audiobook Player");
        stage.show();

        //click screen to open settings
        root.setOnMouseClicked(e -> {
            startSettings(stage);
        });
    }

    /**
     * Starts the settings view in the given stage.
     * @param stage the stage to display the settings view in
     */
    private void startSettings(Stage stage) {
        try {
            // 1. Locate and load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/mycompany/irr00_group_project/settings.fxml"));
            Parent root = loader.load();

            // 2. Set up the scene
            Scene scene = new Scene(
                root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT);
            
            // 3. Display the window
            stage.setScene(scene);
            stage.setTitle("Audiobook Player - Settings");
            stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
            stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
            stage.show();
            
        } catch (IOException e) {
            System.err.println(
                "Error: Could not load the FXML file. Check your file path and location!");
            e.printStackTrace();
        }
    }
}
