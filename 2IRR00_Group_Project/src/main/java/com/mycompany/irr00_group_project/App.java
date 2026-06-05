package com.mycompany.irr00_group_project;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Entry point for the Audiobook Player application. */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        showMainView(stage);
    }

    private void showMainView(Stage stage) {
        Button playerBtn = new Button("Open Player");
        Button settingsBtn = new Button("Settings");

        playerBtn.setOnAction(e -> startPlayer(stage));
        settingsBtn.setOnAction(e -> startSettings(stage));

        VBox root = new VBox(20, playerBtn, settingsBtn);
        root.setStyle("-fx-alignment: center; -fx-padding: 40;");

        stage.setScene(new Scene(
            root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT));
        stage.setTitle("Audiobook Player");
        stage.show();
    }

    private void startPlayer(Stage stage) {
        try {
            AudioPlayer audioPlayer = new AudioPlayer();
            ProgressStore progressStore = new ProgressStore();
            PlayerViewController controller = new PlayerViewController(audioPlayer, progressStore);
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
    private void startSettings(Stage stage) {
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
}
