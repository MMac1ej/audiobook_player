package com.mycompany.irr00_group_project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Entry point for the Audiobook Player application.
 */
public class App extends Application {

    AudioPlayer audioPlayer = new AudioPlayer();
    ProgressStore progressStore = new ProgressStore();
    TimestampStore timestampStore = new TimestampStore();
    Library library = new Library();

    public static void main(String[] args) {
        launch(args);
    }

    // please note that this is still temporary way of starting the app
    @Override
    public void start(Stage stage) {
        showMainView(stage);
    }

    private void showMainView(Stage stage) {
        Button playerBtn = new Button("Open Player");
        Button episodesBtn = new Button("Browse Episodes");
        Button overviewBtn = new Button("Audiobook Overview");
        Button settingsBtn = new Button("Settings");

        playerBtn.setOnAction(e -> startPlayer(stage));
        episodesBtn.setOnAction(e -> startEpisodesView(stage));
        overviewBtn.setOnAction(e -> startAudiobookOverview(stage));
        settingsBtn.setOnAction(e -> startSettings(stage));

        VBox root = new VBox(20, playerBtn, episodesBtn, overviewBtn, settingsBtn);
        root.setStyle("-fx-alignment: center; -fx-padding: 40;");
        stage.setScene(new Scene(
                root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT));
        stage.setTitle("Audiobook Player");
        stage.show();
    }

    private void startEpisodesView(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/irr00_group_project/EpisodesView.fxml"));
            Parent root = loader.load();
            EpisodesViewController controller = loader.getController();
            controller.setOnBack(() -> showMainView(stage));
            controller.setOnEpisodeSelected((book, index) ->
                    startPlayerWithEpisode(stage, book, index));
            stage.setScene(new Scene(
                    root, Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT));
            stage.setTitle("Audiobook Player - Episodes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startPlayerWithEpisode(Stage stage, Audiobook book, int episodeIndex) {
        try {
            PlayerViewController controller =
                    new PlayerViewController(audioPlayer, progressStore, timestampStore);
            controller.setOnBack(() -> startEpisodesView(stage));
            controller.setBook(book, episodeIndex);

            stage.setScene(controller.buildScene());
            stage.setTitle("Audiobook Player");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startPlayer(Stage stage) {
        try {
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
    
     /**
     * Starts the audiobook overview in the given stage.
     * @param stage the stage to display the settings view in
     */
    private void startAudiobookOverview(Stage stage) {
        AudiobookOverview bookOverview = new AudiobookOverview(
                () -> showMainView(stage), library, true);
        bookOverview.loadAudiobookOverview(stage);

    }
}
