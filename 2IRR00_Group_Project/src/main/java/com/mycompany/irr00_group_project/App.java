package com.mycompany.irr00_group_project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Entry point for the Audiobook Player application. */
public class App extends Application {

    TimestampStore timestampStore;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This sets up the screen.
     * Throws IOException if FXML file doesn't load.
     */
    @Override
    public void start(Stage stage) {
        showMainView(stage);
    }

    private void showMainView(Stage stage) {
        try {
            HomeViewController hvController = HomeViewController.getInstance();
            timestampStore = TimestampStore.getInstance();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/mycompany/irr00_group_project/homeview.fxml"));
            loader.setController(hvController);
            setHvControllerProperties(hvController, stage);
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

    private void startPlayer(Stage stage) {
        try {
            AudioPlayer audioPlayer = new AudioPlayer();
            ProgressStore progressStore = new ProgressStore();
            PlayerViewController controller =
                    new PlayerViewController(audioPlayer, progressStore, timestampStore);
            controller.setOnBack(() -> showMainView(stage));
            controller.loadDefaultBook();

            stage.setScene(controller.buildScene());
            stage.setTitle("Audiobook Player");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the settings view in the given stage.
     * @param stage the stage to display the settings view in
     */
    public void startSettings(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/mycompany/irr00_group_project/settings.fxml"));
            Parent root = loader.load();
            SettingsController controller = loader.getController();
            controller.setOnBack(() -> showMainView(stage));

            stage.setScene(new Scene(
                root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT));
            stage.setTitle("Audiobook Player - Settings");
            stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
            stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setHvControllerProperties(HomeViewController hvController, Stage stage) {
        hvController.setApp(this);
        hvController.setStage(stage);
        hvController.setTimestampStore(timestampStore);
    }
}
