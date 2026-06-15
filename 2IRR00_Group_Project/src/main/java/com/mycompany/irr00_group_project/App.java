package com.mycompany.irr00_group_project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the Audiobook Player application.
 */
public class App extends Application {

    private final TimestampStore timestampStore = new TimestampStore();
    private final Library library = new Library();
    private final HomeViewController hvController = new HomeViewController();
    private final ProgressStore progressStore = new ProgressStore();
    private final AudioPlayer audioPlayer = new AudioPlayer();

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
            library.loadFromFolder(Settings.getInstance().getAudiobookFolderPath());
        } catch (IOException e) {
            System.err.println("No Audiobooks found! Try changing the file path.");
        } finally {
            showMainView(stage);
        }
    }

    private void showMainView(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/mycompany/irr00_group_project/homeview.fxml"));
            loader.setController(hvController);
            setHvControllerProperties(hvController, stage);
            Parent root = loader.load();
            Scene scene = new Scene(root, Constants.DEFAULT_WINDOW_WIDTH, 
                Constants.DEFAULT_WINDOW_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("Audiobook Player");
            stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
            stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
            stage.show();
        } catch (IOException e) {
            System.err.println(
                "Error: Could not load the FXML file. Check your file path and location!");
            e.printStackTrace();
        }
    }

    /**
     * Starts the episodes view in the given stage.
     * @param stage the stage to display the episodes view in
     */
    public void startEpisodesView(Stage stage) {
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

    /**
     * Starts the player view in the given stage.
     * @param stage the stage to display the player in
     */
    public void startPlayer(Stage stage) {
        try {
            AudioPlayer audioPlayer = new AudioPlayer();
            ProgressStore progressStore = new ProgressStore();
            TimestampStore timestampStore = new TimestampStore();
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
     * This method open the PlayerView, given we have a certain audiobook that needs to be loaded.
     * @param stage is current stage
     * @param audi is audiobook that needs to be played
     */
    public void startPlayerWithAudiobook(Stage stage, Audiobook audi) {
        try {
            PlayerViewController controller =
                    new PlayerViewController(audioPlayer, progressStore, timestampStore);
            controller.setOnBack(() -> showMainView(stage));
            controller.setBook(audi);
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
        hvController.setLibrary(library);
    }
    
     /**
     * Starts the audiobook overview in the given stage.
     * @param stage the stage to display the settings view in
     */
    public void startAudiobookOverview(Stage stage) {
        AudiobookOverview bookOverview = new AudiobookOverview(
                () -> showMainView(stage), library, true);
        bookOverview.loadAudiobookOverview(stage);

    }
}
