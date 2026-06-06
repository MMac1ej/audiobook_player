package com.mycompany.irr00_group_project;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Entry point for the Audiobook Player application.
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane(new Label("Audiobook Player"));
        stage.setScene(new Scene(
                root, 600, 600));
        stage.setTitle("Audiobook Player");
        stage.show();

        //click screen to open settings
        root.setOnMouseClicked(e -> {
            startEpisodesView(stage);
        });
    }

    private void startEpisodesView(Stage stage) {
        try {
            // 1. Locate and load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/irr00_group_project/EpisodesView.fxml"));
            Parent root = loader.load();

            // 2. Set up the scene
            Scene scene = new Scene(
                    root, 600, 600);

            // 3. Display the window
            stage.setScene(scene);
            stage.setTitle("Audiobook Player - Settings");
            stage.setMinWidth(100);
            stage.setMinHeight(100);
            stage.show();

        } catch (FileNotFoundException e) {
            System.err.println(
                    "Error: Error 1" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(
                    "Error: Error 2" + e.getMessage());
            e.printStackTrace();
        }
    }
}
